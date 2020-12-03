//package com.bsoft.config.interceptor;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
//
//@Configuration
//public class InterfaceInterceptorConfig extends WebMvcConfigurationSupport {
//
//    @Bean
//    public InterfaceInterceptor interfaceInterceptor(){
//        return new InterfaceInterceptor();
//    }
//
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(interfaceInterceptor()).addPathPatterns("/**");// .excludePathPatterns("/user/login");
//        super.addInterceptors(registry);
//    }
//}
