package com.bsoft.service.impl;

import com.bsoft.exception.CommonException;
import com.bsoft.mapper.business.ClinicPaymentMapper;
import com.bsoft.service.ClinicPaymentService;
import com.bsoft.utrl.ConmonUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Clob;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static java.util.Arrays.asList;

/**
 * @author liujx
 * @date 2019/4/26
 */
@Service
@EnableTransactionManagement
public class ClinicPaymentServiceImpl extends ConmonUtil implements ClinicPaymentService {
    @Resource
    ClinicPaymentMapper clinicPaymentMapper;


    /**
     * 门诊就诊记录
     *
     * @param req
     */
    @Override
    public String getMedicalRecords(Map<String, Object> req) throws CommonException {
        isNUll(req, "pendingPaid");
        isNUll(req, "idCard", "patientId");
        if (req.get("idCard") != null && !req.get("idCard").toString().equals("")) {
            toCharacterString(req, "idCard");
        }        //分页
        pagination(req);
        List<Map<String, Object>> list = clinicPaymentMapper.getMedicalRecords(req);
        return getSuccessPagination(list);
    }

    /**
     * 获取处方支付列表
     *
     * @param req
     */
    @Override
    public String getPrescriptionPayment(Map<String, Object> req) throws CommonException {
        //isNUll(req, asList( "hospitalId"));
        if (req.get("idCard") != null && !req.get("idCard").toString().equals("")) {
            toCharacterString(req, "idCard");
        }
        //分页
        pagination(req);
        List<Map<String, Object>> list = clinicPaymentMapper.getPrescriptionPayment(req);
        return getSuccessPagination(list);
    }

    /**
     * 获取处置支付列表
     *
     * @param req
     */
    @Override
    public String getDisposalPayment(Map<String, Object> req) throws CommonException {
        //isNUll(req, asList("visitingId", "hospitalId"));


        //分页
        pagination(req);
        List<Map<String, Object>> list = clinicPaymentMapper.getDisposalPayment(req);
        return getSuccessPagination(list);
    }

    /**
     * 获取处方明细列表
     *
     * @param req
     */
    @Override
    public String getPrescriptionDetailed(Map<String, Object> req) throws CommonException {
        isNUll(req, asList("prescriptionNumber", "hospitalId"));
        toCharacterString(req, "prescriptionNumber");
        //分页
        pagination(req);
        List<Map<String, Object>> list = clinicPaymentMapper.getPrescriptionDetailed(req);
        return getSuccessPagination(list);
    }

    /**
     * 获取处置明细列表
     *
     * @param req
     */
    @Override
    public String getDisposalDetailed(Map<String, Object> req) throws CommonException {
        isNUll(req, asList("disposalNumber", "hospitalId"));
        //分页
        pagination(req);
        List<Map<String, Object>> list = clinicPaymentMapper.getDisposalDetailed(req);
        return getSuccessPagination(list);
    }

    /**
     * 结算处方
     *
     *
     * @param json
     * @return
     */
    @Override
    public String settlementPrescription(JSONObject json) {
        //调用平台上传
//        try {
//            List<Map<String, Object>> list = (List<Map<String, Object>>) json.get("jyjc");
//            for (Map<String, Object> map : list) {
//                if (map.get("TJFL").toString().equals("4")) {
//                    pusPlatform("LabRequestPayBill", map.get("SQID").toString(), "ODS_2104", "L_LIS_SQD", map.get("CFSB").toString());
//
//                }
//                if (map.get("TJFL").toString().equals("5")) {
//                    pusPlatform("ExmRequestPayBill", map.get("SQID").toString(), "ODS_2204", "EMR_JCSQ", map.get("CFSB").toString());
//
//                }
//            }
//            json.remove("jyjc");
//        } catch (Exception e){
//
//            e.printStackTrace();
//        }
        return getSuccess(json);
    }


