package com.bsoft.service;

import com.bsoft.exception.CommonException;
import net.sf.json.JSONObject;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

/**
 * 诊间支付接口
 * @author liujx
 * @date 2019/4/26
 */
public interface ClinicPaymentService {

        /**
     * 门诊就诊记录
     * @param req
     */
    String getMedicalRecords(Map<String, Object> req) throws CommonException;
    /**
     * 获取处方支付列表
     * @param req
     */
    String getPrescriptionPayment(Map<String, Object> req) throws CommonException;
    /**
     * 获取处置支付列表
     * @param req
     */
    String getDisposalPayment(Map<String, Object> req) throws CommonException;
    /**
     * 获取处方明细列表
     * @param req
     */
    String getPrescriptionDetailed(Map<String, Object> req) throws CommonException;
    /**
     * 获取处置明细列表
     * @param req
     */
    String getDisposalDetailed(Map<String, Object> req) throws CommonException;
    /**
     * 结算处方
     * @param json
     */
    String settlementPrescription(JSONObject json) throws CommonException;
    /**
     * 结算处方
     * @param req
     */
    JSONObject settlementPrescriptionJson(Map<String, Object> req) throws CommonException;
    /**
     * 结算处置
     * @param req
     */
    String settlementDisposal(Map<String, Object> req) throws CommonException;
    /**
     * 获取处方已支付列表
     * @param req
     */
    String getPrescriptionPaymented(Map<String, Object> req) throws CommonException;
    /**
     * 获取处置已支付列表
     * @param req
     */
    String getDisposalPaymented(Map<String, Object> req) throws CommonException;
    /**
     * 取消处方结算
     * @param req
     */
    String cancelSettlementPrescription(Map<String, Object> req) throws CommonException;
    /**
     * 取消处置结算
     * @param req
     */
    String cancelSettlementDisposal(Map<String, Object> req) throws CommonException;
    /**
     * 就诊历史列表
     * @param req
     */
    String medicalRecordsList(Map<String, Object> req) throws CommonException;
    /**
     * 就诊历史详情
     * @param req
     */
    String medicalRecordsDetails(Map<String, Object> req) throws CommonException, IOException, SQLException;
    /**
     * his交易记录查询
     * @param req
     */
    String getHisPay(Map<String, Object> req) throws CommonException;
    /**
     * 门诊病人处置检索
     *
     * @param paramMap
     * @return
     */
    String GetManagementInfo(Map<String, Object> paramMap) throws CommonException;
    /**
     * 药 检索
     *
     * @param paramMap
     * @return
     */
    String getUseDrugs(Map<String, Object> paramMap) throws CommonException;
}
