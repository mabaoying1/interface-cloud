package com.bsoft.config.datasource;


import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = "com.bsoft.mapper.identity", sqlSessionFactoryRef = "db2SqlSessionFactory")
public class DataSourceConfigPortal {
    // 将这个对象放入Spring容器中
    @Bean(name = "db2DataSource")
    // 表示这个数据源是默认数据源

    // 读取application.properties中的配置参数映射成为一个对象
    // prefix表示参数的前缀
    @ConfigurationProperties(prefix = "spring.datasource.identity")
    public DataSource getDateSource2() {
        return DataSourceBuilder.create().build();
    }
    @Bean(name = "db2SqlSessionFactory")
    // 表示这个数据源是默认数据源

    // @Qualifier表示查找Spring容器中名字为test1DataSource的对象
    public SqlSessionFactory db2SqlSessionFactory(@Qualifier("db2DataSource") DataSource datasource)
            throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(datasource);
        bean.setMapperLocations(
                // 设置mybatis的xml所在位置
                new PathMatchingResourcePatternResolver().getResources("classpath*:mapping/identity/*.xml"));
        bean.setConfigLocation(new ClassPathResource("mybatis-config.xml"));

        return bean.getObject();
    }
    @Bean("db2SqlSessionTemplate")
    // 表示这个数据源是默认数据源

    public SqlSessionTemplate db2sqlsessiontemplate(
            @Qualifier("db2SqlSessionFactory") SqlSessionFactory sessionfactory) {
        return new SqlSessionTemplate(sessionfactory);
    }

}
