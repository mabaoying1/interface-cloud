package com.bsoft.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author liujx
 * @date 2019/10/14
 */
@Data
public class RequestRecord implements Serializable {
    private String id ;
    private String hospitalId ;
    private String serviceId ;
    private String projectNumber ;
    private String methodId ;
    private String method ;
    private String requestTime ;
    private String year ;
    private String month ;
    private String day ;
    private String hour ;
    private String minute ;
    private String takeUpTime ;
    private String status ;
    private String reponseStatus ;
    private String requestParams ;
    private String responseParams ;
    /**页数*/
    private Integer pageNum;
    /**每页多少行*/
    private Integer pageSize;
}
