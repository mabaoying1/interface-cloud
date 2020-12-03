package com.bsoft.entity;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 24小时返回值
 * @author liujx
 * @date 2019/10/22
 */
@Data
public class HoursBean {
    private String projectNumber;
    private String projectName;
    private List<Object> details;

}
