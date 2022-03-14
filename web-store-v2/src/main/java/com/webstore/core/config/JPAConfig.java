package com.webstore.core.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.Properties;

/**
 * Created by DVaschenko on 15.06.2016.
 */
@Configuration
@PropertySource({"classpath:property/database.properties", "classpath:property/other.properties"})
@ComponentScan("com.webstore.core")
@EnableTransactionManagement
public class JPAConfig {

    private final String DRIVER_CLASS = "connection.driver_class";
    private final String URL = "connection.url";
    private final String USER_NAME = "connection.username";
    private final String PASSWORD = "connection.password";
    private final String HB_DIALECT = "hibernate.dialect";
    private final String HB_AUTO = "hibernate.hbm2ddl.auto";
    private final String HB_SHOW_SQL = "hibernate.show_sql";
    private final String HB_FORMAT_SQL = "hibernate.format_sql";
    private final String HB_USE_SQL_COMMENTS = "hibernate.use_sql_comments";

    @Resource
    private Environment environment;

    private Properties getProperties(){
        Properties properties = new Properties();
        properties.put(HB_DIALECT, environment.getProperty(HB_DIALECT));
        properties.put(HB_AUTO, environment.getProperty(HB_AUTO));
        properties.put(HB_SHOW_SQL, environment.getProperty(HB_SHOW_SQL));
        properties.put(HB_FORMAT_SQL, environment.getProperty(HB_FORMAT_SQL));
        properties.put(HB_USE_SQL_COMMENTS, environment.getProperty(HB_USE_SQL_COMMENTS));
        properties.put("hibernate.event.merge.entity_copy_observer", "allow");

        return properties;
    }

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource source = new DriverManagerDataSource();
        source.setDriverClassName(environment.getProperty(DRIVER_CLASS));
        source.setUrl(environment.getProperty(URL));
        source.setUsername(environment.getProperty(USER_NAME));
        source.setPassword(environment.getProperty(PASSWORD));
        return source;
    }

    @Bean
    public JpaVendorAdapter hibernateJpaVendorAdapter(){
        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        return vendorAdapter;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean(){
        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setDataSource(dataSource());
        factoryBean.setJpaProperties(getProperties());
        factoryBean.setJpaVendorAdapter(hibernateJpaVendorAdapter());

        factoryBean.setPackagesToScan("com.webstore.core");

        return factoryBean;
    }

    @Bean
    public JpaTransactionManager transactionManager(){
        JpaTransactionManager manager = new JpaTransactionManager();
        manager.setEntityManagerFactory(entityManagerFactoryBean().getObject());
        return manager;
    }
}
