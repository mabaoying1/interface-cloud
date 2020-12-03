package com.bsoft.service.impl;


import com.bsoft.exception.CommonException;
import com.bsoft.mapper.business.ClinicPaymentMapper;
import com.bsoft.mapper.lis.ReportQueryLisMapper;
import com.bsoft.mapper.pacs.ReportQueryPacsMapper;
import com.bsoft.service.ReportQueryService;
import com.bsoft.utrl.ConmonUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

import static java.util.Arrays.asList;

/**
 * @author liujx
 * @date 2019/4/26
 */
@Service

public class ReportQueryServiceImpl extends ConmonUtil implements ReportQueryService {
    @Resource
    ReportQueryLisMapper reportQueryLisMapper;
    @Resource
    ReportQueryPacsMapper reportQueryPacsMapper;
    @Resource
    ClinicPaymentMapper clinicPaymentMapper;
    /**
     * 检验报告列表
     *
     * @param req
     */
    @Override
    public String getLisRecord(Map<String, Object> req) throws CommonException {
        toCharacterString(req,"idCard");
        //分页
        pagination(req);
        List<Map<String, Object>> list = reportQueryLisMapper.getLisRecord(req);
        return getSuccessPagination(list);
    }

    /**
     * 检验报告列表详细信息
     *
     * @param req
     */
    @Override
    public String getLisDetails(Map<String, Object> req) throws CommonException {
        isNUll(req, asList("rId","hospitalId"));
        //分页
        pagination(req);
        List<Map<String, Object>> list = reportQueryLisMapper.getLisDetails(req);
        return getSuccessPagination(list);
    }

    /**
     * 检查报告列表
     *
     * @param req
     */
    @Override
    public String getRisRecord(Map<String, Object> req) throws CommonException {
        toCharacterString(req,"idCard");


        //分页
        pagination(req);
        List<Map<String, Object>> list = reportQueryPacsMapper.getRisRecord(req);
        return getSuccessPagination(list);
    }

    /**
     * 检查报告列表详细信息
     *
     * @param req
     */
    @Override
    public String getRisDetails(Map<String, Object> req) throws CommonException {
        isNUll(req, asList("examNo","hospitalId"));
        //分页
        pagination(req);
        List<Map<String, Object>> list = reportQueryPacsMapper.getRisDetails(req);
        return getSuccessPagination(list);
    }

    /**
     * 检验项目查询
     *
     * @param req
     */
    @Override
    public String getTestItems(Map<String, Object> req) throws CommonException {
        //分页
        pagination(req);
        List<Map<String, Object>> list = reportQueryLisMapper.getTestItems(req);
        for(Map<String, Object> map:list){
            if(map.get("YBLX_XZ")!=null&&!map.get("YBLX_XZ").toString().equals("")){
                toCharacterString(map,"YBLX_XZ");
                List<Map<String, Object>> list1 = reportQueryLisMapper.getTestBblx(map);
                map.put("YBLX_XZ",list1);
            }
        }
        return getSuccessPagination(list);
    }

    /**
     * 检验项目明细查询
     *
     * @param req
     */
    @Override
    public String getTestItemsDetails(Map<String, Object> req) throws CommonException {
        isNUll(req,"XMID");
        //分页
        pagination(req);
        List<Map<String, Object>> list = reportQueryLisMapper.getTestItemsDetails(req);
        return getSuccessPagination(list);
    }

