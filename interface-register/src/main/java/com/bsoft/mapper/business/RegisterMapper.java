package com.bsoft.mapper.business;

import java.util.List;
import java.util.Map;


public interface RegisterMapper {
    /**
     * 科室查询
     * @param map
     * @return
     */
    List<Map<String, Object>> listDepartment(Map<String, Object> map);
    /**
     * create by: hxy
     * description: 查询机构
     * create time: 2019/03/15 15:02
     *
     * @return a
     * @Param: null
     */
     List<Map<String, Object>> listOrg(Map<String, Object> map);


    /**
     * create by: hxy
     * description: 获取患者信息
     * create time: 2019/03/19 9:52
     *
     * @return a
     * @Param: null
     */
    public List<Map<String, Object>> patientIsMatch(Map<String, Object> map);

    /**
     * create by: hxy
     * description:患者信息建档
     * create time: 2019/03/19 9:52
     *
     * @return a
     * @Param: null
     */
    int PatientFiling(Map<String, Object> map);
    /**
     * create by: hxy
     * description:患者信息建档
     * create time: 2019/03/19 9:52
     *
     * @return a
     * @Param: null
     */
    int PatientFiling1(Map<String, Object> map);

    /**
     * create by: hxy
     * description:医生信息查询
     * create time: 2019/03/19 9:52
     *
     * @return a
     * @Param: null
     */
    public List<Map<String, Object>> listDoctor(Map<String, Object> map);

    /**
     * 预约支付
     *
     * @param req
     */
    Map<String, Object> appointmentPay(Map<String, Object> req);

    /**
     * 查询排班医生和号源查询
     *
     * @param req
     * @return
     */
    List<Map<String, Object>> queryDeptAndDoctor(Map<String, Object> req);

    /**
     * 修改档案信息
     *
     * @param req
     */
    int PatientEdit(Map<String, Object> req);
    /**
     * 修改档案信息
     *
     * @param req
     */
    int PatientEdit1(Map<String, Object> req);

    /**
     * 取消锁定号源
     *
     * @param req
     */
    void cancelLocking(Map<String, Object> req);

    /**
     * 提交预约挂号
     *
     * @param req
     */
    void confirmAppointment(Map<String, Object> req);

    /**
     * 锁定号源
     *
     * @param map
     * @return
     */
    int lockNumber(Map<String, Object> map);

    /**
     * 解锁号源
     *
     * @param map
     */
    int unlockNumber(Map<String, Object> map);

    /**
     * 查询是否已经预约了
     * @param req
     */
    int queryAppointment(Map<String, Object> req);
    /**
     * 预约
     * 预约不支付，只写预约信息
     * @param req
     */
    int appointment(Map<String, Object> req);

    /**
     * 取号
     * @param map
     */
    int getNumber(Map<String, Object> map);

    /**
     * 预约挂号，没有锁定号源，直接挂号
     * @param map
     */
    void confirmRegister(Map<String, Object> map);

    /**
     * 取消预约(写退费明细)
     * @param map
     */
    int cancelRegister(Map<String, Object> map);

    /**
     * 退费
     * @param map
     */
    int refund(Map<String, Object> map);

    /**
     * 退号
     * @param map
     */
    int withdrawNumber(Map<String, Object> map);

    /**
     * 锁号之后的预约挂号
     * @param map
     */
    void appointmentRegister(Map<String, Object> map);

    /**
     * 预约历史记录查询
     * @param req
     * @return
     */
    List<Map<String, Object>> getRegisteredRecords(Map<String, Object> req);

    /**
     * 查询主键值
     * @param req
     * @return
     */
    String queriesKey(Map<String, Object> req);

    /**
     * 获取系统参数
     * @param req
     * @return
     */
    String queryXtcs(Map<String, Object> req);

    /**
     * 保存预约信息
     * @param req
     * @return
     */
    int saveReservation(Map<String, Object> req);

    /**
     * 判断是否已经取过号
     * @param req
     * @return
     */
    int queryGetNumber(Map<String, Object> req);

