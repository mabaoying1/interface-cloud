package com.bsoft.controller;

import com.bsoft.exception.CommonException;
import com.bsoft.service.ClinicPaymentService;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

/**
 * 诊间支付接口
 * @author liujx
 * @date 2019/7/4
 */
@RestController
@RequestMapping("/clinicPayment")
public class ClinicPaymentController {
    private static Logger logger = LoggerFactory.getLogger(ClinicPaymentController.class);

    @Autowired
    private ClinicPaymentService clinicPaymentService;

    /**
     *  门诊就诊记录
     * @param req
     * @return
     */
    @RequestMapping(value="getMedicalRecords",produces="application/json;charset=utf-8", method = RequestMethod.POST)
    @ResponseBody
    public String getInpatients(@RequestBody Map<String, Object> req) throws CommonException {
        logger.info("门诊就诊记录:入参："+req);
        String res=clinicPaymentService.getMedicalRecords(req);
        logger.info("门诊就诊记录:出参："+res);
        return res;
    }
    /**
     *  获取处方支付列表
     * @param req
     * @return
     */
    @RequestMapping(value="getPrescriptionPayment",produces="application/json;charset=utf-8", method = RequestMethod.POST)
    @ResponseBody
    public String getPrescriptionPayment(@RequestBody Map<String, Object> req) throws CommonException {
        logger.info("获取处方支付列表:入参："+req);
        String res=clinicPaymentService.getPrescriptionPayment(req);
        logger.info("获取处方支付列表:出参："+res);
        return res;
    }
//    /**
//     *  获取处置支付列表
//     * @param req
//     * @return
//     */
//    @RequestMapping(value="getDisposalPayment",produces="application/json;charset=utf-8", method = RequestMethod.POST)
//    @ResponseBody
//    public String getDisposalPayment(@RequestParam Map<String, Object> req) throws CommonException {
//        logger.info("获取处置支付列表:入参："+req);
//        String res=clinicPaymentService.getDisposalPayment(req);
//        logger.info("获取处置支付列表:出参："+res);
//        return res;
//    }
    /**
     *  获取处方明细列表
     * @param req
     * @return
     */
    @RequestMapping(value="getPrescriptionDetailed",produces="application/json;charset=utf-8", method = RequestMethod.POST)
    @ResponseBody
    public String getPrescriptionDetailed(@RequestBody Map<String, Object> req) throws CommonException {
        logger.info("获取处方明细列表:入参："+req);
        String res=clinicPaymentService.getPrescriptionDetailed(req);
        logger.info("获取处方明细列表:出参："+res);
        return res;
    }
//    /**
//     *  获取处置明细列表
//     * @param req
//     * @return
//     */
//    @RequestMapping(value="getDisposalDetailed",produces="application/json;charset=utf-8", method = RequestMethod.POST)
//    @ResponseBody
//    public String getDisposalDetailed(@RequestParam Map<String, Object> req) throws CommonException {
//        logger.info("获取处置明细列表:入参："+req);
//        String res=clinicPaymentService.getDisposalDetailed(req);
//        logger.info("获取处置明细列表:出参："+res);
//        return res;
//    }
    /**
     *  结算处方
     * @param req
     * @return
     */
    @RequestMapping(value="settlementPrescription",produces="application/json;charset=utf-8", method = RequestMethod.POST)
    @ResponseBody
    public String settlementPrescription(@RequestBody Map<String, Object> req) throws CommonException {
        logger.info("结算处方:入参："+req);
        JSONObject json=clinicPaymentService.settlementPrescriptionJson(req);
        String res=clinicPaymentService.settlementPrescription(json);
        logger.info("结算处方:出参："+res);
        return res;
    }
//    /**
//     *  结算处置
//     * @param req
//     * @return
//     */
//    @RequestMapping(value="settlementDisposal",produces="application/json;charset=utf-8", method = RequestMethod.POST)
//    @ResponseBody
//    public String settlementDisposal(@RequestParam Map<String, Object> req) throws CommonException {
//        logger.info("结算处置:入参："+req);
//        String res=clinicPaymentService.settlementDisposal(req);
//        logger.info("结算处置:出参："+res);
//        return res;
//    }

