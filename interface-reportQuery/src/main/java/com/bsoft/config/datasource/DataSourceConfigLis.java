package com.bsoft.config.datasource;


import com.atomikos.jdbc.AtomikosDataSourceBean;
import com.github.pagehelper.PageInterceptor;
import com.microsoft.sqlserver.jdbc.SQLServerXADataSource;
import oracle.jdbc.xa.client.OracleXADataSource;
import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.mapping.VendorDatabaseIdProvider;
import org.apache.ibatis.plugin.Interceptor;
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
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Properties;

@Configuration
@MapperScan(basePackages = "com.bsoft.mapper.lis", sqlSessionFactoryRef = "db3SqlSessionFactory")
public class DataSourceConfigLis {
    // 将这个对象放入Spring容器中
    @Bean(name = "db3DataSource")
    // 表示这个数据源是默认数据源

    // 读取application.properties中的配置参数映射成为一个对象
    // prefix表示参数的前缀
    public DataSource getDateSource2(DbLisConfig dbBusinessConfig) throws SQLException {

        OracleXADataSource oracleXaDataSource = new OracleXADataSource();

        oracleXaDataSource.setURL(dbBusinessConfig.getUrl());
        oracleXaDataSource.setPassword(dbBusinessConfig.getPassword());
        oracleXaDataSource.setUser(dbBusinessConfig.getUsername());

        AtomikosDataSourceBean xaDataSource = new AtomikosDataSourceBean();
        xaDataSource.setXaDataSource(oracleXaDataSource);
        xaDataSource.setUniqueResourceName("getDateSource3");

        xaDataSource.setMinPoolSize(dbBusinessConfig.getMinPoolSize());
        xaDataSource.setMaxPoolSize(dbBusinessConfig.getMaxPoolSize());
        xaDataSource.setMaxLifetime(dbBusinessConfig.getMaxLifetime());
        xaDataSource.setBorrowConnectionTimeout(dbBusinessConfig.getBorrowConnectionTimeout());
        xaDataSource.setLoginTimeout(dbBusinessConfig.getLoginTimeout());
        xaDataSource.setMaintenanceInterval(dbBusinessConfig.getMaintenanceInterval());
        xaDataSource.setMaxIdleTime(dbBusinessConfig.getMaxIdleTime());
        xaDataSource.setTestQuery(dbBusinessConfig.getTestQuery());
        return xaDataSource;
    }
    @Bean(name = "db3SqlSessionFactory")
    // 表示这个数据源是默认数据源

    // @Qualifier表示查找Spring容器中名字为test1DataSource的对象
    public SqlSessionFactory db3SqlSessionFactory(@Qualifier("db3DataSource") DataSource datasource)
            throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(datasource);
        bean.setMapperLocations(
                // 设置mybatis的xml所在位置
                new PathMatchingResourcePatternResolver().getResources("classpath*:mapping/lis/*.xml"));
        bean.setConfigLocation(new ClassPathResource("mybatis-config.xml"));

        bean.setDatabaseIdProvider(databaseIdProvider());
        Interceptor interceptor = new PageInterceptor();
        Properties properties = new Properties();
        properties.setProperty("helperDialect", "oracle");//Oracle数据库时设置为oracle
        properties.setProperty("reasonable", "true");
        interceptor.setProperties(properties);
        bean.setPlugins(new Interceptor[] {interceptor});

        return bean.getObject();
    }
    @Bean("db3SqlSessionTemplate")
    // 表示这个数据源是默认数据源

    public SqlSessionTemplate db3sqlsessiontemplate(
            @Qualifier("db3SqlSessionFactory") SqlSessionFactory sessionfactory) {
        return new SqlSessionTemplate(sessionfactory);
    }


    @Bean
    public DatabaseIdProvider databaseIdProvider(){
        DatabaseIdProvider databaseIdProvider = new VendorDatabaseIdProvider();
        Properties p = new Properties();
        p.setProperty("Oracle", "oracle");
        databaseIdProvider.setProperties(p);
        return databaseIdProvider;
    }

}
