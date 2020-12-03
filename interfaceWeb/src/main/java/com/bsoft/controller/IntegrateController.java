package com.bsoft.controller;

import com.bsoft.service.IntegrateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import java.util.Map;

/**
 * 首页展示功能控制层
 *
 * @author liujx
 * @date 2019/10/12
 */
@RestController
@RequestMapping("/integrate")
public class IntegrateController {
    private static Logger logger = LoggerFactory.getLogger(IntegrateController.class);
    @Resource
    IntegrateService integrateService;

    /**
     * 首页展示内容查询
     */
    @PostMapping("/homePage")
    public String homePage(@RequestBody Map<String, Object> projectInfo) throws Exception {
        logger.info("首页展示内容查询:入参：" + projectInfo);
        String res = integrateService.homePage(projectInfo);
        logger.info("首页展示内容查询:出参：" + res);
        return res;
    }

    /**
     * 通过点击服务看到的信息
     * 机构以及机构下面的服务信息
     */
    @PostMapping("/mechanism")
    public String mechanism(@RequestBody Map<String, Object> projectInfo) throws Exception {
        logger.info("通过点击服务看到的信息:入参：" + projectInfo);
        String res = integrateService.mechanism(projectInfo);
        logger.info("通过点击服务看到的信息:出参：" + res);
        return res;
    }

    /**
     * 通过点击日志看到的信息
     */
    @PostMapping("/logInfo")
    public String logInfo(@RequestBody Map<String, Object> projectInfo) throws Exception {
        logger.info("通过点击日志看到的信息:入参：" + projectInfo);
        String res = integrateService.logInfo(projectInfo);
        logger.info("通过点击日志看到的信息:出参：" + res);
        return res;
    }

    /**
     * 通过点击机构查询服务信息
     * 返回服务下的 每个接口返回的信息
     */
    @PostMapping("/serviceInfo")
    public String serviceInfo(@RequestBody Map<String, Object> projectInfo) throws Exception {
        logger.info("通过点击日志看到的信息:入参：" + projectInfo);
        String res = integrateService.serviceInfo(projectInfo);
        logger.info("通过点击日志看到的信息:出参：" + res);
        return res;
    }

    /**
     * 通过点击日志里面的服务信息
     * 返回 该服务的日志接口统计情况
     */
    @PostMapping("/motendInfo")
    public String motendInfo(@RequestBody Map<String, Object> projectInfo) throws Exception {
        logger.info("通过点击日志里面的服务信息:入参：" + projectInfo);
        String res = integrateService.motendInfo(projectInfo);
        logger.info("通过点击日志里面的服务信息:出参：" + res);
        return res;
    }

    /**
     * 查询日志详情
     *
     */
    @PostMapping("/queryLogInfo")
    public String queryLogInfo(@RequestBody Map<String, Object> projectInfo) throws Exception {
        logger.info("查询日志详情:入参：" + projectInfo);
        String res = integrateService.queryLogInfo(projectInfo);
        logger.info("查询日志详情:出参：" + res);
        return res;
    }


    /**
     * 获取项目下面的机构信息
     * 机构以及机构下面的服务信息
     */
    @PostMapping("/getMechanism")
    public String getMechanism(@RequestBody Map<String, Object> projectInfo) throws Exception {
        logger.info("获取项目下面的机构信息:入参：" + projectInfo);
        String res = integrateService.getMechanism(projectInfo);
        logger.info("获取项目下面的机构信息:出参：" + res);
        return res;
    }

    /**
     * 树状图展示需要的结构
     * 机构以及机构下面的服务信息
     */
    @PostMapping("/getTree")
    public String getTree(@RequestBody Map<String, Object> projectInfo) throws Exception {
        logger.info("树状图展示需要的结构:入参：" + projectInfo);
        String res = integrateService.mechanism(projectInfo);
        logger.info("树状图展示需要的结构:出参：" + res);
        res= res.replace("mechanismName","name").replace("serviceID","name")
                .replace("serviceDetails","children")
                .replace("ipAddressInfo","children")
                .replace("ipAddress","name");
        return res;
    }
    /**
     *  获取金额信息
     */
    @PostMapping("/getMoneyInfo")
    public String getMoneyInfo(@RequestBody Map<String, Object> projectInfo) throws Exception {
        logger.info("获取金额信息:入参：" + projectInfo);
        String res = integrateService.getMoneyInfo(projectInfo);
        logger.info("获取金额信息:出参：" + res);
        return res;
    }

    /**
     * 接口时长数量统计
     * @param projectInfo
     * @return
     * @throws Exception
     */
    @PostMapping("/getMethodStatistics")
    public String getMethodStatistics(@RequestBody Map<String, Object> projectInfo) throws Exception {
        logger.info("接口时长数量统计:入参：" + projectInfo);
        String res = integrateService.getMethodStatistics(projectInfo);
        logger.info("接口时长数量统计:出参：" + res);
        return res;
    }


    /**
     * 24小时内接口调用数统计
     * @param projectInfo
     * @return
     * @throws Exception
     */
    @PostMapping("/getHoursStatistics")
    public String getHoursStatistics(@RequestBody Map<String, Object> projectInfo) throws Exception {
        logger.info("24小时内接口调用数统计:入参：" + projectInfo);
        String res = integrateService.getHoursStatistics(projectInfo);
        logger.info("24小时内接口调用数统计:出参：" + res);
        return res;
    }

    /**
     * 错误日志趋势统计
     * @param projectInfo
     * @return
     * @throws Exception
     */
    @PostMapping("/getErrorLogInfo")
    public String getErrorLogInfo(@RequestBody Map<String, Object> projectInfo) throws Exception {
        logger.info("错误日志趋势统计:入参：" + projectInfo);
        String res = integrateService.getErrorLogInfo(projectInfo);
        logger.info("错误日志趋势统计:出参：" + res);
        return res;
    }
}
