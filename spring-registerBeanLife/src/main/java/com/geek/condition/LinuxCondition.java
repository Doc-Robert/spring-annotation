package com.geek.condition;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * @Author Robert
 * @create 2021/5/17 14:28
 * @Version 1.0
 * @Description: 判断当前环境
 */

//实现条件Condition 接口
public class LinuxCondition implements Condition {

    /**
     * ConditionContext：判断条件能使用的上下文（环境）
     * AnnotatedTypeMetadata：注释的信息
     * @param context
     * @param metadata
     * @return
     */
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        //判断环境是否为linux系统
        //  context上下文
        //1.可以获取到当前ioc容器的beanFactory工厂
        ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();
        //2.获取当前类加载器
        ClassLoader classLoader = context.getClassLoader();
        //3.获取当前系统环境信息
        Environment environment = context.getEnvironment();
        //4.获取到bean定义的注册类
        BeanDefinitionRegistry registry = context.getRegistry();

        //获取系统信息
        String property = environment.getProperty("os.name");

        //判断容器是否包含某个bean
        boolean definition = registry.containsBeanDefinition("Magic");
        //property中不包含window就可以通过
        if(!property.contains("Windows")) {
            return true;
        }
        return false;
    }
}
