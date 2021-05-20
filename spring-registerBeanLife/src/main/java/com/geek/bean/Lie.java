package com.geek.bean;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

/**
 * @Author Robert
 * @create 2021/5/19 10:49
 * @Version 1.0
 * @Description: 通过 实现InitializingBean  DisposableBean来实现bean的生命周期
 */

@Component
public class Lie implements InitializingBean, DisposableBean {

    public Lie() {
        System.out.println("Lie constructor");
    }

    //实现接口 得到的初始化 方法
    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("Lie afterPropertiesSet...");
    }
    //销毁方法
    @Override
    public void destroy() throws Exception {
        System.out.println("Lie destroy...");
    }
}
