package com.bsoft.mapper.business;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author liujx
 * @date 2019/4/26
 */
@Repository
public interface ClinicPaymentMapper {

    /**
     * 门诊就诊记录
     * @param req
     * @return
     */
    List<Map<String, Object>> getMedicalRecords(Map<String, Object> req);
    /**
     * 获取处方支付列表
     * @param req
     * @return
     */
    List<Map<String, Object>> getPrescriptionPayment(Map<String, Object> req);
    /**
     * 获取处置支付列表
     * @param req
     * @return
     */
    List<Map<String, Object>> getDisposalPayment(Map<String, Object> req);
    /**
     * 获取处方明细列表
     * @param req
     * @return
     */
    List<Map<String, Object>> getPrescriptionDetailed(Map<String, Object> req);
    /**
     * 获取处置明细列表
     * @param req
     * @return
     */
    List<Map<String, Object>> getDisposalDetailed(Map<String, Object> req);


    /**
     * 结算处置
     * @param req
     * @return
     */
    List<Map<String, Object>> settlementDisposal(Map<String, Object> req);

    /**
     * 获取处方已支付列表
     * @param req
     * @return
     */
    List<Map<String, Object>> getPrescriptionPaymented(Map<String, Object> req);
    /**
     * 获取处置已支付列表
     * @param req
     * @return
     */
    List<Map<String, Object>> getDisposalPaymented(Map<String, Object> req);
    /**
     * 取消处方结算(根据发票号码查询该发票的处方是否发药或者已经作废)
     * @param req
     * @return
     */
    List<Map<String, Object>> getDispensing(Map<String, Object> req);
    /**
     * 取消处置结算(根据发票号码查询该发票的处置是否执行或者已经作废)
     * @param req
     * @return
     */
    List<Map<String, Object>> getImplement(Map<String, Object> req);
    /**
     * 取消结算(查询处方金额)
     * @param req
     * @return
     */
    Double getPrescriptionAmount(Map<String, Object> req);

    /**
     * 写ms_zffp
     * @param req
     * @return
     */
    int invalidInvoice1(Map<String, Object> req);

    /**
     * 作废ms_mzxx
     * @param req
     * @return
     */
    int invalidInvoice2(Map<String, Object> req);

    /**
     * 作废处方01
     * @param req
     * @return
     */
    int invalidInvoice3(Map<String, Object> req);

    /**
     * 作废医技01
     * @param req
     * @return
     */
    int invalidInvoice4(Map<String, Object> req);
    /**
     * //查询系统参数
     * @param req
     * @return
     */
    String getXtcs(Map<String, Object> req);
    /**
     * 查询传入金额是否与处方号条件下的金额一样
     * @param req
     * @return
     */
    Double settlementPrescription(Map<String, Object> req);
    /**
     * //查询处方金额
     * @param req
     * @return
     */
    Double settlementPrescription1(Map<String, Object> req);
    /**
     * //查询处置金额
     * @param req
     * @return
     */
    Double settlementDisposal1(Map<String, Object> req);


    /**
     * //根据工号查询发票号码
     * @param req
     * @return
     */
    List<Map<String, Object>> settlementPrescription2(String req);
    /**
     * 使用号码等于终止号码，说明是最后一张发票打上sypb
     * @param req
     * @return
     */
    int  settlementPrescription3(Map<String, Object> req);
    /**
     * 发票未使用发票号完加一
     * @param req
     * @return
     */
    int  settlementPrescription4(Map<String, Object> req);
    /**
     * //根据就诊序号从挂号里获取brid，BRXM，BRXB，MZLB，GHGL
     * @param req
     * @return
     */
    List<Map<String, Object>> settlementPrescription5(Map<String, Object> req);
    /**
     * 写ms_mzxx
     * @param req
     * @return
     */
    int  settlementPrescription6(Map<String, Object> req);
    /**
     * 处方写MS_SFMX
     * @param req
     * @return
     */
    int  settlementPrescription7(Map<String, Object> req);
    /**
     * 处置写MS_SFMX
     * @param req
     * @return
     */
    int  settlementDisposal7(Map<String, Object> req);
    /**
     * 写ms_fkxx
     * @param req
     * @return
     */
    int  settlementPrescription8(Map<String, Object> req);
    /**
     * ms_cf01 写上门诊序号和发票号码
     * @param req
     * @return
     */
    int  settlementPrescription9(Map<String, Object> req);
    /**
     * ms_yj011 写上门诊序号和发票号码
     * @param req
     * @return
     */
    int  settlementDisposal9(Map<String, Object> req);
    /**
     * ms_cf02 写上发票号码
     * @param req
     * @return
     */
    int  settlementPrescription13(Map<String, Object> req);
    /**
     * ms_yj012 写上发票号码
     * @param req
     * @return
     */
    int  settlementDisposal13(Map<String, Object> req);
    /**
     * 根据科室分组，只取3个费用归并的处方识别(然后循环每个科室没三个费用归并写一张发票)
     * @param req
     * @return
     */
    List<Map<String, Object>>  settlementPrescription10(Map<String, Object> req);
    /**
     * 根据科室分组，只取3个费用归并的处方识别(然后循环每个科室没三个费用归并写一张发票)
     * @param req
     * @return
     */
    List<Map<String, Object>>  settlementPrescription10_1(Map<String, Object> req);
    /**
     * 根据科室分组，查询处方科室的处方
     * @param req
     * @return
     */
    String  settlementPrescription11(Map<String, Object> req);
    /**
     * 根据科室分组，查询处置科室的处置
     * @param req
     * @return
     */
    String  settlementPrescription12(Map<String, Object> req);
    /**
     * 就诊历史列表
     * @param req
     * @return
     */
    List<Map<String, Object>> medicalRecordsList(Map<String, Object> req);
    /**
     * 就诊历史详情
     * @param req
     * @return
     */
    List<Map<String, Object>> medicalRecordsDetails(Map<String, Object> req);
    /**
     * 查询支付方式
     * @param req
     * @return
     */
    List<Map<String, Object>> getZffs(Map<String, Object> req);
    /**
     * his交易记录查询
     * @param req
     * @return
     */
    List<Map<String, Object>> getHisPay(Map<String, Object> req);

    List<Map<String, Object>> GetManagementInfo(Map<String, Object> paramMap);
    /**
     * 药 查询
     *
     * @param paramMap
     * @return
     */
    List<Map<String, Object>> getUseDrugs(Map<String, Object> paramMap);
    /**
     * //查询处方类型和药房识别
     * @param req
     * @return
     */
    List<Map<String, Object>> settlementPrescription14(Map<String, Object> req);
    /**
     * 查询发药窗口
     * @param req
     * @return
     */
    List<Map<String, Object>> settlementPrescription15(Map<String, Object> req);
    /**
     * update 01 减免标志
     * @param req
     * @return
     */
    int  updateJMBZ01(Map<String, Object> req);
    /**
     * update 02 CFYJ02S属性
     * @param req
     * @return
     */
    int  updateJMBZ02(Map<String, Object> req);
    /**
     * 写jhzf_log
     * @param req
     * @return
     */
    int saveJHZF_LOG(Map<String, Object> req);
    /**
     * 写ms_mzpj_nonprint
     * @param req
     * @return
     */
    int saveMs_mzpj_nonprint(Map<String, Object> req);
}
