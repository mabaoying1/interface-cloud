package com.bsoft.controller;

import com.bsoft.entity.PlatformUser;
import com.bsoft.service.AuthService;
import com.bsoft.service.PlatformAccountManagementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @author ：hxy
 * @date ：Created in 2019/09/23 15:08
 * @description：平台账号管理
 * @modified By：
 * @version: $
 */

@RestController
@RequestMapping("/platformAccountManagement")
public class PlatformAccountManagementController {
    private static Logger logger = LoggerFactory.getLogger(PlatformAccountManagementController.class);

    @Resource
    PlatformAccountManagementService platformAccountManagementService;

    /**
     * 账号注册
     *
     * @param platformUser
     */
    @PostMapping("/register")
    public String register(@Valid @RequestBody PlatformUser platformUser) throws Exception {

        logger.info("账号注册:入参：" + platformUser);
        String res = platformAccountManagementService.register(platformUser);
        logger.info("账号注册:出参：" + res);
        return res;
    }
    /**
     * 账号查询
     *
     * @param platformUser
     */
    @PostMapping("/query")
    public String query( @RequestBody PlatformUser platformUser) {

        logger.info("账号查询:入参：" + platformUser);
        String res = platformAccountManagementService.query(platformUser);
        logger.info("账号查询:出参：" + res);
        return res;
    }
    /**
     * 账号修改
     *
     * @param platformUser
     */
    @PostMapping("/modify")
    public String modify(@RequestBody PlatformUser platformUser) throws Exception {

        logger.info("账号修改:入参：" + platformUser);
        String res = platformAccountManagementService.modify(platformUser);
        logger.info("账号修改:出参：" + res);
        return res;
    }
    /**
     * 账号删除
     *
     * @param platformUser
     */
    @PostMapping("/delete")
    public String delete( @RequestBody PlatformUser platformUser) {

        logger.info("账号删除:入参：" + platformUser);
        String res = platformAccountManagementService.delete(platformUser);
        logger.info("账号删除:出参：" + res);
        return res;
    }
}