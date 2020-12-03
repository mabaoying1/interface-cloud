package com.bsoft.mapper.pacs;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author liujx
 * @date 2019/4/26
 */

public interface ReportQueryPacsMapper {


    /**
     * 检查报告列表
     * @param req
     * @return
     */
    List<Map<String, Object>> getRisRecord(Map<String, Object> req);
    /**
     * 检验报告列表详细信息
     * @param req
     * @return
     */
    List<Map<String, Object>> getRisDetails(Map<String, Object> req);

}
