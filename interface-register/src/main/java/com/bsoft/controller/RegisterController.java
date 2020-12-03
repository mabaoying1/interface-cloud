package com.bsoft.controller;

import com.bsoft.exception.CommonException;
import com.bsoft.service.RegisterService;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * create by: hxy
 * description: 该接口实现预约挂号
 * create time:  17:15
 *
 * @author
 */
@Controller
@RequestMapping(value = "register")
public class RegisterController {
    private static Logger logger = LoggerFactory.getLogger(RegisterController.class);
    @Autowired
    private RegisterService registerService;

    /**
     * 查询挂号科室信息
     *
     * @param req
     * @return
     * @throws CommonException
     */
    @RequestMapping(value = "listDepartment", produces = "application/json;charset=utf-8", method = RequestMethod.POST)
    @ResponseBody
    public String listDepartment(@RequestBody Map<String, Object> req) throws CommonException {
        logger.info("预约挂号:查询科室传入参数：" + req);
        String res = registerService.listDepartment(req);
       // logger.info("预约挂号:查询科室传出参数：" + res);
        return res;
    }

    /**
     * 获取医疗机构信息
     *
     * @param req
     * @return
     * @throws CommonException
     */
    @RequestMapping(value = "listOrg", produces = "application/json;charset=utf-8", method = RequestMethod.POST)
    @ResponseBody
    public String listOrg(@RequestBody Map<String, Object> req) throws CommonException {
        logger.info("预约挂号:获取医疗机构信息传入参数：" + req);
        String res = registerService.ListOrg(req);
        logger.info("预约挂号:获取医疗机构信息传出参数：" + res);
        return res;
    }

    /**
     * 患者查询
     *
     * @param req
     * @return string
     * @throws CommonException
     */
    @RequestMapping(value = "patientIsMatch", produces = "application/json;charset=utf-8", method = RequestMethod.POST)
    @ResponseBody
    public String patientIsMatch(@RequestBody Map<String, Object> req) throws CommonException {
        logger.info("预约挂号:患者查询传入参数：" + req);
        String res = registerService.patientIsMatch(req);
        logger.info("预约挂号:患者查询传出参数：" + res);
        return res;
    }

    /**
     * 获取挂号医生列表
     *
     * @param req
     * @return
     * @throws CommonException
     */
    @RequestMapping(value = "listDoctor", produces = "application/json;charset=utf-8", method = RequestMethod.POST)
    @ResponseBody
    public String listDoctor(@RequestBody Map<String, Object> req) throws CommonException {
        logger.info("预约挂号:获取医生列表传入参数：" + req);
        String res = registerService.listDoctor(req);
      //  logger.info("预约挂号:获取医生列表传出参数：" + res);
        return res;
    }

    /**
     * 号源查询
     *
     * @param req
     * @return
     * @throws CommonException
     */
    @RequestMapping(value = "queryDeptAndDoctor", produces = "application/json;charset=utf-8", method = RequestMethod.POST)
    @ResponseBody
    public String queryDeptAndDoctor(@RequestBody Map<String, Object> req) throws CommonException {
        logger.info("预约挂号:号源查询传入参数：" + req);
        String res = registerService.queryDeptAndDoctor(req);
      //  logger.info("预约挂号:号源查询传出参数：" + res);
        return res;
    }

    /**
     * 锁定号源
     *
     * @param req
     * @return
     * @throws CommonException
     */
    @RequestMapping(value = "lockNumber", produces = "application/json;charset=utf-8", method = RequestMethod.POST)
    @ResponseBody
    public String lockNumber(@RequestBody Map<String, Object> req) throws CommonException {
        logger.info("预约挂号:号源锁定传入参数：" + req);
        String res = registerService.lockNumber(req);
        logger.info("预约挂号:号源锁定传出参数：" + res);
        return res;
    }


    /**
     * 解锁号源
     *
     * @param req
     * @return
     * @throws CommonException
     */
    @RequestMapping(value = "unlockNumber", produces = "application/json;charset=utf-8", method = RequestMethod.POST)
    @ResponseBody
    public String unlockNumber(@RequestBody Map<String, Object> req) throws CommonException {

        logger.info("预约挂号:解锁号源传入参数：" + req);
        String res = registerService.unlockNumber(req);
        logger.info("预约挂号:解锁号源传出参数：" + res);
        return res;
    }

    /**
     * 首诊患者建档
     *
     * @param req
     * @return
     * @throws CommonException
     */

    @RequestMapping(value = "patientFiling", produces = "application/json;charset=utf-8", method = RequestMethod.POST)
    @ResponseBody
    public  String patientFiling(@RequestBody Map<String, Object> req) throws CommonException {
        logger.info("预约挂号:首诊患者建档传入参数：" + req);
        String res = registerService.PatientFiling(req);
        logger.info("预约挂号:首诊患者建档传出参数：" + res);
        return res;
    }

    /**
     * 修改患者档案信息
     *
     * @param req
     * @return
     * @throws CommonException
     */
    @RequestMapping(value = "patientEdit", produces = "application/json;charset=utf-8", method = RequestMethod.POST)
    @ResponseBody
    public String patientEdit(@RequestBody Map<String, Object> req) throws CommonException {
        logger.info("预约挂号:修改患者建档传入参数：" + req);
        String res = registerService.PatientEdit(req);
        logger.info("预约挂号:修改患者建档传出参数：" + res);
        return res;
    }

