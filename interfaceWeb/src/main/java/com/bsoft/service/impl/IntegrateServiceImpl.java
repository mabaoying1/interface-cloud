package com.bsoft.service.impl;


import com.bsoft.constants.InterfaceConstants;
import com.bsoft.dao.MongoDaoSupport;
import com.bsoft.entity.*;
import com.bsoft.exception.CommonException;
import com.bsoft.service.IntegrateService;

import com.google.gson.JsonArray;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.*;

/**
 * 首页展示功能控制层
 *
 * @author liujx
 * @date 2019/10/12
 */
@Service
public class IntegrateServiceImpl extends MongoDaoSupport<User> implements IntegrateService {
    @Autowired
    StringRedisTemplate redisTemplate;
    @Autowired
    LogServiceImpl logService;

    /***
     * 首页内容展示查询
     * @param projectInfo
     * @return
     */
    @Override
    public String homePage(Map<String, Object> projectInfo) {
        IntegrateBean res = new IntegrateBean();
        Query query = new Query();
        if (projectInfo != null && !projectInfo.equals("") && projectInfo.get("projectName") != null) {
            query.addCriteria(Criteria.where("projectName").regex(".*?" + projectInfo.get("projectName") + ".*"));
        }
        query.addCriteria(Criteria.where("status").is("0"));
//       查询机构总数
        List<User> organ = find(query);

        if (organ.size() < 1) {
            res.setHospitalTotal("0");
            res.setProjectTotal("0");
            res.setRequestTotal("0");
            return getSuccess(res);
        }
        List<String> projectNumberLsit = new ArrayList<>();
        Set hospitalSet = new HashSet();
        int hospitalTotal = 0;
        for (User user : organ) {
            projectNumberLsit.add(user.getProjectNumber());
            hospitalSet.addAll(user.getMechanisms());
        }
        hospitalTotal = hospitalSet.size();
//        日志总数
        Criteria criteria = Criteria.where("");
//        如果单个项目查询
        if (projectInfo != null && !projectInfo.equals("") && projectInfo.get("projectName") != null) {
            criteria = Criteria.where("projectNumber").in(projectNumberLsit);
        }

        Aggregation aggregation = Aggregation.newAggregation(Aggregation.match(criteria));
        int logTotal = findAggregateList(new Query(criteria), "requestRecord", aggregation, RequestRecord.class).size();

        // int hospitalTotal = 0;

        // hospitalTotal = findAggregateList(new Query(criteria), "mechanism", aggregation, Mechanism.class).size();


        res.setRequestTotal(logTotal + "");
        res.setProjectTotal(organ.size() + "");
        res.setHospitalTotal(hospitalTotal + "");

        Criteria logCriteria = Criteria.where("");
        if (projectInfo != null && !projectInfo.equals("") && projectInfo.get("projectName") != null) {
            logCriteria = Criteria.where("projectNumber").in(projectNumberLsit);
        }
        //获取每个项目的日志总数
        Map<String, Object> logTotalMap = logProject(logCriteria);
        if (projectInfo != null && !projectInfo.equals("") && projectInfo.get("projectName") != null) {
            logCriteria = Criteria.where("projectNumber").in(projectNumberLsit).and("status").is("1");
        } else {
            logCriteria = Criteria.where("status").is("1");
        }
        //获取每个项目的日志成功总数
        Map<String, Object> logSuccessTotalMap = logProject(logCriteria);
//        获取配置的所有服务信息
        Criteria serviceCriteria = Criteria.where("");
        if (projectInfo != null && !projectInfo.equals("") && projectInfo.get("projectName") != null) {
            serviceCriteria = Criteria.where("projectNumber").in(projectNumberLsit).and("status").is("0");
        } else {
            serviceCriteria = Criteria.where("status").is("0");
        }
        Map<String, Object> serviceMap = serviceInfo(serviceCriteria);
        List<Map> list = new LinkedList<>();
        for (int i = 0; i < organ.size(); i++) {
//    * 项目编号  projectNumber;
//     * 项目名称  projectName;
//     * 项目介绍  projectIntroduction;
//     * 服务总数  serviceTotal
//     * 服务正常运行总数  serviceRunTotal
//     * 调用成功总数  logSuccessTotal
//     * 调用总数  logTotal
//        项目详情

            int fwsl = 0;
            if (redisTemplate.opsForHash().entries(InterfaceConstants.REIDIS_EUREKAINFO_KEY + "-" + organ.get(i).getProjectNumber()).size() > 0) {
                for (Object key : redisTemplate.opsForHash().entries(InterfaceConstants.REIDIS_EUREKAINFO_KEY + "-" + organ.get(i).getProjectNumber()).keySet()) {
                    List<Map<String, String>> listObjectFir = (List<Map<String, String>>) JSONArray.fromObject(redisTemplate.opsForHash().entries(InterfaceConstants.REIDIS_EUREKAINFO_KEY + "-" + organ.get(i).getProjectNumber()).get(key));
                    fwsl = listObjectFir.size() + fwsl;
                }
            }
            Map<String, Object> projectDetails = new HashMap<>();
            projectDetails.put("projectNumber", organ.get(i).getProjectNumber());
            projectDetails.put("projectName", organ.get(i).getProjectName());
            projectDetails.put("projectType", organ.get(i).getProjectType());
            projectDetails.put("projectIntroduction", organ.get(i).getProjectIntroduction());
            projectDetails.put("serviceTotal", serviceMap.get(organ.get(i).getProjectNumber()) == null ? 0 : serviceMap.get(organ.get(i).getProjectNumber()));
            projectDetails.put("serviceRunTotal", fwsl);
            projectDetails.put("logSuccessTotal", logSuccessTotalMap.get(organ.get(i).getProjectNumber()) == null ? 0 : logSuccessTotalMap.get(organ.get(i).getProjectNumber()));
            projectDetails.put("logTotal", logTotalMap.get(organ.get(i).getProjectNumber()) == null ? 0 : logTotalMap.get(organ.get(i).getProjectNumber()));
            list.add(projectDetails);
        }
        res.setProjectDetails(list);
        return getSuccess(res);
    }

