package com.bsoft.entity;

import lombok.Data;
import java.util.List;
import java.util.Map;

/**
 * @author liujx
 * @date 2019/10/15
 */
@Data
public class LogInfoBean {
    //调用总数
    private String requestTotal;
    //成功总数
    private String successTotal;
    //失败总数
    private String failTotal;
    /**
     * 机构详情
     * map里面内容如下：
     * 机构编号  mechanismCode;
     * 机构名称  mechanismName;
     */
    private List<Map> mechanismDetails;
}
