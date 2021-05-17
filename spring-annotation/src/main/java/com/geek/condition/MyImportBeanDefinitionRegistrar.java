package com.geek.condition;

import com.geek.bean.School;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @Author Robert
 * @create 2021/5/17 16:30
 * @Version 1.0
 * @Description:
 */
public class MyImportBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {


    /**
     * AnnotationMetadata 当前类的注解信息
     * BeanDefinitionRegistry bean定义的注册类
     *         把所有要添加到容器中的bean，调用 registry.registerBeanDefinition();方法手动注册
     * @param importingClassMetadata
     * @param registry
     */
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {

        //指定bean定义类型
        RootBeanDefinition beanDefinition = new RootBeanDefinition(School.class);
        //注册一个bean，指定bean名 手动注册
        registry.registerBeanDefinition("school",beanDefinition);
    }
}
