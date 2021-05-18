package com.geek.config;

import com.geek.bean.Magic;
import com.geek.bean.Planet;
import com.geek.bean.SchoolFactoryBean;
import com.geek.condition.LinuxCondition;
import com.geek.condition.MyImportBeanDefinitionRegistrar;
import com.geek.condition.MyImportSelector;
import com.geek.condition.WindowsCondition;
import org.springframework.context.annotation.*;
import org.springframework.context.annotation.ComponentScan.Filter;

/**
 * @Author Robert
 * @create 2021/5/12 14:36
 * @Version 1.0
 * @Description:
 */
@Conditional(WindowsCondition.class)//满足当前条件这个类才生效
@Configuration //配置类
//导入组件 id默认为组件全类名 {多个类}  Class<?>[] value();
@Import({MyImportSelector.class,Planet.class, MyImportBeanDefinitionRegistrar.class})
//三种方式导入
public class MainConfig2 {


    /*@Scope：调整作用域
     *
     * @see ConfigurableBeanFactory#SCOPE_PROTOTYPE
     * @see ConfigurableBeanFactory#SCOPE_SINGLETON
     * @see org.springframework.web.context.WebApplicationContext#SCOPE_REQUEST
     * @see org.springframework.web.context.WebApplicationContext#SCOPE_SESSION
     * @return
     *
     * SCOPE_PROTOTYPE 多实例 多实例情况下 ioc容器启动时不会去调用方法创建对象放在容器中
     *                      而是每次获取时才会调用方法 创建对象
     * SCOPE_SINGLETON 单实例（默认） ioc容器启动时会调用方法创建对象放到ioc容器中
     *                      获取时直接从容器（map.get()）中拿
     * SCOPE_REQUEST 同一次请求创建一次实例
     * SCOPE_SESSION 同一次session 创建一次实例
     */

    /*
        @Lazy 懒加载
                在单实例时 默认是启动容器并调用方法创建对象
                懒加载：容器启动创建对象 只在第一次使用它时（获取）bean创建对象
     */
//    @Scope("prototype")//设置多实例 作用域
//    @Lazy

    @Bean("Magic")//设置bean id
    public Magic magic(){
        System.out.println("容器创建对象magic");
        return new Magic("shine",777);
    }

    /*
     * @Conditional 条件注册bean
     *     -按照一定条件进行判断，满足条件给容器注册bean
     */
//    @Conditional(WindowsCondition.class)
    @Bean("magic01")
    public Magic addNewMagic(){
        System.out.println("容器创建对象litchi");
        return new Magic("litchi",010);
    }

    /**
     * 容器注册组件：
     *  -1.包扫描+组件注解标注 （@Controller @Service @Repository @Component）
     *  -2.第三方导入的组件 @Bean
     *  -3.@Import快速给容器导入一个组件
     *      (1).@Import(要导入到容器的组件)：容器自动注册这个组件 id默认为组件的全类名
     *      (2).ImportSelector:返回需要导入的组件的全类名数组
     *      (3).ImportBeanDefinitionRegistrar：手动注册bean到容器
     *  -4.使用Spring提供的 @FactoryBean 工厂bean
     *      (1).使用 默认获取到的是工厂bean调用getObject方法所创建的对象
     *      (2).需要获取该bean本身 可以在bean id前添加修饰符 & 这样就可以获取到其本身
     *
     * @see SchoolFactoryBean#getObject()
     * @see org.springframework.beans.factory.BeanFactory#FACTORY_BEAN_PREFIX
     * @see org.springframework.beans.factory.FactoryBean
     */
    @Bean //注入bean容器    实际获取的都是School对象
    public SchoolFactoryBean schoolFactoryBean(){
        return new SchoolFactoryBean();
    }

}
