package com.bsoft.service.impl;

import com.bsoft.constants.InterfaceConstants;
import com.bsoft.dao.MongoDaoSupport;
import com.bsoft.entity.PlatformUser;
import com.bsoft.service.AuthService;
import com.bsoft.util.JWTUtil;
import com.bsoft.util.MD5EncodeUtil;
import com.github.pagehelper.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author ：hxy
 * @date ：Created in 2019/09/20 17:16
 * @description：登录认证
 * @modified By：
 * @version: $
 */
@Service
public class AuthServiceImpl   extends MongoDaoSupport<PlatformUser> implements AuthService{
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    /**
     * 登录认证
     * @param platformUser 用户名
     *
     */
    @Override
    public String login(PlatformUser platformUser) throws Exception {
        PlatformUser platformUserFind=new PlatformUser();
        //密码加密
        platformUserFind.setPassword(MD5EncodeUtil.code(platformUser.getPassword(),32));
        platformUserFind.setStatus("0");
        platformUserFind.setAccount(platformUser.getAccount());
        List<PlatformUser> list = findByCondition(platformUserFind);
        if(false){
            return "{\"code\":"+InterfaceConstants.AUTH_ERROR_CODE1+",\"msg\":\"账号不存在！\",\"data\":{}}";
        }
        else{
            //生成token
            String token = JWTUtil.generateToken(platformUser.getAccount());

            //生成refreshToken
            String refreshToken = UUID.randomUUID().toString();

            //数据放入redis
            stringRedisTemplate.opsForHash().put(refreshToken, "token", token);
            stringRedisTemplate.opsForHash().put(refreshToken, "account", "dl");

            //设置token的过期时间
            stringRedisTemplate.expire(refreshToken, JWTUtil.REFRESH_TOKEN_EXPIRE_TIME, TimeUnit.MILLISECONDS);

            return "{\"code\":1,\"msg\":\"请求成功\",\"data\":{\"token\":\""+token+"\",\"refreshToken\":\""+refreshToken+"\"}}";
        }
    }
    /**
     * 刷新token
     * @param refreshToken 用户名
     *
     */
    @Override
    public String refreshToken(String refreshToken) {
        String username = (String)stringRedisTemplate.opsForHash().get(refreshToken , "account");
        if(StringUtil.isEmpty(username)){
            return "{\"code\":"+InterfaceConstants.AUTH_ERROR_CODE1+",\"msg\":\"刷新token失败！\",\"data\":{}}";
        }

        //生成新的token
        String newToken = JWTUtil.generateToken(username);
        stringRedisTemplate.opsForHash().put(refreshToken, "token", newToken);
        return "{\"code\":1,\"msg\":\"请求成功\",\"data\":{\"token\":\""+newToken+"\",\"refreshToken\":\""+refreshToken+"\"}}";
    }

}