    /***
     * 机构页面内容展示
     * @param projectInfo
     * @return
     */
    @Override
    public String mechanism(Map<String, Object> projectInfo) {
        List<Object> resList = new LinkedList<>();
        isNUll(projectInfo, "projectNumber");
        Query query = new Query();
        if (projectInfo != null && !projectInfo.equals("") && projectInfo.get("projectNumber") != null) {
            query.addCriteria(Criteria.where("projectNumber").is(projectInfo.get("projectNumber")));
        }
//       查询机构总数
        List<User> organ = find(query);
        if (organ != null && organ.size() > 0) {
//            获取该项目下的多个机构id
            List<String> projectNumberList = organ.get(0).getMechanisms();
            if (projectNumberList.size() > 0) {
//                放入到条件中
                String[] projectnumber = new String[projectNumberList.size()];
                for (int i = 0; i < projectNumberList.size(); i++) {
                    projectnumber[i] = projectNumberList.get(i);
                }
                Criteria criteria = Criteria.where("status").is("0").and("mechanismCode").in(projectnumber);
                Aggregation customerAgg = Aggregation.newAggregation(
                        Aggregation.project("mechanismName", "mechanismCode", "status"),
                        Aggregation.match(criteria), Aggregation.unwind("mechanismCode"),
                        Aggregation.group("mechanismCode").first("mechanismCode")
                                .as("mechanismCode").first("mechanismName").as("mechanismName"),
                        Aggregation.sort(new Sort(new Sort.Order(Sort.Direction.DESC, "mechanismName")))
                );
                List<Mechanism> mechanismsList = findAggregateList(new Query(criteria), "mechanism", customerAgg, Mechanism.class);

                if (mechanismsList != null && mechanismsList.size() > 0) {
                    List<Map> serviceInfo2 = serviceInfo2(criteria, projectInfo.get("projectNumber").toString());
                    for (int a = 0; a < mechanismsList.size(); a++) {
                        //返回信息
                        MechanismBean res = new MechanismBean();
                        res.setMechanismCode(mechanismsList.get(a).getMechanismCode());
                        res.setMechanismName(mechanismsList.get(a).getMechanismName());
                        List<Map> details = new LinkedList<>();
                        for (int b = 0; b < serviceInfo2.size(); b++) {
                            if (mechanismsList.get(a).getMechanismCode().equals(serviceInfo2.get(b).get("mechanismCode"))) {
                                details.add(serviceInfo2.get(b));
//                                serviceInfo2.remove(b);
                            }
                        }
                        res.setServiceDetails(details);
                        resList.add(res);
                    }
                }
            }
        }
        return getSuccess(resList);
    }

    /***
     * 通过首页点击日志
     * 机构以及机构下面的日志信息
     * @param projectInfo
     * @return
     */
    @Override
    public String logInfo(Map<String, Object> projectInfo) {
        LogInfoBean res = new LogInfoBean();
        isNUll(projectInfo, "projectNumber");
        Query query = new Query();
        if (projectInfo != null && !projectInfo.equals("") && projectInfo.get("projectNumber") != null) {
            query.addCriteria(Criteria.where("projectNumber").is(projectInfo.get("projectNumber")));
        }
//       查询机构总数
        List<User> organ = find(query);
        if (organ != null && organ.size() > 0) {
            //            获取该项目下的多个机构id
            List<String> projectNumberList = organ.get(0).getMechanisms();
            if (projectNumberList.size() > 0) {
//                放入到条件中
                String[] projectnumber = new String[projectNumberList.size()];
                for (int i = 0; i < projectNumberList.size(); i++) {
                    projectnumber[i] = projectNumberList.get(i);
                }
                Criteria logCriteria = Criteria.where("hospitalId").in(projectnumber);
                Aggregation logCustomerAgg = Aggregation.newAggregation(
                        Aggregation.match(logCriteria)
                );
                int logTotal = findAggregateList(new Query(logCriteria), "requestRecord", logCustomerAgg, RequestRecord.class).size();
                Criteria logSuccessCriteria = Criteria.where("hospitalId").in(projectnumber).and("status").is("1");
                Aggregation logSuccessCustomerAgg = Aggregation.newAggregation(
                        Aggregation.match(logCriteria)
                );
                int logSuccessTotal = findAggregateList(new Query(logSuccessCriteria), "requestRecord", logSuccessCustomerAgg, RequestRecord.class).size();

                Criteria criteria = Criteria.where("status").is("0").and("mechanismCode").in(projectnumber);
                Aggregation customerAgg = Aggregation.newAggregation(
                        Aggregation.project("mechanismName", "mechanismCode", "status"),
                        Aggregation.match(criteria), Aggregation.unwind("mechanismCode"),
                        Aggregation.group("mechanismCode").first("mechanismCode")
                                .as("mechanismCode").first("mechanismName").as("mechanismName"),
                        Aggregation.sort(new Sort(new Sort.Order(Sort.Direction.DESC, "mechanismName")))
                );
                List<Mechanism> mechanismsList = findAggregateList(new Query(criteria), "mechanism", customerAgg, Mechanism.class);
                List<Map> mechanismsMap = new LinkedList<>();
                Map<String, Object> logMechanism = logMechanism(logCriteria);
                Map<String, Object> logSuccessMechanism = logMechanism(logSuccessCriteria);
                if (mechanismsList != null && mechanismsList.size() > 0) {
                    for (int a = 0; a < mechanismsList.size(); a++) {
                        Map<String, Object> mechanismsDeatl = new HashMap<>();
                        mechanismsDeatl.put("mechanismName", mechanismsList.get(a).getMechanismName());
                        mechanismsDeatl.put("mechanismCode", mechanismsList.get(a).getMechanismCode());
                        mechanismsDeatl.put("logTotal", logMechanism.get(mechanismsList.get(a).getMechanismCode()));
                        mechanismsDeatl.put("logSuccessTotal", logSuccessMechanism.get(mechanismsList.get(a).getMechanismCode()));
                        mechanismsMap.add(mechanismsDeatl);
                    }
                }

                res.setFailTotal((logTotal - logSuccessTotal) + "");
                res.setRequestTotal(logTotal + "");
                res.setSuccessTotal(logSuccessTotal + "");
                res.setMechanismDetails(mechanismsMap);
            }
        }
        return getSuccess(res);
    }

