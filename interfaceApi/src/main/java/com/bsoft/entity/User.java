package com.bsoft.entity;

import lombok.Data;


import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author ：hxy
 * @date ：Created in 2019/09/20 15:32
 * @description：接口调用用户实体类
 * @modified By：
 * @version: $
 */
@Data
public class User implements Serializable {
    /*** 主键 */
    private String id;
    /** * 账号*/
    @NotNull(message = "账号username不能为空")
    private String username;
    /** * 密码*/
    @NotNull(message = "密码password不能为空")
    private String password;
    /*** 状态*/
//    @NotNull(message = "状态state不能为空")
    private String status;
    /*** 项目编号*/
    @NotNull(message = "项目编号projectNumber不能为空")
    private String projectNumber;
    @NotNull(message = "项目名称projectName不能为空")
    private String projectName;
    @NotNull(message = "项目介绍projectIntroduction不能为空")
    private String projectIntroduction;
    @NotNull(message = "项目类型projectType不能为空")//  1: 标准接口，2：第三方接口
    private String projectType;
    /*** 权限*/
    private List<String> privileges;
    /*** 该账号下机构信息*/
    private List<String> mechanisms;
    /**页数*/
    private Integer pageNum;
    /**每页多少行*/
    private Integer pageSize;
    /**创建时间*/
    private Date createTime;
    /**最后修改时间*/
    private Date lastTime;

}
