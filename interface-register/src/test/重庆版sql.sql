-- Create table
create table ONLINE_PAYMENT_METHOD
(
  fkfs       NUMBER(18) not null,
  dsffkfs    VARCHAR2(40) not null,
  zfbbz      NUMBER(2) default 0 not null,
  wxbz       NUMBER(2) default 0 not null,
  qtfkbz     NUMBER(2) default 0 not null,
  zymz       NUMBER(2) default 1 not null,
  trade_type NUMBER(2) not null,
  pay_from   NUMBER(2) not null,
  pt_type    NUMBER(2) not null,
  gh_type    NUMBER(2),
  bz         VARCHAR2(80)
);
-- Add comments to the columns
comment on column ONLINE_PAYMENT_METHOD.fkfs
  is '系统付款方式';
comment on column ONLINE_PAYMENT_METHOD.dsffkfs
  is '第三方付款方式';
comment on column ONLINE_PAYMENT_METHOD.zfbbz
  is '支付宝标志';
comment on column ONLINE_PAYMENT_METHOD.wxbz
  is '微信标志';
comment on column ONLINE_PAYMENT_METHOD.qtfkbz
  is '其他支付标志';
comment on column ONLINE_PAYMENT_METHOD.zymz
  is '1门诊2住院';
comment on column ONLINE_PAYMENT_METHOD.trade_type
  is '1 、二维码付；2、扫码付；3、公众号支付（限微信）（暂时只在1.2.5版本支持金堂工行公众号支付）4、APP支付';
comment on column ONLINE_PAYMENT_METHOD.pay_from
  is '窗口1，诊间2，自助机3　公众号　4';
comment on column ONLINE_PAYMENT_METHOD.pt_type
  is '１、区域版平台，２、单机版平台；其它平台请勿使用同样的值';
comment on column ONLINE_PAYMENT_METHOD.bz
  is '备注';
-- Create/Recreate primary, unique and foreign key constraints
alter table ONLINE_PAYMENT_METHOD
  add constraint ONLINE_PAYMENT_METHOD_KEY primary key (DSFFKFS, ZYMZ);
alter table ONLINE_PAYMENT_METHOD
  add constraint ONLINE_PAYMENT_METHOD_KEY1 unique (FKFS);



-- Create table
create table MANUFACTURERTYPE
(
  csdm VARCHAR2(40) not null,
  hylx NUMBER(8) not null,
  csmc VARCHAR2(40) not null
)
;
-- Add comments to the columns
comment on column MANUFACTURERTYPE.csdm
  is '厂商代码';
comment on column MANUFACTURERTYPE.hylx
  is '第三方厂商号源类型';
comment on column MANUFACTURERTYPE.csmc
  is '第三方厂商名称';


CREATE OR REPLACE VIEW V_LISTDEPARTMENT AS
SELECT distinct (select csz from gy_xtcs where csmc='JGDM' )as "hospitalId", --医院机构
       (select bz from gy_xtcs where csmc='JGDM') as "hospitalName",--机构名称
       t.ksdm         AS "depCode", --科室码
       t.KSMC         AS "depName", --科室名称
       t.PYDM         AS "depSpell", --拼音代码
       ''         AS "depParentcode", --上级科室
       ''         AS "depMoneycode", --核算科室
      a.EDCW         AS "depBedqty", --科室床位
       t.addr         AS "depAddress", --科室地址
       null         AS "depTel", --科室电话
       null         AS "depLeader", --科室主任
       a.MZSY         AS "depOutpatientclinic", --门诊使用
       a.ZYSY         AS "depHospitaldept", --住院使用
       a.YJSY         AS "depMedicallab", --医技使用
       a.Plsx AS "deplistNo", --科室顺序号
       '' as "hospitalDistrictId",
       '' as "hospitalDistrictName",
       (select count(1) from ghyy_hyxx b where b.ghks=t.ksdm  and b.gzrq>=trunc(sysdate) and b.hylx = '1' and b.tgbz = 0) as "residualSource" --剩余号源
  FROM ms_mzzs t
  inner join ms_yspb pb on  t.ksdm=pb.ksdm
  left join gy_ksdm a on a.ksdm =t.mzks
  where  pb.gzrq>sysdate-30;


