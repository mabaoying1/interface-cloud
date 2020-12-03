package com.bsoft.service.impl;

import com.bsoft.constants.InterfaceConstants;
import com.bsoft.dao.MongoDaoSupport;
import com.bsoft.entity.RequestRecord;
import com.bsoft.entity.ServiceConfigInfo;
import com.bsoft.exception.CommonException;
import com.bsoft.producer.MsgProducer;
import com.bsoft.service.MechanismService;
import com.bsoft.service.ServiceConfigInfoService;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.List;
import java.util.Map;

/**
 * 服务器配置信息实现类
 *
 * @author liujx
 * @date 2019/10/9
 */
@Service
public class ServiceConfigInfoServiceImpl extends MongoDaoSupport<ServiceConfigInfo> implements ServiceConfigInfoService {
    @Resource
    MsgProducer msgProducer;
    @Resource
    MechanismService mechanismService;

    @Autowired
    StringRedisTemplate redisTemplate;

    /**
     * 新增服务配置信息
     *
     * @param serviceConfigInfo
     * @return
     */
    @Override
    public String register(ServiceConfigInfo serviceConfigInfo) {
        Date date = new Date(System.currentTimeMillis());
        serviceConfigInfo.setLastTime(null);
        serviceConfigInfo.setCreateTime(null);
        //判断是否已经有该账号信息
//        Query query = new Query();
//        query.addCriteria(Criteria.where("projectName").is(serviceConfigInfo.getProjectName()));
//        List<ServiceConfigInfo> list = find(query);
//        if (list.size() > 0) {
//            throw new CommonException("该账号已存在");
//        }
        serviceConfigInfo.setCreateTime(date);
        serviceConfigInfo.setLastTime(date);
        serviceConfigInfo.setStatus("0");
        save(serviceConfigInfo);
        return "{\"code\":1,\"msg\":\"请求成功\",\"data\":{}}";
    }

    /**
     * 查询服务配置查询
     *
     * @param map
     * @return
     */
    @Override
    public String query(Map<String, Object> map) {
        isNUll(map, Arrays.asList("pageSize", "pageNum"));
        Integer pageSize = Integer.valueOf(map.get("pageSize").toString());
        Integer pageNum = Integer.valueOf(map.get("pageNum").toString());
        Query query = new Query();
        if (map.get("projectNumber") != null && !map.get("projectNumber").toString().equals("")) {
            query.addCriteria(Criteria.where("projectNumber").is(map.get("projectNumber").toString()));
        }
        if (map.get("serviceName") != null && !map.get("serviceName").toString().equals("")) {
            query.addCriteria(Criteria.where("serviceName").regex(".*?" + map.get("serviceName").toString() + ".*"));
        }
        Page<ServiceConfigInfo> page = findPage(query, pageNum, pageSize, "projectNumber");
        return getSuccessPage(page);
    }

    /**
     * 修改服务配置信息
     *
     * @param serviceConfigInfo
     * @return
     */
    @Override
    public String modify(ServiceConfigInfo serviceConfigInfo) {
        if (serviceConfigInfo.getId() == null || serviceConfigInfo.getId().equals("")) {
            throw new CommonException("Id 不能为空");
        }
        serviceConfigInfo.setLastTime(new Date(System.currentTimeMillis()));
        updateById(serviceConfigInfo.getId(), serviceConfigInfo);
        return "{\"code\":1,\"msg\":\"请求成功\",\"data\":{}}";
    }

    /**
     * 删除服务配置信息
     *
     * @param serviceConfigInfo
     * @return
     */
    @Override
    public String delete(ServiceConfigInfo serviceConfigInfo) {
        if (serviceConfigInfo.getId() == null || serviceConfigInfo.getId().equals("")) {
            throw new CommonException("id 不能为空");
        }
        ServiceConfigInfo serviceConfigInfoFind = new ServiceConfigInfo();
        serviceConfigInfoFind.setStatus("1");
        serviceConfigInfoFind.setLastTime(new Date(System.currentTimeMillis()));
        updateById(serviceConfigInfo.getId(), serviceConfigInfoFind);
        return "{\"code\":1,\"msg\":\"请求成功\",\"data\":{}}";
    }

