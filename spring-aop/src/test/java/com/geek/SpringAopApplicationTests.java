package com.geek;

import com.geek.aop.MathCalculator;
import com.geek.config.MainConfigOfAop;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@SpringBootTest
class SpringAopApplicationTests {

    /**
     * Aop 注解版测试
     */
    @Test
    void contextLoadsAopTest() {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(MainConfigOfAop.class);

        //注意： 不要自己创建对象
//        MathCalculator mathCalculator = new MathCalculator();
//        mathCalculator.div();
        //从容器中获取
        MathCalculator mathCalculator = applicationContext.getBean(MathCalculator.class);
        mathCalculator.div(1,1);
    }

}
