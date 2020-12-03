package com.bsoft.service.impl;

import com.bsoft.exception.CommonException;
import com.bsoft.mapper.business.RegisterMapper;
import com.bsoft.mapper.ecis.EcisMapper;
import com.bsoft.service.RegisterService;
import com.bsoft.utrl.ConmonUtil;
import com.bsoft.utrl.JSONUtil;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 挂号实现类
 *
 * @author liujx
 * @date 2019/7/2
 */
@Service
@EnableTransactionManagement
public class RegisterServiceImpl extends ConmonUtil implements RegisterService {

    private static Logger logger = LoggerFactory.getLogger(RegisterServiceImpl.class);

    @Resource
    private RegisterMapper registerMapper;
    @Resource
    private EcisMapper esisMapper;

    /**
     * create by: hxy
     * description: 查询科室
     * create time: 2019/03/15 14:56
     *
     * @return a
     * @Param: null
     */
    @Override
    public String listDepartment(Map<String, Object> req) throws CommonException {
        //判断必填项是否为空
        isNUll(req, "hospitalId");
        //分页判断
        pagination(req);
        List<Map<String, Object>> department = registerMapper.listDepartment(req);
        return getSuccessPagination(department);
    }

    /**
     * create by: hxy
     * description: 机构查询
     * create time: 2019/03/15 14:57
     *
     * @eturn a
     * @Param: null
     */
    @Override
    public String ListOrg(Map<String, Object> req) throws CommonException {

        //分页判断
        pagination(req);
        List<Map<String, Object>> org = registerMapper.listOrg(req);
        return getSuccessPagination(org);
    }

    /**
     * create by: hxy
     * description: 患者查询
     * create time: 2019/03/15 14:57
     *
     * @eturn a
     * @Param: null
     */
    @Override
    public String patientIsMatch(Map<String, Object> req) throws CommonException {
        //判断必填项是否为空
//        isNUll(req, "idCard");
        List<Map<String, Object>> patientIsMatch = registerMapper.patientIsMatch(req);
        if (patientIsMatch.size() < 1) {
            logger.error("无该病人档案");
            throw new CommonException("无该病人档案");
        }
        return getSuccess(patientIsMatch.get(0));

    }

    /**
     * 查询医生列表
     *
     * @param req
     * @throws CommonException
     */
    @Override
    public String listDoctor(Map<String, Object> req) throws CommonException {
        //分页判断
        pagination(req);
        isNUll(req, "hospitalId");
        List<Map<String, Object>> doctorList = registerMapper.listDoctor(req);
        return getSuccessPagination(doctorList);
    }


    /**
     * 号源查询
     *
     * @param req
     * @throws CommonException
     */
    @Override
    public String queryDeptAndDoctor(Map<String, Object> req) throws CommonException {
        //分页判断
        pagination(req);
        isNUll(req, "hospitalId");
        isNUll(req, "registerEndDate");
        isNUll(req, "registerBeginDate");
        isNUll(req, "dataSources");
;
        req.put("jgid", req.get("hospitalId"));
        List<Map<String, Object>> doctorList = registerMapper.queryDeptAndDoctor(req);




        return getSuccessPagination(doctorList);
    }

    /**
     * 锁定号源
     *
     * @param req
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String lockNumber(Map<String, Object> req) {
        isNUll(req, "patientId");
        isNUll(req, "workId");
//        是否分时段排班
        isNUll(req, "isDayparting");
//        req.put("jgid", req.get("hospitalId"));
//        req.put("hospitalId", change2LocalHospitalId());
        List<Map<String, Object>> doctorList1 = registerMapper.queryDeptAndDoctor(req);
        if (doctorList1.size() < 1) {
            throw new CommonException("没有获取到有效的排班信息！");
        }
//        //查询该病人是否满足年龄科室
//        if(doctorList1.get(0).get("NLKZ_BDS")!=null&&!doctorList1.get(0).get("NLKZ_BDS").toString().equals("")){
//            req.put("NLKZ_BDS",doctorList1.get(0).get("NLKZ_BDS"));
//            List<Map<String, Object>> ageList=registerMapper.queryAge(req);
//            if(ageList.size()<1){
//                throw new CommonException("该患者年龄不能挂该科室");
//            }
//        }
//        //判断性别是否满足
//        if(!doctorList1.get(0).get("WOMEN").toString().equals("0")){
//            req.put("WOMEN",doctorList1.get(0).get("WOMEN"));
//            List<Map<String, Object>> ageList=registerMapper.queryBrxb(req);
//            if(ageList.size()<1){
//                throw new CommonException("该患者性别不能挂该科室");
//            }
//        }



//        解析排班ID
        workIdInfo(req);
        int n = 0;
//        是否分时段预约
        if ("1".equals(req.get("isDayparting"))) {
            isNUll(req, "sourceId");
//            验证号源ID是否正确
            req.put("sourceStatus", 0);
            //查询该病人是否已经锁定改医生
            n=registerMapper.queryLockNumber(req);
            if (n >0) {
                throw new CommonException("病人在该科室已经有预约信息或待支付信息，请取消后在预约");
            }
            n = registerMapper.queryDaypartingInfo(req);
            if (n != 1) {
                throw new CommonException("该号源已被其他用户锁定，请重新选择！");
            }
//            锁定号源
            n = registerMapper.lockDaypartinNumber(req);
            if (n != 1) {
                throw new CommonException("该号源已被其他用户锁定，请重新选择");
            }
        } else {
            throw new CommonException("请选择分时段挂号科室");
//            req.put("sourceStatus", 0);
//            n = registerMapper.queryDaypartingInfo(req);
//            if (n > 0) {
//                throw new CommonException("该排班为分时段排班，请检查传入参数是否正确");
//            }
//            List<Map<String, Object>> doctorList = registerMapper.queryDeptAndDoctor(req);
//            if (doctorList.size() < 1) {
//                throw new CommonException("没有获取到有效的排班信息！");
//            }
////        锁定号源
//            n = registerMapper.lockNumber(req);
        }

//        判断存储过程有没有报错，如果报错抛出异常
        if (n != 1) {
            throw new CommonException("锁定号源失败，请检查排班数据是否正确！");
        }

        return getSuccess("锁号成功");
    }

    /**
     * 解锁号源
     *
     * @param req
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String unlockNumber(Map<String, Object> req) {

        isNUll(req, "dataSources");
        isNUll(req, "workId");
        //        是否分时段排班
        isNUll(req, "isDayparting");
//        req.put("jgid", req.get("hospitalId"));
//        req.put("hospitalId", change2LocalHospitalId());

        int n = 0;
//        是否分时段预约
        //        解析排班ID
        workIdInfo(req);
        if ("1".equals(req.get("isDayparting"))) {
            isNUll(req, "sourceId");
            //            验证号源ID是否正确
            req.put("sourceStatus", 9);
            n = registerMapper.queryDaypartingInfo(req);
            if (n < 1) {
                throw new CommonException("该号源未锁定，不能解锁！");
            }
//            查询是否已经预约
            n = registerMapper.queryIsAppointment(req);
            if (n >= 1) {
                throw new CommonException("该号源已经预约，不能解锁！");
            }
//            锁定号源
            n = registerMapper.unlockDaypartinNumber(req);
        } else {
            req.put("sourceStatus", 1);
            n = registerMapper.queryDaypartingInfo(req);
            if (n > 0) {
                throw new CommonException("该排班为分时段排班，请检查传入参数是否正确");
            }
            List<Map<String, Object>> doctorList = registerMapper.queryDeptAndDoctor(req);
            if (doctorList.size() < 1) {
                throw new CommonException("没有获取到有效的排班信息！");
            }
//        锁定号源
            n = registerMapper.unlockNumber(req);
        }

        //判断存储过程有没有报错，如果报错抛出异常
        if (n != 1) {
            throw new CommonException("解锁号源失败，请检查排班数据是否正确！");
        }
        return getSuccess("解锁号源成功");
    }
    /**
     * 患者建档
     *
     * @Param: req
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String PatientFiling(Map<String, Object> req) throws CommonException {
        JSONObject json=PatientFilingJson(req);
        Map<String,Object> Patient=new HashMap<>();
        Patient.put("AuthorOrganization ","02123213-1");
        Patient.put("SourcePatientId",json.get("patientId"));
        Patient.put("SourcePatientIdType","OV");
        Patient.put("HealthCardId",json.get("EID"));
        Patient.put("Name",json.get("patientName"));
        Patient.put("Sex",json.get("sexCode"));
        Patient.put("PrivacySign","1");
        Patient.put("IdCard",json.get("idCard"));
        //Patient
        pusPlatform("PatientRegistry","3.1","PAT_0101",Patient,null);

        return getSuccess(json);
    }
    /**
     * 患者建档
     *
     * @Param: req
     */

