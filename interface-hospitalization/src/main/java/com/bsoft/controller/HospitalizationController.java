package com.bsoft.controller;

import com.bsoft.exception.CommonException;
import com.bsoft.service.HospitaliztionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 住院费用接口
 * @author liujx
 * @date 2019/4/26
 */
@RestController
@RequestMapping("/hospitalization")
public class HospitalizationController {
    private static Logger logger = LoggerFactory.getLogger(HospitalizationController.class);

    @Autowired
    private HospitaliztionService hospitaliztionService;

    /**
     *  获取住院患者列表
     * @param req
     * @return
     */
    @RequestMapping(value="getInpatients",produces="application/json;charset=utf-8", method = RequestMethod.POST)
    @ResponseBody
    public String getInpatients(@RequestBody Map<String, Object> req) throws CommonException {
        logger.info("获取住院患者列表:入参："+req);
        String res=hospitaliztionService.getInpatients(req);
        logger.info("获取住院患者列表:出参："+res);
        return res;
    }
    /**
     *  获取住院患者基本信息
     * @param req
     * @return
     */
    @RequestMapping(value="getPatientInfo",produces="application/json;charset=utf-8", method = RequestMethod.POST)
    @ResponseBody
    public String PatientInfo(@RequestBody Map<String, Object> req) throws CommonException {

        logger.info("获取住院患者基本信息:入参："+req);
        String res=hospitaliztionService.getPatientInfo(req);
        logger.info("获取住院患者基本信息:出参："+res);
        return res;
    }

    /**
     *  获取住院患者住院详情
     * @param req
     * @return
     */
    @RequestMapping(value="getHospitalizationInfo",produces="application/json;charset=utf-8", method = RequestMethod.POST)
    @ResponseBody
    public String HospitalizationInfo(@RequestBody Map<String, Object> req) throws CommonException {

        logger.info("获取住院患者住院详情:入参："+req);
        String res=hospitaliztionService.getHospitalizationInfo(req);
        logger.info("获取住院患者住院详情:出参："+res);
        return res;
    }
    /**
     *  获取住院患者缴费记录
     * @param req
     * @return
     */
    @RequestMapping(value="getPatientPayRecords",produces="application/json;charset=utf-8", method = RequestMethod.POST)
    @ResponseBody
    public String PatientPayRecords(@RequestBody Map<String, Object> req) throws CommonException {
        logger.info("获取住院患者缴费记录传入参数：" + req);
        String res= hospitaliztionService.getPatientPayRecords(req);
        logger.info("获取住院患者缴费记录传入参数:出参："+res);
        return res;
    }

    /**
     *  //获取指定住院患者长期(临时)医嘱列表（能根据开医嘱时间或医嘱项目搜索）
     * @param req
     * @return
     */
    @RequestMapping(value="getPatientAdviceList",produces="application/json;charset=utf-8", method = RequestMethod.POST)
    @ResponseBody
    public String getPatientAdviceList(@RequestBody Map<String, Object> req) throws CommonException {
        logger.info("获取指定住院患者长期(临时)医嘱列表 入参：" + req);

        String res = hospitaliztionService.getPatientAdviceList(req);
        logger.info("获取指定住院患者长期(临时)医嘱列表:出参："+res);

        return res;
    }

    /**
     *  //获取住院诊断列表
     * @param req
     * @return
     */
    @RequestMapping(value="getHospitalizDiagnosis",produces="application/json;charset=utf-8", method = RequestMethod.POST)
    @ResponseBody
    public String getHospitalizDiagnosis(@RequestBody Map<String, Object> req) throws CommonException {
        logger.info("获取住院诊断列表 入参：" + req);
        String res = hospitaliztionService.getHospitalizDiagnosis(req);
        logger.info("获取住院诊断列表 出参：" + res);
        return res;
    }
    /**
     * 医嘱删除（长期和临时）
     * @param req
     */
    @RequestMapping(value="removePatientAdvice",produces="application/json;charset=utf-8", method = RequestMethod.POST)
    @ResponseBody
    public String getRemovePatientAdvice(@RequestBody Map<String, Object> req) throws CommonException {
        logger.info("医嘱删除（长期和临时） 入参：" + req);
        String res = hospitaliztionService.removePatientAdvice(req);
        logger.info("医嘱删除（长期和临时） 出参：" + res);
        return res;
    }

