package com.bsoft;

import com.github.pagehelper.autoconfigure.PageHelperAutoConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(exclude = {PageHelperAutoConfiguration.class,MongoAutoConfiguration.class, MongoDataAutoConfiguration.class})
@EnableEurekaClient
@EnableDiscoveryClient
@EnableCircuitBreaker
@ServletComponentScan(value = {"com.bsoft.config.filter"}) //注册过滤器注解
@MapperScan({"com.bsoft.mapper.business","com.bsoft.mapper.identity"})
@EnableTransactionManagement
@EnableConfigurationProperties
public class HospitalizationApplication {

    public static void main(String[] args) {

        System.out.println("11111111111111");
        System.out.println("2222222222222222");
        System.out.println("333333333333333");
        System.out.println("444444444444");
        SpringApplication.run(HospitalizationApplication.class, args);
    }

}
