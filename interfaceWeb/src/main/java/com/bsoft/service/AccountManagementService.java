package com.bsoft.service;

import com.bsoft.entity.User;

import java.util.List;
import java.util.Map;

/**
 * @author ：hxy
 * @date ：Created in 2019/09/20 17:16
 * @description：接口调用用户账号管理
 * @modified By：
 * @version: $
 */
public interface AccountManagementService {
    /**
     * 账号注册
     * @param user
     *
     */
    String register(User user) throws Exception;
    /**
     * 账号查询
     * @param map
     *
     */
    String query(Map<String,Object> map);
    /**
     * 账号修改
     * @param user
     *
     */
    String modify(User user) throws Exception;
    /**
     * 账号删除
     * @param user
     *
     */
    String delete(User user);
    /**
     * 判断用户是否有该接口或者该机构
     *
     * @param
     */
     boolean privilegesQuery(String privileges,String mechanisms );

    /**
     * 项目查询
     * @param map
     * @return
     */
    String queryAccount(Map<String,Object> map);
}
