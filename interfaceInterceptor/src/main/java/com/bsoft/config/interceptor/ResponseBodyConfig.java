//package com.bsoft.config.interceptor;
//
//import com.bsoft.constants.InterfaceConstants;
//import net.sf.json.JSONObject;
//import org.springframework.core.MethodParameter;
//import org.springframework.http.HttpRequest;
//import org.springframework.http.MediaType;
//import org.springframework.http.server.ServerHttpAsyncRequestControl;
//import org.springframework.http.server.ServerHttpRequest;
//import org.springframework.http.server.ServerHttpResponse;
//import org.springframework.http.server.ServletServerHttpRequest;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
//
//import javax.servlet.http.HttpServletRequest;
//import java.security.Principal;
//import java.util.Map;
//
//@ControllerAdvice
//public class ResponseBodyConfig implements ResponseBodyAdvice {
//
//
//    @Override
//    public boolean supports(MethodParameter methodParameter, Class aClass) {
//        return true;
//    }
//
//    @Override
//    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType, Class aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
//
//        ServletServerHttpRequest ss = (ServletServerHttpRequest)serverHttpRequest;
//
//
//        HttpServletRequest request = ss.getServletRequest() ;
//
//        System.out.println("===============   o = "+o);
//
//        request.setAttribute(InterfaceConstants.RESPONSEPARAM_KEY,o);
//
//        return o;
//    }
//}