    /**
     * /新增医嘱信息
     * @param req
     */
    @RequestMapping(value="addPatientAdvice",produces="application/json;charset=utf-8", method = RequestMethod.POST)
    @ResponseBody
    public String addPatientAdvice(@RequestBody Map<String, Object> req) throws CommonException {
        logger.info("新增医嘱信息 入参：" + req);
        String res = hospitaliztionService.addPatientAdvice(req);
        logger.info("新增医嘱信息 出参：" + res);
        return res;
    }
    /**
     *         //变更长期医嘱信息
     * @param req
     */
    @RequestMapping(value="modifyPatientAdvice",produces="application/json;charset=utf-8", method = RequestMethod.POST)
    @ResponseBody
    public String  modifyPatientAdvice(@RequestBody Map<String, Object> req) throws CommonException {

        logger.info("变更长期医嘱信息 入参：" + req);
        String res = hospitaliztionService.modifyPatientAdvice(req);
        logger.info("变更长期医嘱信息 出参：" + res);
        return res;

    }
    /**
     *       住院诊断搜索接口
     * @param req
     */
    @RequestMapping(value="getRoutineDiagnosis",produces="application/json;charset=utf-8", method = RequestMethod.POST)
    @ResponseBody
    public String  getRoutineDiagnosis(@RequestBody Map<String, Object> req) throws CommonException {

        logger.info("住院诊断搜索接口 入参：" + req);
        String res = hospitaliztionService.getRoutineDiagnosis(req);
        logger.info("住院诊断搜索接口 出参：" + res);
        return res;

    }
    /**
     *       提交复核医嘱
     * @param req
     */
    @RequestMapping(value="submissionReview",produces="application/json;charset=utf-8", method = RequestMethod.POST)
    @ResponseBody
    public String  submissionReview(@RequestBody Map<String, Object> req) throws CommonException {

        logger.info("提交复核医嘱 入参：" + req);
        String res = hospitaliztionService.submissionReview(req);
        logger.info("提交复核医嘱 出参：" + res);
        return res;

    }
    /**
     *       取消复核医嘱
     * @param req
     */
    @RequestMapping(value="cancelSubmissionReview",produces="application/json;charset=utf-8", method = RequestMethod.POST)
    @ResponseBody
    public String  cancelSubmissionReview(@RequestBody Map<String, Object> req) throws CommonException {

        logger.info("取消复核医嘱 入参：" + req);
        String res = hospitaliztionService.cancelSubmissionReview(req);
        logger.info("取消复核医嘱 出参：" + res);
        return res;

    }
    /**
     *       转床列表（能床号 床位性别搜索）
     * @param req
     */
    @RequestMapping(value="getRotatingBed",produces="application/json;charset=utf-8", method = RequestMethod.POST)
    @ResponseBody
    public String  getRotatingBed(@RequestBody Map<String, Object> req) throws CommonException {

        logger.info("转床列表 入参：" + req);
        String res = hospitaliztionService.getRotatingBed(req);
        logger.info("转床列表 出参：" + res);
        return res;

    }

    /**
     *       提交转床
     * @param req
     */
    @RequestMapping(value="submissionRotatingBed",produces="application/json;charset=utf-8", method = RequestMethod.POST)
    @ResponseBody
    public String  submissionRotatingBed(@RequestBody Map<String, Object> req) throws CommonException {

        logger.info("提交转床 入参：" + req);
        String res = hospitaliztionService.submissionRotatingBed(req);
        logger.info("提交转床 出参：" + res);
        return res;

    }
    /**
     *       获取指定住院患者出院带药列表（能根据开医嘱时间或医嘱项目搜索）
     * @param req
     */
    @RequestMapping(value="getDischargeMedication",produces="application/json;charset=utf-8", method = RequestMethod.POST)
    @ResponseBody
    public String  getDischargeMedication(@RequestBody Map<String, Object> req) throws CommonException {

        logger.info("获取指定住院患者出院带药列表 入参：" + req);
        String res = hospitaliztionService.getDischargeMedication(req);
        logger.info("获取指定住院患者出院带药列表 出参：" + res);
        return res;

    }

