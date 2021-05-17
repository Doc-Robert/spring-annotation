# Spring 注解驱动 ADD

![image-20210512154414405](spring-annotation.assets/image-20210512154414405.png)

# 第一章 容器 

## 1.1组件注册

### 注解开发

> 导入spring所需依赖

> spring原生xml bean注册

![image-20210512154556551](spring-annotation.assets/image-20210512154556551.png)



> 使用配置类 代替xml

MainConfig

```java
@Configuration //配置类
public class MainConfig {

//
//    @Bean(可以指定容器id)
    @Bean //给容器注册一个bean; 类型为返回值的类型 id 为默认方法名
    public Magic magic(){
        return new Magic("Reines",100);
    }

}
```

Magic

```java
public class Magic {

    private String name;

    private int level;

    public Magic(String name, int level) {
        this.name = name;
        this.level = level;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setLevel(int level) {
        this.level = level;
    }
    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }

    @Override
    public String toString() {
        return "Magic{" +
                "name='" + name + '\'' +
                ", level=" + level +
                '}';
    }
}
```

> 替代测试
>
> ```java
> @Test
> void contextLoads() {
> 
>     AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(MainConfig.class);
>     Magic magic = applicationContext.getBean(Magic.class);
>     System.out.println(magic);
> }
> ```

结果：

~~~java
Magic{name='Reines', level=100}
~~~



#### (1)@Bean

- 概念：在容器中配置一个 bean 实例
- 使用
  - 类型为返回值类型，id 默认为方法名
  - 可以指定**注解的 value 值** 为对应的 id
- 标注的方法中使用的参数都会从 IOC 容器中获取

#### (2)@Configuration

- 概念：告诉spring 这是一个配置类

#### (3)@ComponentScan

- 概念：扫描规则 开启组件扫描

- 使用

  - **value**: 扫描 指定包下

    > ```
    > value = "com.geek"
    > ```

  - **includeFilters**： 指定扫描的时候只包含哪些组件

    - @Filter 配置过滤的条件

      - type：过滤规则，值为 FilterType 枚举类实例

        | ANNOTATION      | 根据注解来排除                                               |
        | --------------- | ------------------------------------------------------------ |
        | ASSIGNABLE_TYPE | 根据类类型来排除                                             |
        | ASPECTJ         | 根据AspectJ表达式来排除                                      |
        | REGEX           | 根据正则表达式来排除                                         |
        | CUSTOM          | 自定义FilterClass排除，需要实现`org.springframework.core.type.filter.TypeFilter`接口 |

      - classes：对应的实例类型

    > ```
    > @Filter(type = FilterType.ANNOTATION,classes = {Controller.class, Service.class})//排除或包含带有controller 和service 注解的类
    > ```

  -  **excludeFilters** ：指定扫描的时候排除哪些组件
    - @Filter 配置过滤的条件 与**includeFilters**条件配置相同

  -  useDefaultFilters： 默认自动扫描规则

    默认为全部扫描 开启为true

    ```
    useDefaultFilters = false
    ```

> ##### 扩展

**自定义TypeFilter指定过滤规则**

创建MyTypeFilter 实现TypeFilter 接口

```java
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
```

使用：

```
@Filter(type = FilterType.CUSTOM,classes = {MyTypeFilter.class})//指定自定义Filter规则
public class MainConfig {
...
}
```

> @Filter  (type = FilterType.CUSTOM 中指定自定义注解类 class = {MyTypeFilter.class}

