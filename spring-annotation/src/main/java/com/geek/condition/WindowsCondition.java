package com.geek.condition;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * @Author Robert
 * @create 2021/5/17 14:57
 * @Version 1.0
 * @Description: 判断 环境windows
 */
public class WindowsCondition implements Condition {

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        Environment environment = context.getEnvironment();
        String property = environment.getProperty("os.name");
//        System.out.println(property);
        if (!property.contains("Linux")){
            return true;
        }
        return false;
    }
}
