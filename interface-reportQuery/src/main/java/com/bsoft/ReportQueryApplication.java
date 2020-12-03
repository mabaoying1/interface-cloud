package com.bsoft;

import com.github.pagehelper.autoconfigure.PageHelperAutoConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication(exclude = {PageHelperAutoConfiguration.class,  MongoAutoConfiguration.class, MongoDataAutoConfiguration.class,DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
@ServletComponentScan(value = {"com.bsoft.config.filter"}) //注册过滤器注解
@MapperScan({"com.bsoft.mapper.lis","com.bsoft.mapper.pacs,com.bsoft.mapper.business,com.bsoft.mapper.identity"})
public class ReportQueryApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReportQueryApplication.class, args);
    }

}
