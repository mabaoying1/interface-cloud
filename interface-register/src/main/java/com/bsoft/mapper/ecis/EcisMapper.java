package com.bsoft.mapper.ecis;

import java.util.List;
import java.util.Map;

public interface EcisMapper {
    Map<String,Object> getIdentity(Map<String, Object> paramMap);

    int updateIdentity(Map<String, Object> paramMap);
    /**
     * 获取ECIS_YJFZ
     * @param req
     * @return
     */
    List<Map<String,Object>> getECIS_YJFZ(Map<String, Object> req);

    int insertEcis_jzjl(Map<String, Object> paramMap);
    /**
     * 获取getECIS_CWSZ
     * @param req
     * @return
     */
    List<Map<String,Object>> getECIS_CWSZ(Map<String, Object> req);

    int updateECIS_CWSZ(Map<String, Object> paramMap);
    int updateECIS_YJFZ(Map<String, Object> paramMap);
}