    /**
     * 结算处方
     *
     * @param req
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public synchronized JSONObject settlementPrescriptionJson(Map<String, Object> req) throws CommonException {
        String FPHM = "";
        String his_djh="";
        List<Map<String, Object>> listCK = new ArrayList<>();
        req = change2LocalHospitalId(req);
        isNUll(req, asList("visitingId", "hospitalId", "payedTime", "payAmount", "paymentMethod", "dataSources", "orderNumber"));
        if (req.get("prescriptionNumber") != null && !req.get("prescriptionNumber").toString().equals("")) {
            toCharacterString(req, "prescriptionNumber");
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date payedTime;
        try {
            payedTime = formatter.parse(req.get("payedTime").toString());
        } catch (ParseException e) {
            e.printStackTrace();
            throw new CommonException("传入payedTime时间格式不是yyyy-MM-dd 24HH:mm:ss");
        }
        //操作工号
        String czgh = "";
        //使用号码
        String syhm = "";
        //支付方式
        String zffs = "";
        //支付金额
        Double payAmount = 0.0;
        //门诊序号
        Long mzxh;
        //微信金额
        Double WX_ZFJE = 0.0;
        //支付宝金额
        Double ZFB_ZFJE = 0.0;
        //银联金额
        Double QTFKJE = 0.0;
        //支付宝标志
        String ZFBBZ = "0";
        //微信标志
        String WXBZ = "0";
        //保存所有发票金额的合计
        Double paySum = 0.0;
        //处方处置判断标志
        boolean cfpb;
        //1查询传入金额是否与处方号条件下的金额一样
        //payAmount = clinicPaymentMapper.settlementPrescription(req);
//        payAmount =Double.valueOf(req.get("payAmount").toString());
//        if (payAmount == null) {
//            throw new CommonException("未找到处方的缴费信息");
//        }
//        if (payAmount - Double.valueOf(req.get("payAmount").toString()) != 0) {
//            throw new CommonException("结算失败，传入金额为" + req.get("payAmount").toString() + ",未交费金额为" + (payAmount));
//        }
        //2-获取支付方式
        List<Map<String, Object>> zffsList = clinicPaymentMapper.getZffs(req);
        if (zffsList.size() < 1) {
            throw new CommonException("支付方式paymentMethode输入不对");
        }
        zffs = zffsList.get(0).get("FKFS").toString();


//        //支付宝标志
//        ZFBBZ = zffsList.get(0).get("ZFBBZ").toString();
//        //微信标志
//        WXBZ = zffsList.get(0).get("WXBZ").toString();
//        zffs = zffsList.get(0).get("FKFS").toString();
//        if (ZFBBZ.equals("1")) {
//            ZFB_ZFJE = payAmount;
//        } else if (WXBZ.equals("1")) {
//            WX_ZFJE = payAmount;
//        } else {
//            QTFKJE = payAmount;
//
//        }


        //3获取系统参数的工号
        czgh = clinicPaymentMapper.getXtcs(xtcsMap(req.get("dataSources").toString()));
        if (czgh == null || czgh.equals("")) {
            throw new CommonException("'机构未设置" + req.get("dataSources").toString() + "对应数据来源的系统参数");
        }

        //4根据就诊序号从挂号里获取brid，BRXM，BRXB，MZLB，GHGL
        List<Map<String, Object>> list = clinicPaymentMapper.settlementPrescription5(req);
        if (list.size() < 1) {
            throw new CommonException("根据该就诊号码获取个人信息失败,无该就诊号码挂号或者病人信息");
        }
        List<Map<String, Object>> list_cfsb = clinicPaymentMapper.settlementPrescription10(req);
        List<Map<String, Object>> jyjc= clinicPaymentMapper.settlementPrescription10_1(req);
        List<Map<String, Object>> list_cfsb_count = new ArrayList<>();
        //取出执行科室一样的放在一个map里
        for (Map<String, Object> map_cfsb : list_cfsb) {
            his_djh=his_djh.equals("")?his_djh+map_cfsb.get("CFSB"):his_djh+","+map_cfsb.get("CFSB");
            boolean stay = true;
            for (Map<String, Object> map_cfsb_count : list_cfsb_count) {
                if (map_cfsb.get("ZXKS").toString().equals(map_cfsb_count.get("ZXKS").toString())) {
                    map_cfsb_count.put("CFSB", map_cfsb_count.get("CFSB").toString() + ',' + map_cfsb.get("CFSB").toString());
                    stay = false;
                }
            }
            if (stay) {
                list_cfsb_count.add(map_cfsb);
            }
        }

        //5根据科室分组查询处方号码结算
        for (Map<String, Object> map_cfsb : list_cfsb_count) {
            String cfsb = map_cfsb.get("CFSB").toString();
            String zxks = map_cfsb.get("ZXKS").toString();
            //判断说处方还是处置，处方执行科室说yf字母开头
            if (zxks.contains("yf")) {
                cfpb = true;
            } else {
                cfpb = false;
            }
            req.put("cfsb", cfsb);
            String CFSB = "";
            String YJXH = "";
            //6查询该循环的金额
            if (cfpb) {
                payAmount = clinicPaymentMapper.settlementPrescription1(req);
            } else {
                payAmount = clinicPaymentMapper.settlementDisposal1(req);
            }
            if (payAmount == null || payAmount == 0) {
                throw new CommonException(cfsb + "处方需缴费金额为0");
            }

// 调接口获取打折信息
//            Map<String, Object> dzxx = new HashMap<>();
//            dzxx.put("CFSB", CFSB);
//            dzxx.put("YJXH", YJXH);
//            dzxx.put("BRID", list.get(0).get("BRID").toString());
//            Map<String, Object> result = getDiscount(dzxx);
//            List<Map<String, Object>> RESULTLIST = (List<Map<String, Object>>) result.get("RESULTLIST");
//            BigDecimal ZJJE = new BigDecimal(0);
//            BigDecimal b2 = new BigDecimal(0);
//            for (Map<String, Object> map1 : RESULTLIST) {
//                map1.get("ZJJE").toString();
//                BigDecimal b1 = new BigDecimal(map1.get("ZJJE").toString());
//
//                ZJJE = ZJJE.add(b1);
//                //更01新减免标志的打折比例
//                int n = clinicPaymentMapper.updateJMBZ01(map1);
//                if (n != 1) {
//                    throw new CommonException("减免01更新错误");
//                }
//                //更新02的打折比例
//                List<Map<String, Object>> CFYJ02S = (List<Map<String, Object>>) map1.get("CFYJ02S");
//                for (Map<String, Object> map2 : CFYJ02S) {
//                    map2.putAll(map1);
//                    n = clinicPaymentMapper.updateJMBZ02(map2);
//                    if (n != 1) {
//                        throw new CommonException("减免02更新错误");
//                    }
//                    b1 = b1.subtract(new BigDecimal(map2.get("JMJE").toString()));
//                    b2=b2.add(new BigDecimal(map2.get("JMQTYS").toString()));
//                }
//                //判断01的合计金额等于02不
//                if (b1.compareTo(BigDecimal.ZERO) != 0) {
//                    throw new CommonException("打折返回02和不等于01");
//                }
//            }
//            payAmount = ZJJE.doubleValue();

            paySum = paySum + payAmount;
            //7根据工号获取发票号码
            List<Map<String, Object>> ms_ygpj = clinicPaymentMapper.settlementPrescription2(czgh);
            if (ms_ygpj.size() < 1) {
                throw new CommonException("机构未设置" + czgh + "员工的门诊发票");
            }
            syhm = ms_ygpj.get(0).get("SYHM").toString();
            if (syhm.equals(ms_ygpj.get(0).get("ZZHM").toString())) {
                //使用号码等于终止号码，说明是最后一张发票打上sypb
                int n = clinicPaymentMapper.settlementPrescription3(ms_ygpj.get(0));
                if (n != 1) {
                    throw new CommonException("更新发票号码错误");
                }
            } else {
                //发票未使用发票号完加一,再写回ms_ygpj
                ms_ygpj.get(0).put("SYHM", addOne(syhm));
                int n = clinicPaymentMapper.settlementPrescription4(ms_ygpj.get(0));
                if (n != 1) {
                    throw new CommonException("更新发票号码错误");
                }
            }
            //8获取门诊序号
            mzxh = getIdentity("ms", "MS_MZXX");

            //9写ms_mzxx
            Map<String, Object> mzxx = list.get(0);
            mzxx.put("MZXH", mzxh);
            mzxx.put("JGID", change2LocalHospitalId());
            mzxx.put("FPHM", syhm);
            mzxx.put("DWXH", 0);
            mzxx.put("XJJE", 0);
            mzxx.put("ZPJE", 0);
            mzxx.put("ZHJE", 0);
            mzxx.put("HBWC", 0);
            mzxx.put("QTYS", payAmount);
            mzxx.put("ZJJE", payAmount);
            mzxx.put("ZFJE", payAmount);
            mzxx.put("ZFPB", 0);
            mzxx.put("THPB", 0);
            mzxx.put("MZGL", 0);
            mzxx.put("CZGH", czgh);
            mzxx.put("SFFS", 0);
            mzxx.put("HBBZ", 0);
            mzxx.put("JKJE", 0);
            mzxx.put("TZJE", 0);
            mzxx.put("YBZHJE", 0);
            mzxx.put("POSJE",0);
            mzxx.put("DZJE", 0);
            mzxx.put("FPBD", 0);
            mzxx.put("PLATEFORMNUM", req.get("orderNumber").toString());

            int n=0;
            clinicPaymentMapper.settlementPrescription6(mzxx);

            FPHM = FPHM.equals("") ? FPHM + syhm : FPHM + "," + syhm;





            //11获取记录序号
            Long jlxh = getIdentity("ms", "MS_FKXX");
            //12写ms_fkxx
            Map<String, Object> ms_fkxx = new HashMap<>();
            ms_fkxx.put("JLXH", jlxh);
            ms_fkxx.put("JGID", change2LocalHospitalId());
            ms_fkxx.put("MZXH", mzxh);
            ms_fkxx.put("FKFS", zffs);
            ms_fkxx.put("FKJE", payAmount);
            ms_fkxx.put("TRAN_ID", req.get("orderNumber").toString());
            ms_fkxx.put("TRAN_DATE", payedTime);
            n = clinicPaymentMapper.settlementPrescription8(ms_fkxx);
            if (n < 1) {
                throw new CommonException("写MS_SFMX错误");
            }
            //13ms_cf01 写上门诊序号和发票号码 和ms_cf02 上写发票号码
            req.put("mzxh", mzxh);
            req.put("syhm", syhm);
            if (cfpb) {
                //查看处方类型和药房
                listCK = clinicPaymentMapper.settlementPrescription14(req);
                //查询排队最少的发药窗口
                listCK = clinicPaymentMapper.settlementPrescription15(listCK.get(0));
                if (listCK.size() < 1) {
                    req.put("fyck", "1");

                } else {
                    req.put("fyck", listCK.get(0).get("CKBH"));

                }
                n = clinicPaymentMapper.settlementPrescription9(req);
                if (n < 1) {
                    throw new CommonException("处理ms_cf01失败");
                }
//                n = clinicPaymentMapper.settlementPrescription13(req);
//                if (n < 1 || n > 500) {
//                    throw new CommonException("处理ms_cf02失败");
//                }
            } else {
                n = clinicPaymentMapper.settlementDisposal9(req);
                if (n < 1) {
                    throw new CommonException("处理ms_yj01失败");
                }
//                n = clinicPaymentMapper.settlementDisposal13(req);
//                if (n < 1 || n > 500) {
//                    throw new CommonException("处理ms_yj02失败");
//                }
            }
            //10写MS_SFMX
            req.put("mzxh", mzxh);
            req.put("syhm", syhm);
            if (cfpb) {
                n = clinicPaymentMapper.settlementPrescription7(req);
            } else {
                n = clinicPaymentMapper.settlementDisposal7(req);
            }
            if (n < 1) {
                throw new CommonException("写MS_SFMX错误");
            }


        }

        //14判断发票合计是不是等于出入金额合计
        if (Math.round(paySum * 100) / 100.0 - Double.valueOf(req.get("payAmount").toString()) != 0) {
            throw new CommonException("结算失败，传入金额为" + req.get("payAmount").toString() + ",发票合计金额为" + (paySum));
        }



        Map<String, Object> back = new HashMap<>();
        back.put("invoiceNumber", FPHM);
        back.put("jyjc",jyjc);
        JSONObject json = new JSONObject();
        json.putAll(back);
        return json;
    }

    /**
     * 结算处置
     *
     * @param req
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String settlementDisposal(Map<String, Object> req) throws CommonException {
//        isNUll(req, asList("visitingId","hospitalId","disposalNumber","payedTime","payAmount","paymentMethod","dataSources","orderNumber"));
//        //操作工号
//        String czgh="";
//        //使用号码
//        String syhm="";
//        //支付方式
//        String zffs="";
//        //支付金额
//        Double payAmount=0.0;
//        //门诊序号
//        Long mzxh;
//        //微信金额
//        Double WX_ZFJE=0.0;
//        //支付宝金额
//        Double ZFB_ZFJE=0.0;
//        //银联金额
//        Double QTFKJE=0.0;
//        //支付宝标志
//        String ZFBBZ="0";
//        //微信标志
//        String WXBZ="0";
//        //1查询传入金额是否与处置号条件下的金额一样
//        payAmount=clinicPaymentMapper.settlementDisposal1(req);
//        if(payAmount==null){
//            throw new CommonException("未找到处置的缴费信息");
//        }
//        if(payAmount-Double.valueOf(req.get("payAmount").toString())!=0){
//            throw new CommonException("结算失败，传入金额为"+req.get("payAmount").toString()+",未交费金额为"+(payAmount));
//        }
//        //2-获取支付方式
//        switch(req.get("paymentMethod").toString()){
//            case "1":
//                zffs="ZFFS_ZFB";
//                ZFB_ZFJE=payAmount;
//                ZFBBZ="1";
//                break;
//            case "2":
//                zffs="ZFFS_WX";
//                WX_ZFJE=payAmount;
//                WXBZ="1";
//                break;
//            case "3":
//                zffs="ZFFS_YL";
//                QTFKJE=payAmount;
//                break;
//            default:
//                throw new CommonException("支付方式paymentMethod输入不对");
//        }
//        zffs=clinicPaymentMapper.getXtcs(zffs);
//        if(zffs==null||zffs.equals("")){
//            throw new CommonException("'机构未设置对应ZFFS_ZFB、ZFFS_WX、ZFFS_YL支付方式系统参数'");
//        }
//        //3获取系统参数的工号
//        czgh=clinicPaymentMapper.getXtcs(req.get("dataSources").toString());
//        if(czgh==null||czgh.equals("")){
//            throw new CommonException("'机构未设置"+req.get("dataSources").toString()+"对应数据来源的系统参数");
//        }
//        //4根据工号获取发票号码
//        List<Map<String, Object>> ms_ygpj=clinicPaymentMapper.settlementPrescription2(czgh);
//        if(ms_ygpj.size()<1){
//            throw new CommonException("机构未设置"+czgh+"员工的门诊发票");
//        }
//        syhm=ms_ygpj.get(0).get("SYHM").toString();
//        if(syhm.equals(ms_ygpj.get(0).get("ZZHM").toString())){
//            //使用号码等于终止号码，说明是最后一张发票打上sypb
//            int n=clinicPaymentMapper.settlementPrescription3(ms_ygpj.get(0));
//            if(n!=1){
//                throw new CommonException("更新发票号码错误");
//            }
//        }else {
//            //发票未使用发票号完加一,再写回ms_ygpj
//            ms_ygpj.get(0).put("SYHM",addOne(syhm));
//            int n=clinicPaymentMapper.settlementPrescription4(ms_ygpj.get(0));
//            if(n!=1){
//                throw new CommonException("更新发票号码错误");
//            }
//        }
//        //5获取门诊序号
//        mzxh=getIdentity("ms","MS_MZXX");
//        //6根据就诊序号从挂号里获取brid，BRXM，BRXB，MZLB，GHGL
//        List<Map<String, Object>> list=clinicPaymentMapper.settlementPrescription5(req);
//        if(list.size()<1){
//            throw new CommonException("根据该就诊号码获取个人信息失败,无该就诊号码挂号或者病人信息");
//        }
//        //7写ms_mzxx
//        Map<String, Object> mzxx=list.get(0);
//        mzxx.put("MZXH",mzxh);
//        mzxx.put("JGID",1);
//        mzxx.put("FPHM",syhm);
//        mzxx.put("DWXH",0);
//        mzxx.put("XJJE",0);
//        mzxx.put("ZPJE",0);
//        mzxx.put("ZHJE",0);
//        mzxx.put("HBWC",0);
//        mzxx.put("QTYS",0);
//        mzxx.put("ZJJE",payAmount);
//        mzxx.put("ZFJE",payAmount);
//        mzxx.put("ZFPB",0);
//        mzxx.put("THPB",0);
//        mzxx.put("MZGL",0);
//        mzxx.put("CZGH",czgh);
//        mzxx.put("SFFS",0);
//        mzxx.put("HBBZ",0);
//        mzxx.put("JKJE",0);
//        mzxx.put("TZJE",0);
//        mzxx.put("YBZHJE",0);
//        mzxx.put("POSJE",0);
//        mzxx.put("WX_ZFJE",WX_ZFJE);
//        mzxx.put("ZFB_ZFJE",ZFB_ZFJE);
//        mzxx.put("QTFKJE",QTFKJE);
//        mzxx.put("DZJE",0);
//        mzxx.put("WXBZ",WXBZ);
//        mzxx.put("ZFBBZ",ZFBBZ);
//        mzxx.put("FPBD",0);
//        mzxx.put("TRADE_NO",req.get("orderNumber").toString());
//        int n=clinicPaymentMapper.settlementPrescription6(mzxx);
//        if(n!=1){
//            throw new CommonException("写ms_mzxx错误");
//        }
//        //8写MS_SFMX
//        req.put("mzxh",mzxh);
//        req.put("syhm",syhm);
//        n=clinicPaymentMapper.settlementDisposal7(req);
//        if(n<1){
//            throw new CommonException("写MS_SFMX错误");
//        }
//        //8获取记录序号
//        Long jlxh=getIdentity("ms","MS_FKXX");
//        //9写ms_fkxx
//        Map<String,Object> ms_fkxx=new HashMap<>();
//        ms_fkxx.put("JLXH",jlxh);
//        ms_fkxx.put("JGID",1);
//        ms_fkxx.put("MZXH",mzxh);
//        ms_fkxx.put("FKFS",zffs);
//        ms_fkxx.put("FKJE",payAmount);
//        n=clinicPaymentMapper.settlementPrescription8(ms_fkxx);
//        if(n<1){
//            throw new CommonException("写MS_SFMX错误");
//        }
//        //10yj_cf01 写上门诊序号和发票号码
//        req.put("mzxh",mzxh);
//        req.put("syhm",syhm);
//        n=clinicPaymentMapper.settlementDisposal9(req);
//        if(n<1){
//            throw new CommonException("处理ms_yj01失败");
//        }

        return getSuccess("结算成功");
    }

    /**
     * 获取处方已支付列表
     *
     * @param req
     */
    @Override
    public String getPrescriptionPaymented(Map<String, Object> req) throws CommonException {
        // isNUll(req, asList("visitingId"));
        if (req.get("idCard") != null && !req.get("idCard").toString().equals("")) {
            toCharacterString(req, "idCard");
        }
        //分页
        pagination(req);
        List<Map<String, Object>> list = clinicPaymentMapper.getPrescriptionPaymented(req);
        return getSuccessPagination(list);
    }

