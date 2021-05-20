package com.geek;

import com.geek.bean.Magic;
import com.geek.bean.Star;
import com.geek.config.MainConfig;
import com.geek.config.MainConfig2;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

import java.util.Map;

@SpringBootTest
class SpringAnnotationApplicationTests {


    //spring 注解驱动
    //@Bean  bean注册 @Configuration 配置类 @ComponentScan 组件扫描配置
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

    //bean 的作用范围（作用域）
    @Test
        void testScope(){
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(MainConfig2.class);

        System.out.println("已创建ioc容器");
        //多实例情况下测试 bean所调用的方法
        Object magic1 = applicationContext.getBean("Magic");
        Object magic2 = applicationContext.getBean("Magic");
        System.out.println(magic1 == magic2);

    }

    //懒加载
    @Test
    void testLazy(){
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(MainConfig2.class);
        System.out.println("已创建ioc容器");
        //单实例下 懒加载
        Object magic1 = applicationContext.getBean("Magic");
    }

    //条件注册bean
    @Test
    void conditionalTest(){
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(MainConfig2.class);

        //获取类型包含Magic.class 的bean
        String[] beanNamesForType = applicationContext.getBeanNamesForType(Magic.class);
        for (String name : beanNamesForType) {
            System.out.println(name);//
        }

        Map<String, Magic> magicMap = applicationContext.getBeansOfType(Magic.class);
        System.out.println(magicMap);//magicMap中包含已注册的bean

        ConfigurableEnvironment environment = applicationContext.getEnvironment();//获取当前容器环境
        String property = environment.getProperty("os.name");//打印当前环境名
        System.out.println(property);
    }

    @Test
    void testImport(){
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(MainConfig2.class);
        printBean(applicationContext);
        Star bean = applicationContext.getBean(Star.class);//容器中有star
        System.out.println(bean);//com.geek.bean.Star@64a1923a
    }

    public void printBean(AnnotationConfigApplicationContext applicationContext){
        String[] definitionNames = applicationContext.getBeanDefinitionNames();
        for (String name : definitionNames) {
            System.out.println(name);
        }
    }

    @Test
    void testFactoryBean(){

        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(MainConfig2.class);
        Object bean = applicationContext.getBean("&schoolFactoryBean"); //+ &符 这样获取的就不是School对象 而是SchoolFactoryBean
        //String FACTORY_BEAN_PREFIX = "&"; beanFactory 修饰符
        System.out.println(bean);

    }
}