create or replace view v_listdoctor as
select distinct (select csz from gy_xtcs where csmc='JGDM') as "hospitalId",
       (select bz from gy_xtcs where csmc='JGDM') as "hospitalName",
                YGDM.ygdm AS "doctorCode", --医生内码
                YGDM.ygxm AS "doctorName", --医生姓名
                YGDM.PYDM AS "doctorSpell", --医生拼音码
                to_char(YGDM.CSNY, 'yyyy-mm-dd') AS "brithday", --医生出生年月
                mzzs.ksdm AS "depCode", --归属部门
                mzzs.ksmc as "depName", --科室名称
                 DMZD.DMMC AS "doctTitle", --医生级别
                '' AS "idCard", --医生身份证
                '' AS "synopsis", --医生简介(医生擅长)
                YGDM.SJHM as "phoneNo", --联系电话
                YGDM.YGXB as "sexCode", --医生性别
                YGDM.YXDZ as "email", --邮箱
                ''as "education", --学历
                '' as "address", --联系地址
                '' as "joinInWork", --参加工作时间
                YGDM.Yszjhm as "certificateNum", --执业证书编号
                 DMZD.DMMC as "jobPost", --行政职务（名称见字典）
                '' as "educationbackground", --学位
               '' as "majorName", --所学专业
               '' as "majorqualify", --专业技术资格
               '' as "majorjob", --专业技术职务
               '' as "majortype", --专业类别
               '' as "operationtype", --医师执业类别
                '' as "operationscope", --医师执业范围
               ghks.zjmz as "isExpert", --专家判别 0 否 1是
               ghks.zjf as "expertcost" --专家费
  FROM GY_YGDM YGDM
  LEFT OUTER JOIN GY_DMZD DMZD
    ON YGDM.YGJB = DMZD.DMSB  AND DMLB = 27
  INNER JOIN ms_yspb yspb on  YGDM.YGDM=yspb.ysdm
  INNER JOIN ms_mzzs mzzs on  mzzs.KSDM=yspb.KSDM
  INNER JOIN ms_ghks ghks on  ghks.ksdm=yspb.ghlb
   where yspb.gzrq>sysdate-30
;




create or replace view v_listorg as
select (select csz from gy_xtcs where csmc='JGDM') as "hospitalId", --医疗机构代码
       (select bz from gy_xtcs where csmc='JGDM') "hospitalName", --医疗机构名称
       ''      "hospitalType", --医疗机构类型
       ''         "parentOrgCode", --上级医疗机构代码
       ''    "areaCode" --所在行政区域代码
  from  dual
;


CREATE OR REPLACE VIEW V_PATIENTISMATCH AS
SELECT t.brid AS "patientId", -- true  String  ""  患者内码
          t.mzhm AS "outpatientNo", -- true  String  ""  门诊号码
          t.brxb AS "sexCode", -- true  String  ""  性别字典
          to_char(t.csny, 'yyyy-mm-dd') AS "birthday", --  true  String  ""  患者出生年月 yyyy-mm-dd
          t.hkdz_qtdz AS "address", -- false String  ""  户口地址
          t.mzdm AS "national", --  false String  ""  民族代码
          t.sfzh AS "idCard", --  true  String  ""  身份证号码
          t.ybkh AS "medicareId", --  false String  ""  医保卡号
          t.jtdh AS "phoneNo", -- false String  ""  联系电话
          JHRM AS "ruardian", --  false String  ""  监护人
          JHDH AS "ruarduanTel", -- false String  ""  监护人联系电话
          t.brxz AS "patientNature", -- false String  ""  患者性质字典
          a.xzmc AS "defaultrrxzName", -- false String  ""  患者性质名称
          t.JZKH AS "patcardNo", -- false String  ""  就诊一卡通号
          t.brxm AS "patientName", -- true  String  ""  患者姓名
          t.healthid as EID,
          ''     as "empiid"--基本信息ID
     FROM ms_brda t left join gy_brxz a on a.brxz =t.brxz order by t.jdsj desc;




