package com.bsoft.constants;

public class InterfaceConstants {

    // 获取权限serviceId
    public static final String AUTH_SERVICE_ID = "auth0";

    // 获取权限 token key
    public static final String TOKEN_KEY = "Authorization";

    // 获取权限 refreshtoken key
    public static final String REFRESHTOKEN_KEY = "Authorization-re";

    // 获取权限 获取token的路径
    public static final String GETTOKEN_URI = "auth/getToken";

    // 获取权限 刷新token的路径
    public static final String REFRESHTOKEN_URI = "auth/refreshToken";
    //    接口统一调用地址
    public static final String REFRESH_UNIFIED_URI = "/interface/business";
    //    请求Request-Service-Id
    public static final String REQUEST_SERVICE_ID = "Request-Service-Id";
    //    请求Request-Service-Method
    public static final String REQUEST_SERVICE_METHOD = "Request-Service-Method";


    // 医院ID
    public static final String HOSPITAL_ID = "hospitalId";

    // 未获取到token 错误代码值
    public static final int AUTH_ERROR_CODE1 = 1003;

    // token不正确 错误代码值
    public static final int AUTH_ERROR_CODE2 = 1004;
    // 参数不全 错误代码值
    public static final int PARAMETER_NOTNULL = 16001;
    // 权限不够 错误代码值
    public static final int INSUFFICIENT_AUTHORITY = 17001;
    // 未知运行时的系统异常 错误代码值
    public static final int SYSTEMATIC_ERROR_CODE = -99;
    //    成功运行返回code
    public static final int SYSTEMATIC_SUCCESS_CODE = 1;
    // request
    public static final String REQUESTPARAM_KEY = "$_requestParams_$";

    // response
    public static final String RESPONSEPARAM_KEY = "$_responseParams_$";
    // reidis存账号key
    public static final String REIDIS_USER_KEY = "api-userInfo";
    // reidis存账号权限key前缀
    public static final String REIDIS_USER_PRIVILEGES_KEY = "api-userPrivileges_";
    // reidis存账号机构key前缀
    public static final String REIDIS_USER_MECHANISMS_KEY = "api-userMechanisms_";
    // reidis存机构key前缀
    public static final String REIDIS_MECHANISM_KEY = "api-mechanism_";
    //reidis存放服务配置信息前缀
    public static final String REIDIS_EUREKAINFO_KEY = "eurekaInfo";

}
