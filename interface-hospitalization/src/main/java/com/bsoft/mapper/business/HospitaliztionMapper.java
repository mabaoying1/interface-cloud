package com.bsoft.mapper.business;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author liujx
 * @date 2019/4/26
 */
@Repository
public interface HospitaliztionMapper {

    /**
     * 查询住院患者列表-- 护士站
     * @param req
     * @return
     */
    List<Map<String, Object>> getInpatients(Map<String, Object> req);

    /**
     * 查询患者基本信息
     * @param req
     * @return
     */
    List<Map<String, Object>> getPatientInfo(Map<String, Object> req);
    /**
     * 查询患者住院详情
     * @param req
     * @return
     */
    List<Map<String, Object>> getHospitalizationInfo(Map<String, Object> req);
    /**
     * 获取住院患者缴费记录
     * @param req
     * @return
     */
    List<Map<String, Object>> getPatientPayRecords(Map<String, Object> req);
    /**
     * 获取遗嘱列表  包括长期和临时  包括遗嘱明细
     * @param req
     * @return
     */
    List<Map<String, Object>> getPatientAdviceList(Map<String, Object> req);
    /**
     * --获取住院诊断列表
     * @param req
     * @return
     */
    List<Map<String, Object>> getHospitalizDiagnosis(Map<String, Object> req);
    /**
     * 医嘱删除（长期和临时）
     * @param req
     */
      int removePatientAdvice(Map<String, Object> req);
    /**
     *   医嘱添加（长期和临时）
     * @param req
     */
      int addPatientAdvice(Map<String, Object> req);

    /**
     *   医嘱修改（长期和临时）
     * @param req
     */
     int modifyPatientAdvice(Map<String, Object> req);
    /**
     *   住院诊断搜索接口
     * @param req
     */
    List<Map<String, Object>> getRoutineDiagnosis(Map<String, Object> req);
    /**
     *   提交复核医嘱
     * @param req
     */
    int submissionReview(Map<String, Object> req);
    /**
     *   取消复核医嘱
     * @param req
     */
    int cancelSubmissionReview(Map<String, Object> req);
    /**
     *   转床列表（能床号 床位性别搜索）
     * @param req
     */
    List<Map<String, Object>> getRotatingBed(Map<String, Object> req);
    /**
     *   提交转床更新ZY_CWSZ
     * @param req
     */
    int submissionRotatingBed2(Map<String, Object> req);
    /**
     *   提交转床更新zy_brry
     * @param req
     */
    int submissionRotatingBed1(Map<String, Object> req);
    /**
     *   获取指定住院患者出院带药列表（能根据开医嘱时间或医嘱项目搜索）
     * @param req
     */
    List<Map<String, Object>> getDischargeMedication(Map<String, Object> req);

    /**
     *   新增住院诊断
     * @param req
     */
    int addHospitalizDiagnosis(Map<String, Object> req) ;

    /**
     *   查询住院诊断顺序号
     * @param req
     */
    List<Map<String, Object>> addHospitalizDiagnosis1(Map<String, Object> req) ;
    /**
     *   变更住院诊断
     * @param req
     */
    int modifyHospitalizDiagnosis(Map<String, Object> req) ;
    /**
     *   删除住院诊断
     * @param req
     */
    int removeHospitalizDiagnosis(Map<String, Object> req) ;
    /**
     *   获取待分配病人列表
     * @param req
     */
    List<Map<String, Object>>  waitingDistribution(Map<String, Object> req) ;

    /**
     *   一日清单
     * @param req
     */
    List<Map<String, Object>> onedayBillAll(Map<String, Object> req) ;
    /**
     *   一日清单明细
     * @param req
     */
    List<Map<String, Object>> onedayBill(Map<String, Object> req) ;
    /**
     *   住院历史记录
     * @param req
     */
    List<Map<String, Object>> listHospitalizationRecord(Map<String, Object> req) ;
    /**
     *   费用明细
     * @param req
     */
    List<Map<String, Object>> listHospitalizationPayment(Map<String, Object> req) ;
    /**
     *   缴费记录查询
     * @param req
     */
    List<Map<String, Object>> listPaymentHistory(Map<String, Object> req) ;
    /**
     * //查询系统参数
     * @param req
     * @return
     */
    String getXtcs(Map<String, Object> req);
    /**
     * 1查询该住院号是否在院
     * @param req
     * @return
     */
    List<Map<String, Object>> addPaymentHistory1(Map<String, Object> req);
    /**
     * //根据工号查询发票号码
     * @param req
     * @return
     */
    List<Map<String, Object>> addPaymentHistory2(String req);
    /**
     * 使用号码等于终止号码，说明是最后一张发票打上sypb
     * @param req
     * @return
     */
    int  addPaymentHistory3(Map<String, Object> req);
    /**
     * 发票未使用发票号完加一
     * @param req
     * @return
     */
    int  addPaymentHistory4(Map<String, Object> req);
    /**
     * 写zy_tbkk
     * @param req
     * @return
     */
    int  addPaymentHistory5(Map<String, Object> req);
    /**
     * 查询支付方式
     * @param req
     * @return
     */
    List<Map<String, Object>> getZffs(Map<String, Object> req);
    /**
     * 写jhzf_log
     * @param req
     * @return
     */
    int saveJHZF_LOG(Map<String, Object> req);
}

