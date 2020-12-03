package com.bsoft.service.impl;

import com.bsoft.exception.CommonException;
import com.bsoft.mapper.business.HospitaliztionMapper;
import com.bsoft.service.HospitaliztionService;
import com.bsoft.utrl.ConmonUtil;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.lang.reflect.Array;
import java.util.*;

import static java.util.Arrays.asList;

/**
 * @author liujx
 * @date 2019/4/26
 */
@Service
@EnableTransactionManagement
public class HospitaliztionServiceImpl extends ConmonUtil implements HospitaliztionService {
    @Resource
    HospitaliztionMapper hospitaliztionMapper;


    /**
     * 获取住院患者列表
     *
     * @param req
     */
    @Override
    public String getInpatients(Map<String, Object> req) throws CommonException {
        isNUll(req, "hospitalId");
        //分页
        pagination(req);
        List<Map<String, Object>> list = hospitaliztionMapper.getInpatients(req);
        return getSuccessPagination(list);
    }

    /**
     * 获取住院患者基本信息
     *
     * @param req
     */
    @Override
    public String getPatientInfo(Map<String, Object> req) throws CommonException {
        //判断必填项是否为空
        isNUll(req, "hospitalizNo");
        isNUll(req, "hospitalId");
        List<Map<String, Object>> paatientInfo = hospitaliztionMapper.getPatientInfo(req);
        if (paatientInfo != null && paatientInfo.size() == 1) {
            return getSuccess(paatientInfo.get(0));
        } else if (paatientInfo.size() > 1) {
            throw new CommonException("业务数据错误，查询到多条数据");

        } else {
            throw new CommonException("无此病人");

        }
    }

    /**
     * 获取住院患者住院详情
     *
     * @param req
     */
    @Override
    public String getHospitalizationInfo(Map<String, Object> req) throws CommonException {
        //判断必填项是否为空
        isNUll(req, "hospitalizNo");
        isNUll(req, "hospitalId");
        List<Map<String, Object>> paatientInfo = hospitaliztionMapper.getHospitalizationInfo(req);
        if (paatientInfo != null && paatientInfo.size() == 1) {
            return getSuccess(paatientInfo.get(0));
        } else if (paatientInfo.size() > 1) {
            throw new CommonException("业务数据错误，查询到多条数据");

        } else {
            throw new CommonException("无此病人");

        }
    }

    /**
     * 获取住院患者缴费记录（能根据缴款时间或收据号搜索）
     *
     * @param req
     */
    @Override
    public String getPatientPayRecords(Map<String, Object> req) throws CommonException {

        isNUll(req, "patientId");
        isNUll(req, "hospitalId");
        //分页
        pagination(req);
        List<Map<String, Object>> list = hospitaliztionMapper.getPatientPayRecords(req);
        return getSuccessPagination(list);
    }

    /**
     * 获取医嘱列表  包括长期和临时  包括医嘱明细
     *
     * @param req
     */
    @Override
    public String getPatientAdviceList(Map<String, Object> req) throws CommonException {

        isNUll(req, "hospitalizNo");
        isNUll(req, "hospitalId");
        //分页
        pagination(req);
        List<Map<String, Object>> list = hospitaliztionMapper.getPatientAdviceList(req);
        return getSuccessPagination(list);
    }

    /**
     * --获取住院诊断列表
     *
     * @param req
     */
    @Override
    public String getHospitalizDiagnosis(Map<String, Object> req) throws CommonException {

        isNUll(req, "hospitalizNo");
        isNUll(req, "hospitalId");
        //分页
        pagination(req);
        List<Map<String, Object>> list = hospitaliztionMapper.getHospitalizDiagnosis(req);
        return getSuccessPagination(list);
    }

    /**
     * 医嘱删除（长期和临时）
     *
     * @param req
     */
    @Override
    public String removePatientAdvice(Map<String, Object> req) throws CommonException {
        int n = hospitaliztionMapper.removePatientAdvice(req);
        if (n == 0) {
            throw new CommonException("根据id无法查找到未复核的医嘱");
        }
        return getSuccess("成功删除" + n + "条医嘱");
    }

