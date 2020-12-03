package com.bsoft.service;

import com.bsoft.entity.PlatformUser;

/**
 * @author ：hxy
 * @date ：Created in 2019/09/20 17:16
 * @description：平台账号管理
 * @modified By：
 * @version: $
 */
public interface PlatformAccountManagementService {
    /**
     * 账号注册
     * @param platformUser
     *
     */
    String register(PlatformUser platformUser) throws Exception;
    /**
     * 账号查询
     * @param platformUser
     *
     */
    String query(PlatformUser platformUser);
    /**
     * 账号修改
     * @param platformUser
     *
     */
    String modify(PlatformUser platformUser) throws Exception;
    /**
     * 账号删除
     * @param platformUser
     *
     */
    String delete(PlatformUser platformUser);
}
