package com.bsoft.controller;


import com.bsoft.entity.PlatformUser;
import com.bsoft.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;


@RestController
@RequestMapping("/auth")
public class AuthController {
    private static Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Resource
    AuthService authService;

    /**
     * 登录认证
     * @param platformUser 用户名
     *
     */
    @PostMapping("/getToken")
    public String login(@RequestBody PlatformUser platformUser) throws Exception {

        logger.info("登录认证:入参：" + platformUser);
        String res = authService.login(platformUser);
        logger.info("登录认证:出参：" + res);
        return res;
    }

    /**
     * 刷新token
     */
    @PostMapping("/refreshToken")
    public String refreshToken(@RequestBody Map<String,String> map) {

        logger.info("刷新token:入参：" + map);
        String res = authService.refreshToken(map.get("refreshToken"));
        logger.info("刷新token:出参：" + map);
        return res;
    }


}