    /**
     *  获取处方已支付列表
     * @param req
     * @return
     */
    @RequestMapping(value="getPrescriptionPaymented",produces="application/json;charset=utf-8", method = RequestMethod.POST)
    @ResponseBody
    public String getPrescriptionPaymented(@RequestBody Map<String, Object> req) throws CommonException {
        logger.info("获取处方已支付列表:入参："+req);
        String res=clinicPaymentService.getPrescriptionPaymented(req);
        logger.info("获取处方已支付列表:出参："+res);
        return res;
    }
//    /**
//     *  获取处置已支付列表
//     * @param req
//     * @return
//     */
//    @RequestMapping(value="getDisposalPaymented",produces="application/json;charset=utf-8", method = RequestMethod.POST)
//    @ResponseBody
//    public String getDisposalPaymented(@RequestParam Map<String, Object> req) throws CommonException {
//        logger.info("获取处置已支付列表:入参："+req);
//        String res=clinicPaymentService.getDisposalPaymented(req);
//        logger.info("获取处置已支付列表:出参："+res);
//        return res;
//    }
    /**
     *  取消处方结算
     * @param req
     * @return
     */
    @RequestMapping(value="cancelSettlementPrescription",produces="application/json;charset=utf-8", method = RequestMethod.POST)
    @ResponseBody
    public String cancelSettlementPrescription(@RequestBody Map<String, Object> req) throws CommonException {
        logger.info("取消处方结算:入参："+req);
        String res=clinicPaymentService.cancelSettlementPrescription(req);
        logger.info("取消处方结算:出参："+res);
        return res;
    }
//    /**
//     *  取消处置结算
//     * @param req
//     * @return
//     */
//    @RequestMapping(value="cancelSettlementDisposal",produces="application/json;charset=utf-8", method = RequestMethod.POST)
//    @ResponseBody
//    public String cancelSettlementDisposal(@RequestParam Map<String, Object> req) throws CommonException {
//        logger.info("取消处方结算:入参："+req);
//        String res=clinicPaymentService.cancelSettlementDisposal(req);
//        logger.info("取消处方结算:出参："+res);
//        return res;
//    }
    /**
     * 就诊历史列表
     *
     * @param req
     * @return
     */
    @RequestMapping(value = "medicalRecordsList", produces = "application/json;charset=utf-8", method = RequestMethod.POST)
    @ResponseBody
    public String medicalRecordsList(@RequestBody Map<String, Object> req) throws CommonException {
        logger.info("就诊历史列表:入参：" + req);
        String res = clinicPaymentService.medicalRecordsList(req);
        logger.info("就诊历史列表:出参：" + res);
        return res;
    }
    /**
     * 就诊历史详情
     *
     * @param req
     * @return
     */
    @RequestMapping(value = "medicalRecordsDetails", produces = "application/json;charset=utf-8", method = RequestMethod.POST)
    @ResponseBody
    public String medicalRecordsDetails(@RequestBody Map<String, Object> req) throws CommonException, IOException, SQLException {
        logger.info("就诊历史详情:入参：" + req);
        String res = clinicPaymentService.medicalRecordsDetails(req);
        logger.info("就诊历史详情:出参：" + res);
        return res;
    }
    /**
     * his交易记录查询
     *
     * @param req
     * @return
     */
    @RequestMapping(value = "getHisPay", produces = "application/json;charset=utf-8", method = RequestMethod.POST)
    @ResponseBody
    public String getHisPay(@RequestBody Map<String, Object> req) throws CommonException, IOException, SQLException {
        logger.info("his交易记录查询:入参：" + req);
        String res = clinicPaymentService.getHisPay(req);
        logger.info("his交易记录查询:出参：" + res);
        return res;
    }
    /**
     * 门诊病人处置检索
     *
     * @param paramMap
     * @return
     */

    @RequestMapping(value = "/getManagementInfo", produces = "application/json;charset=utf-8", method = RequestMethod.POST)
    @ResponseBody
    public String outpatientGetManagementInfo(@RequestBody Map<String, Object> paramMap) throws CommonException {
        logger.info("门诊病人处置检索:门诊病人处置检索传入参数：" + paramMap);
        String res = clinicPaymentService.GetManagementInfo(paramMap);
   //     logger.info("门诊病人药品检索:出参：" + res);
        return res;
    }
    /**
     * 药 检索
     *
     * @param paramMap
     * @return
     */

    @RequestMapping(value = "/getUseDrugs", produces = "application/json;charset=utf-8", method = RequestMethod.POST)
    @ResponseBody
    public String getUseDrugs(@RequestBody Map<String, Object> paramMap) throws CommonException {
        logger.info("药 检索：入参" + paramMap);
        String res = clinicPaymentService.getUseDrugs(paramMap);
  //      logger.info("药 检索 出参：" + res);
        return res;
    }
}
