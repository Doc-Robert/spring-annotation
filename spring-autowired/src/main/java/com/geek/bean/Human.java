package com.geek.bean;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EmbeddedValueResolverAware;
import org.springframework.stereotype.Component;
import org.springframework.util.StringValueResolver;

/**
 * @Author Robert
 * @create 2021/5/20 15:57
 * @Version 1.0
 * @Description: Aware注入 实现ApplicationContextAware 测试
 */

@Component
public class Human implements ApplicationContextAware, BeanNameAware, EmbeddedValueResolverAware {

    private ApplicationContext applicationContext;

    //ApplicationContextAware 接口 的setcontext方法
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        System.out.println("传入的IOC："+applicationContext);
        this.applicationContext = applicationContext;
    }

    //ioc 创建对象的bean的名字
    @Override
    public void setBeanName(String name) {
        System.out.println("当前bean的名字："+name);

    }
    //解析string中的占位符
    @Override
    public void setEmbeddedValueResolver(StringValueResolver resolver) {
        String resolveStringValue = resolver.resolveStringValue("你好${os.name}");
        System.out.println("解析的字符串"+resolveStringValue);
    }
}