create or replace view v_source_info as
select to_char(t.gzrq, 'yyyy-MM-dd') || '|' || t.ghks || '|' || t.zblb || '|' ||
       t.ysdm as "workId", --排班ID
        t.zblb  as "timeInterval", --班别(见字典附件，班别)
       jlxh as "sourceId", --号源ID
       t.mzks as MZKS,
        to_char(t.qssj,'yyyy-mm-dd hh24:mi:ss') as "qssj",
         to_char(t.zzsj,'yyyy-mm-dd hh24:mi:ss') as "zzsj",
       to_char(t.qssj, 'HH24:MI')||'-' ||to_char(t.zzsj, 'HH24:MI') as "appointmentDate", --开始时间
      b.csdm as "hylx", --
       case when nvl(t.tgbz,1) = 1 then 1  when nvl(t.ghxh,0)>0 then 1 else  nvl(t.hyzt,0) end as "sourceStatus", --号源状态  0 可用 1已预约
       t.sxh as "serialNumber" --顺序号
  from ghyy_hyxx t,manufacturerType b
 where t.hylx=b.hylx and  TRUNC(t.gzrq) >= TRUNC(sysdate);


CREATE OR REPLACE VIEW V_ADDWORKPBINFO AS
select distinct (select csz from gy_xtcs where csmc='JGDM') as "hospitalId",
d.mzlb,
 to_char(a.gzrq,'yyyy-MM-dd')|| ','||a.ksdm ||  ','|| a.ysdm ||','|| a.zblb ||','||a.jgid     as "his_djh",
mzzs.addr as "addr",
       (select bz from gy_xtcs where csmc='JGDM') as "hospitalName",
       to_char(a.gzrq,'yyyy-MM-dd')|| '|'||a.ksdm ||  '|'|| a.zblb ||'|'|| a.ysdm as "workId",--排班ID
       to_char(a.gzrq,'yyyy-MM-dd') as "registerDate", --挂号排班日期yyyy-mm-dd
        a.zblb   as "timeInterval", --班别(见字典附件，班别)
       case when a.zblb =1 then '上午' when a.zblb =2 then '下午'  when a.zblb =3 then '全天'  when a.zblb =4 then '晚上'end    as "timeIntervalName", --班别
     --  a.kssj as starttime,
     --  a.jssj as endtime,
       a.ysdm as "doctorCode", --排班医生代码
       c.ygxm as "doctorName",--医生名称
    --   case   when c.gender = '1' then  1   else   2  end as doctorgender,
    ( SELECT GY_DMZD.DMMC
    FROM GY_DMZD
   WHERE GY_DMZD.DMLB = 27 and
         GY_DMZD.DMSB <> 0 and GY_DMZD.DMSB  =c.ygjb) as "doctorrank",
    --   case  when c.isexpert = 1 then   5  else 1 end as workrankid,
       d.ksmc as "clinicType", --排班类型
       d.ksdm as "depCode", --排班科室
       mzzs.ksmc as "depName", --排班科室名称
       d.ghf as "registerMoney", --排班挂号参考价（看到的12块，可能实际计算中只有9块，如同一科室同一医生同一班别，本院职工，或者70岁以上病人、复诊病人免挂号费这种情况 ）
       d.zlf as "consultationFee",--诊疗费
       d.ghf+d.zlf "totalRegisteredCost",--挂号总费用
       d.zjf as "expertCost",--专家费 如果不是专家门诊，则不能收取专家费
       d.ghf+d.zlf+d.zjf+d.ysfwf as "totalMoney",--总挂号费用

       d.zjmz as "isExpert",--是否专家门诊 1是 0否
      (select count(1) from v_source_info t where "hylx"=e.csdm and "sourceStatus"=0 and "workId"=to_char(a.gzrq,'yyyy-MM-dd')|| '|'||a.ksdm ||  '|'|| a.zblb ||'|'|| a.ysdm)
             as "registerLimit", --可预约人数
      e.csdm as "dataSources",
    --   '' as reason,
    --   '' as suggestworkid,
    --   a.yyxe-a.yyrs as remain ,
    --   '1' as flag ,
    --   '' as tag1 ,
    --   '' as tag2 ,
    --   '' as ordernumbers ,
    --   '' as originate,
       '' as "idCard", --排班医生证件号
   --    case when TRUNC(a.gzrq)=TRUNC(sysdate)  then a.ygrs when  nvl(e.yxyy,0)=1 then (select count(*) from v_source_info t where "sourceStatus"=1 and "workId"=to_char(a.gzrq,'yyyy-MM-dd')|| '|'||a.ksdm ||  '|'|| a.zblb ||'|'|| a.ysdm) else a.yyrs  end    as "registerHad", --排班已挂人数
                  (select count(1) from v_source_info t where "hylx"=e.csdm and "sourceStatus"=1 and "workId"=to_char(a.gzrq,'yyyy-MM-dd')|| '|'||a.ksdm ||  '|'|| a.zblb ||'|'|| a.ysdm)
        as "registerHad", --排班已挂人数

       a.tgbz as "tegisterStop", --排班是否停诊 1停诊
       '1'  "isDayparting",--是否分时段排班
       a.kssj as "stateDate",--分时段开始时间
       a.jssj as "endDate"--分时段结束时间
  from  manufacturerType e,ms_yspb a
  left join gy_ygdm c
    on a.ysdm = c.ygdm
  left join ms_ghks d
    on a.ghlb = d.ksdm
  left join ms_mzzs mzzs on a.ksdm=mzzs.ksdm
    where  TRUNC(a.gzrq)>=TRUNC(sysdate) --and c.logoff =0

