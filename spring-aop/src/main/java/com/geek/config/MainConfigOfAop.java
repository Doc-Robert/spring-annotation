package com.geek.config;

import com.geek.aop.LogAspects;
import com.geek.aop.MathCalculator;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @Author Robert
 * @create 2021/5/23 11:18
 * @Version 1.0
 * @Description: AOP: Config配置  动态代理
 * Aop指在程序运行期间动态的将某段代码切入到指定的方法指定位置运行的编程方式；
 *
 * 使用：
 * 1、导入Aop模块 spring-aspects 面向切面 包
 * 2、定义一个业务逻辑类（MathCalculator）;在业务运行时将日志打印（方法之前，方法结束，方法出现异常时）
 * @see com.geek.aop.LogAspects
 * @see com.geek.aop.MathCalculator
 *
 * 3、定义日志切面类（LogAspects）;切面类里的方法需要动态感知MathCalculator#div方法的运行 然后对应执行
 *     切面的通知方法：
 *          -前置通知(@Before)：LogStart 在目标方法运行之前运行
 *          -后置通知(@After)：LogEnd 在目标方法运行结束后 运行 （无论方法执行是否正常，都会调用）
 *          -返回通知(@AfterReturning)：LogReturn 在目标方法正常返回之后运行
 *          -异常通知(@AfterThrowing)：LogException 目标方法出现异常后运行
 *          -环绕通知(@Around)：动态代理，手动推进目标方法运行（joinPoint.procced()）
 *
 * @see org.aspectj.lang.annotation.Before
 * @see org.aspectj.lang.annotation.After
 * @see org.aspectj.lang.annotation.AfterReturning
 * @see org.aspectj.lang.annotation.AfterThrowing
 * @see org.aspectj.lang.annotation.Around
 *
 * 4、给切面类的目标方法标注何时何地运行（通知注解）
 * 5、将切面类 和业务逻辑类(目标方法的所在类) 都加入到spring容器中
 * 6、必须告诉spring 哪个是切面类（给切面类增加注解@Aspect）
 * @see org.aspectj.lang.annotation.Aspect
 *
 * 7、【关键】给配置类 增加 @EnableAspectJAutoProxy 注解基于注解的Aop模式
 *      spring 中 的Enablexxx 注解功能都是开启某些功能
 *
 * 总结：
 *     1、将业务逻辑组件和切面类都加入到容器，并且需要告诉spring哪个是切面类@Aspect
 *     2、在切面类的通知方法上标注通知注解 ，并且告诉spring何时何运行（切入点表达式）
 *     3、必须开启基于注解的Aop模式@EnableAspectJAutoProxy
 * @see EnableAspectJAutoProxy
 * Aop原理：[给容器注册了什么组件，该组件什么时候工作，以及该组件的功能]
 *      1.@EnableAspectJAutoProxy是什么
 *          使用@Import(AspectJAutoProxyRegistrar.class)；给容器中导入AspectJAutoProxyRegistrar
 *          利用AspectJAutoProxyRegistrar自定义给容器注册bean
 *          然后调用AopConfigUtils.registerAspectJAnnotationAutoProxyCreatorIfNecessary(registry);//注册Aop注解自动代理创建器 如果需要的话
 *          会继续调用registerOrEscalateApcAsRequired(AnnotationAwareAspectJAutoProxyCreator.class, registry, source);//
 *          org.springframework.aop.config.internalAutoProxyCreator = class org.springframework.aop.aspectj.annotation.AnnotationAwareAspectJAutoProxyCreator
 *          注册/升级一个名为 xxx.internalAutoProxyCreator 的 bean 定义信息(启动的类型指定为 AnnotationAwareAspectJAutoProxyCreator)
 *          //
 *          最后其他选项拿到 EnableAspectJAutoProxy这个注解的信息proxyTargetClass 和 exposeProxy 是否为 true ；等进行其他一系列工作
 * @see org.springframework.aop.aspectj.annotation.AnnotationAwareAspectJAutoProxyCreator
 * @see  org.springframework.aop.aspectj.autoproxy.AspectJAwareAdvisorAutoProxyCreator
 *      2.AnnotationAwareAspectJAutoProxyCreator （1.中注册的该类型的组件）组件功能
 *          AnnotationAwareAspectJAutoProxyCreator extends AspectJAwareAdvisorAutoProxyCreator //父类·AspectJAwareAdvisorAutoProxyCreator
 *              AspectJAwareAdvisorAutoProxyCreator extends AbstractAdvisorAutoProxyCreator//父类 AbstractAdvisorAutoProxyCreator
 *                  AbstractAdvisorAutoProxyCreator extends AbstractAutoProxyCreator//父类 AbstractAutoProxyCreator 抽象的自动代理创建器
 *                      AbstractAutoProxyCreator extends ProxyProcessorSupport // 父类 ProxyProcessorSupport 代理处理器支持
 *                      并且实现了implements SmartInstantiationAwareBeanPostProcessor, BeanFactoryAware //SmartInstantiationAwareBeanPostProcessor的bean后置处理器
 *                      主要关注 bean的后置处理器 （bean 初始化完成前后所做的事情）、以及自动装配BeanFactory
 *
 *                      @see org.springframework.aop.framework.autoproxy.AbstractAutoProxyCreator#setBeanFactory(BeanFactory)
 *                      AbstractAutoProxyCreator.setBeanFactory();
 *                      AbstractAutoProxyCreator中 有后置处理器的逻辑
 *                      AbstractAdvisorAutoProxyCreator.setBeanFactory（抽象的通知自动代理创建器）
 *                          在该方法内部调用 initBeanFactory()
 *                      AnnotationAwareAspectJAutoProxyCreator.initBeanFactory
 *
 *      流程：
 *          （1）、传入配置类，创建ioc容器
 *          （2）、注册配置类，调用refresh()方法 刷新容器；
 *          （3）、registerBeanPostProcessors(beanFactory);创建bean的后置处理器来方便拦截bean的创建,
 *              (1).先获取ioc容器已经定义的了 需要创建对象的 BeanPostProcessor
 *              (2).并且给容器中添加别的beanPostProcessor
 *              //beanFactory.addBeanPostProcessor(new BeanPostProcessorChecker(beanFactory, beanProcessorTargetCount));
 *              (3).// First, register the BeanPostProcessors that implement PriorityOrdered.
 *                  优先注册实现 PriorityOrdered接口的 beanPostProcessor；
 *                  // Next, register the BeanPostProcessors that implement Ordered.
 *                  然后再来注册实现 Ordered接口的 beanPostProcessor
 *                  // Now, register all regular BeanPostProcessors.
 *                  现在 再来注册所有没实现优先级接口的 BeanPostProcessors
 *              (4).注册BeanPostProcessors，实际上时创建BeanPostProcessors对象 保存于容器中
 *                  创建internalAutoProxyCreator 的 BeanPostProcessors [AnnotationAwareAspectJAutoProxyCreator] 类型 的 后置处理器
 *
 * @see org.springframework.context.support.PostProcessorRegistrationDelegate
 */
@EnableAspectJAutoProxy //同在配置文件中 开启 切面自动代理
@Configuration
public class MainConfigOfAop {

    //将业务逻辑类加入到容器中
    @Bean
    public MathCalculator mathCalculator(){
        return new MathCalculator();
    }

    //切面类 加入到容器中
    @Bean
    public LogAspects logAspects(){
        return new LogAspects();
    }
}
