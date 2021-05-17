package com.geek;

import com.geek.bean.Magic;
import com.geek.config.MainConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@SpringBootTest
class SpringAnnotationApplicationTests {

    @Test
    void contextLoads() {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(MainConfig.class);
        Magic magic = applicationContext.getBean(Magic.class);
//        System.out.println(magic);
        String[] DefinitionNames = applicationContext.getBeanDefinitionNames();
        for (String name : DefinitionNames) {
            System.out.println(name);
        }
    }

}
