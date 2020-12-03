package com.bsoft.entity;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 首页内容展示字段
 *
 * @author liujx
 * @date 2019/10/15
 */
@Data
public class IntegrateBean {
    //项目总数
    private String projectTotal;
    //医院总数
    private String hospitalTotal;
    //调用总数
    private String requestTotal;
    /**
     * 项目详情
     * map里面内容如下：
     * 项目编号  projectNumber;
     * 项目名称  projectName;
     * 项目介绍  projectIntroduction;
     * 服务总数  serviceTotal
     * 服务正常运行总数  serviceRunTotal
     * 调用成功总数  logSuccessTotal
     * 调用总数  logTotal
     *
     */
    private List<Map> projectDetails;
}
