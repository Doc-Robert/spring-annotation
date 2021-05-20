package com.geek.bean;

import org.springframework.beans.factory.annotation.Value;

/**
 * @Author Robert
 * @create 2021/5/19 16:23
 * @Version 1.0
 * @Description:
 */


public class Person {


    /**
     * 使用@Value注解赋值
     * 1、基本数值
     * 2、使用SpEl表达式：#{}    spring表达式
     * 3、使用${} 取出配置文件的值（在运行环境变量里面的值）
     */
    @Value("咕哒子")
    private String name;

    @Value("#{20-2}")
    private Integer age;

    @Value("${person.nickName}")//取出配置文件的值
    private String nickName;

    public String getName() {
        return name;
    }

    public Integer getAge() {
        return age;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", nickName='" + nickName + '\'' +
                '}';
    }

}
