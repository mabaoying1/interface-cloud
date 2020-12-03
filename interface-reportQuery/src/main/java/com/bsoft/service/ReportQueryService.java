package com.bsoft.service;


import com.bsoft.exception.CommonException;

import java.util.Map;

/**
 * 报告查询接口
 * @author liujx
 * @date 2019/4/26
 */
public interface ReportQueryService {

        /**
     * 检验报告列表
     * @param req
     */
    String getLisRecord(Map<String, Object> req) throws CommonException;
    /**
     * 检验报告列表详细信息
     * @param req
     */
    String getLisDetails(Map<String, Object> req) throws CommonException;
    /**
     * 检查报告列表
     * @param req
     */
    String getRisRecord(Map<String, Object> req) throws CommonException;
    /**
     * 检验报告列表详细信息
     * @param req
     */
    String getRisDetails(Map<String, Object> req) throws CommonException;
    /**
     * 检验项目查询
     * @param req
     */
    String getTestItems(Map<String, Object> req) throws CommonException;
    /**
     * 检验项目明细查询
     * @param req
     */
    String getTestItemsDetails(Map<String, Object> req) throws CommonException;
    /**
     * 处方开单
     * @param req
     */
    String prescriptionBilling(Map<String, Object> req) throws CommonException;

}
