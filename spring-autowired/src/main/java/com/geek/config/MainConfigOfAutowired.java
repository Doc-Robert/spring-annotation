package com.geek.config;

import com.geek.dao.BookDao;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * @Author Robert
 * @create 2021/5/20 10:01
 * @Version 1.0
 * @Description:自动装配注解 config
 */

/**
 * *自动装配：*
 *      Spring利用依赖注入（DI）完成对各个组件的依赖关系赋值
 * 1、@Autowired 自动注入
 *      (1)、默认按照优先类型去对应的容器中找对应的组件 applicationContext.getBean(BookDao.class);
 *          BookService{
 *              @Autowired
 *              BookDao bookDao;
 *          }
 *      (2)、如果找到多个相同类型的组件 再将属性的名称作为属性id 去容器中查找  applicationContext.getBean(dookDao);
 *      (3)、使用@Qualifier("bookDao")  使用@Qualifier指定需要装配的di，而不是使用属性名
 *      (4)、自动装配默认一定要将属性赋值，没有则会报错
 *          可以使用@Autowired(required = false)    //装配是否必须 默认为true
 *      (5)、可以使用 @Primary 指定首选自动装配 让Spring自动装配的时候，默认使用首选的Bean
 *          也可以继续使用 @Qualifier 指定需要转配的bean的名字
 * 2、@Resource注解 （JSR250里规定）和 @Inject注解 （JSR330）
 *      -@Resource：
 *          可以和@Autowired一样实现自动装配，默认是按照组件名称进行装配
 *          不支持@Primary功能和@Autowired(required = false)功能
 *      -@Inject:
 *          使用需要导入依赖 javax.inject
 *          和@Autowired功能一致 但没有required = false
 * 区别：@Autowired是由spring定义的 而@Resource 和@Inject是java规范
 *
 * AutowiredAnnotationBeanPostProcessor:解析完成自动装配功能 ；
 *
 * 3、
 * -@Target({ElementType.CONSTRUCTOR, ElementType.METHOD, ElementType.PARAMETER, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
 * -@Aurowired： 可以标在 构造器 方法 参数 属性上;都是从容器中获取参数组件的值
 *      (1)、标注在方法上； @bean+方法参数，参数从容器中取获取；默认不写@autowired效果也一致 ，都能够自动装配
 *      @see com.geek.bean.Master
 *      (2)、标注在构造器上; 如果组件只有一个有参构造器 那么这个有参构造器的@Autowired 可以省略，参数位置的组件还是可以自动从容器中取
 *      @see com.geek.bean.Master
 *      (3)、放在参数位置
 *      @see com.geek.bean.Master
 *  4、自定义组件想要使用spring底层的一些组件（ApplicationContext，BeanFactroy）
 *     需自定义组件实现 XXXAware接口，在创建对象的时候，会调用接口规定的方法注入相关的组件；
 *     @see com.geek.bean.Human
 *     把spring底层的一些组件注入到自定义的Bean中；
 *     XXXAware 的功能是使用 XXXProcessor 进行处理的
 *     @see org.springframework.context.ApplicationContextAware => @see org.springframework.context.support.ApplicationContextAwareProcessor

 * @see org.springframework.beans.factory.annotation.Autowired
 * @see javax.annotation.Resource
 * @see javax.inject.Inject
 * @see Primary
 * @see org.springframework.beans.factory.annotation.Qualifier
 * @see org.springframework.beans.factory.Aware
 */
@Configuration
@ComponentScan({"com.geek.service","com.geek.dao","com.geek.bean","com.geek.controller"})
public class MainConfigOfAutowired {

//    @Primary
//    @Bean("bookDao2")

    public BookDao bookDao(){
        BookDao bookDao = new BookDao();
        bookDao.setLabel("2");
        return bookDao;
    }

}