;
create or replace view v_listhospitalizationrecord as
select a.brxm as "patientName", --  姓名
        a.brxb as "sexCode",  --性别
       (select nvl(sum(zy_tbkk.jkje),0) from zy_tbkk where zy_tbkk.zyh=a.zyh and zy_tbkk.zfpb=0) as "advancePayment", --预交款
       (select nvl(sum(zy_fymx.zjje),0) from zy_fymx where zy_fymx.zyh=a.zyh ) as "totalAmount", --费用总金额
       a.zyh as "hospitalizNo", --  住院号
       a.BRID as "patientId", -- 病人ID
       a.SFZH as "idCard", --身份证号
       a.zyhm as "hospitalizCode", --  住院号码
       a.cypb AS "leaveHospital", -- 出院判别
       (select csz from gy_xtcs where csmc='JGDM') as "hospitalId",--就诊机构ID
        (select bz from gy_xtcs where csmc='JGDM') as "hospitalName", --就诊机构名称
       (select t.KSMC from gy_ksdm t where to_char(t.KSDM) = to_char(a.brks)) as "departmentName", --  住院科室
       a.brch as "bedNumber", --  病床号
       (select t.ygxm from gy_ygdm t where to_char(t.ygdm) = to_char(a.zyys)) as "residentDoctor", -- 住院医生
       (select max(t.mszd) from ys_zy_jbzd t where t.jzhm=a.zyh and t.zdlb='入院诊断' and t.jbzh=1) AS "admissionDiagnosis", --入院诊断
       a.jzkh as "patcardNo",
       b.mzhm as "outpatientNo",

       to_char(a.ryrq, 'yyyy-mm-dd') as "inDate", --  入院日期
       to_char(a.cyrq, 'yyyy-mm-dd') as "outDate" --  出院日期
  from zy_brry a,ms_brda b where a.brid=b.brid
 order by a.ryrq
;


create or replace view v_listhospitalizationpayment as
select a.brxm as "patientName", --  姓名
       a.zyh as "hospitalizNo", --  住院号
       a.zyhm as "hospitalizCode", --  住院号码
       (select t.KSMC from gy_ksdm t where to_char(t.KSDM) = to_char(a.brks)) as "departmentName", --  住院科室
       a.brch as "bedNumber", --  病床号
       to_char(a.ryrq, 'yyyy-mm-dd') as "inDate", --  入院日期
       to_char(a.cyrq, 'yyyy-mm-dd') as "outDate", --  出院日期
       b.fymc as "costName", --  费用名称
       b.fysl as "costQuantity", --  数量
       b.fydj as "costUnitPrice", -- 单价
       b.zjje as "total", --总额
       to_char(b.fyrq, 'yyyy-mm-dd') "costDate" --费用日期(yyyy-MM-dd)
  from zy_brry a, zy_fymx b
 where a.zyh = b.zyh