    /**
     * 通过点击机构查询服务信息
     * 返回服务下的 每个接口返回的信息
     *
     * @param projectInfo
     * @return
     */
    @Override
    public String serviceInfo(Map<String, Object> projectInfo) {
        isNUll(projectInfo, "projectNumber");
        isNUll(projectInfo, "mechanismCode");
        Criteria logCriteria = Criteria.where("mechanismCode").is(projectInfo.get("mechanismCode")).and("projectNumber").is(projectInfo.get("projectNumber"));
        List<Map> serviceList = serviceInfo2(logCriteria, projectInfo.get("projectNumber") + "");
        return getSuccess(serviceList);
    }


    /**
     * 查询每个项目的日志总数
     */
    public Map<String, Object> logProject(Criteria criteria) {
        Map<String, Object> logProject = new HashMap<>();
//          分组统计
        Aggregation customerAgg = Aggregation.newAggregation(
                Aggregation.project("projectNumber","methodId","serviceId", "reponseStatus", "status", "hospitalId","requestStamp","requestTime","responseParams","port","ipAddress"),
                Aggregation.match(criteria), Aggregation.unwind("projectNumber"),
                Aggregation.group("projectNumber").count().as("reponseStatus")
        );
        List<RequestRecord> customerList = findAggregateList(new Query(criteria), "requestRecord", customerAgg, RequestRecord.class);
        if (customerList != null && customerList.size() > 0) {
            for (int i = 0; i < customerList.size(); i++) {
                logProject.put(customerList.get(i).getId(), customerList.get(i).getReponseStatus());
            }
        }
        return logProject;
    }

    /**
     * 查询本地配置的服务器信息
     * 返回的是项目下面的服务数量
     *
     * @param criteria
     * @return
     */
    public Map<String, Object> serviceInfo(Criteria criteria) {
        Map<String, Object> serviceProject = new HashMap<>();
//          分组统计
        Aggregation customerAgg = Aggregation.newAggregation(
                Aggregation.project("projectNumber", "status"),
                Aggregation.match(criteria), Aggregation.unwind("projectNumber"),
                Aggregation.group("projectNumber").count().as("status")
        );
        List<ServiceConfigInfo> customerList = findAggregateList(new Query(criteria), "serviceConfigInfo", customerAgg, ServiceConfigInfo.class);
        if (customerList != null && customerList.size() > 0) {
            for (int i = 0; i < customerList.size(); i++) {
                serviceProject.put(customerList.get(i).getId(), customerList.get(i).getStatus());
            }
        }
        return serviceProject;
    }

    /**
     * 查询本地配置的服务器信息
     * 返回的是服务名，服务机构，每个服务的IP信息
     *
     * @param criteria
     * @return
     */
    public List<Map> serviceInfo2(Criteria criteria, String projectNumber) {
//          分组统计
        criteria.and("projectNumber").is(projectNumber);
        Aggregation customerAgg = Aggregation.newAggregation(
                Aggregation.project("projectNumber", "serviceName", "serviceID", "mechanismCode", "status"),
                Aggregation.match(criteria),
//                Aggregation.unwind("projectNumber"),
                Aggregation.group("projectNumber", "mechanismCode", "serviceID").first("serviceID").as("serviceID")
                        .first("serviceName").as("serviceName")
        );
        List<ServiceConfigInfo> customerList = findAggregateList(new Query(criteria), "serviceConfigInfo", customerAgg, ServiceConfigInfo.class);
        Aggregation ipCustomerAgg = Aggregation.newAggregation(
                Aggregation.project("projectNumber", "serviceName", "serviceID", "mechanismCode", "port", "ipAddress")
        );
        List<ServiceConfigInfo> ipAddressList = findAggregateList(new Query(criteria), "serviceConfigInfo", ipCustomerAgg, ServiceConfigInfo.class);
        List<Map> res = new LinkedList<>();
//        获取服务器上还在运行的服务器配置信息
        Map<Object, Object> runMap = redisTemplate.opsForHash().entries(InterfaceConstants.REIDIS_EUREKAINFO_KEY + "-" + projectNumber);
        if (customerList != null && customerList.size() > 0) {
            for (int i = 0; i < customerList.size(); i++) {
                Map<String, Object> serviceProject = new HashMap<>();
                serviceProject.put("serviceName", customerList.get(i).getServiceName());
                serviceProject.put("serviceID", customerList.get(i).getServiceID());
                serviceProject.put("mechanismCode", customerList.get(i).getMechanismCode());
                List<Map<String, Object>> list = new LinkedList<>();
                Map<String, Object> objectMap = new HashMap<>();
                if (runMap.get(customerList.get(i).getServiceID()) != null) {
                    JSONArray jsonArray = JSONArray.fromObject(runMap.get(customerList.get(i).getServiceID()));
                    for (int c = 0; jsonArray.size() > c; c++) {
                        objectMap.put(jsonArray.getJSONObject(c).get("host") + ":" + jsonArray.getJSONObject(c).get("port"), "");
                    }
                }

                for (int a = 0; a < ipAddressList.size(); a++) {
//                    存储每一条地址信息
                    if (ipAddressList.get(a).getServiceID().equals(customerList.get(i).getServiceID())
                            && ipAddressList.get(a).getMechanismCode().equals(customerList.get(i).getMechanismCode())) {
                        Map<String, Object> map = new HashMap<>();
//                        如果服务正常为1，不正常为2
                        map.put("ipAddress", ipAddressList.get(a).getIpAddress() + ":" + ipAddressList.get(a).getPort());
                        Map<String, Object> map2 = new HashMap<>();
                        if (objectMap.get(ipAddressList.get(a).getIpAddress() + ":" + ipAddressList.get(a).getPort()) == null) {
                            map2.put("color", "#F56C6C");
                        } else {
                            map2.put("color", "#67C23A");
                        }
                        map.put("label", map2);
                        list.add(map);
                    }
                }
                serviceProject.put("ipAddressInfo", list);
                res.add(serviceProject);
            }
        }
        System.out.println("测试地址111" + customerList);
        return res;
    }


