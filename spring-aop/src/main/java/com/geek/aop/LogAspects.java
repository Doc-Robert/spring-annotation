package com.geek.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;

import java.util.Arrays;

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
    public void LogStart(JoinPoint joinPoint){
        //获取参数列表
        Object[] args = joinPoint.getArgs();
        //joinPoint 获取切入 方法名
        //注意：JoinPoint 参数必须写在参数表 第一位置
        System.out.println(""+joinPoint.getSignature().getName()+"业务运行@Before，参数列表为:{"+ Arrays.asList(args) +"}");
    }

    //在业务运行结束后 触发 后置通知
    @After("pointcut()")
    public void LogEnd(JoinPoint joinPoint){
        System.out.println(""+ joinPoint.getSignature().getName()+"业务运行结束.@After");
    }

    //在业务运行正常返回结果
    @AfterReturning(value = "pointcut()",returning="result")//returning指定谁来封装返回值
    public void LogReturn(JoinPoint joinPoint, Object result){
        System.out.println(""+joinPoint.getSignature().getName()+"业务正常返回@AfterReturning，运行结果:{"+result+"}");
    }

    //在业务运行时异常 返回
    @AfterThrowing(value = "pointcut()",throwing = "exception")
    public void LogException(JoinPoint joinPoint, Exception exception){
        System.out.println(""+joinPoint.getSignature().getName()+"业务执行异常@AfterThrowing，异常信息为:{"+exception+"}");
    }
}
