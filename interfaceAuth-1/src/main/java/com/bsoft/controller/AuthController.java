package com.bsoft.controller;


import com.bsoft.constants.InterfaceConstants;
import com.bsoft.util.JWTUtil;
import com.bsoft.util.MD5EncodeUtil;
import com.github.pagehelper.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 统一登录认证中心
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    StringRedisTemplate redisTemplate;
    /**
     * 登录认证
     * @param map
     */
    @PostMapping("/getToken")
    public String login(@RequestBody Map<String,Object> map) throws Exception {
        if(map.get("username")==null||map.get("password")==null){
            return "{\"code\":"+ InterfaceConstants.PARAMETER_NOTNULL+",\"msg\":\"账号密码不能为空！\",\"data\":{}}";
        }
        String username=map.get("username").toString();
        String password=map.get("password").toString();
        String pwd = (String)redisTemplate.opsForHash().get(InterfaceConstants.REIDIS_USER_KEY, username);
        if(StringUtil.isEmpty(pwd)){
            return "{\"code\":"+ InterfaceConstants.AUTH_ERROR_CODE2+",\"msg\":\"账号不存在！\",\"data\":{}}";
        }
        //密码md5加密
        password= MD5EncodeUtil.code(password,32);
        if(!pwd.equals(password)){
            return "{\"code\":"+ InterfaceConstants.AUTH_ERROR_CODE2+",\"msg\":\"密码不正确！\",\"data\":{}}";
        }
        else{
            //生成token
            String token = JWTUtil.generateToken(username);

            //生成refreshToken
            String refreshToken = UUID.randomUUID().toString();

            //数据放入redis
            redisTemplate.opsForHash().put(refreshToken, "token", token);
            redisTemplate.opsForHash().put(refreshToken, "username", username);

            //设置token的过期时间
            redisTemplate.expire(refreshToken, JWTUtil.REFRESH_TOKEN_EXPIRE_TIME, TimeUnit.MILLISECONDS);

            return "{\"code\":1,\"msg\":\"请求成功\",\"data\":{\"token\":\""+token+"\",\"refreshToken\":\""+refreshToken+"\"}}";
        }
    }

    /**
     * 刷新token
     */
    @PostMapping("/refreshToken")
    public String refreshToken(@RequestParam String refreshToken) {
        String username = (String)redisTemplate.opsForHash().get(refreshToken, "username");
        if(StringUtil.isEmpty(username)){
            return "{\"code\":"+ InterfaceConstants.AUTH_ERROR_CODE2+",\"msg\":\"刷新token失败！\",\"data\":{}}";
        }

        //生成新的token
        String newToken = JWTUtil.generateToken(username);
        redisTemplate.opsForHash().put(refreshToken, "token", newToken);
        return "{\"code\":1,\"msg\":\"请求成功\",\"data\":{\"token\":\""+newToken+"\",\"refreshToken\":\""+refreshToken+"\"}}";
    }

}