    /**
     * 查询每个机构的日志总数
     */
    public Map<String, Object> logMechanism(Criteria criteria) {
        Map<String, Object> logProject = new HashMap<>();
//          分组统计
        Aggregation customerAgg = Aggregation.newAggregation(
                Aggregation.project("reponseStatus", "hospitalId"),
                Aggregation.match(criteria), Aggregation.unwind("hospitalId"),
                Aggregation.group("hospitalId").first("hospitalId").as("hospitalId").count().as("reponseStatus")
        );
        List<RequestRecord> customerList = findAggregateList(new Query(criteria), "requestRecord", customerAgg, RequestRecord.class);
        if (customerList != null && customerList.size() > 0) {
            for (int i = 0; i < customerList.size(); i++) {
                logProject.put(customerList.get(i).getHospitalId(), customerList.get(i).getReponseStatus());
            }
        }
        return logProject;
    }

    /**
     * 分组统计每个服务每个方法情况
     *
     * @param requstMap
     * @return
     */
    @Override
    public String motendInfo(Map<String, Object> requstMap) {
        isNUll(requstMap, "projectNumber");
        isNUll(requstMap, "serviceId");
        isNUll(requstMap, "mechanismCode");
        String projectNumber = requstMap.get("projectNumber").toString();
        String serviceId = requstMap.get("serviceId").toString();
        String hospitalId = requstMap.get("mechanismCode").toString();
        Criteria criteria = Criteria.where("hospitalId").is(hospitalId).and("serviceId").is(serviceId)
                .and("projectNumber").is(projectNumber).and("projectType").is("1");
        //          分组统计
        Aggregation customerAgg = Aggregation.newAggregation(
                Aggregation.project("projectNumber", "averageTime", "hospitalId", "serviceId", "methodId", "method", "requestParams", "responseParams", "takeUpTime", "requestTime"),
                Aggregation.match(criteria), Aggregation.unwind("methodId"),
                Aggregation.group("hospitalId", "serviceId", "methodId").first("hospitalId").as("mechanismCode")
                        .first("serviceId").as("serviceId").first("methodId").as("methodId")
                        .first("projectNumber").as("projectNumber")
                        .first("method").as("method").max("takeUpTime").as("maxTime")
                        .min("takeUpTime").as("minTime").avg("takeUpTime").as("averageTime").count().as("total")
        );
        List<MotendInfoBean> customerList = findAggregateList(new Query(criteria), "requestRecord", customerAgg, MotendInfoBean.class);
        if (customerList != null && customerList.size() > 0) {
            for (int a = 0; a < customerList.size(); a++) {
                Criteria criteria2 = Criteria.where("").and("status").is("1").and("projectNumber").is(projectNumber)
                        .and("hospitalId").is(hospitalId).and("serviceId").is(serviceId).and("methodId").is(customerList.get(a).getMethodId());
                Aggregation successAgg = Aggregation.newAggregation(
                        Aggregation.project("reponseStatus", "projectNumber", "averageTime", "hospitalId", "serviceId", "methodId", "method", "requestParams", "responseParams", "takeUpTime", "requestTime"),
                        Aggregation.match(criteria2)
                );
                int successTotal = findAggregateList(new Query(criteria2), "requestRecord", successAgg, MotendInfoBean.class).size();
                customerList.get(a).setSuccessTotal(successTotal + "");
                customerList.get(a).setFailTotal(Integer.valueOf(customerList.get(a).getTotal()) - successTotal + "");
            }
        }
        return getSuccess(customerList);
    }

