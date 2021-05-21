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

    AnnotationConfigApplicationContext applicationContext= new AnnotationConfigApplicationContext(MainConfigOfProfile.class);

    @Test
    void profileTest(){
        String[] beanNamesForType = applicationContext.getBeanNamesForType(DataSource.class);
        for (String name : beanNamesForType) {
            System.out.println(name);
        }

    }

}
