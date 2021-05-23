package com.geek.config;

import com.geek.aop.LogAspects;
import com.geek.aop.MathCalculator;
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
