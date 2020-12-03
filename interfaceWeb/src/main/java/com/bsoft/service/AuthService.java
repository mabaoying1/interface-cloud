package com.bsoft.service;

import com.bsoft.entity.PlatformUser;

/**
 * @author ：hxy
 * @date ：Created in 2019/09/20 17:16
 * @description：登录认证
 * @modified By：
 * @version: $
 */
public interface AuthService {
    /**
     * 登录认证
     * @param platformUser 用户名
     *
     */
    String login( PlatformUser platformUser) throws Exception;
    /**
     * 刷新token
     * @param req 用户名
     *
     */
    String refreshToken (String req);
}
