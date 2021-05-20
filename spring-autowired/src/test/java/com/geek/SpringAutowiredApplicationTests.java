package com.geek;

import com.geek.bean.Master;
import com.geek.config.MainConfigOfAutowired;
import com.geek.dao.BookDao;
import com.geek.service.BookService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@SpringBootTest
public class SpringAutowiredApplicationTests {

    AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(MainConfigOfAutowired.class);

    @Test
    void testAutowired() {
        BookService bookService = applicationContext.getBean(BookService.class);
        System.out.println(bookService);
        Master master = applicationContext.getBean(Master.class);
        System.out.println(master);

        applicationContext.close();
    }

}