    /**
     * 医嘱删除（长期和临时）
     *
     * @param req
     */
    @Override
    public String addPatientAdvice(Map<String, Object> req) throws CommonException {

        isNUll(req, "hospitalId");
        isNUll(req, "hospitalizNo");
        isNUll(req, "adviceName");
        isNUll(req, "drugsNo");
        isNUll(req, "productAddr");
        isNUll(req, "programType");
        isNUll(req, "drugsType");
        isNUll(req, "dayTimes");
        isNUll(req, "onceDose");
        isNUll(req, "onceNum");
        isNUll(req, "weekTimes");
        isNUll(req, "createTime");
        isNUll(req, "drugsUnitPrice");
        isNUll(req, "drugsUsage");
        isNUll(req, "adviceDoctor");
        isNUll(req, "createUser");
        isNUll(req, "useFlag");
        isNUll(req, "inputDept");
        isNUll(req, "selfPay");
        isNUll(req, "medicalSkillMain");
        isNUll(req, "mediclSkillNo");
        isNUll(req, "adviceGroupNo");
        isNUll(req, "frequency");
        isNUll(req, "dispensProperty");
        isNUll(req, "adviceType");
        isNUll(req, "hisFlag");
        isNUll(req, "adviceFlag");
        isNUll(req, "chargingFlag");
        isNUll(req, "doctorAdviceFlag");
        isNUll(req, "doctorCommitFlag");
        isNUll(req, "adviceOrder");
        isNUll(req, "cancelFlag");
        isNUll(req, "firstTimes");
        isNUll(req, "patientDept");
        isNUll(req, "patientArea");
        isNUll(req, "patientBedNo");
        isNUll(req, "checkFlag");
        isNUll(req, "hospitalId");
        change2LocalHospitalId(req);
        //获取主键
        Long jlxh = getIdentity("zy", "ZY_BQYZ");
        Long yzzh = getIdentity("bq", "BQ_YZZH");
        req.put("jlxh",jlxh);
        req.put("yzzh",yzzh);
        int n = hospitaliztionMapper.addPatientAdvice(req);
        if (n == 0) {
            throw new CommonException("添加医嘱失败");
        }
        return getSuccess("成功添加" + n + "条医嘱");
    }

    /**
     * 医嘱修改（长期和临时）
     *
     * @param req
     */
    @Override
    public String modifyPatientAdvice(Map<String, Object> req) throws CommonException {
        isNUll(req, "id");
        isNUll(req, "hospitalId");
        isNUll(req, "hospitalizNo");
        isNUll(req, "adviceName");
        isNUll(req, "drugsNo");
        isNUll(req, "productAddr");
        isNUll(req, "programType");
        isNUll(req, "drugsType");
        isNUll(req, "dayTimes");
        isNUll(req, "onceDose");
        isNUll(req, "onceNum");
        isNUll(req, "weekTimes");
        isNUll(req, "createTime");
        isNUll(req, "drugsUnitPrice");
        isNUll(req, "drugsUsage");
        isNUll(req, "adviceDoctor");
        isNUll(req, "createUser");
        isNUll(req, "useFlag");
        isNUll(req, "inputDept");
        isNUll(req, "selfPay");
        isNUll(req, "medicalSkillMain");
        isNUll(req, "mediclSkillNo");
        isNUll(req, "adviceGroupNo");
        isNUll(req, "frequency");
        isNUll(req, "dispensProperty");
        isNUll(req, "adviceType");
        isNUll(req, "hisFlag");
        isNUll(req, "adviceFlag");
        isNUll(req, "chargingFlag");
        isNUll(req, "doctorAdviceFlag");
        isNUll(req, "doctorCommitFlag");
        isNUll(req, "adviceOrder");
        isNUll(req, "cancelFlag");
        isNUll(req, "firstTimes");
        isNUll(req, "patientDept");
        isNUll(req, "patientArea");
        isNUll(req, "patientBedNo");
        isNUll(req, "checkFlag");
        change2LocalHospitalId(req);

        int n = hospitaliztionMapper.modifyPatientAdvice(req);
        if (n == 0) {
            throw new CommonException("根据id无法查找到未复核的医嘱");
        }
        return getSuccess("成功更新" + n + "条医嘱");
    }

    /**
     * 住院诊断搜索接口
     *
     * @param req
     */
    @Override
    public String getRoutineDiagnosis(Map<String, Object> req) throws CommonException {
        //分页
        pagination(req);
        List<Map<String, Object>> list = hospitaliztionMapper.getRoutineDiagnosis(req);
        return getSuccessPagination(list);
    }