    /**
     * 获取处置已支付列表
     *
     * @param req
     */
    @Override
    public String getDisposalPaymented(Map<String, Object> req) throws CommonException {
        //isNUll(req, asList("visitingId"));
        //分页
        pagination(req);
        List<Map<String, Object>> list = clinicPaymentMapper.getDisposalPaymented(req);
        return getSuccessPagination(list);
    }

    /**
     * 取消处方结算
     *
     * @param req
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String cancelSettlementPrescription(Map<String, Object> req) throws CommonException {
        isNUll(req, asList("hospitalId", "visitingId", "invoiceNumber", "payAmount"));
        req = change2LocalHospitalId(req);
        //查询是否有该发票号码的处方
        List<Map<String, Object>> list = clinicPaymentMapper.getPrescriptionPaymented(req);
        if (list.size() == 0) {
            throw new CommonException("取消结算失败,未找到该发票的处方");
        }
        boolean cfpb = true;
        //查询是处置还是处方(看处方号是不是包含cz，包含就是处置)
        if (list.get(0).get("prescriptionNumber").toString().contains("cz")) {
            cfpb = false;
        }
        //根据发票号码查询该发票的处方是否发药或者已经作废
        if (cfpb) {
            list = clinicPaymentMapper.getDispensing(req);
        } else {
            list = clinicPaymentMapper.getImplement(req);
        }
        if (list.size() > 0) {
            throw new CommonException("取消结算失败,该发票已发药、执行或者已作废");
        }
        //查询处方金额，是否与出入金额相同
        Double hjje = clinicPaymentMapper.getPrescriptionAmount(req);
        if (hjje == null) {
            throw new CommonException("取消结算失败,未找到传入发票");
        } else if (hjje - Double.valueOf(req.get("payAmount").toString()) != 0) {
            throw new CommonException("取消结算失败,传入金额不等于发票金额，发票金额为" + hjje);
        }
        //写ms_zffp
        int n = clinicPaymentMapper.invalidInvoice1(req);
        if (n != 1) {
            throw new CommonException("写ms_zffp失败");
        }
        //修改ms_mzxx
        n = clinicPaymentMapper.invalidInvoice2(req);
        if (n != 1) {
            throw new CommonException("写修改ms_mzxx失败");
        }
        if (cfpb) {
            //修改ms_cf01
            n = clinicPaymentMapper.invalidInvoice3(req);
            if (n == 0) {
                throw new CommonException("修改ms_cf01失败");
            }
        } else {
            //修改ms_yi01
            n = clinicPaymentMapper.invalidInvoice4(req);
            if (n == 0) {
                throw new CommonException("修改ms_yi01失败");
            }
        }
        return getSuccess("作废成功");
    }

    /**
     * 取消处置结算
     *
     * @param req
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String cancelSettlementDisposal(Map<String, Object> req) throws CommonException {
        isNUll(req, asList("hospitalId", "visitingId", "invoiceNumber", "payAmount"));
        req = change2LocalHospitalId(req);


        //根据发票号码查询该发票的处置是否执行或者已经作废
        List<Map<String, Object>> list = clinicPaymentMapper.getImplement(req);
        if (list.size() > 0) {
            throw new CommonException("取消结算失败,该发票已作废或者处置已执行");
        }
        //查询处方金额，是否与出入金额相同
        Double hjje = clinicPaymentMapper.getPrescriptionAmount(req);
        if (hjje == null) {
            throw new CommonException("取消结算失败,未找到传入发票");
        } else if (hjje - Double.valueOf(req.get("payAmount").toString()) != 0) {
            throw new CommonException("取消结算失败,传入金额不等于发票金额，发票金额为" + hjje);
        }
        //写ms_zffp
        int n = clinicPaymentMapper.invalidInvoice1(req);
        if (n != 1) {
            throw new CommonException("写ms_zffp失败");
        }
        //修改ms_mzxx
        n = clinicPaymentMapper.invalidInvoice2(req);
        if (n != 1) {
            throw new CommonException("写修改ms_mzxx失败");
        }
        //修改ms_yi01
        n = clinicPaymentMapper.invalidInvoice4(req);
        if (n == 0) {
            throw new CommonException("修改ms_yi01失败");
        }

        return getSuccess("作废成功");
    }

    /**
     * 就诊历史列表
     *
     * @param req
     */
    @Override
    public String medicalRecordsList(Map<String, Object> req) throws CommonException {
        isNUll(req, "idCard", "patientId");
        if (req.get("idCard") != null && !req.get("idCard").toString().equals("")) {
            toCharacterString(req, "idCard");
        }
        //分页
        pagination(req);
        List<Map<String, Object>> list = clinicPaymentMapper.medicalRecordsList(req);
        return getSuccessPagination(list);
    }

