package com.bsoft.model;

import java.io.Serializable;


public class RequestRecord implements Serializable {


    private String id ;
    private String hospitalId ;
    private String serviceId ;
    private String methodId ;
    private String method ;
    private String requestTime ;
    private Long requestStamp ;
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
    private String projectNumber ;
    private String port ;
    private String ipAddress ;

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(String hospitalId) {
        this.hospitalId = hospitalId;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getMethodId() {
        return methodId;
    }

    public void setMethodId(String methodId) {
        this.methodId = methodId;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getRequestTime() {
        return requestTime;
    }
    public void setRequestTime(String requestTime) {
        this.requestTime = requestTime;
    }

    public Long getRequestStamp() {
        return requestStamp;
    }

    public void setRequestStamp(Long requestStamp) {
        this.requestStamp = requestStamp;
    }
    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getMinute() {
        return minute;
    }

    public void setMinute(String minute) {
        this.minute = minute;
    }

    public String getTakeUpTime() {
        return takeUpTime;
    }

    public void setTakeUpTime(String takeUpTime) {
        this.takeUpTime = takeUpTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReponseStatus() {
        return reponseStatus;
    }

    public void setReponseStatus(String reponseStatus) {
        this.reponseStatus = reponseStatus;
    }

    public String getRequestParams() {
        return requestParams;
    }

    public void setRequestParams(String requestParams) {
        this.requestParams = requestParams;
    }

    public String getResponseParams() {
        return responseParams;
    }

    public void setResponseParams(String responseParams) {
        this.responseParams = responseParams;
    }

    public String getProjectNumber() {
        return projectNumber;
    }

    public void setProjectNumber(String projectNumber) {
        this.projectNumber = projectNumber;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

}
