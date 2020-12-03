package com.bsoft.entity;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;


/**
 * @author liujx
 * @date 2019/9/23
 */
@Data
public class InterFaceBean implements Serializable {
    private String id;
    //    接口主键
    @NotNull(message = "接口主键interfaceId不能为空")
    private String interfaceId;
    //    接口名称
    @NotNull(message = "接口名称interfaceName不能为空")
    private String interfaceName;
    //    接口类型
//    @NotNull(message = "接口类型minterfaceType不能为空")
    private String interfaceType;
    //    描述
    private String interfaceDescription;
    //    备注
    private String interfaceRemark;
    //    新增时间
    private Date createTime;
    //    修改时间
    private Date lastTime;
    //    删除标志 0存在，1删除
    private String status;
    /**页数*/
    private Integer pageNum;
    /**每页多少行*/
    private Integer pageSize;

}
