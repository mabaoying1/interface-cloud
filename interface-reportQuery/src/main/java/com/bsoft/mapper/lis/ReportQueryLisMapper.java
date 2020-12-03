package com.bsoft.mapper.lis;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author liujx
 * @date 2019/4/26
 */
public interface ReportQueryLisMapper {
    public Long getMax(Map<String,Object> args);
    public void insertMax(Map<String,Object> cs);
    public void updateMax(Map<String,Object> cs);

    /**
     * 检验报告列表
     * @param req
     * @return
     */
    List<Map<String, Object>> getLisRecord(Map<String, Object> req);
    /**
     * 检验报告列表详细信息
     * @param req
     * @return
     */
    List<Map<String, Object>> getLisDetails(Map<String, Object> req);
    /**
     * 检验项目查询
     * @param req
     * @return
     */
    List<Map<String, Object>> getTestItems(Map<String, Object> req);
    /**
     * 检验项目明细查询
     * @param req
     * @return
     */
    List<Map<String, Object>> getTestItemsDetails(Map<String, Object> req);

    public void saveLisRequest(Map<String, Object> detail);

    public void saveLisRequestDetail(Map<String, Object> detail);

    public Map<String,Object> getBloodParam();

    public Map<String,Object> getNeedleParam();

    public Map<String,Object> getTubeParam();
    public List<Map<String,Object>> getLisByNeedle(Map<String,Object> map);

    public List<Map<String,Object>> getLisByBlood(Map<String,Object> map);

    public List<Map<String,Object>> getLisByTube(Map<String,Object> map);
    /**
     * 标本类型选择
     * @param req
     * @return
     */
    List<Map<String, Object>> getTestBblx(Map<String, Object> req);
}