;
create or replace view v_listpaymenthistory as
select a.brxm as "patientName", --  姓名
       a.zyh as "hospitalizNo", --  住院号
       a.zyhm as "hospitalizCode", --  住院号码
       (select t.KSMC from gy_ksdm t where to_char(t.KSDM) = to_char(a.brks)) as "departmentName", --  住院科室
       a.brch as "bedNumber", --  病床号
       to_char(a.ryrq, 'yyyy-mm-dd') as "inDate", --  入院日期
       to_char(a.cyrq, 'yyyy-mm-dd') as "outDate", --  出院日期
       b.jkje as "payAmount", --  付款金额
       (select t.fkmc from gy_fkfs t where t.fkfs = b.jkfs) as "payMode", --   付款方式
       b.tran_id "billNo",--	VARCHAR(20)	交款单号
       to_char(b.jkrq, 'yyyy-mm-dd') as "payedTime" --   付款时间
  from zy_brry a, zy_tbkk b
 where a.zyh = b.zyh and b.zfpb=0
;
create or replace view v_getmedicalrecords as
select ls.JZXH as "visitingId",--就诊号码
       ls.GHXH as "registerId",--挂号号码
       (select csz from gy_xtcs where csmc='JGDM') as "hospitalId",--就诊机构ID
       (select bz from gy_xtcs where csmc='JGDM') as "hospitalName", --就诊机构名称

       (select  listagg(t.mszd,',') WITHIN GROUP(ORDER BY t.mszd) from ys_mz_jbzd t where t.jzxh = ls.jzxh and t.zfpb = 0 ) as "diagnosisName",--诊断名称
           (select nvl(sum(t2.hjje),0)
                       from ms_cf01 t1, ms_cf02 t2
                      where t1.cfsb = t2.cfsb
                         and t1.jzxh=ls.jzxh
                          and t1.fphm is not   null)
                     +
                     (select nvl(sum(t2.hjje),0)
                       from ms_yj01 t1, ms_yj02 t2
                      where t1.yjxh = t2.yjxh
                       and t1.fphm is not   null
                        and t1.jzxh=ls.jzxh) as "paidAmount", --待付金额
                        '' cf,
                        '' cz,
       0 as "pendingPaid", --是否有待支付的订单 0没有，1有
       ls.BRBH as "patientId",--病人ID
       da.BRXM as "patientName",--病人姓名
       to_char(da.CSNY,'yyyy-mm-dd') as "birthday",--出生年月
       da.SFZH as "idCard",--身份证号
       da.BRXB as "sex",--病人性别
       to_char(ls.KSSJ,'yyyy-mm-dd hh24:mi:ss') as "startDate",--就诊开始时间
       to_char(ls.JSSJ,'yyyy-mm-dd hh24:mi:ss') as "endDate",--就诊结束时间
        (select t.ksmc from gy_ksdm t where t.ksdm=ls.ksdm) as "depName" , -- 科室名称
       ls.KSDM as "depCode",--就诊科室
       (select t.ygxm from gy_ygdm t where t.ygdm = ls.ysdm) as "doctorName" , -- 医生名字
       ls.YSDM as "doctorCode" --就诊医生
from ys_mz_jzls ls
         inner join ms_brda da
                    on da.brid = ls.brbh
