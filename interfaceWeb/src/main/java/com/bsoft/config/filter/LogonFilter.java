package com.bsoft.config.filter;

import com.bsoft.constants.InterfaceConstants;
import com.bsoft.util.JWTUtil;
import com.github.pagehelper.util.StringUtil;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.TimeUnit;

/**
 * @author ：hxy
 * @date ：Created in 2019/09/23 13:59
 * @description：用于平台登录验证
 * @modified By：
 * @version: $
 */

@Component
@WebFilter(urlPatterns = "/*", filterName = "logonFilter")
public class LogonFilter   implements Filter {
    @Autowired
    StringRedisTemplate redisTemplate;
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String url = request.getRequestURI();
        //登录接口不拦截
        if(url.contains(InterfaceConstants.GETTOKEN_URI) || url.contains(InterfaceConstants.REFRESHTOKEN_URI) ){
            filterChain.doFilter(servletRequest,servletResponse);
            return;
        }
//
        JSONObject requestParams = new JSONObject();
//
        //鉴权
        String token = request.getHeader(InterfaceConstants.TOKEN_KEY) ;
        String refreshToken = request.getHeader(InterfaceConstants.REFRESHTOKEN_KEY) ;


        if(StringUtil.isEmpty(token)){


            responseOut(response,"{\"code\":"+InterfaceConstants.AUTH_ERROR_CODE1+",\"msg\":\"未登录或登录已经失效，请重新登录后再试！\",\"data\":{}}");
            return ;
        }

        if(StringUtil.isEmpty(refreshToken)){
            responseOut(response,"{\"code\":"+InterfaceConstants.AUTH_ERROR_CODE1+",\"msg\":\"未登录或登录已经失效，请重新登录后再试！\",\"data\":{}}");
            return ;
        }

        boolean verifyResult = JWTUtil.verify(token);
        if(!verifyResult){
            responseOut(response,"{\"code\":"+InterfaceConstants.AUTH_ERROR_CODE1+",\"msg\":\"令牌错误或者登录已经超时，请重新登录！\",\"data\":{}}");
            return ;
        }

        long time = redisTemplate.getExpire(refreshToken, TimeUnit.MILLISECONDS);
        String cacheToken = (String)redisTemplate.opsForHash().get(refreshToken, "token");


        if(StringUtil.isEmpty(cacheToken)){
            responseOut(response,"{\"code\":"+InterfaceConstants.AUTH_ERROR_CODE1+",\"msg\":\"登录已经超时，请重新登录！\",\"data\":{}}");
            return ;
        }
        if(time <= 0){
            responseOut(response,"{\"code\":"+InterfaceConstants.AUTH_ERROR_CODE1+",\"msg\":\"登录已经超时，请重新登录！\",\"data\":{}}");
            return ;
        }else if(0 < time && time <JWTUtil.REFRESH_TOKEN_EXPIRE_TIME){
            redisTemplate.expire(refreshToken , JWTUtil.REFRESH_TOKEN_EXPIRE_TIME ,TimeUnit.MILLISECONDS);
        }
        filterChain.doFilter(servletRequest,servletResponse);
    }

    @Override
    public void destroy() {

    }

    /**
     * 验证失败返回
     */
    public  void responseOut(HttpServletResponse response,String code) throws IOException {
        ServletOutputStream out=response.getOutputStream();
        OutputStreamWriter ow=new OutputStreamWriter(out,"utf-8");
        ow.write(code);
        ow.flush();
        ow.close();
    }
}