    @Override
    public String queryServiceInfo() {
        Map<Object, Object> map = new HashMap();
        map = redisTemplate.opsForHash().entries(InterfaceConstants.REIDIS_EUREKAINFO_KEY);
        // 1. entrySet遍历，在键和值都需要时使用（最常用）
        List<JSONObject> scheoudList = new LinkedList<>();
        //查询已经配置好的所有服务器信息
        Query query = new Query();
        query.addCriteria(Criteria.where("state").is("0"));
        List<ServiceConfigInfo> listConfigInfo = find(query);


        for (Map.Entry<Object, Object> entry : map.entrySet()) {
            Map<Object, Object> topMap = new HashMap();
            System.out.println("key = " + entry.getKey() + ", value = " + entry.getValue());
            topMap.put("projectName", entry.getKey());
            topMap.put("id", entry.getKey());
            topMap.put("serviceName", "");
            topMap.put("port", "");
            topMap.put("projectNumber", "");
            topMap.put("state", "");
            topMap.put("children", "");
            JSONArray jsonArray = JSONArray.fromObject(entry.getValue());
            List<Map<String, Object>> childrenList = new LinkedList<>();
            for (int i = 0; jsonArray.size() > i; i++) {
                Map<String, Object> objectMap = new HashMap<>();
                objectMap.put("projectName", jsonArray.getJSONObject(i).get("serviceId"));
                objectMap.put("id", jsonArray.getJSONObject(i).get("serviceId"));
                objectMap.put("serviceName", jsonArray.getJSONObject(i).get("host"));
                objectMap.put("port", jsonArray.getJSONObject(i).get("port"));
//                objectMap.put("projectNumber", "");
                if (listConfigInfo.size() == 0) {
                    objectMap.put("state", "本地未配置");
                }
                for (int a = 0; listConfigInfo.size() > a; a++) {
                    if (listConfigInfo.get(a).getServiceName().equals(jsonArray.getJSONObject(i).get("host")) && listConfigInfo.get(a).getPort().equals(jsonArray.getJSONObject(i).get("port").toString()) && listConfigInfo.get(a).getProjectNumber().equals(entry.getKey())) {
                        objectMap.put("state", "正常");
                        listConfigInfo.remove(a);
                    } else {
                        objectMap.put("state", "本地未配置");
                    }
                }

                objectMap.put("children", "");
                childrenList.add(objectMap);
                topMap.put("children", childrenList);
            }
            scheoudList.add(JSONObject.fromObject(topMap));
        }
        for (int a = 0; listConfigInfo.size() > a; a++) {
            Map<Object, Object> topMap = new HashMap();
            topMap.put("projectName", listConfigInfo.get(a).getProjectNumber());
            topMap.put("id", listConfigInfo.get(a).getId());
            topMap.put("serviceName", listConfigInfo.get(a).getServiceName());
            topMap.put("port", listConfigInfo.get(a).getPort());
            topMap.put("projectNumber", "");
            topMap.put("state", "服务器异常");
            topMap.put("children", "");
            scheoudList.add(JSONObject.fromObject(topMap));
        }
        return scheoudList.toString();
    }

    /**
     * 获取服务器上面配置的服务信息
     *
     * @return
     */
    @Override
    public List<ServiceConfigInfo> findService() {
        //查询已经配置好的所有服务器信息
        Query query = new Query();
        query.addCriteria(Criteria.where("state").is("0"));
        List<ServiceConfigInfo> serveiceConfigInfo = find(query);
        return serveiceConfigInfo;
    }

    /**
     * 查询服务配置查询
     *
     * @param map
     * @return
     */
    @Override
    public String queryService(Map<String, Object> map) {

        Query query = new Query();
        if (map.get("projectNumber") != null && !map.get("projectNumber").toString().equals("")) {
            query.addCriteria(Criteria.where("projectNumber").is(map.get("projectNumber").toString()));
        }
        if (map.get("mechanismCode") != null && !map.get("mechanismCode").toString().equals("")) {
            query.addCriteria(Criteria.where("mechanismCode").is(map.get("mechanismCode").toString()));
        }
        Sort sort = new Sort(Sort.Direction.ASC, "projectNumber").and(new Sort(Sort.Direction.ASC, "mechanismCode")).and(new Sort(Sort.Direction.ASC, "serviceName"));
        query.with(sort);
        List<ServiceConfigInfo> page = find(query);
        if (page.size() > 0) {

            for (int i = 0; i < page.size(); i++) {
                Criteria criteria = Criteria.where("");
                criteria.and("projectNumber").is(page.get(i).getProjectNumber());
//                criteria.and("serviceId").is(page.get(i).getServiceID());
                criteria.and("hospitalId").is(page.get(i).getMechanismCode());
                criteria.and("port").is(page.get(i).getPort());
                criteria.and("ipAddress").is(page.get(i).getIpAddress());
                page.get(i).setLogTotal(logProject(criteria) + "");

                Map<Object, Object> runMap = redisTemplate.opsForHash().entries(InterfaceConstants.REIDIS_EUREKAINFO_KEY + "-" + page.get(i).getProjectNumber());
                if (runMap.size() > 0) {
                    if (runMap.get(page.get(i).getServiceID()) != null) {
                        JSONArray jsonArray = JSONArray.fromObject(runMap.get(page.get(i).getServiceID()));
                        page.get(i).setStatus("2");
                        page.get(i).setServerInfo("");
                        for (int c = 0; jsonArray.size() > c; c++) {
                            if (page.get(i).getIpAddress().equals(jsonArray.getJSONObject(c).get("host"))
                                    && page.get(i).getPort().equals(jsonArray.getJSONObject(c).get("port").toString())) {
                                page.get(i).setStatus("1");
                                if(jsonArray.getJSONObject(c).get("cpqk")!=null ){
                                    page.get(i).setServerInfo(jsonArray.getJSONObject(c).get("cpqk").toString()+jsonArray.getJSONObject(c).get("ncqk").toString());
                                }
                            }
                        }
                    }
                } else {
                    page.get(i).setStatus("2");
                }
                criteria.and("status").ne("1");
                page.get(i).setLogError(logProject(criteria) + "");
            }
        }

        //        获取服务器上还在运行的服务器配置信息


        return getSuccess(page);
    }

    /**
     * 查询每个项目的日志总数
     */
    public String logProject(Criteria criteria) {
//          分组统计
        Aggregation customerAgg = Aggregation.newAggregation(
                Aggregation.project("projectNumber", "methodId", "hospitalId", "reponseStatus", "status", "requestStamp", "requestTime", "responseParams", "port", "ipAddress"),//
                Aggregation.match(criteria),// Aggregation.unwind("projectNumber"),
                Aggregation.group("projectNumber").count().as("reponseStatus")
        );
        List<RequestRecord> customerList = findAggregateList(new Query(criteria), "requestRecord", customerAgg, RequestRecord.class);
        String a="0";
        if(customerList.size()>0){
            a=customerList.get(0).getReponseStatus();
        }
        return a;
    }

}