union all
select ls.JZXH as "visitingId",--就诊号码
       ls.GHXH as "registerId",--挂号号码
       (select csz from gy_xtcs where csmc='JGDM') as "hospitalId",--就诊机构ID
       (select bz from gy_xtcs where csmc='JGDM') as "hospitalName", --就诊机构名称

       (select  listagg(t.mszd,',') WITHIN GROUP(ORDER BY t.mszd) from ys_mz_jbzd t where t.jzxh = ls.jzxh and t.zfpb = 0 ) as "diagnosisName",--诊断名称
              0 "paidAmount", --待付金额
        (select listagg(t1.cfsb,',') WITHIN GROUP(ORDER BY t1.cfsb)
                       from ms_cf01 t1
                      where  t1.jzxh=ls.jzxh
                          and t1.fphm is    null  and t1.zfpb=0) as cf,

                     ( select listagg(t1.yjxh,',') WITHIN GROUP(ORDER BY t1.yjxh)
                       from ms_yj01 t1
                      where t1.fphm is    null
                        and t1.jzxh=ls.jzxh and t1.zfpb=0)  as cz,

       1 as "pendingPaid", --是否有待支付的订单 0没有，1有
       ls.BRBH as "patientId",--病人ID
       da.BRXM as "patientName",--病人姓名
       to_char(da.CSNY,'yyyy-mm-dd') as "birthday",--出生年月
       da.SFZH as "idCard",--身份证号
       da.BRXB as "sex",--病人性别
       to_char(ls.KSSJ,'yyyy-mm-dd hh24:mi:ss') as "startDate",--就诊开始时间
       to_char(ls.JSSJ,'yyyy-mm-dd hh24:mi:ss') as "endDate",--就诊结束时间
        (select t.ksmc from gy_ksdm t where t.ksdm=ls.ksdm) as "depName" , -- 科室名称
       ls.KSDM as "depCode",--就诊科室
       (select t.ygxm from gy_ygdm t where t.ygdm = ls.ysdm) as "doctorName" , -- 医生名字
       ls.YSDM as "doctorCode" --就诊医生
from ys_mz_jzls ls
         inner join ms_brda da
                    on da.brid = ls.brbh
    where  (ls.kssj>sysdate-(select t.csz from gy_xtcs t where csmc='CFXQ' ) and ls.jzzt>1) or (ls.jzlx   in (4,5,6) and ls.kssj>sysdate-10)
;
create or replace view v_getprescriptionpayment as
select  (select csz from gy_xtcs where csmc='JGDM') as "hospitalId", /*医疗机构代码  YLJGDM*/
       a.jzxh as  "visitingId", /*就诊号码*/
       b.sfzh as "idCard", --身份证号
       b.brxm as "patientName" ,--患者姓名
       b.brid as "patientId",
       'cf'||a.cfsb as "prescriptionNumber", /*处方号码*/
       (case a.cflx when 1 then '西药' when 2 then '中药' else '草药' end )as "prescriptionType", /*处方类型*/
       a.ksdm as "depCode", /*开方科室代码*/
       f.ksmc as "depName", /*开方科室名称*/
         'yf'||a.yfsb as "examDepCode",--执行科室代码
        (select t.yfmc from yf_yflb t where t.yfsb=a.yfsb) as "examDepName",--执行科室
       a.ysdm as "doctorCode", /*开方医生编号*/
       g.YGXM as "doctorName", /*开方医生姓名*/
       to_char(a.kfrq,'yyyy-mm-dd hh24:mi:ss') as "prescriptionDate", /*开方时间 （yyyy-MM-dd HH：MM：SS）*/
       nvl((select sum(nvl(hjje,0)) from ms_cf02 mx where mx.cfsb=a.cfsb),0) as "paymentAmount",/*处方金额 单位：元*/
       1 as "type" /*项目类型 1处方，2处置*/
  from ms_cf01 a
  left join ys_mz_jzls jz on jz.jzxh = a.jzxh
  left join MS_BRDA b on b.BRID = a.brid
  left join gy_ygdm g  on g.ygdm = a.ysdm
  left join gy_ksdm f on f.ksdm = a.ksdm
  where a.zfpb=0 and a.kfrq>sysdate-(select t.csz from gy_xtcs t where csmc='CFXQ' )  and   (jz.jzzt>1 or jz.jzlx   in (4,5,6))  and a.fphm is null/*作废处方不查询*/
union all

 --获取处置支付列表
