package com.bsoft.entity;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;


/**
 * @author liujx
 * @date 2019/10/9
 */
@Data
public class ServiceConfigInfo implements Serializable {
    /*** 主键 */
    private String id;
    /** * 项目编号*/
    @NotNull(message = "项目名称projectNumber不能为空")
    private String projectNumber;
    /** * 服务名称*/
    @NotNull(message = "服务名称serviceID不能为空")
    private String serviceID;
    /** * 机构名称*/
    @NotNull(message = "机构名称mechanismCode不能为空")
    private String mechanismCode;
    @NotNull(message = "服务名称serviceName不能为空")
    private String serviceName;
    /*** IP地址*/
    @NotNull(message = "IP地址ipAddress不能为空")
    private String ipAddress;
    /*** 端口*/
    @NotNull(message = "端口port不能为空")
    private String port;
    /** 状态*/
    private String status;
    /*** 项目编号*/
//    @NotNull(message = "项目编号projectNumber不能为空")
//    private String projectNumber;

    /**页数*/
    private Integer pageNum;
    /**每页多少行*/
    private Integer pageSize;
    /**创建时间*/
    private Date createTime;
    /**最后修改时间*/
    private Date lastTime;
}
