//package com.bsoft.config.filter;
//
//import com.bsoft.wrapper.MyRequestWrapper;
//
//import javax.servlet.*;
//import javax.servlet.annotation.WebFilter;
//import javax.servlet.http.HttpServletRequest;
//import java.io.IOException;
//
///**
// * 该过滤器与MyRequestWrapper配合使用，可以让@RequestBody注解的参数至少可以读取两次，
// * 从而使得在拦截器中可以获取@RequestBody注解的参数。
// */
//@WebFilter(filterName="HttpServletRequestWrapperFilter",urlPatterns="/*")
//public class HttpServletRequestWrapperFilter implements Filter {
//    @Override
//    public void init(FilterConfig filterConfig) throws ServletException {
//
//    }
//
//    @Override
//    public void doFilter(ServletRequest request, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
//        ServletRequest requestWrapper= null;
//        if(request instanceof HttpServletRequest){
//            requestWrapper = new MyRequestWrapper((HttpServletRequest) request);
//        }
//        if(null == requestWrapper){
//            filterChain.doFilter(request,servletResponse);
//        }else{
//            filterChain.doFilter(requestWrapper,servletResponse);
//        }
//    }
//
//    @Override
//    public void destroy() {
//
//    }
//}