    /***
     * 判断是否预约过该排班医生
     * @param req
     * @return
     */
    int queryOrdered(Map<String, Object> req);

    /**
     * 根据工号获取票据号码
     * @param req
     * @return
     */
    List<Map<String, Object>> settlementPrescription2(Map<String, Object> req);

    /**
     * 更新票据信息，号码使用完，更新sypb
     * @param stringObjectMap
     * @return
     */
    int settlementPrescription3(Map<String, Object> stringObjectMap);

    /**
     * 更新票据信息，号码未使用号码完加一
     * @param stringObjectMap
     * @return
     */
    int settlementPrescription4(Map<String, Object> stringObjectMap);
    /**
     * 保存挂号明细
     * @param map
     * @return
     */
    int saveGhmx(Map<String, Object> map);
    /**
     * 保存付款信息
     * @param map
     * @return
     */
    int saveFkxx(Map<String, Object> map);

    /**
     * 查询是否就诊
     * @param req
     * @return
     */
    int querySfjz(Map<String, Object> req);

    /**
     * 查询是否已经退费
     * @param req
     * @return
     */
    int querySftf(Map<String, Object> req);

    /**
     * 查询是否已经取消预约
     * @param req
     * @return
     */
    int querySfqxyy(Map<String, Object> req);

    /**
     * 保存退费明细
     * @param req
     * @return
     */
    int saveThmx(Map<String, Object> req);

    /**
     * 修改号源信息
     * @param req
     * @return
     */
    int editNumber(Map<String, Object> req);

    /**
     * 查询病人ID
     * @param req
     * @return
     */
    String queryBrda(Map<String, Object> req);
    /**
     * 查询病人mzhm
     * @param req
     * @return
     */
    String queryBrdaMzhm(Map<String, Object> req);

    /**
     * 根据排班ID查询号源信息
     * @param req
     * @return
     */
    List<Map<String, Object>> queryHyxx(Map<String, Object> req);

    /**
     * 预约挂号 时候保存预约信息
     * @param req
     * @return
     */
    int saveYygh(Map<String, Object> req);

    /**
     * 更新预约信息
     * @param req
     * @return
     */
    int updateYygh(Map<String, Object> req);

    /***
     * 分时段号源详细信息
     * @param req
     * @return
     */
    List<Map<String, Object>> getSourceInfo(Map<String, Object> req);

    /**
     * 验证分时段号源ID是否正确
     * @param req
     * @return
     */
    int queryDaypartingInfo(Map<String, Object> req);
    /**
     * 验证该病人是否在该科室已经锁号或者挂号
     * @param req
     * @return
     */
    int queryLockNumber(Map<String, Object> req);
    /**
     * 分时段号源锁号
     * @param req
     * @return
     */
    int lockDaypartinNumber(Map<String, Object> req);

    /**
     * 查询是否已经预约
     * @param req
     * @return
     */
    int queryIsAppointment(Map<String, Object> req);

    /**
     * 锁定号源
     * @param req
     * @return
     */
    int unlockDaypartinNumber(Map<String, Object> req);

    /**
     * 保存挂号预约信息ghyy_yyxx
     * @param req
     * @return
     */
    int saveGhyyYyxx(Map<String, Object> req);

    /**
     * 更新号源信息，未支付 预约 ghyy_hyxx
     * @param req
     * @return
     */
    int updateGhyy_hyxx(Map<String, Object> req);

    /**
     * 取号的时候更新号源信息
     * @param req
     * @return
     */
    int updateGhyy_hyxx_ghxh(Map<String, Object> req);

    /**
     * 取号的时候更新预约信息
     * @param req
     * @return
     */
    int updateGhyy_yyxx_Zt_Fkbz(Map<String, Object> req);

    /**
     * 退号的时候更新分时段号源信息
     * @param req
     * @return
     */
    int updateGhyy_hyxx_th(Map<String, Object> req);

    /**
     * 退号的时候更新分时段预约信息
     * @param req
     * @return
     */
    int updateGhyy_yyxx_th(Map<String, Object> req);