    @Transactional(rollbackFor = Exception.class)
    public synchronized JSONObject PatientFilingJson(Map<String, Object> req) throws CommonException {
        isNUll(req, Arrays.asList("sexCode", "birthday", "idCard", "dataSources", "patientName"));
        JSONObject json = new JSONObject();
        req.put("idCard",req.get("idCard").toString().toUpperCase());
        String patientId = registerMapper.queryBrda(req);
        //获取患者信息
        Map<String, Object> parimenMap = new HashMap<>();
        //如果病人已经建档
        if (StringUtils.isNotEmpty(patientId)) {
            parimenMap.put("patientId", patientId);
            req.put("patientId", patientId);
            PatientEdit(req);
            List<Map<String, Object>> patientIsMatch = registerMapper.patientIsMatch(parimenMap);
            if (patientIsMatch.size() < 1) {
                throw new CommonException("无该病人档案");
            }
            json.putAll(patientIsMatch.get(0));
            return json;
        }
        String czgh = registerMapper.queryXtcs(xtcsMap(req.get("dataSources").toString()));
        if (czgh == null || "".equals(czgh)) {
            throw new CommonException("'机构未设置" + req.get("dataSources").toString() + "对应数据来源的系统参数");
        }
        req.put("czgh", czgh);
        String brxz = registerMapper.queryXtcs(xtcsMap("MRXZ"));
        if (brxz == null || "".equals(brxz)) {
            throw new CommonException("机构未设置MRXZ系统参数");
        }
        req.put("brxz", brxz);
//        获取门诊号码
        req.put("mzhm", getSyhm(czgh, "3"));
//        获取病人档案主键ID
        patientId = getIdentity("ms", "MS_BRDA").toString();
        req.put("patientId", patientId);
        req.put("JDJG", change2LocalHospitalId());
        int sl = registerMapper.PatientFiling(req);
        registerMapper.PatientFiling1(req);
        if (sl != 1) {
            throw new CommonException("保存病人档案信息失败！");
        }
//      写msBrzh
//        Map<String,Object> msBrzh=new HashMap<>();
//        req.put("jgid", change2LocalHospitalId());
//        req.put("brid", patientId);
//        req.put("brkh", req.get("mzhm"));
//        registerMapper.saveMs_brzh(msBrzh);

        parimenMap.put("patientId", patientId);
//        获取病人信息
        List<Map<String, Object>> patientIsMatch = registerMapper.patientIsMatch(parimenMap);
        if (patientIsMatch.size() < 1) {
            throw new CommonException("无该病人档案");
        }
//        else if (patientIsMatch.size() > 1) {
//            throw new CommonException("该患者重复建档，请联系医院合并档案");
//        }
        json.putAll(patientIsMatch.get(0));
       return json;
    }

