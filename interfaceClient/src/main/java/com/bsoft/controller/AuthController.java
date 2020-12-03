//package com.bsoft.controller;
//
//
//import com.bsoft.util.JWTUtil;
//import com.github.pagehelper.util.StringUtil;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.redis.core.StringRedisTemplate;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.UUID;
//import java.util.concurrent.TimeUnit;
//
//@RestController
//@RequestMapping("/auth")
//public class AuthController {
//
//    @Autowired
//    StringRedisTemplate redisTemplate;
//    /**
//     * 登录认证
//     * @param username 用户名
//     * @param password 密码
//     */
//    @PostMapping("/getToken")
//    public String login(@RequestParam String username, @RequestParam String password) {
//
//        String pwd = (String)redisTemplate.opsForHash().get("api-userInfo", username);
//        if(StringUtil.isEmpty(pwd)){
//            return "{\"code\":0,\"msg\":\"账号不存在！\",\"data\":{}}";
//        }
//        if(!pwd.equals(password)){
//            return "{\"code\":0,\"msg\":\"密码不正确！\",\"data\":{}}";
//        }
//        else{
//            //生成token
//            String token = JWTUtil.generateToken(username);
//
//            //生成refreshToken
//            String refreshToken = UUID.randomUUID().toString();
//
//            //数据放入redis
//            redisTemplate.opsForHash().put(refreshToken, "token", token);
//            redisTemplate.opsForHash().put(refreshToken, "username", username);
//
//            //设置token的过期时间
//            redisTemplate.expire(refreshToken, JWTUtil.REFRESH_TOKEN_EXPIRE_TIME, TimeUnit.MILLISECONDS);
//
//            return "{\"code\":1,\"msg\":\"请求成功\",\"data\":{\"token\":\""+token+"\",\"refreshToken\":\""+refreshToken+"\"}}";
//        }
//    }
//
//    /**
//     * 刷新token
//     */
//    @PostMapping("/refreshToken")
//    public String refreshToken(@RequestParam String refreshToken) {
//        String username = (String)redisTemplate.opsForHash().get(refreshToken, "username");
//        if(StringUtil.isEmpty(username)){
//            return "{\"code\":0,\"msg\":\"刷新token失败！\",\"data\":{}}";
//        }
//
//        //生成新的token
//        String newToken = JWTUtil.generateToken(username);
//        redisTemplate.opsForHash().put(refreshToken, "token", newToken);
//        return "{\"code\":1,\"msg\":\"请求成功\",\"data\":{\"token\":\""+newToken+"\",\"refreshToken\":\""+refreshToken+"\"}}";
//    }
//
//}
