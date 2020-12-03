package com.bsoft.service;

import com.bsoft.entity.Mechanism;
import com.bsoft.entity.User;

import java.util.List;
import java.util.Map;

/**
 * @author ：hxy
 * @date ：Created in 2019/09/20 17:16
 * @description：维护机构
 * @modified By：
 * @version: $
 */
public interface MechanismService {
    /**
     * 机构注册
     * @param mechanism
     *
     */
    String register(Mechanism mechanism) ;
    /**
     * 机构查询
     * @param mechanism
     *
     */
    String query(Mechanism mechanism);
    /**
     * 机构修改
     * @param mechanism
     *
     */
    String modify(Mechanism mechanism) ;
    /**
     * 机构删除
     * @param mechanism
     *
     */
    String delete(Mechanism mechanism);
    /**
     * 机构查询
     *
     * @param mechanisms
     */
     List<Mechanism> query(List<String> mechanisms);

    /***
     * 查询机构信息，code和name:入参
     * @param mechanism
     * @return
     */
    String queryMechainsm();
}
