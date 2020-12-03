package com.bsoft.controller;

import com.bsoft.model.RequestRecord;
import com.bsoft.service.LogEsService;
import com.bsoft.service.LogService;
import net.sf.json.JSON;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 日志查询
 * @author liujx
 * @date 2019/10/16
 */
@RestController
@RequestMapping("/logQuery")
public class LogControoler {
    private static Logger logger = LoggerFactory.getLogger(LogControoler.class);
    @Resource
    LogEsService logService;
//    /**
//     * 查询日志信息
//     *
//     */
//    @PostMapping("/queryLog")
//    public String getMechanism(@RequestBody Map<String, Object> req) throws Exception {
//        logger.info("查询日志信息:入参：" + req);
//        String res = logService.queryLog(req);
//        logger.info("查询日志信息:出参：" + res);
//        return res;
//    }

    /**
     * {"idxName":"inedxlog",
     * "idxSQL":{
     *   "dynamic":"false",
     * 	"properties": {
     * 		"hospitalId": {
     * 			"type": "keyword"
     *                },
     * 		"serviceId": {
     * 			"type": "keyword"
     *        },
     * 		"projectNumber": {
     * 			"type": "keyword"
     *        },
     * 		"methodId": {
     * 			"type": "keyword"
     *        },
     * 		"requestTime": {
     * 			"type": "keyword"
     *        },
     * 		"year": {
     * 			"type": "keyword"
     *        },
     * 		"month": {
     * 			"type": "keyword"
     *        },
     * 		"day": {
     * 			"type": "keyword"
     *        },
     * 		"hour": {
     * 			"type": "keyword"
     *        },
     * 		"minute": {
     * 			"type": "keyword"
     *        },
     * 		"takeUpTime": {
     * 			"type": "keyword"
     *        },
     * 		"status": {
     * 			"type": "keyword"
     *        },
     * 		"reponseStatus": {
     * 			"type": "keyword"
     *        },
     * 		"requestParams": {
     * 			"type": "text"
     *        },
     * 		"responseParams": {
     * 			"type": "text"
     *        }* 	}
     * }
     * }
     * @param req
     * @return
     * @throws Exception
     */
    @PostMapping("/createIndex")
    public String createIndex(@RequestBody Map<String, Object> req) throws Exception {
        logger.info("查询日志信息:入参：" + req);
        logService.createIndex(req.get("idxName").toString() , JSONObject.fromObject( req.get("idxSQL")).toString());
//        logger.info("查询日志信息:出参：" + res);
        return "ok";
    }


    /**
     * {"idxName":"inedxlog",
     *  "id" : "4" ,
     * "requestRecord":{
     * 		"hospitalId": "1",
     * 		"serviceId": "register",
     * 		"projectNumber": "5100001",
     * 		"methodId": "confirm",
     * 		"requestTime": "2020-03-05 12:12:12",
     * 		"year": "2020",
     * 		"month": "03",
     * 		"day": "05",
     * 		"hour": "12",
     * 		"minute": "12",
     * 		"takeUpTime": "200",
     * 		"status": "1",
     * 		"reponseStatus": "200",
     * 		"requestParams": "{'key':'value','key1':'创业安逸创业巴适的班1','key2':'创业慧康创业航创巴适的班'}",
     * 		"responseParams": "{'key':'value','key1':'体育语文数学给一门课号接口好看1','key2':'记录同反腐斗志昂扬了解败你马上到'}"
     *        }
     * }
     * @param req
     * @return
     * @throws Exception
     */
    @PostMapping("/insertIndex")
    public String insertIndex(@RequestBody Map<String, Object> req ) throws Exception {
        logger.info("查询日志信息:入参：" + req);
        logService.insertOrUpdateOne(req.get("idxName").toString() , req.get("id").toString(), (Map)req.get("requestRecord"));
//        logger.info("查询日志信息:出参：" + res);
        return "ok";
    }

    /**
     * {"idxName":"inedxlog",
     *  "id" : "1"
     * }
     * @param req
     * @return
     * @throws Exception
     */
    @PostMapping("/deleteIndex")
    public String deleteIndex(@RequestBody Map<String, Object> req ) throws Exception {
        logger.info("查询日志信息:入参：" + req);
        logService.deleteOne(req.get("idxName").toString() , req.get("id").toString());
//        logger.info("查询日志信息:出参：" + res);
        return "ok";
    }

    @PostMapping("/deleteAll")
    public String deleteAll(@RequestBody Map<String, Object> req ) throws Exception {
        logger.info("查询日志信息:入参：" + req);
        logService.deleteAll(req.get("idxName").toString());
//        logger.info("查询日志信息:出参：" + res);
        return "ok";
    }

    @PostMapping("/searchMatch")
    public String searchMatch(@RequestBody Map<String, Object> req ) throws Exception {
        logger.info("查询日志信息:入参：" + req);
        logService.searchMatch(req.get("idxName").toString(),req.get("key").toString(),req.get("value").toString());
//        logger.info("查询日志信息:出参：" + res);
        return "ok";
    }
}