    /**
     * 预约
     * 预约不支付，只写预约信息
     *
     * @param req
     * @return
     * @throws CommonException
     */
    @RequestMapping(value = "appointment", produces = "application/json;charset=utf-8", method = RequestMethod.POST)
    @ResponseBody
    public String appointment(@RequestBody Map<String, Object> req) throws CommonException {
        logger.info("预约挂号:预约传入参数：" + req);
        String res = registerService.appointment(req);
        logger.info("预约挂号:预约传出参数：" + res);
        return res;
    }

    /**
     * 取号
     * 写入支付信息，挂号明细
     *
     * @param req
     * @return
     * @throws CommonException
     */
    @RequestMapping(value = "getNumber", produces = "application/json;charset=utf-8", method = RequestMethod.POST)
    @ResponseBody
    public String getNumber(@RequestBody Map<String, Object> req) throws CommonException {
        logger.info("预约挂号:取号传入参数：" + req);
        String res = registerService.getNumber(req);
        logger.info("预约挂号:取号传出参数：" + res);
        return res;
    }

    /**
     * 预约挂号
     * 直接预约挂号
     *
     * @param req
     * @return
     * @throws CommonException
     */
    @RequestMapping(value = "confirmRegister", produces = "application/json;charset=utf-8", method = RequestMethod.POST)
    @ResponseBody
    public String confirmRegister(@RequestBody Map<String, Object> req) throws CommonException {
        logger.info("预约挂号:预约挂号confirmRegister传入参数：" + req);
        String res = registerService.confirmRegisterJson(req);
        //String res = registerService.confirmRegister(json);
        logger.info("预约挂号:预约挂号confirmRegister传出参数：" + res);
        return res;
    }

    /**
     * 预约挂号
     * 锁号之后的预约挂号
     *
     * @param req
     * @return
     * @throws CommonException
     */
    @RequestMapping(value = "appointmentRegister", produces = "application/json;charset=utf-8", method = RequestMethod.POST)
    @ResponseBody
    public String appointmentRegister(@RequestBody Map<String, Object> req) throws CommonException {
        logger.info("预约挂号:预约挂号appointmentRegister传入参数：" + req);
        String res = registerService.appointmentRegister(req);
        logger.info("预约挂号:预约挂号appointmentRegister传出参数：" + res);
        return res;
    }

    /**
     * 取消预约
     *
     * @param req
     * @return
     * @throws CommonException
     */
    @RequestMapping(value = "cancelRegister", produces = "application/json;charset=utf-8", method = RequestMethod.POST)
    @ResponseBody
    public String cancelRegister(@RequestBody Map<String, Object> req) throws CommonException {
        logger.info("预约挂号:取消预约传入参数：" + req);
        String res = registerService.cancelRegister(req);
        logger.info("预约挂号:取消预约传出参数：" + res);
        return res;
    }


    /**
     * 退费
     *
     * @param req
     * @return
     * @throws CommonException
     */
    @RequestMapping(value = "refund", produces = "application/json;charset=utf-8", method = RequestMethod.POST)
    @ResponseBody
    public String refund(@RequestBody Map<String, Object> req) throws CommonException {
        logger.info("预约挂号:退费传入参数：" + req);
        String res = registerService.refund(req);
        logger.info("预约挂号:退费传出参数：" + res);
        return res;
    }

    /**
     * 退号
     *
     * @param req
     * @return
     * @throws CommonException
     */
    @RequestMapping(value = "withdrawNumber", produces = "application/json;charset=utf-8", method = RequestMethod.POST)
    @ResponseBody
    public String withdrawNumber(@RequestBody Map<String, Object> req) throws CommonException {
        logger.info("预约挂号:取消预约传入参数：" + req);
        String res = registerService.withdrawNumber(req);
        logger.info("预约挂号:取消预约传出参数：" + res);
        return res;
    }

    /**
     * 查询预约记录
     *
     * @param req
     * @return
     * @throws CommonException
     */
    @RequestMapping(value = "getRegisteredRecords", produces = "application/json;charset=utf-8", method = RequestMethod.POST)
    @ResponseBody
    public String getRegisteredRecords(@RequestBody Map<String, Object> req) throws CommonException {
        logger.info("预约挂号:查询预约记录传入参数：" + req);
        String res = registerService.getRegisteredRecords(req);
        logger.info("预约挂号:查询预约记录传出参数：" + res);
        return res;
    }


    /**
     * 查询分时段号源信息
     *
     * @param req
     * @return
     * @throws CommonException
     */
    @RequestMapping(value = "getSourceInfo", produces = "application/json;charset=utf-8", method = RequestMethod.POST)
    @ResponseBody
    public String getSourceInfo(@RequestBody Map<String, Object> req) throws CommonException {
        logger.info("预约挂号:查询分时段号源详细信息传入参数：" + req);
        String res = registerService.getSourceInfo(req);
       // logger.info("预约挂号:查询分时段号源详细信息传出参数：" + res);
        return res;
    }
    /**
     * 一码付挂号
     *
     * @param req
     * @return
     * @throws CommonException
     */
    @RequestMapping(value = "PayRegister", produces = "application/json;charset=utf-8", method = RequestMethod.POST)
    @ResponseBody
    public String PayRegister(@RequestBody Map<String, Object> req) throws CommonException {
        logger.info("预约挂号:一码付挂号传入参数：" + req);
        JSONObject json = registerService.PayRegisterJson(req);
        String res = registerService.PayRegister(json);
         logger.info("预约挂号:一码付挂号参数：" + res);
        return res;
    }


}
