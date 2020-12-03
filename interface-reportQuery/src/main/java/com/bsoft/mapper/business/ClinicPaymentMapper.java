package com.bsoft.mapper.business;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author liujx
 * @date 2019/4/26
 */
@Repository
public interface ClinicPaymentMapper {

    /**
     * 写ys_mz_jzls
     * @param req
     * @return
     */
    int saveHistory(Map<String, Object> req);
    /**
     * 查询病人信息
     *
     * @param req
     * @return
     */
    List<Map<String, Object>> brxx(Map<String, Object> req);
    /**
     * 查询his医技信息
     *
     * @param req
     * @return
     */
    List<Map<String, Object>> yjxx(Map<String, Object> req);

    /**
     * 保存医技01表
     * @param mscf01
     */
      void saveMsyj01(Map<String, Object> mscf01);

    /**
     * 保存医技02表
     * @param mscf02
     */
      void saveMsyj02(Map<String, Object> mscf02);
    /**
     * 查询费用、项目、单价、费用归并
     * @param map
     * @return
     */
    public Map<String,Object> getLISParam(Map<String,Object> map);
    /**
     * //查询系统参数
     * @param req
     * @return
     */
    String getXtcs(Map<String, Object> req);
    /**
     * 保存ms_ghmx
     * @param mscf02
     */
    void saveGhmx(Map<String, Object> mscf02);
}
