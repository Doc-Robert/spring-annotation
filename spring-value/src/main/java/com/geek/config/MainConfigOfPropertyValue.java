package com.geek.config;

import com.geek.bean.Person;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

/**
 * @Author Robert
 * @create 2021/5/19 16:21
 * @Version 1.0
 * @Description:
 */

//String[] value();
//保存到运行的环境变量中
@PropertySource(value = {"classpath:/person.properties"})//读取外部配置文件 导入类路径 //使用${}取出配置文件的值
@Configuration
public class MainConfigOfPropertyValue {

    @Bean
    public Person person(){
        return new Person();
    }

}
