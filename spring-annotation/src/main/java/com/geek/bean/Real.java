package com.geek.bean;

/**
 * @Author Robert
 * @create 2021/5/19 10:23
 * @Version 1.0
 * @Description: @bean实现 bean生命周期
 */
public class Real {

    public Real() {
        System.out.println("Real Constructor....");
    }

    public void init(){
        System.out.println("Real init method");
    }
    public void destroy(){
        System.out.println("Real destroy method");
    }
}
