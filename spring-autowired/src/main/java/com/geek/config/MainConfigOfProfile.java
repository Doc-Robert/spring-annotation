package com.geek.config;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.EmbeddedValueResolverAware;
import org.springframework.context.annotation.*;
import org.springframework.util.StringValueResolver;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;

/**
 * @Author Robert
 * @create 2021/5/21 9:09
 * @Version 1.0
 * @Description: Profile config @Profile注解 环境标识
 * Profile：
 *      是spring为我们提供可以根据当前环境，动态的激活和切换一系列组件的功能
 *      指定组件在哪一种环境下才能被注册到环境中，不指定，则任何环境下都能注册这个组件
 *      环境标识 只有环境被激活的时候 才会被激活
 *
 * @see org.springframework.context.annotation.Profile
 */

@PropertySource("classpath:/database.properties")
@Configuration
public class MainConfigOfProfile implements EmbeddedValueResolverAware {
    //以数据源 为例 c3p0
    //需要导入 c3p0 和mysql 依赖

    @Value("${db.user}")
    private String user;

    @Value("${db.password}")
    private String password;

    private StringValueResolver valueResolver;

    private String driverClass;

    //测试环境
    /**
     *
     * @param pwd //与前面的password一致 注解标注于参数
     * @return
     * @throws PropertyVetoException
     */
    @Profile("Test")
    @Bean("TestDataSource")
    public DataSource dataSourceTest(@Value("${db.password}") String pwd) throws PropertyVetoException {
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        dataSource.setUser(user);
        dataSource.setPassword(pwd);
        dataSource.setDriverClass(driverClass);
        dataSource.setJdbcUrl("jdbc:mysql://localhost:3306/spring");
        return dataSource;
    }
    //开发环境
    @Profile("Dev")
    @Bean("DevDataSource")
    public DataSource dataSourceDev() throws PropertyVetoException {
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        dataSource.setUser(user);
        dataSource.setPassword(password);
        dataSource.setDriverClass(driverClass);
        dataSource.setJdbcUrl("jdbc:mysql://localhost:3306/spring");
        return dataSource;
    }
    //生产环境
    @Profile("Prod")
    @Bean("ProdDataSource")
    public DataSource dataSourceProd() throws PropertyVetoException {
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        dataSource.setUser(user);
        dataSource.setPassword(password);
        dataSource.setJdbcUrl("jdbc:mysql://localhost:3306/spring");
        dataSource.setDriverClass(driverClass);
        return dataSource;
    }

    //实现EmbeddedValueResolverAware 接口 来设置 获取外部文件properties的值
    @Override
    public void setEmbeddedValueResolver(StringValueResolver resolver) {
        this.valueResolver = resolver;
        driverClass = valueResolver.resolveStringValue("${db.Driver}");
    }
}
