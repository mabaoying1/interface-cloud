package com.bsoft.controller;

import com.bsoft.entity.User;
import com.bsoft.service.AccountManagementService;
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
 * @author ：hxy
 * @date ：Created in 2019/09/23 15:08
 * @description：接口调用用户账号管理
 * @modified By：
 * @version: $
 */

@RestController
@RequestMapping("/accountManagement")
public class AccountManagementController {
    private static Logger logger = LoggerFactory.getLogger(AccountManagementController.class);

    @Resource
    AccountManagementService accountManagementService;

    /**
     * 账号注册
     *
     * @param user
     */
    @PostMapping("/register")
    public String register(@Valid @RequestBody User user) throws Exception {
        logger.info("账号注册:入参：" + user);
        String res = accountManagementService.register(user);
        logger.info("账号注册:出参：" + res);
        return res;
    }
    /**
     * 账号查询
     *
     * @param map
     */
    @PostMapping("/query")
    public String query( @RequestBody Map<String,Object> map) {

        logger.info("账号查询:入参：" + map);
        String res = accountManagementService.query(map);
        logger.info("账号查询:出参：" + res);
        return res;
    }
    /**
     * 账号修改
     *
     * @param user
     */
    @PostMapping("/modify")
    public String modify( @RequestBody User user) throws Exception {

        logger.info("账号修改:入参：" + user);
        String res = accountManagementService.modify(user);
        logger.info("账号修改:出参：" + res);
        return res;
    }
    /**
     * 账号删除
     *
     * @param user
     */
    @PostMapping("/delete")
    public String delete( @RequestBody User user) {

        logger.info("账号删除:入参：" + user);
        String res = accountManagementService.delete(user);
        logger.info("账号删除:出参：" + res);
        return res;
    }


    /**
     * 项目查询
     *
     * @param map
     */
    @PostMapping("/queryAccount")
    public String queryAccount( @RequestBody Map<String,Object> map) {


        String res = accountManagementService.queryAccount(map);
        logger.info("项目查询:出参：" + res);
        return res;
    }
}