    /**
     * 修改档案信息
     *
     * @param req
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String PatientEdit(Map<String, Object> req) throws CommonException {
        isNUll(req, "patientId");
        int sl = 0;
        String patientId = registerMapper.queryBrda(req);
        if (StringUtils.isEmpty(patientId)) {
            throw new CommonException("该证件无档案 不能修改建档");
        }
        sl = registerMapper.PatientEdit(req);
        if (sl != 1) {
            throw new CommonException("修改档案信息失败！");
        }
        return getSuccess("修改成功");
    }

    /**
     * 预约
     * 预约不支付，只写预约信息
     *
     * @param req
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String appointment(Map<String, Object> req) throws CommonException {
        isNUll(req, Arrays.asList("workId", "hospitalId", "patientId", "dataSources", "isDayparting"));
        req.put("jgid", req.get("hospitalId"));
        req.put("hospitalId", change2LocalHospitalId());

//        分时段号源预约
        int n = 0;
        if ("1".equals(req.get("isDayparting"))) {
            isNUll(req, "sourceId");
            //            验证号源ID是否正确
            req.put("sourceStatus", 0);
            n = registerMapper.queryDaypartingInfo(req);
            if (n != 1) {
                throw new CommonException("号源ID不正确，请检查排班数据是否正确！");
            }

        }
//        解析排班ID
        workIdInfo(req);


//        定义数量
        List<Map<String, Object>> patientIsMatch = registerMapper.patientIsMatch(req);
        if (patientIsMatch.size() < 1) {
            throw new CommonException("病人ID信息有误，未查到该病人档案信息");
        }
        String czgh = registerMapper.queryXtcs(xtcsMap(req.get("dataSources").toString()));
        if (czgh == null || "".equals(czgh)) {
            throw new CommonException("'机构未设置" + req.get("dataSources").toString() + "对应数据来源的系统参数");
        }
        List<Map<String, Object>> doctorList = registerMapper.queryDeptAndDoctor(req);
        if (doctorList.size() < 1) {
            throw new CommonException("没有获取到有效的排班信息！");
        }
        req.put("patientName", patientIsMatch.get(0).get("patientName"));
        req.put("idCard", patientIsMatch.get(0).get("idCard"));
        req.put("sexCode", patientIsMatch.get(0).get("sexCode"));
        req.put("czgh", czgh);
        int sl = 0;
//        查询是否已经预约
        sl = registerMapper.queryAppointment(req);
        if (sl > 0) {
            throw new CommonException("该病人已经预约过，请不要重复预约！");
        }
        String yyxh = getIdentity("ms", "MS_YYGH").toString();
        req.put("orderId", yyxh);
        //获取当前时间
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");

        req.put("newDate", df.format(new Date()));
        //当天挂号不写ms_yygh
        try {
            if (!df1.format(new Date()).equals(df1.format(df1.parse(req.get("registrationDate").toString())))) {
                //        预约信息ms_yygh
                sl = registerMapper.saveYygh(req);
                if (sl <= 0) {
                    throw new CommonException("保存预约信息失败");
                }
            }
        } catch (ParseException e) {
            throw new CommonException("传入时间格式错误");
        }

//        是否分时段预约 1是，0否
        if ("1".equals(req.get("isDayparting"))) {
            String ghyy_yyxx_yyxh = getIdentity("ghyy", "GHYY_YYXX").toString();
            req.put("ghyy_yyxx_yyxh", ghyy_yyxx_yyxh);
            sl = registerMapper.saveGhyyYyxx(req);
            if (sl < 0) {
                throw new CommonException("保存挂号预约信息失败");
            }
            sl = registerMapper.updateGhyy_hyxx(req);
            if (sl != 1) {
                throw new CommonException("更新号源信息失败");
            }
        }


        JSONObject json = new JSONObject();
        req.clear();
        req.put("orderId", yyxh);
        json.putAll(req);
        return getSuccess(json);
    }

    /**
     * 取号
     * 写入支付信息，挂号明细
     *
     * @param req
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String getNumber(Map<String, Object> req) {
        //首先分是否是分时段号源    如果是  则直接就是号源信息里面的顺序号
        // 如果不是就是   上午或者下午的已挂号+1   下午的好是从1开始
        String jzxh = "";

        boolean isPaymentAmount = false;
        isNUll(req, "paymentAmount");
        //如果交易金额为0，则交易类型，与交易单号可以不传
        if (Double.valueOf(req.get("paymentAmount").toString()) == 0) {
            isNUll(req, Arrays.asList("workId", "hospitalId", "orderId", "patientId", "dataSources", "isDayparting"));
        } else {
            isPaymentAmount = true;
            isNUll(req, Arrays.asList("workId", "hospitalId", "source", "orderId", "wcoerderId", "patientId", "dataSources", "isDayparting"));
        }
        req.put("jgid", req.get("hospitalId"));
        req.put("hospitalId", change2LocalHospitalId());
//        解析排班ID
        workIdInfo(req);
//        分时段号源预约
        int n = 0;
        if ("1".equals(req.get("isDayparting"))) {
            isNUll(req, "sourceId");
            //            验证号源ID是否正确
            req.put("sourceStatus", 0);
            List<Map<String, Object>> isDaypartingLsit = registerMapper.getSourceInfo(req);
            if (isDaypartingLsit.size() != 1) {
                throw new CommonException("号源ID不正确，请检查排班数据是否正确！");
            }
            //获取就诊序号
            jzxh = isDaypartingLsit.get(0).get("serialNumber").toString();
        } else {
            if (req.get("timeInterval").equals("1")) {
                req.put("timeIntervalSw", 1);
            } else if (req.get("timeInterval").equals("2")) {
                req.put("timeIntervalXw", 2);
            } else {
                throw new CommonException("排班暂只支持上午下午");
            }
            List<Map<String, Object>> isDaypartingLsit = registerMapper.getJzxh(req);
            jzxh = isDaypartingLsit.get(0).get("JZXH").toString();

        }
        req.put("jzxh", jzxh);


        int sl = 0;
//        判断是否已经取号
        sl = registerMapper.queryGetNumber(req);
        if (sl > 0) {
            throw new CommonException("该病人已经取过号，请不要重复取号！");
        }
//        判断是否预约当前排班医生
        sl = registerMapper.queryOrdered(req);
        if (sl <= 0) {
            throw new CommonException("该病人没有预约信息，不能取号！");
        }
        List<Map<String, Object>> doctorList1 = registerMapper.queryDeptAndDoctor(req);
        if (doctorList1.size() < 1) {
            throw new CommonException("没有获取到有效的排班信息！");
        }
        //判断支付金额与预约金额是否一致
        if (!Double.valueOf(doctorList1.get(0).get("totalMoney").toString()).equals(Double.valueOf(req.get("paymentAmount").toString()))) {
            throw new CommonException("支付金额与应付金额不一致，不能预约！");
        }
        //3获取系统参数的工号
        String czgh = registerMapper.queryXtcs(xtcsMap(req.get("dataSources").toString()));


        if (czgh == null || czgh.equals("")) {
            throw new CommonException("'机构未设置" + req.get("dataSources").toString() + "对应数据来源的系统参数");
        }


//        获取就诊好嘛

        String sbxh = getIdentity("ms", "MS_GHMX").toString();
        req.put("sbxh", sbxh);
        req.put("czgh", czgh);
//        获取就诊号码
        req.put("jzhm", getSyhm(czgh, "1"));
//        保存挂号明细

        //看是支付宝还是微信银联支付
        //银联标志
        String QTFKJE = "0";
        //支付宝标志
        String ZFBBZ = "0";
        //微信标志
        String WXBZ = "0";
        switch (req.get("source").toString()) {
            case "ZFFS_ZFB":
                ZFBBZ = "1";
                break;
            case "ZFFS_WX":
                WXBZ = "1";
                break;
            case "ZFFS_YL":
                QTFKJE = "1";
                break;
            default:
                throw new CommonException("支付方式source输入不对");
        }
        req.put("WX_ZFJE", WXBZ == "1" ? req.get("paymentAmount") : 0);
        req.put("ZFB_ZFJE", ZFBBZ == "1" ? req.get("paymentAmount") : 0);
        req.put("QTFKJE", QTFKJE == "1" ? req.get("paymentAmount") : 0);
        req.put("WXBZ", WXBZ);
        req.put("ZFBBZ", ZFBBZ);
        req.put("TRADE_NO", req.get("wcoerderId").toString());
        req.put("TRAN_ID", req.get("wcoerderId").toString());
        sl = registerMapper.saveGhmx(req);
        if (sl <= 0) {
            throw new CommonException("保存挂号明细失败！");
        }

        if (isPaymentAmount == true) {
            String fkfs = registerMapper.queryXtcs(xtcsMap(req.get("source").toString()));
            if (fkfs == null || fkfs.equals("")) {
                throw new CommonException("'机构未设置" + req.get("source").toString() + "对应支付来源的系统参数");
            }


            String jlxh = getIdentity("ms", "MS_GH_FKXX").toString();
            req.put("jlxh", jlxh);
            req.put("fkfs", fkfs);
            //        保存付款信息
            sl = registerMapper.saveFkxx(req);
            if (sl <= 0) {
                throw new CommonException("保存付款信息失败！");
            }
        }

        //        更新预约信息
        sl = registerMapper.updateYygh(req);
        if (sl <= 0) {
            throw new CommonException("更新预约信息失败！");
        }

        //        分时段号源预约

        if ("1".equals(req.get("isDayparting"))) {
            sl = registerMapper.updateGhyy_hyxx_ghxh(req);
            if (sl != 1) {
                throw new CommonException("更新号源信息失败！");
            }
            sl = registerMapper.updateGhyy_yyxx_Zt_Fkbz(req);
            if (sl != 1) {
                throw new CommonException("更新号源信息失败！");
            }
        }


        req.remove("hospitalId");
        List<Map<String, Object>> doctorList = registerMapper.queryDeptAndDoctor(req);
        Map<String, Object> back = new HashMap<>();
//               "availableCount" 剩余可预约号源数//        "seqCode"  医院的挂号序号//        "price" 挂号费
//                "transDate" 交易时间//        "orderFee" 诊疗费//        "orderTimeRange" 挂号时间段//        "orderDate" 预约日期

        back.put("availableCount", doctorList.get(0).get("registerLimit"));
        back.put("seqCode", sbxh);
        back.put("price", doctorList.get(0).get("totalMoney"));
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        back.put("transDate", df.format(new Date()));
        back.put("orderFee", doctorList.get(0).get("consultationFee"));
        back.put("orderTimeRange", "1".equals(doctorList.get(0).get("timeInterval").toString()) ? "上午" : "下午");
        back.put("orderDate", doctorList.get(0).get("registerDate"));

        JSONObject json = new JSONObject();
        json.putAll(back);
        return getSuccess(json);
    }


    /**
     * 退费
     *
     * @param req
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String refund(Map<String, Object> req) {
        isNUll(req, Arrays.asList("workId", "hospitalId", "dataSources", "orderId", "seqCode", "isDayparting"));
        req.put("hospitalId", change2LocalHospitalId());

//        分时段号源预约
        int n = 0;
        if ("1".equals(req.get("isDayparting"))) {
            isNUll(req, "sourceId");

            //            验证号源ID是否正确
            req.put("sourceStatus", 0);
            n = registerMapper.queryDaypartingInfo(req);
            if (n != 1) {
                throw new CommonException("号源ID不正确，请检查排班数据是否正确！");
            }
        }

        int sl = 0;
//        查询是否就诊
        sl = registerMapper.querySfjz(req);
        if (sl > 0) {
            throw new CommonException("该病人已经就诊，不能退费！");
        }
//        查询是否已经退费
        sl = registerMapper.querySftf(req);
        if (sl > 0) {
            throw new CommonException("该病人已经退费，不能再次退费！");
        }
//       查询是否已经取消预约
        sl = registerMapper.querySfqxyy(req);
        if (sl > 0) {
            throw new CommonException("该病人已经取消预约，不能退费！");
        }
        //3获取系统参数的工号
        String czgh = registerMapper.queryXtcs(xtcsMap(req.get("dataSources").toString()));

        if (czgh == null || czgh.equals("")) {
            throw new CommonException("'机构未设置" + req.get("dataSources").toString() + "对应数据来源的系统参数");
        }
        req.put("czgh", czgh);
//        修改挂号明细收费状态
        sl = registerMapper.refund(req);
        if (sl <= 0) {
            throw new CommonException("更新挂号明细状态信息错误！");
        }
//        保存退费明细
        sl = registerMapper.saveThmx(req);
        if (sl <= 0) {
            throw new CommonException("更新挂号明细状态信息错误！");
        }
        return getSuccess("退费成功！");
    }

    /**
     * 退号
     *
     * @param req
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String withdrawNumber(Map<String, Object> req) {
        isNUll(req, Arrays.asList("workId", "hospitalId", "dataSources", "orderId", "isDayparting"));
        req.put("hospitalId", change2LocalHospitalId());

//        分时段号源预约
        int n = 0;
        if ("1".equals(req.get("isDayparting"))) {
            isNUll(req, "sourceId");
//            验证号源ID是否正确
            req.put("sourceStatus", 0);
            n = registerMapper.queryDaypartingInfo(req);
            if (n != 1) {
                throw new CommonException("号源ID不正确，请检查排班数据是否正确！");
            }

        }
//        修改号源信息
        workIdInfo(req);


        int sl = 0;
        //        查询是否就诊
        sl = registerMapper.querySfjz(req);
        if (sl > 0) {
            throw new CommonException("该病人已经就诊，不能退号！");
        }
        //       查询是否已经取消预约
        sl = registerMapper.querySfqxyy(req);
        if (sl > 0) {
            throw new CommonException("该病人已经取消预约，不能退号！");
        }
//        修改预约信息中的退号标志
        sl = registerMapper.withdrawNumber(req);
        if (sl <= 0) {
            throw new CommonException("退号失败！");
        }
        if ("1".equals(req.get("isDayparting"))) {
            sl = registerMapper.updateGhyy_hyxx_th(req);
            if (sl != 1) {
                throw new CommonException("更新号源信息错误！");
            }
            req.put("hyStatus", "1");
            sl = registerMapper.updateGhyy_yyxx_th(req);
            if (sl != 1) {
                throw new CommonException("更新预约信息错误！");
            }
        } else {
            sl = registerMapper.editNumber(req);
            if (sl != 1) {
                throw new CommonException("更新排班信息错误！");
            }
        }

        return getSuccess("退号成功");
    }

    /**
     * 预约挂号
     * 没有锁号的预约挂号
     *
     * @param json
     * @return
     */
    @Override
    public String confirmRegister(JSONObject json) {

//        //调用平台上传
//
//        pusPlatform("PatientRegistry","PAT_0101",PatientId);
        return getSuccess(json);
    }
    /**
     * 预约挂号
     * 没有锁号的预约挂号
     *
     * @param req
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public synchronized String confirmRegisterJson(Map<String, Object> req)  {
        isNUll(req, Arrays.asList("sourceId","patientId","workId", "hospitalId", "source", "wcoerderId", "dataSources", "isDayparting","paymentAmount"));
        if(req.get("isDayparting").toString().equals("0")){
            throw new CommonException("请选择有号源的科室进行挂号");
        }
        req.put("sourceStatus", 9);
        List<Map<String, Object>> isDaypartingLsit = registerMapper.getSourceInfo(req);
        if (isDaypartingLsit.size() != 1) {
            throw new CommonException("号源ID不正确，请先锁号在进行操作！");
        }
        //定义收费员工号
        String czgh = null;

        czgh = registerMapper.queryXtcs(xtcsMap(req.get("dataSources").toString()));
        if (czgh == null || "".equals(czgh)) {
            throw new CommonException("'机构未设置" + req.get("dataSources").toString() + "对应数据来源的系统参数");
        }
        //1查询预约日期范围内医生是否有号源
        List<Map<String, Object>> hyList = registerMapper.opAppAppoint_1(req);
        if (hyList.size() == 0) {
            throw new CommonException("未找到该医生该时间范围内的号源");
        }
        //判断传入金额是否等于 系统的挂号费+诊疗费+专家费+药事服务费
        BigDecimal b1 = new BigDecimal(req.get("paymentAmount").toString());
//        BigDecimal b2 = new BigDecimal(req.get("APP_PHARMACY_FEE").toString());
//        BigDecimal b3 = new BigDecimal(req.get("APP_SUM_FEE").toString());
        BigDecimal b4 = new BigDecimal(hyList.get(0).get("GHF").toString());
        BigDecimal b5 = new BigDecimal(hyList.get(0).get("ZLF").toString());
        BigDecimal b6 = new BigDecimal(hyList.get(0).get("YSFWF").toString());
        BigDecimal b7 = new BigDecimal(hyList.get(0).get("ZJF").toString());
        BigDecimal b0 = new BigDecimal(0);
        if (b1.subtract(b4).subtract(b5).subtract(b6).subtract(b7).compareTo(b0) != 0) {
            throw new CommonException("传入金额合计和系统金额不等");
        }
        //2查询是否有该患者的身份证和brid
        List<Map<String, Object>> brdaList = registerMapper.opAppAppoint_2(req);

        if (brdaList.size() < 1) {
            throw new CommonException("根据patientId未找到患者信息");
        }
        req.put("brid",brdaList.get(0).get("BRID"));
        //根据身份证更新电子健康卡 EID
        //  registerMapper.opAppAppoint_3(req);
        //3查询该患者是否有今天的挂号信息
        req.put("ysdm",hyList.get(0).get("YSDM"));
        req.put("gzrq",hyList.get(0).get("GZRQ"));
        List<Map<String, Object>> ghmxList = registerMapper.opAppAppoint_4(req);
        if (ghmxList.size() > 0) {
            throw new CommonException("当天该医生已有该患者的挂号信息");
        }
        //4写ms_ghmx
        //获取识别序号
        String sbxh = getIdentity("ms", "MS_GHMX").toString();
        Map<String, Object> ghmx = new HashMap<>();
        ghmx.put("jzsj", hyList.get(0).get("KSSJ"));

        ghmx.put("sbxh", sbxh);
        ghmx.put("jgid", change2LocalHospitalId());
        ghmx.put("brid", brdaList.get(0).get("BRID"));
        ghmx.put("brxz", brdaList.get(0).get("BRXZ"));
        ghmx.put("ghlb", hyList.get(0).get("GHLB"));
        ghmx.put("ksdm", hyList.get(0).get("KSDM1"));
        ghmx.put("ysdm", hyList.get(0).get("YSDM"));
        ghmx.put("jzxh", hyList.get(0).get("JZXH1"));
        ghmx.put("mzks", hyList.get(0).get("MZKS1"));
        ghmx.put("ghcs", 1);
        ghmx.put("ghje", hyList.get(0).get("GHF"));
        ghmx.put("zlje", hyList.get(0).get("ZLF"));
        ghmx.put("zjfy", hyList.get(0).get("ZJF"));
        ghmx.put("ysfwfje", hyList.get(0).get("YSFWF"));
        ghmx.put("blje", 0);
        ghmx.put("xjje", 0);
        ghmx.put("zpje", 0);
        ghmx.put("zhje", 0);
        ghmx.put("hbwc", 0);
        ghmx.put("qtys", b1.doubleValue());
        ghmx.put("thbz", 0);
        ghmx.put("czgh", czgh);
        ghmx.put("czpb", 0);
        ghmx.put("jzhm", getSyhm(czgh, "2"));
        ghmx.put("xnfp", ghmx.get("jzhm"));
        ghmx.put("mzlb", hyList.get(0).get("MZLB"));
        ghmx.put("yybz", hyList.get(0).get("YYBZ"));
        ghmx.put("yspb", 0);
        ghmx.put("dzsb", 0);
        ghmx.put("sffs", 0);
        ghmx.put("jzzt", 0);
        ghmx.put("zblb", hyList.get(0).get("ZBLB"));
        ghmx.put("ghxh", hyList.get(0).get("GHXH"));
        ghmx.put("zlxh", hyList.get(0).get("ZLXH"));
        ghmx.put("zjxh", hyList.get(0).get("ZJXH"));
        ghmx.put("ghlx", hyList.get(0).get("KSDM"));
        ghmx.put("plateformnum", req.get("wcoerderId"));
        // ghmx.put("qtfkje", hyList.get(0).get("HJJE"));
        //ghmx.put("bzxx", req.get("ShowText"));
        registerMapper.opAppAppoint_5(ghmx);
        //4写ms_ghmx_fkxx
        Map<String, Object> ghmx_fkxx = new HashMap<>();
        //获取付款方式
        //获取支付方式
        List<Map<String, Object>> zffsList = registerMapper.getZffs(req);
        if (zffsList.size() < 1) {
            throw new CommonException("支付方式source输入不对");
        }
        String fkfs = zffsList.get(0).get("FKFS").toString();
        String jlxh = getIdentity("ms", "MS_GH_FKXX").toString();
        ghmx_fkxx.put("jlxh", jlxh);
        ghmx_fkxx.put("sbxh", sbxh);
        ghmx_fkxx.put("fkfs", fkfs);
        ghmx_fkxx.put("fkje", b1.doubleValue());
        registerMapper.opAppAppoint_6(ghmx_fkxx);
        //5写预约信息ms_yygh
        Map<String, Object> yygh = new HashMap<>();
        String yyxh = getIdentity("ms", "MS_YYGH").toString();
        yygh.put("yyxh", yyxh);
        yygh.put("jgid", change2LocalHospitalId());
        yygh.put("yymm", "123456");
        yygh.put("brid", ghmx.get("brid"));
        yygh.put("ksdm", ghmx.get("ksdm"));
        yygh.put("zblb", ghmx.get("zblb"));
        yygh.put("ysdm", ghmx.get("ysdm"));
        yygh.put("yylb", 3);
        yygh.put("yylx", "zzj");
        yygh.put("ghbz", 3);
        yygh.put("yyrq",ghmx.get("jzsj"));
        yygh.put("jzxh", ghmx.get("jzxh"));
        yygh.put("sbxh", sbxh);
        yygh.put("zcid", 0);
        yygh.put("djgh", czgh);
        registerMapper.opAppAppoint_7(yygh);
        //6写ghyy_yyxx
        Map<String, Object> yyxx = new HashMap<>();
        String ghyy_yyxx_yyxh = getIdentity("ghyy", "GHYY_YYXX").toString();
        yyxx.put("yyxh", ghyy_yyxx_yyxh);
        yyxx.put("jgid", change2LocalHospitalId());
        yyxx.put("brid", ghmx.get("brid"));
        yyxx.put("brxm", brdaList.get(0).get("BRXM"));
        yyxx.put("sfzh", brdaList.get(0).get("SFZH"));
        yyxx.put("hyxh", req.get("sourceId"));
        yyxx.put("yyzt", 1);
        yyxx.put("yysj", ghmx.get("jzsj"));
        yyxx.put("yyr", czgh);
        yyxx.put("ghr", czgh);
        yyxx.put("dybz", 0);
        yyxx.put("fkbz", 2);
        yyxx.put("brxb", brdaList.get(0).get("BRXB"));
        yyxx.put("yytj", 4);
        yyxx.put("yyid", sbxh);
        registerMapper.opAppAppoint_8(yyxx);

        //8修改排班数据
        Map<String, Object> pbxx = new HashMap<>();
        pbxx.put("gzrq", req.get("gzrq"));
        pbxx.put("ksdm", ghmx.get("ksdm"));
        pbxx.put("ysdm", ghmx.get("ysdm"));
        pbxx.put("zblb", ghmx.get("zblb"));
        int n = registerMapper.opAppAppoint_9(pbxx);
        if (n != 1) {
            throw new CommonException("更新排班信息错误,是否挂号人数大于预挂人数！");
        }
        //9回写ghyy_hyxx
        n=registerMapper.updateHyxx(yyxx);
        if(n!=1){
            throw new CommonException("未找到锁号号源");
        }
        Map<String, Object> back = new HashMap<>();
        String mzhm = registerMapper.queryBrdaMzhm(req);
        back.put("outpatientNumber", mzhm);
        back.put("qssj", hyList.get(0).get("KSSJ"));
        back.put("zzsj", hyList.get(0).get("JSSJ"));
        back.put("orderId", hyList.get(0).get("JSSJ"));
        back.put("appointmentDate", hyList.get(0).get("GZRQ"));
        back.put("invoiceNumber", ghmx.get("jzhm"));
        back.put("availableCount", "");
        back.put("addr", hyList.get(0).get("CroomName"));
        back.put("seqCode", sbxh);
        back.put("patientSerialNumber", hyList.get(0).get("JZXH1"));
        back.put("price",hyList.get(0).get("GHF"));
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        back.put("transDate", df.format(new Date()));
        back.put("orderFee", hyList.get(0).get("ZLF"));
        back.put("orderTimeRange", hyList.get(0).get("orderTimeRange"));
        back.put("orderDate", hyList.get(0).get("GZRQ"));
        JSONObject json = new JSONObject();
        json.putAll(back);

        //调用平台上传
        Map<String,Object> Patient=new HashMap<>();
        Patient.put("AuthorOrganization ","02123213-1");
        Patient.put("SourcePatientId",brdaList.get(0).get("BRID"));
        Patient.put("SourcePatientIdType","OV");
        Patient.put("Name",brdaList.get(0).get("BRXM"));
        Patient.put("Sex",brdaList.get(0).get("BRXB"));
        Patient.put("IdCard",brdaList.get(0).get("SFZH"));
        Map<String,Object> OptRegister=new HashMap<>();
        OptRegister.put("OutRegistryId",sbxh);
        OptRegister.put("PatientType ","01");
        OptRegister.put("ClinicId",mzhm);
        OptRegister.put("MedicalCardId",mzhm);
        OptRegister.put("MedicalCardType ","21");
        OptRegister.put("AuthorOrganization ","02123213-1");
        OptRegister.put("RegisteredDept ",ghmx.get("ksdm"));
        OptRegister.put("RegisteredType  ","1");
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("HHmmss");
        String EffectiveTime = dateFormat1.format(new Date())+"T"+dateFormat2.format(new Date());
        OptRegister.put("RegisteredDateTime",EffectiveTime);
        OptRegister.put("RegisteredSequence",hyList.get(0).get("JZXH1"));
        OptRegister.put("IsAppoints",ghmx.get("YYBZ"));
        pusPlatform("OutPatientRegistered","3.0","ODS_1001",Patient,OptRegister);
        return getSuccess(json);
    }

    /**
     * 取消预约
     *
     * @param req
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String cancelRegister(Map<String, Object> req) {
        isNUll(req, Arrays.asList("workId", "hospitalId", "dataSources", "seqCode", "isDayparting"));
        req.put("hospitalId", change2LocalHospitalId());

        //        分时段号源预约
        int n = 0;
        if ("1".equals(req.get("isDayparting"))) {
            isNUll(req, "sourceId");
//            验证号源ID是否正确
            req.put("sourceStatus", 1);
            n = registerMapper.queryDaypartingInfo(req);
            if (n != 1) {
                throw new CommonException("退费号源id不存在，请检查是否已经退费或者超过就诊时间");
            }

        }
        workIdInfo(req);

        int sl = 0;
//        查询是否就诊
        sl = registerMapper.querySfjz(req);
        if (sl > 0) {
            throw new CommonException("该病人已经就诊，不能取消预约！");
        }
//       查询是否已经取消预约
        sl = registerMapper.querySfqxyy(req);
        if (sl > 0) {
            throw new CommonException("该病人已经取消预约，不能再次取消预约！");
        }
//        查询是否已经退费
        sl = registerMapper.querySftf(req);
        if (sl > 0) {
            throw new CommonException("该病人已经取消预约，不能再次取消预约！");
        }
        //3获取系统参数的工号
        String czgh = registerMapper.queryXtcs(xtcsMap(req.get("dataSources").toString()));
        if (czgh == null || czgh.equals("")) {
            throw new CommonException("'机构未设置" + req.get("dataSources").toString() + "对应数据来源的系统参数");
        }
        req.put("czgh", czgh);
//        修改退费标志
        sl = registerMapper.refund(req);
        if (sl <= 0) {
            throw new CommonException("更新挂号明细状态信息错误！");
        }

//        修改预约标志
        sl = registerMapper.withdrawNumber(req);
        if (sl <= 0) {
            throw new CommonException("更新预约信息错误！");
        }
        if ("1".equals(req.get("isDayparting"))) {
            sl = registerMapper.updateGhyy_hyxx_th(req);
            if (sl != 1) {
                throw new CommonException("更新号源信息错误！");
            }
            req.put("hyStatus", "1");
            sl = registerMapper.updateGhyy_yyxx_th(req);
            if (sl != 1) {
                throw new CommonException("更新预约信息错误！");
            }
        }
        //        修改号源信息
        sl = registerMapper.editNumber(req);
        if (sl <= 0) {
            throw new CommonException("更新排班信息错误(请确认排班ID正确)！");
        }


//        保存退号信息
        sl = registerMapper.cancelRegister(req);
        if (sl <= 0) {
            throw new CommonException("保存退费明细失败！");
        }

        // 保存 取消挂号  队列信息
//        sl = registerMapper.updatePdxx(req);
//        if (sl >1) {
//            throw new CommonException("保存取消挂号队列信息失败！");
//        }

        Map<String, Object> back = new HashMap<>();
        List<Map<String, Object>> doctorList = registerMapper.queryHyxx(req);
        if (doctorList.size() < 1) {
            throw new CommonException("没有获取到有效的排班信息！");
        }
        back.put("availableCount", doctorList.get(0).get("registerLimit"));
        back.put("orderFee", doctorList.get(0).get("totalMoney"));
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        back.put("transDate", df.format(new Date()));
        back.put("seqCode", req.get("seqCode"));
        JSONObject json = new JSONObject();
        json.putAll(back);
        //调用平台上传
        pusPlatform("OutPatientRegisteredCancel",req.get("seqCode").toString(),"ODS_1002","MS_THMX");
        return getSuccess(json);
    }


    /**
     * 锁号之后的预约挂号
     *
     * @param req
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String appointmentRegister(Map<String, Object> req) {
        //首先分是否是分时段号源    如果是  则直接就是号源信息里面的顺序号
        // 如果不是就是   上午或者下午的已挂号+1   下午的好是从1开始
        String jzxh = "";
        boolean isPaymentAmount = false;
        isNUll(req, "paymentAmount");
        //如果交易金额为0，则交易类型，与交易单号可以不传
        if (Double.valueOf(req.get("paymentAmount").toString()) == 0) {
            isNUll(req, Arrays.asList("workId", "hospitalId", "dataSources", "isDayparting"));
        } else {
            isPaymentAmount = true;
            isNUll(req, Arrays.asList("workId", "hospitalId", "source", "wcoerderId", "dataSources", "isDayparting"));
        }
        req.put("hospitalId", change2LocalHospitalId());
        workIdInfo(req);

        int n = 0;
        if ("1".equals(req.get("isDayparting"))) {
            isNUll(req, "sourceId");
            //            验证号源ID是否正确
            req.put("sourceStatus", 0);
            List<Map<String, Object>> isDaypartingLsit = registerMapper.getSourceInfo(req);
            if (isDaypartingLsit.size() != 1) {
                throw new CommonException("号源ID不正确，请检查排班数据是否正确！");
            }
            //获取就诊序号
            jzxh = isDaypartingLsit.get(0).get("serialNumber").toString();
        } else {
            if (req.get("timeInterval").equals("1")) {
                req.put("timeIntervalSw", 1);
            } else if (req.get("timeInterval").equals("2")) {
                req.put("timeIntervalXw", 2);
            } else {
                throw new CommonException("排班暂只支持上午下午");
            }
            List<Map<String, Object>> isDaypartingLsit = registerMapper.getJzxh(req);
            jzxh = isDaypartingLsit.get(0).get("JZXH").toString();

        }
        req.put("jzxh", jzxh);


        int sl = 0;

        //如果病人没有建档
        if (StringUtils.isEmpty(req.get("patientId").toString())) {
            //获取机构list
            String patientId = registerMapper.queryBrda(req);
            if (StringUtils.isEmpty(patientId)) {
                String czgh = registerMapper.queryXtcs(xtcsMap(req.get("dataSources").toString()));
                if (czgh == null || "".equals(czgh)) {
                    throw new CommonException("'机构未设置" + req.get("dataSources").toString() + "对应数据来源的系统参数");
                }
                req.put("czgh", czgh);
                String brxz = registerMapper.queryXtcs(xtcsMap("MRXZ"));
                if (brxz == null || "".equals(brxz)) {
                    throw new CommonException("机构未设置MRXZ系统参数");
                }
                req.put("brxz", brxz);
//        获取门诊号码
                req.put("mzhm", getSyhm(czgh, "3"));
                //        获取病人档案主键ID
                patientId = getIdentity("ms", "MS_BRDA").toString();
                req.put("patientId", patientId);
                req.put("JDJG", change2LocalHospitalId());
                registerMapper.PatientFiling(req);
                //获取机构list

            }
            req.put("patientId", patientId);
        }
//        查询是否已经预约
        sl = registerMapper.queryAppointment(req);
        if (sl > 0) {
            throw new CommonException("该病人已经预约过，请不要重复预约！");
        }
        List<Map<String, Object>> doctorList1 = registerMapper.queryHyxx(req);
        if (doctorList1.size() < 1) {
            throw new CommonException("没有获取到有效的排班信息！");
        }
        //判断支付金额与预约金额是否一致
        if (!Double.valueOf(doctorList1.get(0).get("totalMoney").toString()).equals(Double.valueOf(req.get("paymentAmount").toString()))) {
            throw new CommonException("支付金额与应付金额不一致，不能预约！");
        }
        //3获取系统参数的工号
        String czgh = registerMapper.queryXtcs(xtcsMap(req.get("dataSources").toString()));

        if (czgh == null || czgh.equals("")) {
            throw new CommonException("'机构未设置" + req.get("dataSources").toString() + "对应数据来源的系统参数");
        }


        String sbxh = getIdentity("ms", "MS_GHMX").toString();

        req.put("sbxh", sbxh);

        req.put("czgh", czgh);
//        获取就诊号码
        req.put("jzhm", getSyhm(czgh, "1"));
        //看是支付宝还是微信银联支付
        //银联标志
        String QTFKJE = "0";
        //支付宝标志
        String ZFBBZ = "0";
        //微信标志
        String WXBZ = "0";
        switch (req.get("source").toString()) {
            case "ZFFS_ZFB":
                ZFBBZ = "1";
                break;
            case "ZFFS_WX":
                WXBZ = "1";
                break;
            case "ZFFS_YL":
                QTFKJE = "1";
                break;
            default:
                throw new CommonException("支付方式source输入不对");
        }
        req.put("WX_ZFJE", WXBZ == "1" ? req.get("paymentAmount") : 0);
        req.put("ZFB_ZFJE", ZFBBZ == "1" ? req.get("paymentAmount") : 0);
        req.put("QTFKJE", QTFKJE == "1" ? req.get("paymentAmount") : 0);
        req.put("WXBZ", WXBZ);
        req.put("ZFBBZ", ZFBBZ);
        req.put("TRADE_NO", req.get("wcoerderId").toString());
        req.put("TRAN_ID", req.get("wcoerderId").toString());
//        保存挂号明细
        sl = registerMapper.saveGhmx(req);
        if (sl <= 0) {
            throw new CommonException("保存挂号明细失败！");
        }
        if (isPaymentAmount == true) {
            String fkfs = registerMapper.queryXtcs(xtcsMap(req.get("source").toString()));
            if (fkfs == null || fkfs.equals("")) {
                throw new CommonException("'机构未设置" + req.get("source").toString() + "对应支付来源的系统参数");
            }
            String jlxh = getIdentity("ms", "MS_GH_FKXX").toString();
            req.put("fkfs", fkfs);
            req.put("jlxh", jlxh);
            //        保存付款信息
            sl = registerMapper.saveFkxx(req);
            if (sl <= 0) {
                throw new CommonException("保存付款信息失败！");
            }
//            req.remove("hospitalId");
        }

        String yyxh = getIdentity("ms", "MS_YYGH").toString();
        req.put("orderId", yyxh);
        //获取当前时间
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        req.put("newDate", df.format(new Date()));
//        预约信息ms_yygh
        sl = registerMapper.saveYygh(req);
        if (sl <= 0) {
            throw new CommonException("保存预约信息失败");
        }

        if ("1".equals(req.get("isDayparting"))) {
            String ghyy_yyxx_yyxh = getIdentity("ghyy", "GHYY_YYXX").toString();
            req.put("ghyy_yyxx_yyxh", ghyy_yyxx_yyxh);
            sl = registerMapper.saveGhyyYyxx(req);
            if (sl <= 0) {
                throw new CommonException("保存分时段预约信息失败");
            }
            sl = registerMapper.updateGhyy_hyxx_Nhy(req);
            if (sl != 1) {
                throw new CommonException("更新号源信息错误！");
            }
        }
        Map<String, Object> back = new HashMap<>();
        List<Map<String, Object>> doctorList = registerMapper.queryHyxx(req);
        if (doctorList.size() < 1) {
            throw new CommonException("没有获取到有效的排班信息！");
        }
        back.put("availableCount", doctorList.get(0).get("registerLimit"));
        back.put("seqCode", sbxh);
        back.put("price", doctorList.get(0).get("totalMoney"));
        back.put("transDate", df.format(new Date()));
        back.put("orderFee", doctorList.get(0).get("consultationFee"));
        back.put("orderTimeRange", "1".equals(doctorList.get(0).get("timeInterval").toString()) ? "上午" : "下午");
        back.put("orderDate", doctorList.get(0).get("registerDate"));
        JSONObject json = new JSONObject();
        json.putAll(back);
        return getSuccess(json);
    }

    /**
     * 预约历史记录查询
     *
     * @param req
     * @return
     * @throws CommonException
     */
    @Override
    public String getRegisteredRecords(Map<String, Object> req) throws CommonException {
        //判断必填项是否为空
        toCharacterString(req, "idCard");
        //分页判断
        pagination(req);
        List<Map<String, Object>> department = registerMapper.getRegisteredRecords(req);
//        if (department.size() < 1) {
//            logger.error("预约历史记录为空");
//            throw new CommonException("预约历史记录为空");
//        }
        return getSuccessPagination(department);
    }

