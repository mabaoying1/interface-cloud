package com.bsoft.entity;

import lombok.Data;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * @author ：hxy
 * @date ：Created in 2019/09/20 15:32
 * @description：机构实体类
 * @modified By：
 * @version: $
 */
@Data
public class Mechanism implements Serializable {
    private String id;
    /** * 机构代码*/
    @NotNull(message = "机构代码mechanismCode不能为空")
    private String mechanismCode;
    /** * 机构名称*/
    @NotNull(message = "机构名称mechanismName不能为空")
    private String mechanismName;
    /*** 机构对应服务代码*/
    @NotNull(message = "机构对应服务代码mechanismServiceCode不能为空")
    private String mechanismServiceCode;
    @NotNull(message = "机构介绍mechanismIntroduction不能为空")
    private String mechanismIntroduction;
    //    删除标志 0存在，1删除
    private String status;
    //    新增时间
    private Date createTime;
    //    修改时间
    private Date lastTime;
    /**页数*/
    private Integer pageNum;
    /**每页多少行*/
    private Integer pageSize;
}
