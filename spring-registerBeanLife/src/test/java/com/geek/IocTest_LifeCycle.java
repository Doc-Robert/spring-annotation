package com.geek;

import com.geek.config.MainConfigOfLifeCycle;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @Author Robert
 * @create 2021/5/19 10:27
 * @Version 1.0
 * @Description: bean生命周期测试
 */

@SpringBootTest
public class IocTest_LifeCycle {


    @Test
     void lifeCycleTest(){
        //容器创建
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(MainConfigOfLifeCycle.class);
        //创建单实例的bean
        //容器创建后 调用对象构造方法和 初始化方法
        System.out.println("容器创建完成");
        //容器关闭 并调用销毁方法
        applicationContext.close();
    }
    //生命周期测试 @bean注解方式
    /* 测试结果
        Real Constructor....
        Real init method
        容器创建完成
        Real destroy method
     */
    //生命周期 ，实现接口方式
    /*
        Lie constructor
        Lie afterPropertiesSet...
        容器创建完成
        Lie destroy...
     */

}