    /**
     * 处方开单
     *
     * @param req
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String prescriptionBilling(Map<String, Object> req) throws CommonException {
        //新加写挂号明细
        String sbxhGh = getIdentity("ms", "MS_GHMX").toString();
        String jzhm = "Auto"+getIdentity("ms", "MS_GHMX_AUTOJZHM").toString();
        req.put("seqCode",sbxhGh);
        isNUll(req,asList("seqCode","XMID","patientId"));
        //获取自助开单医生
        String ysdm = clinicPaymentMapper.getXtcs(xtcsMap("ZZKDYS"));

        if (ysdm == null || ysdm.equals("")) {
            throw new CommonException("'机构未设置ZZKDYS对应数据来源的系统参数");
        }
        //获取自助开单科室
        String ksdm =clinicPaymentMapper.getXtcs(xtcsMap("ZZKDKS"));

        if (ksdm == null || ksdm.equals("")) {
            throw new CommonException("'机构未设置ZZKDKS对应数据来源的系统参数");
        }
        Map<String,Object> ghmx=new HashMap<>();
        ghmx.put("sbxh",sbxhGh);
        ghmx.put("jgid","1");
        ghmx.put("brid",req.get("patientId"));
        ghmx.put("brxz","4");
        ghmx.put("ghsj",new Date());
        ghmx.put("ghlb","1");
        ghmx.put("ksdm",ksdm);
        ghmx.put("ysdm",ysdm);
        ghmx.put("jzxh","1");
        ghmx.put("ghcs","0");
        ghmx.put("ghje","0");
        ghmx.put("zlje","0");
        ghmx.put("zjfy","0");
        ghmx.put("blje","0");
        ghmx.put("jzje","0");
        ghmx.put("xjje","0");
        ghmx.put("zpje","0");
        ghmx.put("zhje","0");
        ghmx.put("hbwc","0");
        ghmx.put("qtys","0");
        ghmx.put("zhlb","0");
        ghmx.put("jzjs","0");
        ghmx.put("thbz","0");
        ghmx.put("czgh",ysdm);
        ghmx.put("czpb","0");
        ghmx.put("jzhm",jzhm);
        ghmx.put("mzlb","101");
        ghmx.put("yybz","0");
        ghmx.put("yspb","0");
        ghmx.put("dzsb","0");
        ghmx.put("sffs","0");
        ghmx.put("jzzt","0");
        ghmx.put("sfsj",new Date());
        ghmx.put("zblb","1");
        ghmx.put("bzxx","自助开单");
        clinicPaymentMapper.saveGhmx(ghmx);



        //定义写入数据的list
        List<Map<String,Object>> yj01List=new ArrayList<>();
        List<Map<String,Object>> yj02List=new ArrayList<>();
        List<Map<String,Object>> l_lis_sqdList_All = new ArrayList<>();
        List<Map<String,Object>> l_lis_sqdmxList_All = new ArrayList<>();
        String xmmc="";
        String xmid="";
        String zxks="";
        //1.先写ys_mz_jzls-----------------------------------------------------------------------------------
        //根据seqCode获取jzls需要的数据
        List<Map<String,Object>> brxx=new ArrayList<>();
        brxx=clinicPaymentMapper.brxx(req);
        if(brxx.size()<1){
            throw new CommonException("根据病人id和挂号序号未获取到病人信息");
        }
        Map<String,Object> jzls=new HashMap<>();
        String JZXH = getIdentity("ys", "YS_MZ_JZLS").toString();
        jzls.put("JZXH",JZXH);
        jzls.put("GHXH",req.get("seqCode"));
        jzls.put("BRBH",req.get("patientId"));
        jzls.put("KSDM",brxx.get(0).get("MZKS"));
        jzls.put("YSDM",brxx.get(0).get("YSDM"));
        jzls.put("ZYZD",0);
        jzls.put("JZZT",2);
        jzls.put("JGID",change2LocalHospitalId());
        jzls.put("JZLX",3);
        jzls.put("CZPB",0);
        jzls.put("FZXH",0);
        //2.lis中查询所有的组套项目--------------------------------------------------------------------------------
        List<Map<String,Object>> ztxmList=new ArrayList<>();
        ztxmList=reportQueryLisMapper.getTestItems(req);
        String XMID=req.get("XMID").toString();
        String [] yjArr=XMID.split(",");
        //生成申请单
        String DOCTREQUESTNO=String.valueOf(getMax("DOCTREQUESTNO", 1));



        for(Map<String,Object> ztxm:ztxmList) {
         //3.写ms_yj01------------------------------------------------------------------------------------------------

            Map<String, Object> yj01 = new HashMap<>();
            String yjxh = getIdentity("ms", "MS_YJ01").toString();
            yj01.put("yjxh",yjxh);
            yj01.put("jgid",change2LocalHospitalId());
            yj01.put("mzxh",0);
            yj01.put("brid",req.get("patientId"));
            yj01.put("brxm",brxx.get(0).get("BRXM"));
            yj01.put("kdrq",new Date());
            yj01.put("ksdm",brxx.get(0).get("MZKS"));
            yj01.put("ysdm",brxx.get(0).get("YSDM"));
            yj01.put("zxks",ztxm.get("ZXKS"));
            zxks=ztxm.get("ZXKS").toString();
            yj01.put("tjfl",4);
            yj01.put("zxpb",0);
            yj01.put("zfpb",0);
            yj01.put("cfbz",0);
            yj01.put("sqdmc",ztxm.get("XMMC"));
            yj01.put("jzxh",JZXH);
            yj01.put("djzt",0);
            yj01.put("djly",12);
            yj01.put("sqid",DOCTREQUESTNO);
            yj01.put("tjhm",DOCTREQUESTNO);
            yj01List.add(yj01);
            List<Map<String,Object>> detailList=new ArrayList<>();

            //4.写yj02------------------------------------------------------------------------------------------------
            detailList=reportQueryLisMapper.getTestItemsDetails(ztxm);
            for (Map<String,Object> detail:detailList){
                //获取his项目信息
                List<Map<String,Object>> yjxx=clinicPaymentMapper.yjxx(detail);
                if(yjxx.size()<1){
                    throw new CommonException("his未获取到该费用序号"+detail.get("FYXH"));
                }
                String sbxh = getIdentity("ms", "MS_YJ02").toString();
                Map<String,Object> yj02=new HashMap<>();
                yj02.put("sbxh",sbxh);
                yj02.put("jgid",change2LocalHospitalId());
                yj02.put("yjxh",yjxh);
                yj02.put("ylxh",detail.get("FYXH"));
                yj02.put("xmlx",yjxx.get(0).get("XMLX"));
                yj02.put("yjzx",0);
                yj02.put("fymc",yjxx.get(0).get("FYMC"));
                yj02.put("fydw",yjxx.get(0).get("FYDW"));
                yj02.put("yldj",yjxx.get(0).get("FYDJ"));
                yj02.put("ylsl",detail.get("MXSL"));
                yj02.put("hjje",Integer.valueOf(detail.get("MXSL").toString())*Integer.valueOf(yjxx.get(0).get("FYDJ").toString()));
                yj02.put("fygb",yjxx.get(0).get("FYGB"));
                yj02.put("zfbl",1);
                yj02.put("cxbz",0);
                yj02.put("dzbl",0);
                yj02List.add(yj02);
               //4.写lis申请单明细-------------------------------------------------------------------------------------
                Map<String,Object> jysqmx=new HashMap<>();
                xmid=xmid.equals("")?detail.get("XMID").toString():xmid+","+detail.get("XMID");
                xmmc=xmmc.equals("")?detail.get("HYXMMC").toString():xmmc+","+detail.get("HYXMMC");

                jysqmx.put("doctrequestno",DOCTREQUESTNO);
                jysqmx.put("prehyid",detail.get("XMID"));
                jysqmx.put("ztmc",detail.get("HYXMMC"));
                jysqmx.put("hyid",detail.get("MXID"));
                jysqmx.put("ylxh",detail.get("FYXH"));
                jysqmx.put("ylmc",yjxx.get(0).get("FYMC"));
                jysqmx.put("dj",yjxx.get(0).get("FYDJ"));
                jysqmx.put("sl",detail.get("MXSL"));
                jysqmx.put("feestatus",1);
                if(req.get("SAMPLETYPE")!=null&&!req.get("SAMPLETYPE").toString().equals("")){
                    jysqmx.put("sampletype",req.get("SAMPLETYPE"));
                }else {
                    jysqmx.put("sampletype",ztxm.get("YBLX"));
                }

                jysqmx.put("zxpb",0);
                jysqmx.put("cxbz",0);
                jysqmx.put("mfxm",0);
                jysqmx.put("xmlba",0);
                jysqmx.put("jgid",change2LocalHospitalId());
                jysqmx.put("bz","");
                jysqmx.put("jlxh",yjxh);
                jysqmx.put("yjxh",sbxh);
                l_lis_sqdmxList_All.add(jysqmx);

            }

        }
        //5.写lis的申请单
        Map<String,Object> jysq=new HashMap<>();
        jysq.put("doctrequestno",DOCTREQUESTNO);
        jysq.put("requesttime",new Date());
        jysq.put("requester",brxx.get(0).get("YSDM"));
        jysq.put("sqdstatus","2");
        jysq.put("stayhospitalmode","1");
        jysq.put("brxz",brxx.get(0).get("BRXZ"));
        jysq.put("patientid",brxx.get(0).get("MZHM"));
        jysq.put("patientname",brxx.get(0).get("BRXM"));
        jysq.put("sex",brxx.get(0).get("BRXB"));
        jysq.put("age",brxx.get(0).get("AGE"));
        jysq.put("age_unit","1");
        jysq.put("section",brxx.get(0).get("MZKS"));
        jysq.put("bed_no","");
        jysq.put("requestmode","1");
        jysq.put("diagnostic","");
        jysq.put("examinaim",xmmc);
        jysq.put("examinaimcode",xmid);
        jysq.put("hislb","1");
        jysq.put("jzxh",JZXH);
        jysq.put("brid",req.get("patientId"));
        jysq.put("jgid",change2LocalHospitalId());
        jysq.put("djrq",new Date());
        jysq.put("sfzh",brxx.get(0).get("SFZH"));
        jysq.put("lxdh",brxx.get(0).get("JTDH"));
        jysq.put("fphm","");
        if(req.get("SAMPLETYPE")!=null&&!req.get("SAMPLETYPE").toString().equals("")){
            jysq.put("sampletype",req.get("SAMPLETYPE"));
        }else {
            jysq.put("sampletype","");
        }


        jysq.put("birthday",brxx.get(0).get("CSNY"));
        l_lis_sqdList_All.add(jysq);

       //6.判断试管抽血费针头费----------------------------------------------------------
        //检验是否需要材料计费
        //材料费 （ 抽血费、针头、试管）
        //1、抽血费
        //SELECT CSZ1--费用序号 ,CSZ2 --参数值（是否收取采血费） From V_LIS_XTCS Where  JGID = 1 And CSMC = 'MZSQDCXF'
        Map<String,Object> bloodMap = reportQueryLisMapper.getBloodParam();
        if(bloodMap != null){
            if( bloodMap.get("CSZ2") != null && ("1").equals(bloodMap.get("CSZ2").toString())){
                String fyxh = bloodMap.get("CSZ1").toString();
                if(bloodMap.get("CSZ1") != null && !"".equals(bloodMap.get("CSZ1").toString())
                        && Integer.parseInt(bloodMap.get("CSZ1").toString()) > 0){
//								--2、查询抽血(判断是否有数据，有数据就写ms_yj01,ms_yj02)
                    Map<String, Object> temp = new HashMap<String, Object>();
                    temp.put("hyid", xmid);//xmid XMID
                    //根据hyid查询出要计费的数据
                    List<Map<String, Object>> bloodList = reportQueryLisMapper.getLisByBlood(temp);
                    for(Map<String, Object> mp : bloodList){
                        //mp->HYID,YBLX,XMLBA,BQLX,FHLX,XMLBB,SRM3,BLOODTYPE,ZXKS
                        //插入HIS
                        temp.put("fyxh", fyxh);
                        //查询出nvl(xmlx,11) xmlx, fydj, fygb
                        Map<String,Object> fygbMap = clinicPaymentMapper.getLISParam(temp);

                        //获取病人性质
                        temp.put("brid", req.get("patientId"));
                       // 插入YJ01

                        Map<String, Object> yj01 = new HashMap<String, Object>();

                        String yjxh = getIdentity("ms", "MS_YJ01").toString();
                        yj01.put("yjxh",yjxh);
                        yj01.put("jgid",change2LocalHospitalId());
                        yj01.put("mzxh",0);
                        yj01.put("brid",req.get("patientId"));
                        yj01.put("brxm",brxx.get(0).get("BRXM"));
                        yj01.put("kdrq",new Date());
                        yj01.put("ksdm",brxx.get(0).get("MZKS"));
                        yj01.put("ysdm",brxx.get(0).get("YSDM"));
                        yj01.put("zxks",zxks);
                        yj01.put("tjfl","4");
                        yj01.put("zxpb",0);
                        yj01.put("zfpb",0);
                        yj01.put("cfbz",0);
                        yj01.put("sqdmc","");
                        yj01.put("jzxh",JZXH);
                        yj01.put("djzt",0);
                        yj01.put("djly",12);
                        yj01.put("sqid",DOCTREQUESTNO);
                        yj01.put("tjhm",DOCTREQUESTNO);
                        yj01List.add(yj01);
                        //新增ms_yj02
//
                        String sbxh = getIdentity("ms", "MS_YJ02").toString();
                        Map<String,Object> yj02=new HashMap<>();
                        yj02.put("sbxh",sbxh);
                        yj02.put("jgid",change2LocalHospitalId());
                        yj02.put("yjxh",yjxh);
                        yj02.put("ylxh",fyxh);
                        yj02.put("xmlx",fygbMap.get("XMLX"));
                        yj02.put("yjzx",0);

                        yj02.put("yldj",fygbMap.get("FYDJ"));
                        yj02.put("fymc",fygbMap.get("FYMC"));
                        yj02.put("ylsl",1);
                        yj02.put("hjje",Double.parseDouble(fygbMap.get("FYDJ").toString()) * 1);
                        yj02.put("fygb",fygbMap.get("FYGB"));
                        yj02.put("zfbl",1);
                        yj02.put("cxbz",0);
                        yj02.put("dzbl",0);
                        yj02List.add(yj02);
                        break;
                    }
                }
            }
        }

        //针头
        Map<String,Object> needleMap = reportQueryLisMapper.getNeedleParam();
        if(needleMap != null){
            if( needleMap.get("CSZ2") != null && ("1").equals(needleMap.get("CSZ2").toString())){
                String fyxh = needleMap.get("CSZ1").toString();
                if(needleMap.get("CSZ1") != null && !"".equals(needleMap.get("CSZ1").toString())
                        && Integer.parseInt(needleMap.get("CSZ1").toString()) > 0){
                    Map<String, Object> temp = new HashMap<String, Object>();
                    temp.put("hyid", xmid);//xmidstr是放的ECIS_JYSQMX XMID
                    //根据hyid查询出要计费的数据
                    List<Map<String, Object>> needleList = reportQueryLisMapper.getLisByNeedle(temp);
                    for(Map<String, Object> mp : needleList){
                        temp.put("fyxh", fyxh);
                        //查询出nvl(xmlx,11) xmlx, fydj, fygb
                        Map<String,Object> fygbMap = clinicPaymentMapper.getLISParam(temp);
                        //写yj01
                        Map<String, Object> yj01 = new HashMap<String, Object>();
                        String yjxh = getIdentity("ms", "MS_YJ01").toString();
                        yj01.put("yjxh",yjxh);
                        yj01.put("jgid",change2LocalHospitalId());
                        yj01.put("mzxh",0);
                        yj01.put("brid",req.get("patientId"));
                        yj01.put("brxm",brxx.get(0).get("BRXM"));
                        yj01.put("kdrq",new Date());
                        yj01.put("ksdm",brxx.get(0).get("MZKS"));
                        yj01.put("ysdm",brxx.get(0).get("YSDM"));
                        yj01.put("zxks",zxks);
                        yj01.put("tjfl","4");
                        yj01.put("zxpb",0);
                        yj01.put("zfpb",0);
                        yj01.put("cfbz",0);
                        yj01.put("sqdmc","");
                        yj01.put("jzxh",JZXH);
                        yj01.put("djzt",0);
                        yj01.put("djly",12);
                        yj01.put("sqid",DOCTREQUESTNO);
                        yj01.put("tjhm",DOCTREQUESTNO);
                        yj01List.add(yj01);
                        //yj02
                        String sbxh = getIdentity("ms", "MS_YJ02").toString();
                        Map<String,Object> yj02=new HashMap<>();
                        yj02.put("sbxh",sbxh);
                        yj02.put("jgid",change2LocalHospitalId());
                        yj02.put("yjxh",yjxh);
                        yj02.put("ylxh",fyxh);
                        yj02.put("xmlx",fygbMap.get("XMLX"));
                        yj02.put("yjzx",0);
                        yj02.put("fymc",fygbMap.get("FYMC"));
                        yj02.put("yldj",fygbMap.get("FYDJ"));
                        yj02.put("ylsl",1);
                        yj02.put("hjje",Double.parseDouble(fygbMap.get("FYDJ").toString()) * 1);
                        yj02.put("fygb",fygbMap.get("FYGB"));
                        yj02.put("zfbl",1);
                        yj02.put("cxbz",0);
                        yj02.put("dzbl",0);
                        yj02List.add(yj02);
                        break;
                    }
                }
            }
        }
        //试管
        Map<String,Object> TubeMap = reportQueryLisMapper.getTubeParam();
        if(TubeMap != null){
            if( TubeMap.get("CSZ2") != null && ("1").equals(TubeMap.get("CSZ2").toString())){
                long ll_fyxh=0;
                String fyxh = TubeMap.get("CSZ1").toString();
                ll_fyxh = Long.valueOf(fyxh);
                if(TubeMap.get("CSZ1") != null && !"".equals(TubeMap.get("CSZ1").toString())
                        && ll_fyxh > 0){
                    Map<String, Object> temp = new HashMap<String, Object>();
                    temp.put("hyid", xmid);//xmidstr是放的ECIS_JYSQMX XMID
                    //根据hyid查询出要计费的数据
                    List<Map<String, Object>> tubeList = reportQueryLisMapper.getLisByTube(temp);

                    int ll_fysls=0;
                    int ll_dmlb = 0;
                    int li_flag = 0;
                    int li_i = 0;
                    long ll_new_sgid=0;
                    long ll_zxks = 0;
                    long ll_sgid;
                    double ld_hjje=0.00,ld_yldj=0.00;

                    String ls_zxks;
                    for(int i=0;i<tubeList.size();i++) {
                        li_i = i + 1;
                        //取试管ID
                        ll_sgid = Long.valueOf(tubeList.get(i).get("SRM3").toString());
                        //取执行科室
                        if (li_i < tubeList.size()) {
                            ll_new_sgid = Long.valueOf(tubeList.get(li_i).get("SRM3").toString());
                            if (ll_new_sgid != ll_sgid) {
                                li_flag = 1;
                            } else {
                                li_flag = 0;
                            }
                        } else if (i + 1 == tubeList.size()) {
                            li_flag = 1;
                        }
                        if (ll_sgid != 0) {
                            ll_dmlb = 1;
                            ll_fysls = ll_fysls + 1;
                            ll_fyxh = ll_sgid;
                        } else {
                            ll_dmlb = 0;
                            ll_fysls = 0;
                        }

                        if (ll_dmlb > 0 && li_flag == 1) {
                            temp.put("fyxh",ll_fyxh);
                            //查询出nvl(xmlx,11) xmlx, fydj, fygb
                            Map<String, Object> fygbMap = clinicPaymentMapper.getLISParam(temp);
                            //写yj01
                            Map<String, Object> yj01 = new HashMap<String, Object>();
                            String yjxh = getIdentity("ms", "MS_YJ01").toString();
                            yj01.put("yjxh",yjxh);
                            yj01.put("jgid",change2LocalHospitalId());
                            yj01.put("mzxh",0);
                            yj01.put("brid",req.get("patientId"));
                            yj01.put("brxm",brxx.get(0).get("BRXM"));
                            yj01.put("kdrq",new Date());
                            yj01.put("ksdm",brxx.get(0).get("MZKS"));
                            yj01.put("ysdm",brxx.get(0).get("YSDM"));
                            yj01.put("zxks",zxks);
                            yj01.put("tjfl","4");
                            yj01.put("zxpb",0);
                            yj01.put("zfpb",0);
                            yj01.put("cfbz",0);
                            yj01.put("sqdmc","");
                            yj01.put("jzxh",JZXH);
                            yj01.put("djzt",0);
                            yj01.put("djly",12);
                            yj01.put("sqid",DOCTREQUESTNO);
                            yj01.put("tjhm",DOCTREQUESTNO);
                            yj01List.add(yj01);
                            //yj02
                            String sbxh = getIdentity("ms", "MS_YJ02").toString();
                            Map<String,Object> yj02=new HashMap<>();
                            yj02.put("sbxh",sbxh);
                            yj02.put("jgid",change2LocalHospitalId());
                            yj02.put("yjxh",yjxh);
                            yj02.put("ylxh",fyxh);
                            yj02.put("xmlx",fygbMap.get("XMLX"));
                            yj02.put("yjzx",0);
                            yj02.put("fymc",fygbMap.get("FYMC"));
                            yj02.put("yldj",fygbMap.get("FYDJ"));
                            yj02.put("ylsl",1);
                            yj02.put("hjje",Double.parseDouble(fygbMap.get("FYDJ").toString()) * 1);
                            yj02.put("fygb",fygbMap.get("FYGB"));
                            yj02.put("zfbl",1);
                            yj02.put("cxbz",0);
                            yj02.put("dzbl",0);
                            yj02List.add(yj02);
                            ll_dmlb = 0;
                            ll_fysls = 0;
                        }
                    }

                }
            }
        }





        String visitNo="";
        clinicPaymentMapper.saveHistory(jzls);
        for(Map<String,Object> map:yj01List){
            clinicPaymentMapper.saveMsyj01(map);
            visitNo=visitNo==""?"cz"+map.get("yjxh"):visitNo+",cz"+map.get("yjxh");

        }
        double totalAmount=0;
        List<Map<String,Object>> chargingProjects=new ArrayList<>();
        for(Map<String,Object> map:yj02List){
            clinicPaymentMapper.saveMsyj02(map);
            Map<String,Object> chargingProject=new HashMap<>();
            chargingProject.put("projectName",map.get("fymc"));
            chargingProject.put("projectAmount",map.get("hjje"));
            totalAmount=totalAmount+Double.valueOf(map.get("hjje").toString());
            chargingProjects.add(chargingProject);
        }
        for(Map<String,Object> map:l_lis_sqdList_All){
            reportQueryLisMapper.saveLisRequest(map);
        }
        for(Map<String,Object> map:l_lis_sqdmxList_All){
            reportQueryLisMapper.saveLisRequestDetail(map);
        }
        Map<String,Object> map=new HashMap<>();
        map.put("departmentCode",brxx.get(0).get("MZKS"));
        map.put("departmentName",brxx.get(0).get("KSMC"));
        map.put("doctorCode",brxx.get(0).get("YSDM"));
        map.put("doctorName",brxx.get(0).get("YGXM"));
        map.put("visitingId",JZXH);
        map.put("visitNo",visitNo);
        map.put("totalAmount",totalAmount);
        map.put("chargingProjects",chargingProjects);
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        map.put("jzsj", sdf.format(new Date()));


        return getSuccess(map);
    }


    /**
     * 传入需要获取的表名及需要的主键数量
     * */
    public  long getMax(String bmc,int count){

        boolean upateOk = true;
        if(count <= 0) return 0;
        bmc = bmc.toUpperCase();
        String sysName =bmc;


        Map<String,Object> map = new HashMap<String,Object>();
        map.put("sysName", sysName);
        map.put("bmc",bmc);
        //获取当前的主键
        Long max = reportQueryLisMapper.getMax(map);
        if(max == null)max = 0l;

        long maxReturn = 0;
        int ok = 0;
        //主键不存在则新增

        map.put("bmc",bmc);
        if(max <= 0){
          throw new CommonException("未获取到lis的"+bmc+"主键");
        }else{//更新主键
            try {
                map.put("dqz",max + count);
                reportQueryLisMapper.updateMax(map);
                ok = 1;
            } catch (Exception e) {
                ok = 0;
            }
            if( ok == 1) {
                maxReturn = max + 1;
            }else{
                upateOk = false;
            }
        }
        if(!upateOk){//连续提交2次
            for(int i = 0;i < 2;i++){
                max = reportQueryLisMapper.getMax(map);
                if(max == null)max = 0l;
                try {
                    map.put("dqz",max + count);
                    reportQueryLisMapper.updateMax(map);
                    ok = 1;
                } catch (Exception e) {
                    ok = 0;
                }
                if( ok == 1){
                    maxReturn = max + 1;
                    break;
                }
            }
        }
        return maxReturn;
    }
}