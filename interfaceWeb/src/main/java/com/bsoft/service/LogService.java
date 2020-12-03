package com.bsoft.service;

import com.bsoft.entity.RequestRecord;

import java.util.List;
import java.util.Map;

/**
 * 日志查询接口
 * @author liujx
 * @date 2019/10/14
 */
public interface LogService {
    /**
     * 查询错误的日志信息
     * @return
     */
    List<RequestRecord> queryError();
    /**
     * 查询日志信息
     * @param requstMap
     * @return
     */
    String queryLog(Map<String,Object> requstMap);
}
