package com.bsoft.service;


import com.bsoft.exception.CommonException;

import java.util.Map;

/**
 * 住院费用接口
 * @author liujx
 * @date 2019/4/26
 */
public interface HospitaliztionService {

        /**
     * 获取住院患者列表
     * @param req
     */
    String getInpatients(Map<String, Object> req) throws CommonException;
    /**
     * 获取住院患者基本信息
     * @param req
     */
    String getPatientInfo(Map<String, Object> req) throws CommonException;
    /**
     * 获取住院患者住院详情
     * @param req
     */
    String getHospitalizationInfo(Map<String, Object> req) throws CommonException;
    /**
     * 获取住院患者缴费记录（能根据缴款时间或收据号搜索）
     * @param req
     */
    String getPatientPayRecords(Map<String, Object> req) throws CommonException;
    /**
     * 获取遗嘱列表  包括长期和临时  包括遗嘱明细
     * @param req
     */
    String getPatientAdviceList(Map<String, Object> req) throws CommonException;
    /**
     * --获取住院诊断列表
     * @param req
     */
    String getHospitalizDiagnosis(Map<String, Object> req) throws CommonException;
    /**
     * 医嘱删除（长期和临时）
     * @param req
     */
   String removePatientAdvice(Map<String, Object> req) throws CommonException;

    /**
     *   医嘱添加（长期和临时）
     * @param req
     */
    String addPatientAdvice(Map<String, Object> req) throws CommonException;
    /**
     *   医嘱修改（长期和临时）
     * @param req
     */
    String modifyPatientAdvice(Map<String, Object> req) throws CommonException;
    /**
     *   住院诊断搜索接口
     * @param req
     */
    String getRoutineDiagnosis(Map<String, Object> req) throws CommonException;
    /**
     *   提交复核医嘱
     * @param req
     */
    String submissionReview(Map<String, Object> req) throws CommonException;
    /**
     *   取消复核医嘱
     * @param req
     */
    String cancelSubmissionReview(Map<String, Object> req) throws CommonException;

    /**
     *   转床列表（能床号 床位性别搜索）
     * @param req
     */
    String getRotatingBed(Map<String, Object> req) throws CommonException;

    /**
     *   提交转床
     * @param req
     */
    String submissionRotatingBed(Map<String, Object> req) throws CommonException;
    /**
     *   获取指定住院患者出院带药列表（能根据开医嘱时间或医嘱项目搜索）
     * @param req
     */
    String getDischargeMedication(Map<String, Object> req) throws CommonException;
    /**
     *   新增住院诊断
     * @param req
     */
    String addHospitalizDiagnosis(Map<String, Object> req) throws CommonException;
    /**
     *   变更住院诊断
     * @param req
     */
    String modifyHospitalizDiagnosis(Map<String, Object> req) throws CommonException;
    /**
     *   删除住院诊断
     * @param req
     */
    String removeHospitalizDiagnosis(Map<String, Object> req) throws CommonException;
    /**
     *   获取待分配病人列表
     * @param req
     */
    String waitingDistribution(Map<String, Object> req) throws CommonException;
    /**
     *   一日清单
     * @param req
     */
    String onedayBillAll(Map<String, Object> req) throws CommonException;
    /**
     *   一日清单明细
     * @param req
     */
    String onedayBill(Map<String, Object> req) throws CommonException;
    /**
     *   住院历史记录
     * @param req
     */
    String listHospitalizationRecord(Map<String, Object> req) throws CommonException;
    /**
     *   费用明细
     * @param req
     */
    String listHospitalizationPayment(Map<String, Object> req) throws CommonException;
    /**
     *   缴费记录查询
     * @param req
     */
    String listPaymentHistory(Map<String, Object> req) throws CommonException;
    /**
     *   新增缴费记录
     * @param req
     */
    String addPaymentHistory(Map<String, Object> req) throws CommonException;


}