select (select csz from gy_xtcs where csmc='JGDM') as "hospitalId", /*医疗机构代码  YLJGDM*/
       a.jzxh as  "visitingId", /*就诊号码*/
       b.sfzh as "idCard", --身份证号
       b.brxm as "patientName" ,--患者姓名
       b.brid as "patientId",
       'cz'|| a.yjxh as "prescriptionNumber", /*处置号码*/
       '处置' as "prescriptionType", /*处方类型*/

       a.ksdm as "depCode", /*开处置科室代码*/
       f.ksmc as "depName", /*开处置科室名称*/
       ''||a.zxks as "examDepCode",--执行科室代码
        (select t.ksmc from gy_ksdm t where t.ksdm=nvl(a.zxks,a.ksdm)) as "examDepName",--执行科室
       a.ysdm as "doctorCode", /*开方医生编号*/
       g.YGXM as "doctorName", /*开方医生姓名*/
       to_char(a.kdrq,'yyyy-mm-dd hh24:mi:ss') as "prescriptionDate", /*开方处置时间 （yyyy-MM-dd HH：MM：SS）*/
       nvl((select sum(nvl(hjje,0)) from ms_yj02 mx where mx.yjxh=a.yjxh),0) as "paymentAmount",/*处置金额 单位：元*/
       2 as "type" /*项目类型 1处方，2处置*/
  from ms_yj01 a
  left join ys_mz_jzls jz on jz.jzxh = a.jzxh
  left join MS_BRDA b on b.BRID = a.brid
  left join gy_ygdm g  on g.ygdm = a.ysdm
  left join gy_ksdm f on f.ksdm = a.ksdm
  where a.zfpb=0 and a.kdrq>sysdate-(select t.csz from gy_xtcs t where csmc='CFXQ' ) and (jz.jzzt>1 or jz.jzlx   in (4,5,6)) and   a.fphm is null /*作废处方不查询*/
;
create or replace view v_getprescriptiondetailed as
select (select csz from gy_xtcs where csmc='JGDM') as  "hospitalId", /*医疗机构代码  YLJGDM*/
       a.jzxh as  "visitingId", /*就诊号码*/
       'cf'||a.cfsb as "prescriptionNumber", /*处方号码*/
       'cf'||mx.sbxh as "detailedNumber",/*处方明细编号*/
       g.ypmc as "projectName",/*项目名称*/
       g.yfgg as"drugSpecification",--药品规格
       (select t.YGXM from gy_ygdm t where t.YGDM =a.ysdm) as "depName",
      (select t.KSMC from gy_ksdm t where t.KSDM=a.ksdm)"doctorName",
      ypyf as "usageFrequency",
      (select t.yfmc from yf_yflb t where t.yfsb=a.yfsb) as "examDepName",--执行科室
       mx.yfdw as "projectUnit",/*项目单位*/
       to_char(mx.ypsl) as "projectNumber" ,/*项目数量*/
       nvl(mx.ypdj,0)*mx.zfbl as "unitPrice",/*项目单价 单位：元*/
       nvl(mx.hjje,0)*mx.zfbl as "totalAmount"/*项目金额 单位：元*/
  from ms_cf01 a
  left join ms_cf02 mx on a.cfsb=mx.cfsb
  left join MS_BRDA b on b.BRID = a.brid
  left join yk_typk g on g.ypxh = mx.ypxh
 union all
    --获取处置明细列表
select (select csz from gy_xtcs where csmc='JGDM') as  "hospitalId", /*医疗机构代码  YLJGDM*/
       a.jzxh as  "visitingId", /*就诊号码*/
       'cz'||a.yjxh as "prescriptionNumber", /*处置号码*/
       'cz'||mx.sbxh as "detailedNumber",/*处方明细编号*/
       g.fymc as "projectName",/*项目名称*/
       '' as"drugSpecification",--药品规格
       (select t.YGXM from gy_ygdm t where t.YGDM =a.ysdm) as "depName",
      (select t.KSMC from gy_ksdm t where t.KSDM=a.ksdm)"doctorName",
      null as "usageFrequency",
        (select t.ksmc from gy_ksdm t where t.ksdm=a.zxks) as "examDepName",--执行科室
      g.fydw as "projectUnit",/*项目单位*/
       to_char(mx.ylsl) as "projectNumber" ,/*项目数量*/
       nvl(mx.yldj,0)*mx.zfbl as "unitPrice",/*项目单价 单位：元*/
       nvl(mx.hjje,0)*mx.zfbl as "totalAmount"/*项目金额 单位：元*/
  from ms_yj01 a
  left join ms_yj02 mx on a.yjxh=mx.yjxh
  left join MS_BRDA b on b.BRID = a.brid
  left join gy_ylsf g on g.fyxh = mx.ylxh