    /**
     * 分时段号源详细信息
     *
     * @param req
     * @return
     * @throws CommonException
     */
    @Override
    public String getSourceInfo(Map<String, Object> req) throws CommonException {
        //判断必填项是否为空
        isNUll(req, "workId");
        isNUll(req, "dataSources");

        List<Map<String, Object>> department = registerMapper.getSourceInfo(req);
        return getSuccess(department);
    }

    /**
     * 解析workid信息
     *
     * @param req
     * @return
     */
    public Map<String, Object> workIdInfo(Map<String, Object> req) {
        List<String> list = Arrays.asList(req.get("workId").toString().replace("|", ",").split(","));
//        挂号日期
        String registrationDate = list.get(0);
//        科室代码
        String depCode = list.get(1);
//        值班类别
        String timeInterval = list.get(2);
//        医生代码
        String doctorCode = list.get(3);
        req.put("registrationDate", registrationDate);
        req.put("depCode", depCode);
        req.put("timeInterval", timeInterval);
        req.put("doctorCode", doctorCode);
        return req;
    }

    /**
     * 获取分时段排班信息
     *
     * @param req
     * @return
     */
    public Map<String, Object> sourceInfo(Map<String, Object> req) {
        req.put("sourceId", req.get("workId"));
        req.remove("workId");
        List<Map<String, Object>> sourceInfo = registerMapper.getSourceInfo(req);
        if (sourceInfo != null && sourceInfo.size() > 0) {
            req.put("workId", sourceInfo.get(0).get("workId"));
            List<String> list = Arrays.asList(req.get("workId").toString().replace("|", ",").split(","));
//        挂号日期
            String registrationDate = list.get(0);
//        科室代码
            String depCode = list.get(1);
//        值班类别
            String timeInterval = list.get(2);
//        医生代码
            String doctorCode = list.get(3);
            req.put("registrationDate", registrationDate);
            req.put("depCode", depCode);
            req.put("timeInterval", timeInterval);
            req.put("doctorCode", doctorCode);
        }
        return req;
    }

