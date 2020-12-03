package com.bsoft.controller;

import com.bsoft.entity.ServiceConfigInfo;
import com.bsoft.entity.User;
import com.bsoft.service.ServiceConfigInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Map;

/**
 * 服务器配置信息
 * @author liujx
 * @date 2019/10/9
 */
@RestController
@RequestMapping("/serviceConfigInfo")
public class ServiceConfigInfoController {

    private static Logger logger = LoggerFactory.getLogger(ServiceConfigInfoController.class);
    @Resource
    ServiceConfigInfoService serviceConfigInfoService;

    /**
     * 服务器配置信息新增
     *
     * @param serviceConfigInfo
     */
    @PostMapping("/register")
    public String register(@Valid @RequestBody ServiceConfigInfo serviceConfigInfo) throws Exception {

        logger.info("服务器配置信息新增:入参：" + serviceConfigInfo);
        String res = serviceConfigInfoService.register(serviceConfigInfo);
        logger.info("服务器配置信息新增:出参：" + res);
        return res;
    }
    /**
     * 服务器配置信息查询
     *
     * @param map
     */
    @PostMapping("/query")
    public String query( @RequestBody Map<String,Object> map) {

        logger.info("服务器配置信息查询:入参：" + map);
        String res = serviceConfigInfoService.query(map);
        logger.info("服务器配置信息查询:出参：" + res);
        return res;
    }
    /**
     * 服务情况查看
     *
     * @param map
     */
    @PostMapping("/queryService")
    public String queryService( @RequestBody Map<String,Object> map) {

        logger.info("服务情况查看:入参：" + map);
        String res = serviceConfigInfoService.queryService(map);
        logger.info("服务情况查看:出参：" + res);
        return res;
    }

    /**
     * 服务器配置信息修改
     *
     * @param serviceConfigInfo
     */
    @PostMapping("/modify")
    public String modify( @RequestBody ServiceConfigInfo serviceConfigInfo) throws Exception {

        logger.info("服务器配置信息修改:入参：" + serviceConfigInfo);
        String res = serviceConfigInfoService.modify(serviceConfigInfo);
        logger.info("服务器配置信息修改:出参：" + res);
        return res;
    }
    /**
     * 服务器配置信息删除
     *
     * @param serviceConfigInfo
     */
    @PostMapping("/delete")
    public String delete( @RequestBody ServiceConfigInfo serviceConfigInfo) {

        logger.info("服务器配置信息删除:入参：" + serviceConfigInfo);
        String res = serviceConfigInfoService.delete(serviceConfigInfo);
        logger.info("服务器配置信息删除:出参：" + res);
        return res;
    }
    /**
     * 查询服务器配置情况与运行情况
     *
     * @param
     */
    @PostMapping("/queryServiceInfo")
    public String queryServiceInfo() {
        logger.info("查询服务器配置情况与运行情况:入参：" );
        String res = serviceConfigInfoService.queryServiceInfo();
        logger.info("查询服务器配置情况与运行情况:出参：" + res);
        return res;
    }
}
