package com.geek.config;

import com.geek.bean.Magic;
import org.springframework.context.annotation.*;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

/**
 * @Author Robert
 * @create 2021/5/12 14:36
 * @Version 1.0
 * @Description:
 */

@Configuration //配置类
@ComponentScan(value = "com.geek",includeFilters = {
//        @Filter(type = FilterType.ANNOTATION,classes = {Controller.class, Service.class}),//排除带有controller 和service 注解的类
//        @Filter(type = FilterType.ASSIGNABLE_TYPE,classes = {Repository.class}),//所有类型 不管子类父类 都会注入
        @Filter(type = FilterType.CUSTOM,classes = {MyTypeFilter.class})//指定自定义Filter规则
},useDefaultFilters = false) //扫描指定包
//excludeFilters = Filter[] 数组形式选择排除 type 为排除类型  FilterType.ANNOTATION 注解 指定扫描的时候排除哪些组件
//includeFilters = Filter[] 指定扫描的时候只包含哪些组件
//useDefaultFilters = false 默认扫描规则 默认为true 也就是全部扫描
//@ComponentScans(value = ) +s扫苗策略

//@Filter(type = FilterType.ANNOTATION 按照注解
//FilterType.ASSIGNABLE_TYPE 按照给定的类型
//FilterType.ASPECTJ 使用ASPECTJ 表达式
//FilterType.REGEX 正则表达式
//FilterType.CUSTOM 使用自定义规则
//Filter candidates using a given custom
//* {@link org.springframework.core.type.filter.TypeFilter} implementation. 给定的自定义规则必须是TypeFilter 的实现类
public class MainConfig {

//    只要标注了#@Controller @Service @Repository @Component 就会自动被spring扫描

//
//    @Bean(可以指定容器id)
    @Bean //给容器注册一个bean; 类型为返回值的类型 id 为默认方法名
    public Magic magic(){
        return new Magic("Reines",100);
    }

}
