package com.geek;

import com.geek.config.MainConfigOfProfile;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.sql.DataSource;

/**
 * @Author Robert
 * @create 2021/5/21 9:45
 * @Version 1.0
 * @Description:
 */
@SpringBootTest
public class TestProfile {
    //激活环境
    //使用命令行动态参数：在虚拟机参数位置加载 -Dspring.profiles.active=test
    //也可以用代码方式
    @Test
    void profileTest(){
        AnnotationConfigApplicationContext applicationContext= new AnnotationConfigApplicationContext(MainConfigOfProfile.class);
        //1.创建applicationContext
        //2，设置需要激活的环境
        applicationContext.getEnvironment().setActiveProfiles("Test","Dev");
        //3.注册配置类
        applicationContext.register(MainConfigOfProfile.class);
        //4.启动刷新容器
//        applicationContext.refresh();
        String[] beanNamesForType = applicationContext.getBeanNamesForType(DataSource.class);
        for (String name : beanNamesForType) {
            System.out.println(name);
        }

    }

}
