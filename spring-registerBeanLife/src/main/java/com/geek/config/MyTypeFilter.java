package com.geek.config;

import org.springframework.core.io.Resource;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.TypeFilter;

import java.io.IOException;

/**
 * @Author Robert
 * @create 2021/5/13 11:00
 * @Version 1.0
 * @Description: 自定义Filter 规则
 */
public class MyTypeFilter implements TypeFilter {
//    实现的match方法 返回一个boolean 值
    /*
     * @param metadataReader the metadata reader for the target class
     * MetadataReader 读取到的当前正在扫描的类的扫描信息
     *
     * @param metadataReaderFactory a factory for obtaining metadata readers
     * for other classes (such as superclasses and interfaces)
     * MetadataReaderFactory 可以获取到其他任何类的信息
     */

    @Override
    public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
        //获取当前类注解信息
        AnnotationMetadata annotationMetadata = metadataReader.getAnnotationMetadata();
        //获取当前正在扫描的类的信息
        ClassMetadata classMetadata = metadataReader.getClassMetadata();
        //获取到当前扫描类的类名
        String className = classMetadata.getClassName();
        System.out.println("--->"+className);
        //获取当前类资源信息（类的路径）
        Resource resource = metadataReader.getResource();
        //如果包含类名包含er 就扫描进去
        if (className.contains("er")) return true;
        return false;
    }
}
