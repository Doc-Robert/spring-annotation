package com.geek.aop;

import org.aspectj.lang.annotation.*;

/**
 * @Author Robert
 * @create 2021/5/23 11:29
 * @Version 1.0
 * @Description: aop 业务日志 切面类
 * -@Aspect注解告诉spring 表示当前类为一个切面类
 */

@Aspect
public class LogAspects {

    //抽取公共切入点表达式
    @Pointcut("execution(public int com.geek.aop.MathCalculator.*(..))")
    public void pointcut(){ }

    //在业务运行之前 触发切入；切入点表达式（指定在哪个方法进行切入）（*(..)）
//    @Before(" public int com.geek.aop.MathCalculator.div(int,int)")//参数为 目标方法全类名 方法类型和返回值类型
    @Before("com.geek.aop.LogAspects.pointcut()")
    public void LogStart(){
        System.out.println("业务运行，参数列表为:{}");
    }

    //在业务运行结束后 触发 后置通知
    @After("pointcut()")
    public void LogEnd(){
        System.out.println("业务运行结束.");
    }

    //在业务运行正常返回结果
    @AfterReturning("pointcut()")
    public void LogReturn(){
        System.out.println("业务正常返回，运行结果:{}");
    }

    //在业务运行时异常 返回
    @AfterThrowing("pointcut()")
    public void LogException(){
        System.out.println("业务执行异常，异常信息为:{}");
    }
}
