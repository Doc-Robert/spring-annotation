package com.geek.condition;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

import java.util.function.Predicate;

/**
 * @Author Robert
 * @create 2021/5/17 15:59
 * @Version 1.0
 * @Description: import 导入选择器
 */
//自定义编辑返回需要导入的组件
public class MyImportSelector implements ImportSelector{
    //返回值为要返回的组件全类名 数组
    //AnnotationMetadata :当前
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        //importingClassMetadata
        //方法不能返回null值
        return new String[]{"com.geek.bean.Star"};
    }

    @Override
    public Predicate<String> getExclusionFilter() {
        return null;
    }
}
