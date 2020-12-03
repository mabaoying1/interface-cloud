package com.bsoft.service.impl;

import com.bsoft.dao.MongoDaoSupport;
import com.bsoft.entity.RequestRecord;
import com.bsoft.exception.CommonException;
import com.bsoft.service.LogService;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 * 日志信息查询
 *
 * @author liujx
 * @date 2019/10/14
 */
@Service
public class LogServiceImpl extends MongoDaoSupport<RequestRecord> implements LogService {

    @Autowired
    @Qualifier("mongoTemplate")
    protected MongoTemplate mongoTemplate;

    /**
     * 查询错误日志信息
     *
     * @return
     */
    @Override
    public List<RequestRecord> queryError() {
        Query query = new Query();
        Criteria criteria = Criteria.where("status").ne("1");
        query.addCriteria(criteria);
        List<RequestRecord> logList = find(query);
        return logList;
    }

    /**
     * 查询日志信息
     *
     * @return
     */
    @Override
    public String queryLog(Map<String, Object> requstMap) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Query query = new Query();
        Criteria criteria = Criteria.where("");
        if (requstMap.get("serviceId") != null && StringUtils.isNotEmpty(requstMap.get("serviceId").toString())) {
            criteria.and("serviceId").is(requstMap.get("serviceId"));
        }
        if (requstMap.get("methodId") != null && StringUtils.isNotEmpty(requstMap.get("methodId").toString())) {
            criteria.and("methodId").is(requstMap.get("methodId"));
        }

        if (requstMap.get("requestParams") != null && StringUtils.isNotEmpty(requstMap.get("requestParams").toString())) {
            criteria.and("requestParams").regex(".*?" + escapeExprSpecialWord(requstMap.get("requestParams").toString())+ ".*");
        }
        if (requstMap.get("responseParams") != null && StringUtils.isNotEmpty(requstMap.get("responseParams").toString())) {
            String ssss= requstMap.get("responseParams").toString();
            System.out.println(escapeExprSpecialWord(ssss));
            criteria.and("responseParams").regex(".*?" +escapeExprSpecialWord(ssss)+ ".*");
        }
        if (requstMap.get("projectNumber") != null && StringUtils.isNotEmpty(requstMap.get("projectNumber").toString())) {
            criteria.and("projectNumber").is(requstMap.get("projectNumber"));
        }
        if (requstMap.get("requestTime") != null && StringUtils.isNotEmpty(requstMap.get("requestTime").toString())) {
            criteria.and("requestTime").is(requstMap.get("requestTime"));
        }
        if (requstMap.get("status") != null && StringUtils.isNotEmpty(requstMap.get("status").toString())) {
            if (requstMap.get("status").toString().equals("1")) {
                criteria.and("status").is(requstMap.get("status"));
            } else {
                criteria.and("status").ne("1");
            }
        }
        if (requstMap.get("requestStartTime") != null && StringUtils.isNotEmpty(requstMap.get("requestStartTime").toString())) {
            try {
                long requestStartTime = df.parse(requstMap.get("requestStartTime").toString()).getTime();
                long requestEndTime = df.parse(requstMap.get("requestEndTime").toString()).getTime();
                criteria.andOperator(Criteria.where("requestStamp").gte(requestStartTime), Criteria.where("requestStamp").lte(requestEndTime));

            } catch (ParseException e) {
                e.printStackTrace();
                throw new CommonException("requestStartTime/requestEndTime时间格式错误");
            }
        }

        query.addCriteria(criteria);
        isNUll(requstMap, Arrays.asList("pageSize", "pageNum"));
        Integer pageSize = Integer.valueOf(requstMap.get("pageSize").toString());
        Integer pageNum = Integer.valueOf(requstMap.get("pageNum").toString());
        Page<RequestRecord> page = findPage(query, pageNum, pageSize, "requestTime");
        return getSuccessPage(page);
    }
    public static String escapeExprSpecialWord(String keyword) {
        if (StringUtils.isNotBlank(keyword)) {
            String[] fbsArr = { "\\", "$", "(", ")", "*", "+", ".", "[", "]", "?", "^", "{", "}", "|" };
            for (String key : fbsArr) {
                if (keyword.contains(key)) {
                    keyword = keyword.replace(key, "\\" + key);
                }
            }
        }
        return keyword;
    }
    //聚合函数测试例子
    public void test() {
        Criteria criteria = Criteria.where("hospitalId").is("510108000001");
//        Criteria criteria = Criteria.where("");
//          分组统计
        Aggregation customerAgg = Aggregation.newAggregation(
                Aggregation.project("hospitalId", "serviceId", "methodId", "reponseStatus", "takeUpTime"),
                Aggregation.match(criteria), Aggregation.unwind("hospitalId"),
                Aggregation.group("hospitalId", "serviceId").first("hospitalId").as("hospitalId").
                        first("serviceId").as("serviceId").max("takeUpTime").as("takeUpTime").count().as("reponseStatus"),
                Aggregation.sort(new Sort(new Sort.Order(Sort.Direction.DESC, "takeUpTime"))),
                Aggregation.skip(0),
                Aggregation.limit(20)
        );
//        多表关联
        Aggregation test = Aggregation.newAggregation(Aggregation.lookup("hospitalInfo", "hospitalId", "hospitalId", "222"), Aggregation.match(criteria));
        List<RequestRecord> customerList = findAggregateList(new Query(criteria), "requestRecord", test, RequestRecord.class);
        System.out.println("分页测试：" + customerList);
    }

}