    /**
     *       新增住院诊断
     * @param req
     */
    @RequestMapping(value="addHospitalizDiagnosis",produces="application/json;charset=utf-8", method = RequestMethod.POST)
    @ResponseBody
    public String  addHospitalizDiagnosis(@RequestBody Map<String, Object> req) throws CommonException {

        logger.info("新增住院诊断 入参：" + req);
        String res = hospitaliztionService.addHospitalizDiagnosis(req);
        logger.info("新增住院诊断 出参：" + res);
        return res;

    }
    /**
     *       变更住院诊断
     * @param req
     */
    @RequestMapping(value="modifyHospitalizDiagnosis",produces="application/json;charset=utf-8", method = RequestMethod.POST)
    @ResponseBody
    public String  modifyHospitalizDiagnosis(@RequestBody Map<String, Object> req) throws CommonException {

        logger.info("变更住院诊断 入参：" + req);
        String res = hospitaliztionService.modifyHospitalizDiagnosis(req);
        logger.info("变更住院诊断 出参：" + res);
        return res;

    }
    /**
     *       删除住院诊断
     * @param req
     */
    @RequestMapping(value="removeHospitalizDiagnosis",produces="application/json;charset=utf-8", method = RequestMethod.POST)
    @ResponseBody
    public String  removeHospitalizDiagnosis(@RequestBody Map<String, Object> req) throws CommonException {

        logger.info("删除住院诊断 入参：" + req);
        String res = hospitaliztionService.removeHospitalizDiagnosis(req);
        logger.info("删除住院诊断 出参：" + res);
        return res;

    }
    /**
     *       获取待分配病人列表
     * @param req
     */
    @RequestMapping(value="waitingDistribution",produces="application/json;charset=utf-8", method = RequestMethod.POST)
    @ResponseBody
    public String  waitingDistribution(@RequestBody Map<String, Object> req) throws CommonException {

        logger.info("获取待分配病人列表 入参：" + req);
        String res = hospitaliztionService.waitingDistribution(req);
        logger.info("获取待分配病人列表 出参：" + res);
        return res;

    }

    /**
     *  一日清单
     * @param req
     * @return
     */
    @RequestMapping(value="onedayBillAll",produces="application/json;charset=utf-8", method = RequestMethod.POST)
    @ResponseBody
    public String  onedayBillAll(@RequestBody Map<String, Object> req) throws CommonException {
        logger.info("一日清单 入参：" + req);
        String res = hospitaliztionService.onedayBillAll(req);
        logger.info("一日清单 出参：" + res);
        return res;
    }
    /**
     *  一日清单明细
     * @param req
     * @return
     */
    @RequestMapping(value="onedayBill",produces="application/json;charset=utf-8", method = RequestMethod.POST)
    @ResponseBody
    public String  onedayBill(@RequestBody Map<String, Object> req) throws CommonException {
        logger.info("一日清单明细 入参：" + req);
        String res = hospitaliztionService.onedayBill(req);
        logger.info("一日清单明细 出参：" + res);
        return res;
    }
    /**
     *  住院历史记录
     * @param req
     * @return
     */
    @RequestMapping(value="listHospitalizationRecord",produces="application/json;charset=utf-8", method = RequestMethod.POST)
    @ResponseBody
    public String  listHospitalizationRecord(@RequestBody Map<String, Object> req) throws CommonException {
        logger.info("住院历史记录 入参：" + req);
        String res = hospitaliztionService.listHospitalizationRecord(req);
        logger.info("住院历史记录 出参：" + res);
        return res;
    }
    /**
     *  费用明细
     * @param req
     * @return
     */
    @RequestMapping(value="listHospitalizationPayment",produces="application/json;charset=utf-8", method = RequestMethod.POST)
    @ResponseBody
    public String  listHospitalizationPayment(@RequestBody Map<String, Object> req) throws CommonException {
        logger.info("费用明细 入参：" + req);
        String res = hospitaliztionService.listHospitalizationPayment(req);
        logger.info("费用明细 出参：" + res);
        return res;
    }
    /**
     *  缴费记录查询
     * @param req
     * @return
     */
    @RequestMapping(value="listPaymentHistory",produces="application/json;charset=utf-8", method = RequestMethod.POST)
    @ResponseBody
    public String  listPaymentHistory(@RequestBody Map<String, Object> req) throws CommonException {
        logger.info("缴费记录查询 入参：" + req);
        String res = hospitaliztionService.listPaymentHistory(req);
        logger.info("缴费记录查询 出参：" + res);
        return res;
    }
    /**
     *  新增住院缴费
     * @param req
     * @return
     */
    @RequestMapping(value="addPaymentHistory",produces="application/json;charset=utf-8", method = RequestMethod.POST)
    @ResponseBody
    public String  addPaymentHistory(@RequestBody Map<String, Object> req) throws CommonException {
        logger.info("新增住院缴费 入参：" + req);
        String res = hospitaliztionService.addPaymentHistory(req);
        logger.info("新增住院缴费 出参：" + res);
        return res;
    }

}