    /**
     * 根据工号 票据类型获取发票号码,门诊号码，就诊号码
     *
     * @param czgh
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public String getSyhm(String czgh, String pjlx) {
        int sl = 0;
        String text = "";
        if ("1".equals(pjlx)) {
            text = "就诊号码";
        } else if ("2".equals(pjlx)) {
            text = "发票号码";
        } else if ("3".equals(pjlx)) {
            text = "门诊号码";
        }
        //根据工号 票据类型获取发票号码,门诊号码，就诊号码
        Map<String, Object> map = new HashMap<>();
        map.put("czgh", czgh);
        map.put("pjlx", pjlx);
        List<Map<String, Object>> syhmList = registerMapper.settlementPrescription2(map);
        if (syhmList.size() < 1) {
            throw new CommonException("机构未设置" + czgh + "员工的" + text);
        }
        String syhm = syhmList.get(0).get("SYHM").toString();
        if (syhm.equals(syhmList.get(0).get("ZZHM").toString())) {
            //使用号码等于终止号码，说明是最后一张号码打上sypb
            sl = registerMapper.settlementPrescription3(syhmList.get(0));
            if (sl != 1) {
                throw new CommonException("更新" + text + "错误");
            }
        } else {            //号码未使用发票号完加一,再写回ms_ygpj
            syhmList.get(0).put("SYHM", addOne(syhm));
            sl = registerMapper.settlementPrescription4(syhmList.get(0));
            if (sl != 1) {
                throw new CommonException("更新" + text + "错误");
            }
        }
        return syhm;
    }


    /**
     * 预约挂号
     * 没有锁号的预约挂号
     *
     * @param json
     * @return
     */
    @Override
    public String PayRegister(JSONObject json) {
//        //调用平台上传
//        try {
//            pusPlatform("OutPatientRegistered", json.get("seqCode").toString(), "ODS_1001", "MS_GHMX");
//            pusPlatform("OutPatientVisitStarted", json.get("JZXH").toString(), "ODS_1101", "YS_MZ_JZLS");
//        } catch (Exception e){
//            logger.info(e.getMessage());
//            e.printStackTrace();
//        }
        return getSuccess(json);
    }
    /**
     * 预约挂号
     * 没有锁号的预约挂号
     *
     * @param req
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public synchronized JSONObject PayRegisterJson(Map<String, Object> req)  {
        if(!req.get("return_code").toString().equals("SUCCESS")){
            throw new CommonException("传入return_code不为SUCCESS，不能结算");
        }
        //1为微信 2支付宝
        if(req.get("pay_type").toString().equals("1")){
            if(!req.get("trade_status").toString().equals("SUCCESS")){
                throw new CommonException("传入trade_status不为SUCCESS，不能结算");
            }
            req.put("source","ZFFS_JHZF_YY_WX");
        }else{
            if(!req.get("trade_status").toString().equals("TRADE_SUCCESS")){
                throw new CommonException("传入trade_status不为TRADE_SUCCESS，不能结算");
            }
            req.put("source","ZFFS_JHZF_YY_ZFB");
        }
        Map<String,Object> busInfo= JSONUtil.toHashMap(req.get("busInfo").toString());
        req.put("paymentAmount",busInfo.get("GHJE"));
        req.put("dataSources","CZGH_JHZF");
        req.put("isDayparting","1");
        req.put("sourceId",busInfo.get("HYXH"));
        req.put("patientId",busInfo.get("BRID"));
        req.put("FZXH",busInfo.get("FZXH"));
         List<Map<String,Object>> brxxList=registerMapper.patientIsMatch(req);
         req.putAll(brxxList.get(0));
        req.put("wcoerderId",req.get("out_trade_no"));
        req.put("hospitalId","511700001");
        List<Map<String,Object>> listHy= registerMapper.getSourceInfo(req);
        if(listHy.size()<1){
            throw  new CommonException("无效号源");
        }
        req.put("workId",listHy.get(0).get("workId").toString());
        //首先分是否是分时段号源    如果是  则直接就是号源信息里面的顺序号
        // 如果不是就是   上午或者下午的已挂号+1   下午的好是从1开始
        String jzxh = "";
        String qssj = "";
        String zzsj = "";
        String appointmentDate = "";

        Map<String, Object> back = new HashMap<>();
        boolean isPaymentAmount = false;
        isNUll(req, "paymentAmount");
        //如果交易金额为0，则交易类型，与交易单号可以不传
        if (Double.valueOf(req.get("paymentAmount").toString()) == 0) {
            isNUll(req, Arrays.asList("workId", "hospitalId", "dataSources", "isDayparting"));
        } else {
            isPaymentAmount = true;
            isNUll(req, Arrays.asList("workId", "hospitalId", "source", "wcoerderId", "dataSources", "isDayparting"));
        }

        req.put("jgid", req.get("hospitalId"));
        req.put("hospitalId", change2LocalHospitalId());

        workIdInfo(req);


//        分时段号源预约
        int n = 0;
        if ("1".equals(req.get("isDayparting"))) {
            isNUll(req, "sourceId");
            //            验证号源ID是否正确
            req.put("sourceStatus", 1);
            List<Map<String, Object>> isDaypartingLsit = registerMapper.getSourceInfo(req);
            if (isDaypartingLsit.size() != 1) {
                throw new CommonException("号源ID不正确，请先锁号在进行操作！");
            }
            //获取就诊序号
            jzxh = isDaypartingLsit.get(0).get("serialNumber").toString();
            qssj = isDaypartingLsit.get(0).get("qssj").toString();
            zzsj = isDaypartingLsit.get(0).get("zzsj").toString();
            appointmentDate = isDaypartingLsit.get(0).get("appointmentDate").toString();
            req.put("qssj", isDaypartingLsit.get(0).get("qssj"));

        } else {
            if (req.get("timeInterval").equals("1")) {
                req.put("timeIntervalSw", 1);
            } else if (req.get("timeInterval").equals("2")) {
                req.put("timeIntervalXw", 2);
            } else {
                throw new CommonException("排班暂只支持上午下午");
            }
            List<Map<String, Object>> isDaypartingLsit = registerMapper.getJzxh(req);
            jzxh = isDaypartingLsit.get(0).get("JZXH").toString();

        }
        req.put("jzxh", jzxh);


        int sl = 0;
        //如果病人没有建档
        if (StringUtils.isEmpty(req.get("patientId").toString())) {
            //获取机构list
            String patientId = registerMapper.queryBrda(req);
            if (StringUtils.isNotEmpty(patientId)) {
                req.put("patientId", patientId);
            } else {

                String czgh = registerMapper.queryXtcs(xtcsMap(req.get("dataSources").toString()));
                if (czgh == null || "".equals(czgh)) {
                    throw new CommonException("'机构未设置" + req.get("dataSources").toString() + "对应数据来源的系统参数");
                }
                req.put("czgh", czgh);
                String brxz = registerMapper.queryXtcs(xtcsMap("MRXZ"));
                if (brxz == null || "".equals(brxz)) {
                    throw new CommonException("机构未设置MRXZ系统参数");
                }
                req.put("brxz", brxz);
//        获取门诊号码
                req.put("mzhm", getSyhm(czgh, "3"));
                //        获取病人档案主键ID
                patientId = getIdentity("ms", "MS_BRDA").toString();
                req.put("patientId", patientId);
                req.put("JDJG", change2LocalHospitalId());
                registerMapper.PatientFiling(req);
                req.put("patientId", patientId);
                back.put("patientId", patientId);
//                Map<String,Object> msBrzh=new HashMap<>();
//                req.put("jgid", change2LocalHospitalId());
//                req.put("brid", patientId);
//                req.put("brkh", req.get("mzhm"));
//                registerMapper.saveMs_brzh(msBrzh);
            }
        }
//        查询是否已经预约
        sl = registerMapper.queryAppointment(req);
        if (sl > 0) {
            throw new CommonException("该病人已经预约过，请不要重复预约！");
        }
        List<Map<String, Object>> doctorList1 = registerMapper.queryDeptAndDoctor(req);
        if (doctorList1.size() < 1) {
            throw new CommonException("没有获取到有效的排班信息！");
        }
        String sbxh = getIdentity("ms", "MS_GHMX").toString();


        //获取打折后金额
        //调打折信息返回
        Map<String,Object> dzxx=new HashMap<>();
        dzxx.put("KSDM",req.get("depCode"));
        dzxx.put("YSDM",req.get("doctorCode"));
        dzxx.put("BRID",req.get("patientId"));
        dzxx.put("SBXH",sbxh);
        Map<String,Object> result=getDiscount(dzxx);
        Map<String,Object> AMOUNT= (Map<String, Object>) result.get("AMOUNT");
        List<String> INSERT_LIST= (List<String>) result.get("INSERT_LIST");
        req.putAll(AMOUNT);
        //判断支付金额与预约金额是否一致
        if (!Double.valueOf(AMOUNT.get("ZFJE").toString()).equals(Double.valueOf(req.get("paymentAmount").toString()))) {
            throw new CommonException("支付金额与应付金额不一致，不能预约！");
        }
        //循环写ms_ghmx_fy
        for(String sql:INSERT_LIST){
            Map<String,Object> map=new HashMap<>();
            map.put("sql",sql);
            logger.info(sql);
            registerMapper.insertMs_ghmx_fy(map);
        }


        //3获取系统参数的工号
        String czgh = registerMapper.queryXtcs(xtcsMap(req.get("dataSources").toString()));

        if (czgh == null || czgh.equals("")) {
            throw new CommonException("'机构未设置" + req.get("dataSources").toString() + "对应数据来源的系统参数");
        }




        req.put("sbxh", sbxh);

        req.put("czgh", czgh);
//        获取就诊号码
        req.put("jzhm", getSyhm(czgh, "1"));

        //获取支付方式
        List<Map<String, Object>> zffsList = registerMapper.getZffs(req);
        if (zffsList.size() < 1) {
            throw new CommonException("支付方式source输入不对");
        }
        //银联标志
        String QTFKBZ = zffsList.get(0).get("QTFKBZ").toString();
        //支付宝标志
        String ZFBBZ = zffsList.get(0).get("ZFBBZ").toString();
        //微信标志
        String WXBZ = zffsList.get(0).get("WXBZ").toString();
        String fkfs = zffsList.get(0).get("FKFS").toString();
        req.put("WX_ZFJE", WXBZ.equals("1") ? req.get("paymentAmount") : 0);
        req.put("ZFB_ZFJE", ZFBBZ.equals("1") ? req.get("paymentAmount") : 0);
        req.put("POSJE", QTFKBZ.equals("1") ? req.get("paymentAmount") : 0);
        req.put("QTFKJE", 0);
        req.put("WXBZ", WXBZ);
        req.put("ZFBBZ", ZFBBZ);
        req.put("TRADE_NO", req.get("wcoerderId").toString());
        req.put("TRAN_ID", req.get("wcoerderId").toString());

//        保存挂号明细
        sl = registerMapper.saveGhmx(req);
        if (sl <= 0) {
            throw new CommonException("保存挂号明细失败！");
        }

        if (isPaymentAmount == true) {

            String jlxh = getIdentity("ms", "MS_GH_FKXX").toString();
            req.put("fkfs", fkfs);
            req.put("jlxh", jlxh);
            //        保存付款信息
            sl = registerMapper.saveFkxx(req);
            if (sl <= 0) {
                throw new CommonException("保存付款信息失败！");
            }
//            req.remove("hospitalId");
        }

        String yyxh = getIdentity("ms", "MS_YYGH").toString();
        req.put("orderId", yyxh);
        //获取当前时间
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");

        req.put("newDate", df.format(new Date()));

        //当天挂号不写ms_yygh
        try {
            if (!df1.format(new Date()).equals(df1.format(df1.parse(req.get("registrationDate").toString())))) {
                //        预约信息ms_yygh
                sl = registerMapper.saveYygh(req);
                if (sl <= 0) {
                    throw new CommonException("保存预约信息失败");
                }
            }
        } catch (ParseException e) {
            throw new CommonException("传入时间格式错误");
        }

        if ("1".equals(req.get("isDayparting"))) {

            String ghyy_yyxx_yyxh = getIdentity("ghyy", "GHYY_YYXX").toString();
            req.put("ghyy_yyxx_yyxh", ghyy_yyxx_yyxh);
            sl = registerMapper.saveGhyyYyxx(req);
            if (sl <= 0) {
                throw new CommonException("保存分时段预约信息失败");
            }
            sl = registerMapper.updateGhyy_hyxx_Nhy2(req);
            if (sl != 1) {
                throw new CommonException("更新号源信息错误！该号源被使用");
            }
        }
        //        修改排班数据
        sl = registerMapper.lockNumber(req);
        //判断存储过程有没有报错，如果报错抛出异常
        if (sl != 1) {
            throw new CommonException("更新排班信息错误！");
        }

        //写ys_mz_jzls
        Map<String,Object> jzls=new HashMap<>();
        String JZXH = getIdentity("ys", "YS_MZ_JZLS").toString();
        jzls.put("JZXH",JZXH);
        jzls.put("GHXH",sbxh);
        jzls.put("BRBH",req.get("patientId"));
        jzls.put("KSDM",listHy.get(0).get("MZKS"));
        jzls.put("YSDM",req.get("doctorCode"));
        jzls.put("ZYZD",0);
        jzls.put("JZZT",1);
        jzls.put("JGID",change2LocalHospitalId());
        jzls.put("JZLX",5);
        jzls.put("CZPB",0);
        jzls.put("FZXH",0);
        registerMapper.saveHistory(jzls);
        // 写入队列信息
        String enterFlag = "0";

        enterFlag = registerMapper.getEnterFlag(req);
        if (!"0".equals(enterFlag)) {
            Map<String, Object> queueInfo = new HashMap<>();
            String rdid = getIdentity("pd", "PD_DLB").toString();

            queueInfo.put("rdid", rdid);
            //获取队列ID

            Map<String, Object> queueIdInfo = registerMapper.getQueueId(req);
            if(queueIdInfo.get("queueID")!=null) {
                String queueID = queueIdInfo.get("queueID").toString();
                queueInfo.put("dlid", queueIdInfo.get("queueID").toString());
                queueInfo.put("jgid", "1");
                //获取入队时间
                if (null == req.get("sourceId") || "".equals(req.get("sourceId").toString())) {
                    queueInfo.put("rdsj", queueIdInfo.get("startTime").toString());
                    queueInfo.put("kssj", queueIdInfo.get("startTime").toString());
                    queueInfo.put("hyjzsd", queueIdInfo.get("timeBet").toString());
                    queueInfo.put("jssj", queueIdInfo.get("endTime").toString());

                } else {
                    queueIdInfo = registerMapper.getStartTime(req);
                    queueInfo.put("rdsj", queueIdInfo.get("startTime").toString());
                    queueInfo.put("kssj", queueIdInfo.get("startTime").toString());
                    queueInfo.put("hyjzsd", queueIdInfo.get("timeBet").toString());
                    queueInfo.put("jssj", queueIdInfo.get("endTime").toString());

                }
                //获取排队号码

                int queueNo = registerMapper.getQueueNo(sbxh);
                queueInfo.put("pdhm", queueNo);
                queueInfo.put("pdcy", req.get("patientName").toString());


                if ("1".equals(enterFlag)) {
                    queueInfo.put("pdzt", "1");
                    queueInfo.put("bzxx", "");
                } else if ("2".equals(enterFlag)) {
                    queueInfo.put("pdzt", "0");
                    queueInfo.put("bzxx", "网络入队暂停");
                }
                queueInfo.put("ddsj", "0");
//            queueInfo.put("wcsj" , null) ;
                queueInfo.put("jlsj", "0");
                queueInfo.put("slgh", "");
                queueInfo.put("slrm", "");
                //获取医生姓名
                queueInfo.put("ysid", queueIdInfo.get("doctorCode").toString());
                queueInfo.put("ysxm", queueIdInfo.get("doctorName").toString());
                //获取科室名称
//            String deptName = registerMapper.getDepName(req.get("depCode").toString());
                queueInfo.put("ksid", queueIdInfo.get("deptCode").toString());
                queueInfo.put("ksmc", queueIdInfo.get("deptName").toString() + " " + queueIdInfo.get("doctorName").toString());

                queueInfo.put("qtid", yyxh);
//            queueInfo.put("qtmc" , "01.预约挂号") ;
                queueInfo.put("ywlb", "GH");
                queueInfo.put("ywid", sbxh);
                queueInfo.put("brid", req.get("patientId").toString());
                queueInfo.put("pmid", "0");
                queueInfo.put("pmqc", "0");
                queueInfo.put("sxh", queueNo);

                queueInfo.put("cdbz", "0");
                queueInfo.put("zsid", "0");
                queueInfo.put("jhbz", "0");
                queueInfo.put("ggdl", "0");
                queueInfo.put("zhcl", "0");

                queueInfo.put("zblb", req.get("timeInterval").toString());
                queueInfo.put("ysdlid", queueID);

                queueInfo.put("dlid_for", "");
                queueInfo.put("ghtzcs", "");

                queueInfo.put("ghbz", "0");

                queueInfo.put("jzbz", "0");

                sl = registerMapper.savePdxx(queueInfo);
                if (sl != 1) {
                    throw new CommonException("挂号写入排队信息错误！");
                }
            }
        } else {

        }








        List<Map<String, Object>> doctorList = registerMapper.queryDeptAndDoctor(req);
        if (doctorList.size() < 1) {
            throw new CommonException("没有获取到有效的排班信息！");
        }

        //写jhzf_log
        String jlxh = getIdentity("jhzf", "JHZF_LOG").toString();
        Map<String,Object> JHZF_LOG=new HashMap<>();
        JHZF_LOG.put("jlxh",jlxh);
        JHZF_LOG.put("trade_no",req.get("wcoerderId"));
        JHZF_LOG.put("trade_id",req.get("wcoerderId"));
        JHZF_LOG.put("trade_type","3");
        JHZF_LOG.put("his_fphm",req.get("jzhm"));
        JHZF_LOG.put("his_djh",doctorList.get(0).get("his_djh")+","+req.get("patientId"));
        JHZF_LOG.put("trade_user",czgh);
        JHZF_LOG.put("charge_type","1");
        JHZF_LOG.put("trade_amount",req.get("paymentAmount"));
        JHZF_LOG.put("his_operate","1");
        JHZF_LOG.put("pay_type","1");
        if(req.get("dataSources").toString().equals("CZGH_GZH_QY")||req.get("dataSources").toString().equals("CZGH_GZH_YY")||req.get("dataSources").toString().equals("CZGH_JHZF")) {
            registerMapper.saveJHZF_LOG(JHZF_LOG);
        }

        //写ms_mzpj_nonprint
        String Mssbxh = getIdentity("ms", "MS_MZPJ_NONPRINT").toString();
        Map<String,Object> Ms_mzpj_nonprint=new HashMap<>();
        Ms_mzpj_nonprint.put("sbxh",Mssbxh);
        Ms_mzpj_nonprint.put("czgh",czgh);
        Ms_mzpj_nonprint.put("pjlx","1");
        String pjly="";
        if(req.get("dataSources").toString().equals("CZGH_ZZJ_CC")){
            pjly="5";
        }else if(req.get("dataSources").toString().equals("CZGH_ZZJ_KLE")){
            pjly="2";
        }else if(req.get("dataSources").toString().equals("CZGH_JHZF")){
            pjly="3";
        }else if(req.get("dataSources").toString().equals("CZGH_GZH_YY")){
            pjly="6";
        }else{
            pjly="4";
        }
        Ms_mzpj_nonprint.put("pjly",pjly);
        Ms_mzpj_nonprint.put("mz_pjhm",req.get("jzhm"));
        Ms_mzpj_nonprint.put("mzlb",doctorList.get(0).get("MZLB"));

        registerMapper.saveMs_mzpj_nonprint(Ms_mzpj_nonprint);
        String mzhm = registerMapper.queryBrdaMzhm(req);
        back.put("outpatientNumber", mzhm);
        back.put("JZXH", JZXH);
        back.put("qssj", qssj);
        back.put("zzsj", zzsj);
        back.put("appointmentDate", appointmentDate);
        back.put("invoiceNumber", req.get("jzhm"));
        back.put("availableCount", doctorList.get(0).get("registerLimit"));
        back.put("addr", doctorList.get(0).get("addr"));
        back.put("seqCode", sbxh);
        back.put("patientSerialNumber", jzxh);
        back.put("price", doctorList.get(0).get("totalMoney"));
        back.put("transDate", df.format(new Date()));
        back.put("orderFee", doctorList.get(0).get("consultationFee"));
        back.put("orderTimeRange", "1".equals(doctorList.get(0).get("timeInterval").toString()) ? "上午" : "下午");
        back.put("orderDate", doctorList.get(0).get("registerDate"));
        JSONObject json = new JSONObject();
        json.putAll(back);



        //写急诊的分床
        String jzlsh = getIdentityEcis("ecis", "ECIS_JZJL").toString();
        List<Map<String,Object>> list=esisMapper.getECIS_YJFZ(req);
        if(list.size()<1){
            throw new CommonException("未获取到急诊病人信息FZXH"+req.get("FZXH"));
        }
        //获取床位更新
        String brch="";
        List<Map<String,Object>> listECIS_CWSZ=esisMapper.getECIS_CWSZ(list.get(0));
        if(listECIS_CWSZ.size()<1){
            throw new CommonException("床位已满");
        }
        for(Map<String,Object> ECIS_CWSZ:listECIS_CWSZ){
            ECIS_CWSZ.put("JZLSH",jzlsh);
            n=esisMapper.updateECIS_CWSZ(ECIS_CWSZ);
            if(n==1){
                brch=ECIS_CWSZ.get("CWHM").toString();
                break;
            }
        }
        //写ECIS_JZJL
        Map<String,Object> ECIS_JZJL=new HashMap<>();
        ECIS_JZJL.put("jzlsh",jzlsh);
        ECIS_JZJL.put("fzxh",list.get(0).get("FZXH"));
        ECIS_JZJL.put("mzhm",list.get(0).get("MZHM"));
        ECIS_JZJL.put("brxm",list.get(0).get("BRXM"));
        ECIS_JZJL.put("brnl",list.get(0).get("BRNL"));
        ECIS_JZJL.put("brxb",list.get(0).get("BRXB"));
        ECIS_JZJL.put("zgys",list.get(0).get("ZGYS"));
        ECIS_JZJL.put("zghs",list.get(0).get("FZHS"));
        ECIS_JZJL.put("jzlx",list.get(0).get("FZQX").toString().equals("1")?"1":"2");
        ECIS_JZJL.put("brch",brch);
        ECIS_JZJL.put("brks",list.get(0).get("FZKS"));
//        ECIS_JZJL.put("brzd",list.get(0).get(""));
//        ECIS_JZJL.put("brzdid",list.get(0).get(""));
        ECIS_JZJL.put("brly",1);
        ECIS_JZJL.put("lspb",0);
        ECIS_JZJL.put("lsqx",1);
        ECIS_JZJL.put("zxzt",0);
        ECIS_JZJL.put("jgid",1);
        ECIS_JZJL.put("hisjzxh",JZXH);
        ECIS_JZJL.put("csyy",list.get(0).get(""));
        ECIS_JZJL.put("zyyy",list.get(0).get(""));
        ECIS_JZJL.put("zyks",list.get(0).get(""));
        ECIS_JZJL.put("yydh",list.get(0).get(""));
        ECIS_JZJL.put("wjzbz",0);
        ECIS_JZJL.put("yjje",list.get(0).get(""));
        esisMapper.insertEcis_jzjl(ECIS_JZJL);
        //更新ECIS_YJFZ
        esisMapper.updateECIS_YJFZ(list.get(0));
        return json;
    }
    /**
     * create by:
     * description: 获取主键方法
     * create time: 2019/03/11 10:35
     *
     * @return a
     * @Param null
     */
    public Long getIdentityEcis(String identity, String bmc) {
        long dqz = 0L;
        long dzz = 0L;
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("bmc", bmc);
        paramMap.put(identity, "1");
        int i = 0;
        do {
            Map<String, Object> map = esisMapper.getIdentity(paramMap);
            try {
                dqz = Long.valueOf(map.get("dqz").toString());
                dzz = Long.valueOf(map.get("dzz").toString());
                paramMap.putAll(map);
                i = esisMapper.updateIdentity(paramMap);
            } catch (Exception e) {
                throw new CommonException("获取" + bmc + "主键失败");
            }

        } while (i == 0);
        return dqz + dzz;
    }
}
