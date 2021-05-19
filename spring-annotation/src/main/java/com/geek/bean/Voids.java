package com.geek.bean;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * @Author Robert
 * @create 2021/5/19 11:05
 * @Version 1.0
 * @Description:    第三种方式实现bean 生命周期 两个注解
 */

@Component//将其加入到容器 
public class Voids {


    public Voids() {
        System.out.println("Voids Constructor....");
    }

    //在对象创建 并赋值完成后调用
    @PostConstruct
    public void init(){
        System.out.println("Real @PostConstruct");
    }

    //在容器移除对象之前通知我们
    @PreDestroy
    public void destroy(){
        System.out.println("Real @PreDestroy");
    }

}