    /**
     * 查询日志详情
     *
     * @param requstMap
     * @return
     */
    @Override
    public String queryLogInfo(Map<String, Object> requstMap) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        isNUll(requstMap, Arrays.asList("pageSize", "pageNum", "projectNumber", "mechanismCode"));
        Integer pageSize = Integer.valueOf(requstMap.get("pageSize").toString());
        Integer pageNum = Integer.valueOf(requstMap.get("pageNum").toString());
        Query query = new Query();
        Criteria logCriteria = Criteria.where("");
        if (requstMap.get("serviceId") != null && StringUtils.isNotEmpty(requstMap.get("serviceId").toString())) {
            query.addCriteria(Criteria.where("serviceId").is(requstMap.get("serviceId").toString()));
            logCriteria.and("serviceId").is(requstMap.get("serviceId").toString());
        }
        if (requstMap.get("methodId") != null && StringUtils.isNotEmpty(requstMap.get("methodId").toString())) {
            query.addCriteria(Criteria.where("methodId").is(requstMap.get("methodId").toString()));
            logCriteria.and("methodId").is(requstMap.get("methodId").toString());
        }
        if (requstMap.get("ipAddress") != null && StringUtils.isNotEmpty(requstMap.get("ipAddress").toString())) {
            query.addCriteria(Criteria.where("ipAddress").is(requstMap.get("ipAddress").toString()));
            logCriteria.and("ipAddress").is(requstMap.get("ipAddress").toString());
        }
        if (requstMap.get("port") != null && StringUtils.isNotEmpty(requstMap.get("port").toString())) {
            query.addCriteria(Criteria.where("port").is(requstMap.get("port").toString()));
            logCriteria.and("port").is(requstMap.get("port").toString());
        }
        if (requstMap.get("requestParams") != null && StringUtils.isNotEmpty(requstMap.get("requestParams").toString())) {
            query.addCriteria(Criteria.where("requestParams").is(requstMap.get("requestParams").toString()));
            logCriteria.and("requestParams").is(requstMap.get("requestParams").toString());
        }
        if (requstMap.get("responseParams") != null && StringUtils.isNotEmpty(requstMap.get("responseParams").toString())) {
            query.addCriteria(Criteria.where("responseParams").is(requstMap.get("responseParams").toString()));
            logCriteria.and("responseParams").is(requstMap.get("responseParams").toString());
        }
        if (requstMap.get("projectNumber") != null && StringUtils.isNotEmpty(requstMap.get("projectNumber").toString())) {
            query.addCriteria(Criteria.where("projectNumber").is(requstMap.get("projectNumber").toString()));
            logCriteria.and("projectNumber").is(requstMap.get("projectNumber").toString());
        }
        if (requstMap.get("requestTime") != null && StringUtils.isNotEmpty(requstMap.get("requestTime").toString())) {
            query.addCriteria(Criteria.where("requestTime").is(requstMap.get("requestTime").toString()));
            logCriteria.and("requestTime").is(requstMap.get("requestTime").toString());
        }
        if (requstMap.get("mechanismCode") != null && StringUtils.isNotEmpty(requstMap.get("mechanismCode").toString())) {
            query.addCriteria(Criteria.where("hospitalId").is(requstMap.get("mechanismCode").toString()));
            logCriteria.and("hospitalId").is(requstMap.get("mechanismCode").toString());
        }
        int issusscess = 0;
        if (requstMap.get("status") != null && StringUtils.isNotEmpty(requstMap.get("status").toString())) {
            if (requstMap.get("status").toString().equals("1")) {
                query.addCriteria(Criteria.where("status").is("1"));
                logCriteria.and("status").is("1");
            } else {
                query.addCriteria(Criteria.where("status").ne("1"));
                issusscess = 1;
                logCriteria.and("status").ne("1");
            }
        }else {
            logCriteria.and("status").is("1");
        }


        if (requstMap.get("requestStartTime") != null && StringUtils.isNotEmpty(requstMap.get("requestStartTime").toString())) {
            try {
                long requestStartTime = df.parse(requstMap.get("requestStartTime").toString()).getTime();
                long requestEndTime = df.parse(requstMap.get("requestEndTime").toString()).getTime();
                logCriteria.andOperator(Criteria.where("requestStamp").gte(requestStartTime), Criteria.where("requestStamp").lte(requestEndTime));
//                query.addCriteria(Criteria.where("requestStamp").gte(requestStartTime));
                Criteria s = Criteria.where("");
                s.andOperator(Criteria.where("requestStamp").gte(requestStartTime), Criteria.where("requestStamp").lte(requestEndTime));
                query.addCriteria(s);
//                query.addCriteria(Criteria.where("requestStamp").lte(requestStartTime));
            } catch (ParseException e) {
                e.printStackTrace();
                throw new CommonException("requestStartTime/requestEndTime时间格式错误");
            }
        }


        Page<RequestRecord> customerList = logService.findPage(query, pageNum, pageSize, "projectNumber,requestTime");
//        List<RequestRecord> customerList = findAggregateList(new Query(criteria), "requestRecord", customerAgg, RequestRecord.class);
        //获取每个项目的日志成功总数

