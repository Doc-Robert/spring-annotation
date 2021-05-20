package com.geek.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 * @Author Robert
 * @create 2021/5/19 14:09
 * @Version 1.0
 * @Description:  后置处理器 初始化前后进行处理
 * 将后置处理器加入到容器中 
 */

@Component
public class MyBeanPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("postProcessBeforeInitialization。。。"+beanName+"->"+bean);
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("postProcessAfterInitialization。。。"+beanName+"->"+bean);
        return bean;
    }
}
