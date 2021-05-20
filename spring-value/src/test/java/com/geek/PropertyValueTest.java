package com.geek;

import com.geek.config.MainConfigOfPropertyValue;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * @Author Robert
 * @create 2021/5/19 16:25
 * @Version 1.0
 * @Description:
 */
@SpringBootTest
public class PropertyValueTest {

    AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(MainConfigOfPropertyValue.class);

    @Test
    void testPropertyValue(){
        printBeans(applicationContext);
        System.out.println("==========================");
        System.out.println(applicationContext.getBean("person"));

        ConfigurableEnvironment environment = applicationContext.getEnvironment();
        String property = environment.getProperty("person.nickName");
        System.out.println(property);

        applicationContext.close();
    }

    public void printBeans(AnnotationConfigApplicationContext applicationContext){
        String[] definitionNames = applicationContext.getBeanDefinitionNames();
        for (String name : definitionNames) {
            System.out.println(name);
        }

    }
}
