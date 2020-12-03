package com.bsoft.controller;

import com.bsoft.exception.CommonException;
import com.bsoft.service.ReportQueryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 报告查询
 * @author liujx
 * @date 2019/7/4
 */
@RestController
@RequestMapping("/reportQuery")
public class ReportQueryController {
    private static Logger logger = LoggerFactory.getLogger(ReportQueryController.class);

    @Autowired
    private ReportQueryService reportQueryService;

    /**
     *  检验报告列表
     * @param req
     * @return
     */
    @RequestMapping(value="getLisRecord",produces="application/json;charset=utf-8", method = RequestMethod.POST)
    @ResponseBody
    public String getLisRecord(@RequestBody Map<String, Object> req) throws CommonException {
        logger.info("检验报告列表:入参："+req);
        String res=reportQueryService.getLisRecord(req);
     //   logger.info("检验报告列表:出参："+res);
        return res;
    }
    /**
     *  检验报告列表详细信息
     * @param req
     * @return
     */
    @RequestMapping(value="getLisDetails",produces="application/json;charset=utf-8", method = RequestMethod.POST)
    @ResponseBody
    public String getLisDetails(@RequestBody Map<String, Object> req) throws CommonException {
        logger.info("检验报告列表详细信息:入参："+req);
        String res=reportQueryService.getLisDetails(req);
      //  logger.info("检验报告列表详细信息:出参："+res);
        return res;
    }
    /**
     *  检查报告列表
     * @param req
     * @return
     */
    @RequestMapping(value="getRisRecord",produces="application/json;charset=utf-8", method = RequestMethod.POST)
    @ResponseBody
    public String getRisRecord(@RequestBody Map<String, Object> req) throws CommonException {
        logger.info("检查报告列表:入参："+req);
        String res=reportQueryService.getRisRecord(req);
      //  logger.info("检查报告列表:出参："+res);
        return res;
    }
    /**
     *  检验报告列表详细信息
     * @param req
     * @return
     */
    @RequestMapping(value="getRisDetails",produces="application/json;charset=utf-8", method = RequestMethod.POST)
    @ResponseBody
    public String getRisDetails(@RequestBody Map<String, Object> req) throws CommonException {
        logger.info("检验报告列表详细信息:入参："+req);
        String res=reportQueryService.getRisDetails(req);
      //  logger.info("检验报告列表详细信息:出参："+res);
        return res;
    }
    /**
     *  检验项目查询
     * @param req
     * @return
     */
    @RequestMapping(value="getTestItems",produces="application/json;charset=utf-8", method = RequestMethod.POST)
    @ResponseBody
    public String getTestItems(@RequestBody Map<String, Object> req) throws CommonException {
        logger.info("检验项目查询:入参："+req);
        String res=reportQueryService.getTestItems(req);
       logger.info("检验项目查询:出参："+res);
        return res;
    }
    /**
     *  检验项目明细查询
     * @param req
     * @return
     */
    @RequestMapping(value="getTestItemsDetails",produces="application/json;charset=utf-8", method = RequestMethod.POST)
    @ResponseBody
    public String getTestItemsDetails(@RequestBody Map<String, Object> req) throws CommonException {
        logger.info("检验项目明细查询:入参："+req);
        String res=reportQueryService.getTestItemsDetails(req);
        logger.info("检验项目明细查询:出参："+res);
        return res;
    }
    /**
     *  处方开单
     * @param req
     * @return
     */
    @RequestMapping(value="prescriptionBilling",produces="application/json;charset=utf-8", method = RequestMethod.POST)
    @ResponseBody
    public String prescriptionBilling(@RequestBody Map<String, Object> req) throws CommonException {
        logger.info("处方开单:入参："+req);
        String res=reportQueryService.prescriptionBilling(req);
        logger.info("处方开单:出参："+res);
        return res;
    }
}
