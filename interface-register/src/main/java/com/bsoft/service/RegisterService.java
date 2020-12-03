package com.bsoft.service;

import com.bsoft.exception.CommonException;
import net.sf.json.JSONObject;

import java.text.ParseException;
import java.util.Map;

public interface RegisterService {

    /**
     * 查询科室
     *
     * @param req
     * @return
     * @throws CommonException
     */
    String listDepartment(Map<String, Object> req) throws CommonException;

    /**
     * create by: hxy
     * description: 查询机构
     * create time: 2019/03/15 14:55
     *
     * @rturn a
     * @Param: null
     */
    String ListOrg(Map<String, Object> req) throws CommonException;

    /**
     * create by: hxy
     * description: 患者查询
     * create time: 2019/03/15 14:55
     *
     * @rturn a
     * @Param: null
     */
    String patientIsMatch(Map<String, Object> req) throws CommonException;

    /**
     * create by: hxy
     * description: 值班医生和号源查询
     * create time: 2019/03/15 14:55
     *
     * @rturn a
     * @Param: null
     */
    String queryDeptAndDoctor(Map<String, Object> req) throws CommonException;

    /**
     * create by: hxy
     * description: 首诊患者建档
     * create time: 2019/03/15 14:55
     *
     * @rturn a
     * @Param: null
     */
    String PatientFiling(Map<String, Object> req) throws CommonException;

    /**
     * create by: hxy
     * description: 获取医生列表
     * create time: 2019/03/15 14:55
     *
     * @rturn a
     * @Param: null
     */
    String listDoctor(Map<String, Object> req) throws CommonException;


    /**
     * 修改档案信息
     *
     * @param req
     */
    String PatientEdit(Map<String, Object> req) throws CommonException;

    /**
     * 锁定号源
     *
     * @param req
     * @return
     */
    String lockNumber(Map<String, Object> req);

    /**
     * 解锁号源
     *
     * @param req
     * @return
     */
    String unlockNumber(Map<String, Object> req);

    /**
     * 预约
     * 预约不支付，只写预约信息
     *
     * @param req
     * @return
     */
    String appointment(Map<String, Object> req);

    /**
     * 取号
     * 写入支付信息，挂号明细
     *
     * @param req
     * @return
     */
    String getNumber(Map<String, Object> req);

    /**
     * 预约挂号
     * 锁号或者直接预约挂号
     *
     * @param json
     * @return
     */
    String confirmRegister(JSONObject json);
    /**
     * 预约挂号
     * 锁号或者直接预约挂号
     *
     * @param req
     * @return
     */
    String confirmRegisterJson(Map<String, Object> req);

    /**
     * 取消预约
     *
     * @param req
     * @return
     */
    String cancelRegister(Map<String, Object> req);

    /**
     * 退费
     *
     * @param req
     * @return
     */
    String refund(Map<String, Object> req);

    /**
     * 退费
     *
     * @param req
     * @return
     */
    String withdrawNumber(Map<String, Object> req);

    /**
     * 锁号之后的预约挂号
     *
     * @param req
     * @return
     */
    String appointmentRegister(Map<String, Object> req);

    /**
     * 查询预约记录
     *
     * @param req
     * @return
     */
    String getRegisteredRecords(Map<String, Object> req);

    /**
     * 分时段号源详细信息
     * @param req
     * @return
     */
    String getSourceInfo(Map<String, Object> req);
    /**
     * 一码付挂号
     * @param req
     * @return
     */
    String PayRegister(JSONObject json);

    /**
     * 一码付挂号
     *
     *
     * @param req
     * @return
     */
    JSONObject PayRegisterJson(Map<String, Object> req);
}
