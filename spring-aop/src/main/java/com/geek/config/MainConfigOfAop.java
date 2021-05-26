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
 *                  FactoryBean.createBean() -> doCreateBean()：创建 bean 实例
 *                      1.创建bean的实例
 *                      2.调用 populateBean() 方法完成对 bean 实例的属性赋值
 *                          populateBean(beanName, mbd, instanceWrapper);
 *                      3.调用 initializeBean() 方法进行 bean 的初始化工作
 *                          初始化bean的流程：
 *                          1）invokeAwareMethods();进行判断 bean 实例是否实现某些 **Aware ** 接口，如果有就注册对应的组件
 *                          2）applyBeanPostProcessorsBeforeInitialization(wrappedBean, beanName);//应用后置处理器的postProcessBeforeInitialization（）初始化之前方法
 *                          3）invokeInitMethod(): 执行自定义的初始化方法
 *                          4）applyBeanPostProcessorsAfterInitialization 拿到所有的后置处理器 执行后置处理器的postProcessAfterInitialization方法
 *                          5）返回包装后的 bean 实例 - wrappedBean
 *                      4.以BeanPostProcessor(AnnotationAwareAspectJAutoProxyCreator)为例 创建成功
 *                      5.beanfactory.registerBeanPostProcessors()：将 bean 后置处理器注册到 beanfactory 中
 *
 * @see org.springframework.context.support.PostProcessorRegistrationDelegate
 *
 * =====以上为创建 AnnotationAwareAspectJAutoProxyCreator 的过程===== 后置处理器 BeanPostProcessor 的创建
 *           AnnotationAwareAspectJAutoProxyCreator 类型的 =>  InstantiationAwareBeanPostProcessor 的执行时机
 *           【BeanPostProcessor 是在Bean创建完成初始化前后调用】
 *           【InstantiationAwareBeanPostProcessor 是在创建Bean实例之前尝试用】
 *
 *          (4)、finishBeanFactoryInitialization(beanFactory); 完成对 BeanFactory 工厂的初始化工作，注册所有剩余的非延迟初始化单例 bean 实例
 *              (1).遍历获取容器中的所有Bean ，依次创建对象getBean(beanName);
 *              	//List<String> beanNames = new ArrayList<>(this.beanDefinitionNames);
 * 		            //for (String beanName : beanNames) {
 * 		            内部会调用 preInstantiateSingletons() 方法完成
 * 		                获取容器中所有 bean id 以及对于的定义信息对象
 * 		                调用 isFactoryBean() 判断其是否为 FactoryBean，如果不是 -> getBean() -> doGetBean()
 * 		                调用 getSingleton() 判断该 bean 是否存在于单例缓存(是否注册过)：如果存在就获取对应的 bean 实例，进行包装后返回
 * 		                调用 createBean() -> toCreateBean() 创建实例
 * 		                在实例化 bean 之前，会调用 resolveBeforeInstantiation()
 * 		                    -该方法的主要作用是，判断能否通过 BeanPostProcessor 返回一个目标 bean 的代理对象，而不是手动注册一个 bean
 * 		                    -调用 applyBeanPostProcessorsBeforeInstantiation() & applyBeanPostProcessorsAfterInitialization() 方法
 *                          判断是否支持为当前 bean 实例创建代理对象，如果代理对象不为 null，就返回该代理对象
 *                              //该方法中
 * 					            bean = applyBeanPostProcessorsBeforeInstantiation(targetType, beanName);
 * 					            if (bean != null) {
 * 						            bean = applyBeanPostProcessorsAfterInitialization(bean, beanName);
 *                              }
 *
 *                  AnnotationAwareAspectJAutoProxyCreator 作为 InstantiationAwareBeanPostProcessor 接口(该接口也是 BeanProcessor 接口的实现类)的实现类
 *                  有两个对应的主要方法，一个是 postProcessBeforeInstantiation(在创建实例之前执行)，另一个是 postProcessAfterInstantiation(在创建实例之后执行)
 *                  在创建 bean 实例之前，会调用这两个方法为符合规则的 bean 实例创建代理对象
 *
 *      AnnotationAwareAspectJAutoProxyCreator[InstantiationAwareBeanPostProcessor]的作用：
 *         1）、每一个bean创建之前，调用 postProcessBeforeInstantiation() 方法
 *                  目前到 MathCalculator 和 LogAspects 的bean创建注册
 *                  (1).判断 当前的Bean是否在AdvisedBeans中（保存了所有需要增强的bean；例如MathCalculator等 增强业务逻辑）
 *                  (2).判断当前bean是否是基础类型  boolean retVal = Advice || Pointcut || Advisor || AopInfrastructureBean这些接口 ；
 *                  或者 判断其是否为切面 标识了@Aspect注解
 *                  (3).后又判断是否需要跳过
 *                      1.先获取候选的增强器 也就是切面里的增强的通知方法 将其包装了【List<Advisor> candidateAdvisors】
 *                          且每一个封装的通知方法的增强器是InstantiationModelAwarePointCutAdvisor 类型
 *                          判断每一个增强器是否是AspectsPointcutAdvisor类型的
 *                      2.永远会返回false
 *        （2）、创建对象
 *          postProcessBeforeInstantiation
 *              当没能在创建 bean 实例前创建对应的代理对象时，会在bean 实例的初始化工作之后
 *              调用 AbstractAutoProxyCreator.postProcessAfterInitialization() -> wrapIfNecessary() - 在需要时进行包装
 *              (1).获取当前bean的所有增强器（通知方法）
 *                  1.找到候选增强器 （寻找那些通知方法是需要切入当前bean方法的）
 *                  2.获取到能够在当前bean使用的增强器
 *                  3.给增强器排序
 *              (2).保存当前bean在advisorBean中
 *              (3).如果当前bean需要增强，就创建当前bean的代理对象
 *                  1.获取到所有增强器（通知方法）
 *                  2.保存到代理工厂ProxyFactory中
 *                  3.用代理工厂创建代理对象 ，由spring决定
 *                      -1)JdkDynamicAopProxy(config)；  (jdk形式的动态代理)
 *                      -2)ObjenesisCglibAopProxy(config)； (Cglib形式的动态代理)
 *              (4).wrapIfNecessary方法最终给容器中返回了当前组件使用了 cglib增强了的代理对象
 *              (5).以后容器获取到的就是这个组件的代理对象，执行目标方法的时候，代理对象就会执行通知方法的流程
 *         （3）、目标方法的执行
 *               容器中保存了组件的代理对象（增强后的对象cglib），对象中保存了详细信息（比如增强器，目标对象，xxx）
 *               (1).CglibAopProxy.intercept(); 先拦截目标方法的执行
 *               (2).根据ProxyFactory对象获取将要执行目标方法拦截器链
 *                   List<Object> chain = this.advised.getInterceptorsAndDynamicInterceptionAdvice(method, targetClass);
 *                   1.会创建一个List<Object> interceptorList = new ArrayList<>(advisors.length);保存所有拦截器链
 *                   2.遍历所有的增强器，将其转换为Intercepter
 *                      registry
 *                   3.将增强器转为List<MethodInterceptor>
 *                       如果是MethodInterceptor 将其加入到 interceptorList集合中
 *                       如果不是，使用AdvisorAdapter 将增强器转换为 MethodInterceptor
 *                       转换完成返回MethodInterceptor 数组
 *
 *               (3).如果没有拦截器链，就会直接执行目标方法
 *               拦截器链（每一个通知方法又被包装为方法拦截器，利用 MethodInterceptor 机制）
 *               (4).如果有拦截器链，把需要执行的 目标对象，目标方法，拦截器链等信息传入 一个CglibMethodInvocation对象
 *                   并调用 retVal = new CglibMethodInvocation(proxy, target, method, args, targetClass, chain, methodProxy).proceed();
 *               (5).拦截器链的触发过程：

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
