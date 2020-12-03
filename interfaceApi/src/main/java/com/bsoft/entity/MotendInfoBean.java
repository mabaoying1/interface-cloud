package com.bsoft.entity;

import lombok.Data;

/**
 * @author liujx
 * @date 2019/10/16
 */
@Data
public class MotendInfoBean {
    private String mechanismCode;
    private String serviceId;
    private String projectNumber;
    private String methodId;
//    调用方法
    private String method;
//    调用总数
    private String total;
//    最大用时
    private String maxTime;
//    最小用时
    private String minTime;
//    成功次数
    private String successTotal;
//    失败次数
    private String failTotal;
//    平均调用时长
    private String averageTime;
}