    /**
     * 提交复核医嘱
     *
     * @param req
     */
    @Override
    public String submissionReview(Map<String, Object> req) throws CommonException {
        isNUll(req, "checkUser");
        isNUll(req, "checkTime");
        isNUll(req, "adviceGroupNo");

        int n = hospitaliztionMapper.submissionReview(req);
        if (n == 0) {
            throw new CommonException("未查询到未复核医嘱");
        }

        return getSuccess("成功提交" + n + "条医嘱");

    }

    /**
     * 取消复核医嘱
     *
     * @param req
     */
    @Override
    public String cancelSubmissionReview(Map<String, Object> req) throws CommonException {
        isNUll(req, "hospitalizNo");
        isNUll(req, "adviceGroupNo");

        int n = hospitaliztionMapper.cancelSubmissionReview(req);
        if (n == 0) {
            throw new CommonException("未查询到复核医嘱");
        }

        return getSuccess("成功取消" + n + "条医嘱");

    }

    /**
     * 转床列表（能床号 床位性别搜索）
     *
     * @param req
     */
    @Override
    public String getRotatingBed(Map<String, Object> req) throws CommonException {
        isNUll(req, "hospitalId");
        isNUll(req, "depCode");
        //分页
        pagination(req);
        List<Map<String, Object>> list = hospitaliztionMapper.getRotatingBed(req);
        return getSuccessPagination(list);
    }

    /**
     * 提交转床
     *
     * @param req
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String submissionRotatingBed(Map<String, Object> req) throws CommonException {
        isNUll(req, "bedNumber");
        isNUll(req, "hospitalId");
        isNUll(req, "hospitalizNo");
        isNUll(req, "depCode");
        int i = hospitaliztionMapper.submissionRotatingBed2(req);
        if (i == 0) {
            throw new CommonException("未查询病区有该床位的空床");
        } else if (i > 1) {
            throw new CommonException("错误，根据条件查询到床位有2个");
        }
        i = hospitaliztionMapper.submissionRotatingBed1(req);
        if (i == 0) {
            throw new CommonException("未找到该住院号病人");
        }
        return getSuccess("转床成功");
    }

    /**
     * 获取指定住院患者出院带药列表（能根据开医嘱时间或医嘱项目搜索）
     *
     * @param req
     */
    @Override
    public String getDischargeMedication(Map<String, Object> req) throws CommonException {

        isNUll(req, "hospitalizNo");
        //分页
        pagination(req);
        List<Map<String, Object>> list = hospitaliztionMapper.getDischargeMedication(req);
        return getSuccessPagination(list);
    }


    /**
     * 新增住院诊断
     *
     * @param req
     */
    @Override
    public String addHospitalizDiagnosis(Map<String, Object> req) throws CommonException {
        isNUll(req,asList("hospitalizNo","diseaseNo","hospitalId","diagnosticTypes","diagnosticCategories","medicalCategory","diseaseName","doctorId","diagnosis","diagnosticTime","morbidityTime"));
        //转换时间格式
        stringToDate(req,"diagnosticTime");
        stringToDate(req,"morbidityTime");

        change2LocalHospitalId(req);
        //查询该人的病案号码，和诊断的顺序号
        List<Map<String,Object>> list=hospitaliztionMapper.addHospitalizDiagnosis1(req);
        if (list.get(0)==null) {
            throw new CommonException("无法找到该住院号的在院病人");
        }
        req.putAll(list.get(0));
        //获取主键JLBH,ZDXH
        Long JLBH = getIdentity("ys", "YS_ZY_JBZD");
        req.put("JLBH",JLBH);
        if(req.get("ZDXH")==null||req.get("ZDXH").toString().equals("")) {
            Long ZDXH = getIdentity("ys", "YS_ZY_JBZD_ZDXH");
            req.put("ZDXH", ZDXH);
        }
        int n = hospitaliztionMapper.addHospitalizDiagnosis(req);
        if (n == 0) {
            throw new CommonException("写入诊断失败");
        }

        return getSuccess("成功写入" + n + "条诊断");

    }

