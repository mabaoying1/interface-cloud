package com.bsoft.exception;/**

/**
 * @author     ：hxy
 * @date       ：Created in 2019/03/08 16:02
 * @description：接口中出现错误返回此异常
 * @modified By：
 * @version: 1$
 */
public class CommonException extends RuntimeException {

    public  String  message;
    public CommonException(String  message){
        this.message=message;
    }
    @Override
    public String getMessage() {
        return this.message;
    }


}
