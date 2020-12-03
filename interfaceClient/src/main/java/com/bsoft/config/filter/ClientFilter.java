package com.bsoft.config.filter;

import com.alibaba.dubbo.common.utils.IOUtils;
import com.bsoft.constants.InterfaceConstants;
import com.bsoft.util.JWTUtil;
import com.bsoft.wrapper.MyRequestWrapper;
import com.github.pagehelper.util.StringUtil;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.http.ServletInputStreamWrapper;
import net.sf.json.JSONObject;
import org.apache.http.annotation.Obsolete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class ClientFilter extends ZuulFilter {

    @Autowired
    StringRedisTemplate redisTemplate;

    @Value("${hospitalIds}")
    private String hospitalIds;

    /**
     * 过滤器的类型，它决定过滤器在请求的哪个生命周期中执行。
     * 这里定义为pre，代表会在请求被路由之前执行。
     *
     * @return
     */
    @Override

    public String filterType() {
        return "pre";
    }

    /**
     * filter执行顺序，通过数字指定。
     * 数字越大，优先级越低。
     *
     * @return
     */
    @Override
    public int filterOrder() {
        return -1000;
    }

    /**
     * 判断该过滤器是否需要被执行。这里我们直接返回了true，因此该过滤器对所有请求都会生效。
     * 实际运用中我们可以利用该函数来指定过滤器的有效范围。
     *
     * @return
     */
    @Override
    public boolean shouldFilter() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        String uri = request.getRequestURI();

        if (uri.contains(InterfaceConstants.GETTOKEN_URI) || uri.contains(InterfaceConstants.REFRESHTOKEN_URI)) {
            return false;
        }
        return true;
    }

    /**
     * 过滤器的具体逻辑
     *
     * @return
     */
    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        String url = request.getRequestURI();
        if (url.contains(InterfaceConstants.GETTOKEN_URI) || url.contains(InterfaceConstants.REFRESHTOKEN_URI)) {
            ctx.put(FilterConstants.SERVICE_ID_KEY, InterfaceConstants.AUTH_SERVICE_ID);
            return null;
        }
        if(!url.equals(InterfaceConstants.REFRESH_UNIFIED_URI)){
            ctx.setSendZuulResponse(false);
            ctx.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());
            ctx.setResponseBody("{\"code\":" + InterfaceConstants.AUTH_ERROR_CODE1 + ",\"msg\":\"非法请求，请检查请求地址！\",\"data\":{}}");
            return null;
        }
//
        JSONObject requestParams = new JSONObject();
