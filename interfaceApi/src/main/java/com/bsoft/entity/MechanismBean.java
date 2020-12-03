package com.bsoft.entity;

import lombok.Data;
import java.util.List;
import java.util.Map;

/**
 * 机构返回信息bean
 *
 * @author liujx
 * @date 2019/10/15
 */
@Data
public class MechanismBean {
    /**
     * 机构名称
     */
    private String mechanismName;
    /**
     * 机构编码
     */
    private String mechanismCode;
    /**
     * 服务详情
     */
    private List<Map> serviceDetails;
}
