package com.geek.bean;

import org.springframework.beans.factory.FactoryBean;

/**
 * @Author Robert
 * @create 2021/5/18 14:40
 * @Version 1.0
 * @Description:
 */
public class SchoolFactoryBean implements FactoryBean<School> {
    @Override
    public School getObject() throws Exception {
        return new School();
    }

    @Override
    public Class<?> getObjectType() {
        return School.class;
    }

    //控制是否是单实例
    //true 单实例 在容器中保存一份
    //false 多实例 每次获取会创建一个新的bean
    @Override
    public boolean isSingleton() {
        return false;
    }
}
