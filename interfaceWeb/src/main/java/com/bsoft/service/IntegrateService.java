package com.bsoft.service;

import java.util.Map;

/**
 * 首页展示功能控制层
 *
 * @author liujx
 * @date 2019/10/12
 */
public interface IntegrateService {


    /***
     * 首页内容展示查询信息
     * @param projectInfo
     * @return
     */
    String homePage(Map<String, Object> projectInfo);

    /***
     * 通过点击服务看到的信息
     * 机构以及机构下面的服务信息
     * @param projectInfo
     * @return
     */
    String mechanism(Map<String, Object> projectInfo);

    /**
     * 通过点击日志看到的信息
     * @param projectInfo
     * @return
     */
    String logInfo(Map<String, Object> projectInfo);

    /***
     * 通过点击机构查询服务信息
     * 返回服务下的 每个接口返回的信息
     * @param projectInfo
     * @return
     */
    String serviceInfo(Map<String, Object> projectInfo);

    /**
     * 通过点击日志里面的服务信息
     * @param projectInfo
     * @return
     */
    String motendInfo(Map<String, Object> projectInfo);

    /***
     * 查询日志详情
     * @param projectInfo
     * @return
     */
    String queryLogInfo(Map<String, Object> projectInfo);

    /**
     * 获取项目下面的机构信息
     * @param projectInfo
     * @return
     */
    String getMechanism(Map<String, Object> projectInfo);

    /***
     * 获取金额信息
     * @param projectInfo
     * @return
     */
    String getMoneyInfo(Map<String, Object> projectInfo);

    /***
     * 接口时长数量统计
     * @param projectInfo
     * @return
     */
    String getMethodStatistics(Map<String, Object> projectInfo);

    /***
     * 24小时内接口调用数统计
     * @param projectInfo
     * @return
     */
    String getHoursStatistics(Map<String, Object> projectInfo);

    /**
     * 错误日志趋势统计
     * @param projectInfo
     * @return
     */
    String getErrorLogInfo(Map<String, Object> projectInfo);
}