    /**
     * 预约的时候更新号源信息 没有锁定号源
     * @param req
     * @return
     */
    int updateGhyy_hyxx_Nhy(Map<String, Object> req);
    /**
     * 预约的时候更新号源信息 没有锁定号源
     * @param req
     * @return
     */
    int updateGhyy_hyxx_Nhy2(Map<String, Object> req);

    /***
     * 分时段号源详细信息
     * @param req
     * @return
     */
    List<Map<String, Object>> getJzxh(Map<String, Object> req);
    /**
     * 查询支付方式
     * @param req
     * @return
     */
    List<Map<String, Object>> getZffs(Map<String, Object> req);
    /**
     * 获取号源是否入队  并以什么状态入队
     * @param req
     * @return
     */
    String getEnterFlag(Map<String, Object> req);



    /**
     * 获取队列ID
     * @param req
     * @return
     */
    Map<String, Object> getQueueId(Map<String, Object> req);


    /**
     * 获取入队时间
     * @param req
     * @return
     */
    Map<String, Object> getStartTime(Map<String, Object> req);
    /**
     * 获取排队号码
     * @param sbxh
     * @return
     */
    int getQueueNo(String sbxh);

    /**
     * 写入排队信息
     * @param req
     * @return
     */
    int savePdxx(Map<String, Object> req);
    /**
     * 写入排队信息
     * @param req
     * @return
     */
    int updatePdxx(Map<String, Object> req);
    /**
     * 插入Ms_ghmx_fy
     * @param req
     * @return
     */
    int insertMs_ghmx_fy(Map<String, Object> req);
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
    /**
     * 解锁号源
     * @param
     * @return
     */
    int opt_0018_2();
    /**
     * 写saveMs_brzh
     * @param req
     * @return
     */
    int saveMs_brzh(Map<String, Object> req);

    /**
     * 写ys_mz_jzls
     * @param req
     * @return
     */
    int saveHistory(Map<String, Object> req);
    /**
     * 查询年龄是否满足
     *
     * @param req
     * @return
     */
    List<Map<String, Object>> queryAge(Map<String, Object> req);
    /**
     * 查询年龄是否满足
     *
     * @param req
     * @return
     */
    List<Map<String, Object>> queryBrxb(Map<String, Object> req);

    /**
     * 查询预约日期范围内医生是否有号源
     *
     * @param req
     */
    List<Map<String, Object>> opAppAppoint_1(Map<String, Object> req);

    /**
     * 查询该患者是否建档
     *
     * @param req
     */
    List<Map<String, Object>> opAppAppoint_2(Map<String, Object> req);

    /**
     * create by: hxy
     * description:患者修改电子健康卡号
     * create time: 2019/03/19 9:52
     *
     * @return a
     * @Param: null
     */
    int opAppAppoint_3(Map<String, Object> map);

    /**
     * 查询该患者今天是否已经挂该医生
     *
     * @param req
     */
    List<Map<String, Object>> opAppAppoint_4(Map<String, Object> req);

    /**
     * create by: hxy
     * description:插入挂号明细
     * create time: 2019/03/19 9:52
     *
     * @return a
     * @Param: null
     */
    int opAppAppoint_5(Map<String, Object> map);

    /**
     * create by: hxy
     * description:插入ms_ghmx_fkxx
     * create time: 2019/03/19 9:52
     *
     * @return a
     * @Param: null
     */
    int opAppAppoint_6(Map<String, Object> map);

    /**
     * create by: hxy
     * description:插入ms_yygh
     * create time: 2019/03/19 9:52
     *
     * @return a
     * @Param: null
     */
    int opAppAppoint_7(Map<String, Object> map);

    /**
     * create by: hxy
     * description:插入ghyy_yyxx
     * create time: 2019/03/19 9:52
     *
     * @return a
     * @Param: null
     */
    int opAppAppoint_8(Map<String, Object> map);



    /**
     * 更新ms_yspb
     *
     * @param map
     * @return
     */
    int opAppAppoint_9(Map<String, Object> map);

    /**
     * 更新ghyy_hyxx
     *
     * @param map
     * @return
     */
    int updateHyxx(Map<String, Object> map);
}
