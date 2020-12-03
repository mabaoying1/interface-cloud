package com.bsoft.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author liujx
 * @date 2019/10/17
 */
@Data
public class BillListBean implements Serializable {
    private String id ;
    private String hospitalId ;
    private String serviceId ;
    private String methodId ;
    private String method ;
    private String requestTime ;
    private String takeUpTime ;
    private String requestParams ;
    private String responseParams ;
    private String projectNumber ;
    //交易金额
    private int paymentAmount;
    //交易单号
    private String wcoerderId;
    //支付方式
    private String payType;
//    总金额
    private String totalMoney;
    //    金额
    private int price;
}