    /**
     * 变更住院诊断
     *
     * @param req
     */
    @Override
    public String modifyHospitalizDiagnosis(Map<String, Object> req) throws CommonException {
        isNUll(req,asList("recordNumber","hospitalizNo","diseaseNo","hospitalId","diagnosticTypes","medicalCategory","diseaseName","doctorId","diagnosis","morbidityTime"));
        //转换时间格式
        stringToDate(req,"diagnosticTime");
        stringToDate(req,"morbidityTime");

        int n = hospitaliztionMapper.modifyHospitalizDiagnosis(req);
        if (n == 0) {
            throw new CommonException("未找到该诊断");
        }

        return getSuccess("成功修改" + n + "条诊断");

    }

    /**
     * 删除住院诊断
     *
     * @param req
     */
    @Override
    public String removeHospitalizDiagnosis(Map<String, Object> req) throws CommonException {
        isNUll(req, "hospitalizNo");
        isNUll(req, "recordNumber");
        isNUll(req, "hospitalId");
        int n = hospitaliztionMapper.removeHospitalizDiagnosis(req);
        if (n == 0) {
            throw new CommonException("未找到该诊断");
        }

        return getSuccess("成功删除" + n + "条诊断");

    }


    /**
     * 获取待分配病人列表
     *
     * @param req
     */
    @Override
    public String waitingDistribution(Map<String, Object> req) throws CommonException {

        isNUll(req, "hospitalId");
        isNUll(req, "patientDept");
        //分页
        pagination(req);
        List<Map<String, Object>> list = hospitaliztionMapper.waitingDistribution(req);
        return getSuccessPagination(list);
    }

    /**
     * 一日清单
     *
     * @param req
     */
    @Override
    public String onedayBillAll(Map<String, Object> req) throws CommonException {

        isNUll(req, "hospitalizNo");
        //分页
        pagination(req);
        List<Map<String, Object>> list = hospitaliztionMapper.onedayBillAll(req);
        return getSuccessPagination(list);
    }
    /**
     * 一日清单明细
     *
     * @param req
     */
    @Override
    public String onedayBill(Map<String, Object> req) throws CommonException {

        isNUll(req, "hospitalizNo");
        isNUll(req, "costDate");
        //分页
        pagination(req);
        List<Map<String, Object>> list = hospitaliztionMapper.onedayBill(req);
        return getSuccessPagination(list);
    }
    /**
     * 住院历史记录
     *
     * @param req
     */
    @Override
    public String listHospitalizationRecord(Map<String, Object> req) throws CommonException {
        if(req.get("EID")==null||req.get("EID").toString().equals("")) {
            isNUll(req, "patientId", "idCard", "hospitalizNo","outpatientNo");
        }
        if(req.get("idCard")!=null&&!req.get("idCard").toString().equals("")){
            toCharacterString(req,"idCard");
        }
        //分页
        pagination(req);
        List<Map<String, Object>> list = hospitaliztionMapper.listHospitalizationRecord(req);
        return getSuccessPagination(list);
    }

    /**
     * 费用明细
     *
     * @param req
     */
    @Override
    public String listHospitalizationPayment(Map<String, Object> req) throws CommonException {

        isNUll(req, "hospitalizNo");
        //分页
        pagination(req);
        List<Map<String, Object>> list = hospitaliztionMapper.listHospitalizationPayment(req);
        return getSuccessPagination(list);
    }

    /**
     * 缴费记录查询
     *
     * @param req
     */
    @Override
    public String listPaymentHistory(Map<String, Object> req) throws CommonException {

        isNUll(req, "hospitalizNo");
        //分页
        pagination(req);
        List<Map<String, Object>> list = hospitaliztionMapper.listPaymentHistory(req);
        return getSuccessPagination(list);
    }

    /**
     * 新增预交款
     *
     * @param req
     */
    @Override
    @Transactional(rollbackFor = Exception.class)

