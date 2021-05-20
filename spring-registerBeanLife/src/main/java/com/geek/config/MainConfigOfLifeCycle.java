package com.geek.config;

import com.geek.bean.Real;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @Author Robert
 * @create 2021/5/19 10:13
 * @Version 1.0
 * @Description: spring bean 生命周期
 *
 *  --bean的生命周期：
 *          bean创建 --- 初始化 --- 销毁的过程
 *  容器管理bean的生命周期
 *     可以自定义初始化和销毁方法：容器在bean进行到当前2生命周期的时候
 *  来调用我们自定义初始化和销毁方法
 *
 *  构造（创建对象）
 *      单实例：在容器启动的时候创建对象
 *      多实例；在每次获取的时候才创建对象
 *  初始化
 *      在对象创建后 并赋值完成，调用初始化方法
 *  销毁
 *      单实例：在容器关闭的时候 调用销毁方法
 *      多实例：容器并不会管理这个bean 因为多实例只在获取它时才进行创建，容器不会调用销毁方法 可以手动调用
 *
 *  (1)指定初始化和销毁方法
 *      通过@Bean注解
 *      xml内相当于指定
 *  init-method="" destroy-method=""
 * @see Bean
 *
 *  (2)可以通过让Bean 实现InitializingBean接口 定义初始化逻辑
 *                  实现DisposableBean接口 定义销毁逻辑
 * @see org.springframework.beans.factory.InitializingBean
 * @see org.springframework.beans.factory.DisposableBean
 *
 *  (3)可以使用JSR250
 *      @PostConstruct :在bean创建完成并且属性赋值完成；来执行初始化方法
 *      @PreDestory : 在容器销毁bean之前 通知我们进行清理工作
 * @see javax.annotation.PostConstruct
 * @see javax.annotation.PreDestroy
 *
 *  (4)BeanPostProcessor [interface]: bean的后置处理器
 *      在bean初始化前后进行一些处理工作
 *      postProcessBeforeInitialization： 初始化之前进行工作
 *      postProcessAfterInitialization： 初始化之后进行工作
 * @see org.springframework.beans.factory.config.BeanPostProcessor
 * @see org.springframework.beans.factory.config.BeanPostProcessor#postProcessBeforeInitialization(Object, String)
 * @see org.springframework.beans.factory.config.BeanPostProcessor#postProcessAfterInitialization(Object, String)
 *
 *   BeanPostProcessor原理：
 *
 *   断点进入bean初始化
 *   以下部分都是在populateBean(beanName, mbd, instanceWrapper)执行后执行的; // 给bean进行属性赋值
 *   initializeBean
 *   {
 *      applyBeanPostProcessorsBeforeInitialization(wrappedBean, beanName); 初始化之前
 *      先遍历的得到所有的getBeanPostProcessors然后挨个执行BeforeInitialization
 *      一旦返回null，跳出for循环 不会执行后面的BeanPostProcessors
 *
 *      invokeInitMethods(beanName, wrappedBean, mbd); 执行自定义初始化
 *      applyBeanPostProcessorsAfterInitialization(wrappedBean, beanName);初始化之后
 *   }
 *
 *  扩展：
 *      spring底层对BeanPostProcessor的使用：
 *          bean赋值，注入其他组件，@Autowired ,生命周期注解功能，@Async
 */

@ComponentScan("com.geek.bean")
@Configuration
public class MainConfigOfLifeCycle {


    //注入bean   Real
//    @Scope("prototype") 多实例情况下
    @Bean(initMethod = "init",destroyMethod = "destroy")//可以指定 初始化方法 和销毁方法
    public Real real(){
        return new Real();
    }


}
