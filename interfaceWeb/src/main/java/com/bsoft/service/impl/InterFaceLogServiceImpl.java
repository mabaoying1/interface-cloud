package com.bsoft.service.impl;

import com.bsoft.service.InterFaceLogService;
import com.mongodb.*;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCursor;
import com.mongodb.util.JSON;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author liujx
 * @date 2019/10/11
 */
@Service
public class InterFaceLogServiceImpl implements InterFaceLogService {


    @Autowired
    @Qualifier("mongoTemplate")
    protected MongoTemplate mongoTemplate;

    @Override
    public String query() {
        List<Bson> list = new ArrayList<>();
//        查询所有的日志去重
//        AggregateIterable<Document> iterable = mongoTemplate.getCollection("requestRecord").aggregate(list);

//        BasicDBObject _id = new BasicDBObject("_id", "$hospitalId");
//        _id.append("value", new BasicDBObject("$sum", 1));
//        BasicDBObject group = new BasicDBObject("$group", _id);
//        BasicDBObject match = new BasicDBObject("$match", "{$methodId:getMedicalRecords}");
        BasicDBObject group = (BasicDBObject) JSON.parse("{$group:{_id:'$hospitalId',takeUpTime:{$max:'takeUpTime'},count:{$sum:1} }}");

        list.add(group);
//        list.add(match);
//        BasicDBObject result = new BasicDBObject();
//        result.append("_id", 0);
//        result.append("name", "$_id");
//        result.append("value", "$value");
//        BasicDBObject project = new BasicDBObject("$methodId ", result);
//        DBObject project = (DBObject) JSON.parse("{$project:{_id:1,serviceId:1,methodId:1,value:1}}");
//        list.add((Bson) project);
        System.out.println(list);
        AggregateIterable<Document> iterable = mongoTemplate.getCollection("requestRecord").aggregate(list);
        MongoCursor<org.bson.Document> set = iterable.iterator();
        List<Document> returnList = new LinkedList<>();
        while (set.hasNext()) {
            org.bson.Document map = set.next();
            returnList.add(map);
            System.out.println(map);
        }
        return returnList.toString();

    }


    public static void main(String[] args) {

        Mongo m = new Mongo("localhost", 27017);
        DB db = m.getDB("test");
        DBCollection coll = db.getCollection("requestRecord");

        /* 创建 $unwind 操作, 用于切分数组*/
        DBObject unwind = new BasicDBObject("$unwind", "$hospitalId");

        /* Group操作*/
        DBObject groupFields = new BasicDBObject("_id", new BasicDBObject("hospitalId", "$hospitalId")
                .append("serviceId", "$serviceId").append("methodId","$methodId"));
        groupFields.put("times", new BasicDBObject("$sum", 1));
        groupFields.put("maxTime", new BasicDBObject("$max", "$takeUpTime"));
        groupFields.put("minTime", new BasicDBObject("$min", "$takeUpTime"));
        DBObject group = new BasicDBObject("$group", groupFields);

        /* Reshape Group Result*/
        DBObject projectFields = new BasicDBObject();
        projectFields.put("hospitalId",  new BasicDBObject("hospitalId","$_id.hospitalId").append("serviceId", "$_id.serviceId"));
        projectFields.put("methodId", new BasicDBObject("methodId","$_id.methodId")
                .append("times", "$times").append("maxTime","$maxTime").append("minTime","$minTime"));
        DBObject project = new BasicDBObject("$project", projectFields);

        /* 将结果push到一起*/
        DBObject groupAgainFields = new BasicDBObject("_id", "$hospitalId");
        groupAgainFields.put("methodIdinfo", new BasicDBObject("$push", "$methodId"));
        DBObject reshapeGroup = new BasicDBObject("$group", groupAgainFields);

        /* 查看Group结果 */
        AggregationOutput output = coll.aggregate(unwind, group, project, reshapeGroup);

        System.out.println(output.results());
    }
}