    public String addPaymentHistory(Map<String, Object> req) throws CommonException {
        isNUll(req, asList("hospitalId", "hospitalizNo", "payedTime", "payAmount", "paymentMethod", "dataSources","orderNumber"));
        stringToDate(req,"payedTime");
        req=change2LocalHospitalId(req);
        String czgh;// VARCHAR2(100); --操作工号
        String sjhm;// VARCHAR2(100); --住院收据号码
        Date lyrq;// DATE ;--领用时间
        String zzhm;// VARCHAR2(100) ;--终止号码
        String syhm;// VARCHAR2(100) ;--使用号码
        String zffs;// VARCHAR2(100);--支付方式
        String zyh;//  VARCHAR2(100);--住院号
        List<Map<String,Object>> list=new ArrayList();
        //1查询该住院号是否在院
        list=hospitaliztionMapper.addPaymentHistory1(req);
        if(list.size()<1){
            throw new CommonException("未找到该住院正在住院的病人");
        }
        //2获取支付方式
        List<Map<String,Object>> zffsList=hospitaliztionMapper.getZffs(req);
        if(zffsList.size()<1){
            throw new CommonException("支付方式paymentMethod输入不对");
        }
        zffs = zffsList.get(0).get("FKFS").toString();

        //3获取系统参数的工号
        czgh = hospitaliztionMapper.getXtcs(xtcsMap(req.get("dataSources").toString()));
        if (czgh == null || czgh.equals("")) {
            throw new CommonException("'机构未设置" + req.get("dataSources").toString() + "对应数据来源的系统参数");
        }
       // 4根据工号获取发票号码
        List<Map<String, Object>> ms_ygpj = hospitaliztionMapper.addPaymentHistory2(czgh);
        if (ms_ygpj.size() < 1) {
            throw new CommonException("机构未设置" + czgh + "员工的住院发票");
        }
        syhm = ms_ygpj.get(0).get("DQHM").toString();
        if (syhm.equals(ms_ygpj.get(0).get("ZZHM").toString())) {
            //使用号码等于终止号码，说明是最后一张发票打上sypb
            int n = hospitaliztionMapper.addPaymentHistory3(ms_ygpj.get(0));
            if (n != 1) {
                throw new CommonException("更新发票号码错误");
            }
        } else {
            //发票未使用发票号完加一,再写回ms_ygpj
            ms_ygpj.get(0).put("DQHM", addOne(syhm));
            int n = hospitaliztionMapper.addPaymentHistory4(ms_ygpj.get(0));
            if (n != 1) {
                throw new CommonException("更新发票号码错误");
            }
        }
        //5写zy_tbkk
        //获取zy_tbkk主键jkxh
        Long jkxh = getIdentity("zy", "ZY_TBKK");
        Map<String,Object> zy_tbkk=new HashMap();
        zy_tbkk.put("jkxh",jkxh);
        zy_tbkk.put("zyh",req.get("hospitalizNo").toString());
        zy_tbkk.put("jkje",req.get("payAmount"));
        zy_tbkk.put("jkfs",zffs);
        zy_tbkk.put("sjhm",syhm);
        zy_tbkk.put("czgh",czgh);
        zy_tbkk.put("jgid",change2LocalHospitalId());
        zy_tbkk.put("plateformnum",req.get("orderNumber"));


        int n = hospitaliztionMapper.addPaymentHistory5(zy_tbkk);
        if (n != 1) {
            throw new CommonException("插入zy_tbkk失败");
        }
        Map<String,Object> back=new HashMap<>();
        back.put("invoiceNumber",syhm);

//
//        //写jhzf_log
//        String jlxh = getIdentity("jhzf", "JHZF_LOG").toString();
//        Map<String,Object> JHZF_LOG=new HashMap<>();
//        JHZF_LOG.put("jlxh",jlxh);
//        JHZF_LOG.put("trade_no",req.get("orderNumber"));
//        JHZF_LOG.put("trade_id",req.get("orderNumber"));
//        JHZF_LOG.put("trade_type","3");
//        JHZF_LOG.put("his_fphm",syhm);
//        JHZF_LOG.put("his_djh",syhm);
//        JHZF_LOG.put("trade_user",czgh);
//        JHZF_LOG.put("charge_type","1");
//        JHZF_LOG.put("trade_amount",req.get("payAmount"));
//        JHZF_LOG.put("his_operate","3");
//        JHZF_LOG.put("pay_type","1");
//
//        if(req.get("dataSources").toString().equals("CZGH_GZH_QY")||req.get("dataSources").toString().equals("CZGH_GZH_YY")) {
//            hospitaliztionMapper.saveJHZF_LOG(JHZF_LOG);
//        }

        JSONObject json = new JSONObject();
        json.putAll(back);
        return getSuccess(json );

    }
}