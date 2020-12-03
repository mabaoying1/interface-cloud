package com.bsoft.entity;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * @author ：hxy
 * @date ：Created in 2019/09/20 15:32
 * @description：平台登录用户实体类
 * @modified By：
 * @version: $
 */
@Data
public class PlatformUser  implements Serializable {
    /*** 主键 */
    private String id;
    /** * 账号*/
    @NotNull(message = "账号account不能为空")
    private String account;
    /** * 密码*/
    @NotNull(message = "密码password不能为空")
    private String password;
    /*** 状态*/
    @NotNull(message = "状态state不能为空")
    private String status;
    /**页数*/
    private Integer pageNum;
    /**每页多少行*/
    private Integer pageSize;
    /**创建时间*/
    private Date createTime;
    /**最后修改时间*/
    private Date lastTime;

}
