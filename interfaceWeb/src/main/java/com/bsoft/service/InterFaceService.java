package com.bsoft.service;

import com.bsoft.entity.InterFaceBean;

import java.util.Map;

/**
 * @author liujx
 * @date 2019/9/23
 */
public interface InterFaceService {
    /**
     * 查询接口信息
     * @param reqBean
     * @return
     */
    String queryInterface(InterFaceBean reqBean);

    /**
     * 增加接口信息
     * @param reqBean
     * @return
     */
    String addInterface(InterFaceBean reqBean);

    /**
     * 修改接口信息
     * @param reqBean
     * @return
     */
    String updateInterface(InterFaceBean reqBean);
    /**
     * 修改接口信息
     * @param
     * @return
     */
    String queryMethod();


}