;
create or replace view v_getprescriptionpaymented as
select (select csz from gy_xtcs where csmc='JGDM') as "hospitalId", /*医疗机构代码  YLJGDM*/
       a.jzxh as  "visitingId", /*就诊号码*/
       b.sfzh as "idCard", --身份证
       b.BRXM as "patientName", --病人姓名
       b.brid as "patientId",
       'cf'||a.cfsb as "prescriptionNumber", /*处方号码*/
       (case a.cflx when 1 then '西药' when 2 then '中药' else '草药' end )as "prescriptionType", /*处方类型*/
       a.ksdm as "depCode", /*开方科室代码*/
       f.ksmc as "depName", /*开方科室名称*/
       a.ysdm as "doctorCode", /*开方医生编号*/
       g.YGXM as "doctorName", /*开方医生姓名*/
       to_char(a.kfrq,'yyyy-mm-dd hh24:mi:ss') as "prescriptionDate", /*开方时间 （yyyy-MM-dd HH：MM：SS）*/
       (select sum(nvl(hjje,0)) from ms_cf02 mx where mx.cfsb=a.cfsb) as "paymentAmount",/*处方金额 单位：元*/
       m.fphm as "invoiceNumber", --发票号码
       m.mzxh as "outpatientNo",--门诊序号（每次结算的结算序号）
       m.zfpb as "discrimination", --作废判别
       to_char(m.sfrq,'yyyy-mm-dd hh24:mi:ss')  as "settlementDate" , --结算日期
       (select to_char(listagg(t1.fkmc) WITHIN GROUP (ORDER BY t1.fkmc) )   from ms_fkxx t,gy_fkfs t1 where t.fkfs=t1.fkfs and  t.mzxh=m.mzxh) as "paymentMethod",-- 付款方式
       m.trade_no as  "orderNumber"--订单号
  from ms_cf01 a
  left join ys_mz_jzls jz on jz.jzxh = a.jzxh
  left join MS_BRDA b on b.BRID = a.brid
  left join gy_ygdm g  on g.ygdm = a.ysdm
  left join gy_ksdm f on f.ksdm = a.ksdm
  inner join ms_mzxx m on a.fphm=m.fphm and m.mzxh=a.mzxh
  where a.fphm is not null

union all
--获取处方已支付列表
select (select csz from gy_xtcs where csmc='JGDM') as "hospitalId", /*医疗机构代码  YLJGDM*/
       a.jzxh as  "visitingId", /*就诊号码*/
       b.sfzh as "idCard", --身份证
       b.BRXM as "patientName", --病人姓名
       b.brid as "patientId",
       'cz'||a.yjxh as "prescriptionNumber", /*处置号码*/
       '处置' as "prescriptionType", /*处方类型*/
       a.ksdm as "depCode", /*开处置科室代码*/
       f.ksmc as "depName", /*开处置科室名称*/
       a.ysdm as "doctorCode", /*开方医生编号*/
       g.YGXM as "doctorName", /*开方医生姓名*/
       to_char(a.kdrq,'yyyy-mm-dd hh24:mi:ss') as "prescriptionDate", /*开方处置时间 （yyyy-MM-dd HH：MM：SS）*/
       (select sum(nvl(hjje,0)) from ms_yj02 mx where mx.yjxh=a.yjxh) as "paymentAmount",/*处置金额 单位：元*/
              m.fphm as "invoiceNumber", --发票号码
       m.mzxh as "outpatientNo",--门诊序号（每次结算的结算序号）
       m.zfpb as "discrimination", --作废判别
       to_char(m.sfrq,'yyyy-mm-dd hh24:mi:ss')  as "settlementDate" , --结算日期
        (select to_char(listagg(t1.fkmc) WITHIN GROUP (ORDER BY t1.fkmc) ) from ms_fkxx t,gy_fkfs t1 where t.fkfs=t1.fkfs and  t.mzxh=m.mzxh) as "paymentMethod",-- 付款方式
       m.trade_no as  "orderNumber"--订单号
  from ms_yj01 a
  left join ys_mz_jzls jz on jz.jzxh = a.jzxh
  left join MS_BRDA b on b.BRID = a.brid
  left join gy_ygdm g  on g.ygdm = a.ysdm
  left join gy_ksdm f on f.ksdm = a.ksdm
  inner join ms_mzxx m on a.fphm=m.fphm and m.mzxh=a.mzxh

  where a.fphm is not null
;