//
        //鉴权
        String token = request.getHeader(InterfaceConstants.TOKEN_KEY);
        String refreshToken = request.getHeader(InterfaceConstants.REFRESHTOKEN_KEY);

        if (StringUtil.isEmpty(token)) {
            ctx.setSendZuulResponse(false);
            ctx.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());
            ctx.setResponseBody("{\"code\":" + InterfaceConstants.AUTH_ERROR_CODE1 + ",\"msg\":\"未登录或登录已经失效，请重新登录后再试！\",\"data\":{}}");
            return null;
        }

        if (StringUtil.isEmpty(refreshToken)) {
            ctx.setSendZuulResponse(false);
            ctx.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());
            ctx.setResponseBody("{\"code\":" + InterfaceConstants.AUTH_ERROR_CODE1 + ",\"msg\":\"未登录或登录已经失效，请重新登录后再试！\",\"data\":{}}");
            return null;
        }

        boolean verifyResult = JWTUtil.verify(token);
        if (!verifyResult) {
            ctx.setSendZuulResponse(false);
            ctx.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());
            ctx.setResponseBody("{\"code\":" + InterfaceConstants.AUTH_ERROR_CODE1 + ",\"msg\":\"令牌错误或者登录已经超时，请重新登录！\",\"data\":{}}");
            return null;
        }

        long time = redisTemplate.getExpire(refreshToken, TimeUnit.MILLISECONDS);
        String cacheToken = (String) redisTemplate.opsForHash().get(refreshToken, "token");
        String username = (String) redisTemplate.opsForHash().get(refreshToken, "username");
        String requestServiceId = request.getHeader(InterfaceConstants.REQUEST_SERVICE_ID);
        String requestServiceMethod =  request.getHeader(InterfaceConstants.REQUEST_SERVICE_METHOD);

        if (StringUtil.isEmpty(requestServiceId) || StringUtil.isEmpty(requestServiceMethod)) {
            ctx.setSendZuulResponse(false);
            ctx.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());
            ctx.setResponseBody("{\"code\":" + InterfaceConstants.AUTH_ERROR_CODE1 + ",\"msg\":\""+InterfaceConstants.REQUEST_SERVICE_ID+"或者"+InterfaceConstants.REQUEST_SERVICE_METHOD+"为空，请检查调用请求！\",\"data\":{}}");
            return null;
        }
        if (StringUtil.isEmpty(cacheToken)) {
            ctx.setSendZuulResponse(false);
            ctx.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());
            ctx.setResponseBody("{\"code\":" + InterfaceConstants.AUTH_ERROR_CODE1 + ",\"msg\":\"登录已经超时，请重新登录！\",\"data\":{}}");
            return null;
        }
        if (time <= 0) {
            ctx.setSendZuulResponse(false);
            ctx.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());
            ctx.setResponseBody("{\"code\":" + InterfaceConstants.AUTH_ERROR_CODE1 + ",\"msg\":\"登录已经超时，请重新登录！\",\"data\":{}}");
            return null;
        } else if (0 < time && time < JWTUtil.REFRESH_TOKEN_EXPIRE_TIME) {
            redisTemplate.expire(refreshToken, JWTUtil.REFRESH_TOKEN_EXPIRE_TIME, TimeUnit.MILLISECONDS);
        }

        String hospitalId = "";




        Map requestMap = request.getParameterMap();
        InputStream in = null;
        if (requestMap == null || requestMap.isEmpty()) {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
                String body = IOUtils.read(reader);
                requestParams = JSONObject.fromObject(body);
                try {
                    hospitalId = requestParams.getString(InterfaceConstants.HOSPITAL_ID);
                } catch (Exception ex) {
                    //ex.printStackTrace();
                }
                final String hid = hospitalId ;
                ctx.setRequest(new MyRequestWrapper(request) {
                    @Override
                    public ServletInputStream getInputStream() throws IOException {
                        return new ServletInputStreamWrapper(body.getBytes());
                    }

                    @Override
                    public int getContentLength() {
                        return body.getBytes().length;
                    }

                    @Override
                    public long getContentLengthLong() {
                        return body.getBytes().length;
                    }

                    @Override
                    public String getRequestURI() {
//                        if(hospitalIds.indexOf(hid) > 0){
//                            return "/repost/doRepost" ;
//                        }
                        return "/" +requestServiceId + "/" + requestServiceMethod;
                    }
                });



            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            requestParams = JSONObject.fromObject(requestMap);
        }

        request = ctx.getRequest();
        url = request.getRequestURI();



        if ("".equals(hospitalId)) {
            ctx.setSendZuulResponse(false);
            ctx.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());
            ctx.setResponseBody("{\"code\":" + InterfaceConstants.PARAMETER_NOTNULL + ",\"msg\":\"hospitalId不能为空！！\",\"data\":{}}");
            return null;
        }
        String[] split = null;
        split = url.split("/");
        String privilege = split[1] + "/" + split[2];
        //验证机构权限
        if (!redisTemplate.opsForSet().isMember(InterfaceConstants.REIDIS_USER_MECHANISMS_KEY + username, hospitalId)) {
            ctx.setSendZuulResponse(false);
            ctx.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());
            ctx.setResponseBody("{\"code\":" + InterfaceConstants.INSUFFICIENT_AUTHORITY + ",\"msg\":\"该账号未有该机构权限！！\",\"data\":{}}");
            return null;
        }
        //验证接口权限
        if (!redisTemplate.opsForSet().isMember(InterfaceConstants.REIDIS_USER_PRIVILEGES_KEY + username, privilege)) {
            ctx.setSendZuulResponse(false);
            ctx.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());
            ctx.setResponseBody("{\"code\":" + InterfaceConstants.INSUFFICIENT_AUTHORITY + ",\"msg\":\"该账号未有该接口权限！！\",\"data\":{}}");
            return null;
        }
        //获取该机构的机构对应服务代码
        String mechanismServiceCode = (String) redisTemplate.opsForHash().get(InterfaceConstants.REIDIS_MECHANISM_KEY + hospitalId, "mechanismServiceCode");
        String serviceId = mechanismServiceCode + "-" + split[1];
        ctx.put(FilterConstants.SERVICE_ID_KEY, serviceId);

        // 如果是第三方接口   我们只做转发 切换到另外一个服务
//        if(hospitalIds.indexOf(hospitalId) > 0){
//            ctx.put(FilterConstants.SERVICE_ID_KEY, InterfaceConstants.THIRD_PART_INTERFACE);
//        }

        return null;
    }
}
