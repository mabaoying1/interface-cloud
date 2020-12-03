package com.bsoft.service;

import com.bsoft.entity.ServiceConfigInfo;

import java.util.List;
import java.util.Map;

/**
 * 服务器配置信息接口
 * @author liujx
 * @date 2019/10/9
 */
public interface ServiceConfigInfoService {
    /**
     * 服务器配置信息新增
     * @param serviceConfigInfo
     * @return
     */
    String register(ServiceConfigInfo serviceConfigInfo);

    /**
     * 服务器配置信息查询
     * @param map
     * @return
     */
    String query(Map<String, Object> map);

    /**
     * 服务器配置信息修改
     * @param serviceConfigInfo
     * @return
     */
    String modify(ServiceConfigInfo serviceConfigInfo);

    /**
     * 服务器配置信息删除
     * @param serviceConfigInfo
     * @return
     */
    String delete(ServiceConfigInfo serviceConfigInfo);

    /**
     * 查询服务器配置情况与运行情况
     * @return
     */
    String queryServiceInfo();

    /**
     * 查询服务器配置的服务情况
     * @return
     */
    List<ServiceConfigInfo> findService();

    /***
     * 服务情况查看
     * @param map
     * @return
     */
    String queryService(Map<String, Object> map);
}