    /**
     * 就诊历史详情
     *
     * @param req
     */
    @Override
    public String medicalRecordsDetails(Map<String, Object> req) throws CommonException, IOException, SQLException {
        isNUll(req, asList("visitingId", "hospitalId"));
        List<Map<String, Object>> list = clinicPaymentMapper.medicalRecordsDetails(req);
        if (list.size() < 1) {
            throw new CommonException("无就诊历史详情");
        }
        return getSuccess(list.get(0));
    }

    /**
     * his交易记录查询
     *
     * @param req
     */
    @Override
    public String getHisPay(Map<String, Object> req) throws CommonException {


        List<Map<String, Object>> list = clinicPaymentMapper.getHisPay(req);
        return getSuccessZd(JSONArray.fromObject(list));
    }

    /**
     * 门诊病人处置检索
     *
     * @param req
     * @return
     */
    @Override
    public String GetManagementInfo(Map<String, Object> req) throws CommonException {
        //分页判断
        pagination(req);
        List<Map<String, Object>> list = clinicPaymentMapper.GetManagementInfo(req);
        return getSuccessPagination(list);
    }

    /**
     * 药 查询
     *
     * @param req
     * @return
     */
    @Override
    public String getUseDrugs(Map<String, Object> req) throws CommonException {
        //分页判断
        pagination(req);
        List<Map<String, Object>> list = clinicPaymentMapper.getUseDrugs(req);
        return getSuccessPagination(list);
    }
}