        Map<String, Object> logSuccessTotalMap = logProject(logCriteria);
        int logSuccess = logSuccessTotalMap.get(requstMap.get("projectNumber")) == null ? 0 : Integer.valueOf(logSuccessTotalMap.get(requstMap.get("projectNumber")).toString());
        logSuccess = issusscess == 0 ? logSuccess : 0;
        JSONArray jsonArray = JSONArray.fromObject(customerList.getContent());
        JSONObject json = new JSONObject();
        json.put("total", customerList.getTotalElements());
        json.put("logSuccessTotal", logSuccess);
        json.put("logErrorTotal", customerList.getTotalElements() - logSuccess);
        json.put("list", jsonArray);
        return getSuccess(json);
    }

    /***
     * 获取项目下面的机构信息
     * @param projectInfo
     * @return
     */
    @Override
    public String getMechanism(Map<String, Object> projectInfo) {
        List<Object> resList = new LinkedList<>();
        isNUll(projectInfo, "projectNumber");
        Query query = new Query();
        if (projectInfo != null && !projectInfo.equals("") && projectInfo.get("projectNumber") != null) {
            query.addCriteria(Criteria.where("projectNumber").is(projectInfo.get("projectNumber")));
        }
//       查询机构总数
        List<User> organ = find(query);
        if (organ != null && organ.size() > 0) {
//            获取该项目下的多个机构id
            List<String> projectNumberList = organ.get(0).getMechanisms();
            if (projectNumberList.size() > 0) {
//                放入到条件中
                String[] projectnumber = new String[projectNumberList.size()];
                for (int i = 0; i < projectNumberList.size(); i++) {
                    projectnumber[i] = projectNumberList.get(i);
                }
                Criteria criteria = Criteria.where("status").is("1").and("mechanismCode").in(projectnumber);
                Aggregation customerAgg = Aggregation.newAggregation(
                        Aggregation.project("mechanismName", "mechanismCode", "status"),
                        Aggregation.match(criteria), Aggregation.unwind("mechanismCode"),
                        Aggregation.group("mechanismCode").first("mechanismCode")
                                .as("mechanismCode").first("mechanismName").as("mechanismName")
                );
                List<Mechanism> mechanismsList = findAggregateList(new Query(criteria), "mechanism", customerAgg, Mechanism.class);
                if (mechanismsList != null && mechanismsList.size() > 0) {
                    for (int a = 0; a < mechanismsList.size(); a++) {
                        //返回信息
                        MechanismBean res = new MechanismBean();
                        res.setMechanismCode(mechanismsList.get(a).getMechanismCode());
                        res.setMechanismName(mechanismsList.get(a).getMechanismName());
                        resList.add(res);
                    }
                }
            }
        }
        return getSuccess(resList);
    }

    /**
     * 获取接口调用 流水信息
     *
     * @return
     */
    @Override
    public String getMoneyInfo(Map<String, Object> reqMap) {
        Criteria criteria = Criteria.where("");
        if (reqMap.get("projectNumber") != null && StringUtils.isNotEmpty(reqMap.get("projectNumber").toString())) {
            criteria.and("projectNumber").is(reqMap.get("projectNumber"));
        }
        Aggregation payTypeAgg = Aggregation.newAggregation(
                Aggregation.project("projectNumber", "paymentAmount", "payType"),
                Aggregation.match(criteria),
                Aggregation.group("payType").first("payType").as("payType").sum("paymentAmount").as("paymentAmount")
        );
        List<BillListBean> payTpyeList = findAggregateList(new Query(criteria), "billListBean", payTypeAgg, BillListBean.class);
        List<Map<String, Object>> resList = new LinkedList<>();
        Map<String, Object> zffsMap = new HashMap<>();
//        zffsMap.put("ZFFS_YL", "银联");
        zffsMap.put("ZFFS_QT", "其他");
        zffsMap.put("ZFFS_WX", "微信");
        zffsMap.put("ZFFS_ZFB", "支付宝");
        for (Map.Entry<String, Object> entry : zffsMap.entrySet()) {
            Map<String, Object> map = new HashMap<>();
            map.put("payType", entry.getKey());
            map.put("payName", entry.getValue());
            map.put("totalMoney", 0);
            if (payTpyeList != null && payTpyeList.size() > 0) {
                for (int i = 0; i < payTpyeList.size(); i++) {
                    if (entry.getKey().equals(payTpyeList.get(i).getPayType())) {
                        map.put("totalMoney", changeF2Y(payTpyeList.get(i).getPaymentAmount() + ""));
                    }
                }
            }
            resList.add(map);
        }

        return getSuccess(resList);
    }

    /***
     * 接口时长数量统计
     * @param requstMap
     * @return
     */
    @Override
    public String getMethodStatistics(Map<String, Object> requstMap) {
        isNUll(requstMap, "requestStartTime");
        isNUll(requstMap, "requestEndTime");
        isNUll(requstMap, "takeUpTime");

        Criteria criteria = Criteria.where("");
        if (requstMap.get("projectNumber") != null && StringUtils.isNotEmpty(requstMap.get("projectNumber").toString())) {
            String projectNumber = requstMap.get("projectNumber").toString();
            criteria.and("projectNumber").is(projectNumber);
        }
        if (requstMap.get("mechanismCode") != null && StringUtils.isNotEmpty(requstMap.get("mechanismCode").toString())) {
            String hospitalId = requstMap.get("mechanismCode").toString();
            criteria.and("hospitalId").is(hospitalId);
        }

        String requestStartTime = requstMap.get("requestStartTime").toString();
        String requestEndTime = requstMap.get("requestEndTime").toString();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = format.parse(requestStartTime);
            Date date2 = format.parse(requestEndTime);
            criteria.andOperator(Criteria.where("requestStamp").gte(date.getTime()), Criteria.where("requestStamp").lte(date2.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String takeUpTime = requstMap.get("takeUpTime").toString();
        criteria.and("takeUpTime").gte(takeUpTime);
        criteria.and("reponseStatus").is("200");
        Aggregation payTypeAgg = Aggregation.newAggregation(
                Aggregation.project("projectNumber", "methodId", "hospitalId", "takeUpTime", "requestStamp","reponseStatus"),
                Aggregation.match(criteria),
                Aggregation.group( "methodId").first("methodId")
                        .as("methodId").count().as("takeUpTime"),
                Aggregation.sort(new Sort(new Sort.Order(Sort.Direction.DESC, "methodId")))

        );
        List<RequestRecord> payTpyeList = findAggregateList(new Query(criteria), "requestRecord", payTypeAgg, RequestRecord.class);
        return getSuccess(payTpyeList);
    }


    /***
     * 24小时内接口调用数统计
     * @param requstMap
     * @return
     */
    @Override
    public String getHoursStatistics(Map<String, Object> requstMap) {
        //获取时间前24小时

        Integer[] hours = Get24Hours();
        Map<String, String> projectMap = getProject();
        String projectNumber = requstMap.get("projectNumber") + "";
        List<HoursBean> resList = new LinkedList<>();

        if (requstMap.get("projectNumber") != null && !requstMap.get("projectNumber").toString().equals("")) {
            Map<String, String> mecharismMap = getMecharism(projectNumber);
            for (Map.Entry<String, String> entry : mecharismMap.entrySet()) {
                HoursBean mecharism = new HoursBean();
                List<Object> mecharismList = new LinkedList<>();
                for (int i = 0; i < hours.length; i++) {
                    Criteria criteria = Criteria.where("").and("projectNumber").is(projectNumber)
                            .and("hour").is(hours[i].toString()).and("hospitalId").is(entry.getKey());
                    Aggregation payTypeAgg = Aggregation.newAggregation(
                            Aggregation.project("projectNumber", "methodId", "hospitalId", "hour", "takeUpTime", "year", "month", "day"),
                            Aggregation.match(criteria),
                            Aggregation.group("projectNumber", "hour").first("projectNumber")
                                    .as("projectNumber").first("hospitalId")
                                    .as("hospitalId").first("hour").as("hour").count().as("takeUpTime")
                    );
                    List<RequestRecord> payTpyeList = findAggregateList(new Query(criteria), "requestRecord", payTypeAgg, RequestRecord.class);
                    Map<String, Object> hoursBean = new HashMap<>();

                    if (payTpyeList != null && payTpyeList.size() > 0) {
                        hoursBean.put("hour", hours[i]);
                        hoursBean.put("logTotal", payTpyeList.get(0).getTakeUpTime());
                    } else {
                        hoursBean.put("hour", hours[i]);
                        hoursBean.put("logTotal", "0");
                    }
                    mecharismList.add(hoursBean);
                }
                mecharism.setDetails(mecharismList);
                mecharism.setProjectNumber(entry.getKey());
                mecharism.setProjectName(entry.getValue());
                resList.add(mecharism);
            }
        } else {
            for (Map.Entry<String, String> entry : projectMap.entrySet()) {
                HoursBean mecharism = new HoursBean();
                List<Object> mecharismList = new LinkedList<>();
                for (int i = 0; i < hours.length; i++) {
                    Criteria criteria = Criteria.where("").and("projectNumber").is(entry.getKey())
                            .and("hour").is(hours[i].toString());
                    Aggregation payTypeAgg = Aggregation.newAggregation(
                            Aggregation.project("projectNumber", "methodId", "hospitalId", "hour", "takeUpTime", "year", "month", "day"),
                            Aggregation.match(criteria),
                            Aggregation.group("projectNumber", "hour").first("projectNumber")
                                    .as("projectNumber").first("hospitalId")
                                    .as("hospitalId").first("hour").as("hour").count().as("takeUpTime")
                    );
                    List<RequestRecord> payTpyeList = findAggregateList(new Query(criteria), "requestRecord", payTypeAgg, RequestRecord.class);
                    Map<String, Object> hoursBean = new HashMap<>();
                    if (payTpyeList != null && payTpyeList.size() > 0) {
                        hoursBean.put("hour", hours[i]);
                        hoursBean.put("logTotal", payTpyeList.get(0).getTakeUpTime());
                    } else {
                        hoursBean.put("hour", hours[i]);
                        hoursBean.put("logTotal", "0");

                    }
                    mecharismList.add(hoursBean);
                }
                mecharism.setDetails(mecharismList);
                mecharism.setProjectNumber(entry.getKey());
                mecharism.setProjectName(entry.getValue());
                resList.add(mecharism);
            }
        }
        return getSuccess(resList);
    }

    /***
     * 错误日志趋势统计
     * @param requstMap
     * @return
     */
    @Override
    public String getErrorLogInfo(Map<String, Object> requstMap) {
        //是否循环机构
        boolean mechanismPb = false;
        if (requstMap.get("projectNumber") != null && !requstMap.get("projectNumber").toString().equals("")) {
            mechanismPb = true;
        }
        Calendar ca = Calendar.getInstance();//得到一个Calendar的实例
        ca.setTime(new Date()); //设置时间为当前时间
        ca.add(Calendar.DATE, -180);
        long time = ca.getTimeInMillis(); //结果
        //获取当前时间前2年时间
        String[] days = Get2Year(180);
        Map<String, String> projectMap = new LinkedHashMap();
        if (mechanismPb) {
            projectMap = getMecharism(requstMap.get("projectNumber").toString());
        } else {
            projectMap = getProject();

        }
        List<HoursBean> resList = new LinkedList<>();

        for (Map.Entry<String, String> entry : projectMap.entrySet()) {
            HoursBean mecharism = new HoursBean();
            List<Object> mecharismList = new LinkedList<>();
            //////
            Criteria criteria = new Criteria();
            Aggregation payTypeAgg;
            if (mechanismPb) {
                criteria = Criteria.where("status").ne("1").and("projectNumber").is(requstMap.get("projectNumber").toString())
                        .and("requestStamp").gte(time).and("hospitalId").is(entry.getKey());
                payTypeAgg = Aggregation.newAggregation(//2019-09-09
                        Aggregation.project("projectNumber", "hospitalId", "year", "month", "day", "takeUpTime", "requestStamp", "status"),
                        Aggregation.match(criteria),
                        Aggregation.group("projectNumber", "hospitalId", "year", "month", "day").first("projectNumber")
                                .as("projectNumber").count().as("takeUpTime")
                );
            } else {
                criteria = Criteria.where("status").ne("1").and("projectNumber").is(entry.getKey())
                        .and("requestStamp").gte(time);
                payTypeAgg = Aggregation.newAggregation(//2019-09-09
                        Aggregation.project("projectNumber", "year", "month", "day", "takeUpTime", "requestStamp", "status"),
                        Aggregation.match(criteria),
                        Aggregation.group("projectNumber", "year", "month", "day").first("projectNumber")
                                .as("projectNumber").count().as("takeUpTime")
                );
            }

            List<RequestRecord> payTpyeList = findAggregateList(new Query(criteria), "requestRecord", payTypeAgg, RequestRecord.class);
            System.out.println(1);
            for (int i = 0; i < days.length; i++) {
                Map<String, Object> hoursBean = new HashMap<>();
                hoursBean.put("day", days[i]);
                hoursBean.put("logTotal", "0");
                for (RequestRecord requestRecord : payTpyeList) {
                    if ((requestRecord.getYear() + "-" + (requestRecord.getMonth().length() == 1 ? "0" + requestRecord.getMonth() : requestRecord.getMonth()) + "-" + (requestRecord.getDay().length() == 1 ? "0" + requestRecord.getDay() : requestRecord.getDay())).equals(days[i])) {
                        hoursBean.put("logTotal", requestRecord.getTakeUpTime());
                    }
                }

                mecharismList.add(hoursBean);
            }

            mecharism.setDetails(mecharismList);
            mecharism.setProjectNumber(entry.getKey());
            mecharism.setProjectName(entry.getValue());
            resList.add(mecharism);
        }
        return getSuccess(resList);
    }


    /**
     * 获取所有机构信息
     *
     * @return
     */
    public Map<String, String> getMecharism(String projectNumber) {

        Criteria projectCri = Criteria.where("status").is("0").and("projectNumber").is(projectNumber);
        Aggregation jrectAgg = Aggregation.newAggregation(
                Aggregation.project("projectNumber", "mechanisms", "status"),
                Aggregation.match(projectCri)
        );
        List<User> projcctList = findAggregateList(new Query(projectCri), "user", jrectAgg, User.class);
        Map<String, String> resMap = new LinkedHashMap<>();
        if (projcctList != null && projcctList.size() > 0 && projcctList.get(0).getMechanisms() != null && projcctList.get(0).getMechanisms().size() > 0) {
            for (int a = 0; a < projcctList.get(0).getMechanisms().size(); a++) {
                Criteria criteria = Criteria.where("status").is("0").and("mechanismCode").is(projcctList.get(0).getMechanisms().get(a));
                Aggregation payTypeAgg = Aggregation.newAggregation(
                        Aggregation.project("mechanismCode", "mechanismName", "status"),
                        Aggregation.match(criteria),
                        Aggregation.group("mechanismCode").first("mechanismName").as("mechanismName")
                                .first("mechanismCode").as("mechanismCode"),
                        Aggregation.sort(new Sort(new Sort.Order(Sort.Direction.DESC, "mechanismCode")))
                );
                List<Mechanism> mechainsmList = findAggregateList(new Query(criteria), "mechanism", payTypeAgg, Mechanism.class);

                if (mechainsmList != null && mechainsmList.size() > 0) {
                    for (int i = 0; i < mechainsmList.size(); i++) {
                        resMap.put(mechainsmList.get(i).getMechanismCode(), mechainsmList.get(i).getMechanismName());
                    }
                }
            }
        }
        return resMap;
    }

    /**
     * 获取所有项目信息
     *
     * @return
     */
    public Map<String, String> getProject() {

        Criteria criteria = Criteria.where("status").is("0");
        Aggregation payTypeAgg = Aggregation.newAggregation(
                Aggregation.project("projectNumber", "projectName", "status"),
                Aggregation.match(criteria),
                Aggregation.group("projectNumber").first("projectName").as("projectName")
                        .first("projectNumber").as("projectNumber"),
                Aggregation.sort(new Sort(new Sort.Order(Sort.Direction.DESC, "projectNumber")))

        );
        List<User> mechainsmList = findAggregateList(new Query(criteria), "user", payTypeAgg, User.class);
        Map<String, String> resMap = new LinkedHashMap<>();
        if (mechainsmList != null && mechainsmList.size() > 0) {
            for (int i = 0; i < mechainsmList.size(); i++) {
                resMap.put(mechainsmList.get(i).getProjectNumber(), mechainsmList.get(i).getProjectName());
            }
        }
        return resMap;
    }

    /**
     * 获取当前时间之前的24小时，年月日
     */
    public static List<Map<String, Object>> Get24Time() {
        List<Map<String, Object>> list = new LinkedList<>();
        Integer[] hours = Get24Hours();
        LocalDate date = LocalDate.now();
//        获取当前时间的年月
        int year = date.get(ChronoField.YEAR);
        int month = date.get(ChronoField.MONTH_OF_YEAR);
        int day = date.get(ChronoField.DAY_OF_MONTH);
        LocalDate yesterday = date.plusDays(-1);


        for (int i = 0; i < hours.length; i++) {
            //        存放时间，年月日
            Map<String, Object> map = new HashMap<>();
            map.put("year", year);
            map.put("month", month);
//            全是当天时间内容
            if (hours[23] == 23) {
                map.put("day", day);
                map.put("hour", hours[i] + 1);
            } else {
                int lastHour = hours[23];
                //如果不是当天，则用前一天的日期
                if (i < 24 - (lastHour + 1)) {
                    map.put("day", yesterday.getDayOfMonth());
                    map.put("hour", hours[i] + 1);
                } else {
                    map.put("day", day);
                    map.put("hour", hours[i] + 1);
                }

            }
            list.add(map);
        }
        return list;
    }

    /**
     * 获取过去24小时 每个小时信息
     *
     * @return
     */
    public static Integer[] Get24Hours() {
        Integer[] hours = new Integer[24];
        for (int i = 0; i < 24; i++) {
//            Calendar calendar = Calendar.getInstance();
//            calendar.add(Calendar.HOUR_OF_DAY, i);
            hours[i] = i + 1;
        }
        return hours;
    }

    /**
     * 获取当前日期的N天时间
     *
     * @param ts 当前时间多少天之前
     * @return
     */
    public static String[] Get2Year(int ts) {
        String[] days = new String[ts];
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        c.set(year, month, day);
        for (int i = ts - 1; i >= 0; i--) {
            days[i] = df.format(c.getTime());
            c.set(Calendar.DATE, c.get(Calendar.DATE) - 1);
        }
        return days;
    }


}
