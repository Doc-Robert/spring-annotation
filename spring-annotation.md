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

#### (4)@Scope

- 概念：设置组件（bean）作用域

- 参数 可设置 默认为（单实例）

  -  prototype 多实例
  -  singleton 单实例 （默认）

  - request(不常用)：在同一次请求内创建一次
  - session(不常用)：在同一次会话内创建一次

- 测试 （多实例）

> ```java
> @Configuration //配置类
> public class MainConfig2 {
> 
> 
>     /*@Scope：调整作用域
>      *
>      * @see ConfigurableBeanFactory#SCOPE_PROTOTYPE
>      * @see ConfigurableBeanFactory#SCOPE_SINGLETON
>      * @see org.springframework.web.context.WebApplicationContext#SCOPE_REQUEST
>      * @see org.springframework.web.context.WebApplicationContext#SCOPE_SESSION
>      * @return
>      *
>      * SCOPE_PROTOTYPE 多实例 多实例情况下 ioc容器启动时不会去调用方法创建对象放在容器中
>      *                      而是每次获取时才会调用方法 创建对象
>      * SCOPE_SINGLETON 单实例（默认） ioc容器启动时会调用方法创建对象放到ioc容器中
>      *                      获取时直接从容器（map.get()）中拿
>      * SCOPE_REQUEST 同一次请求创建一次实例
>      * SCOPE_SESSION 同一次session 创建一次实例
>      */
>     @Scope("prototype")//设置多实例 作用域
>     @Bean("Magic")//设置bean id
>     public Magic magic(){
>         return new Magic("shine",777);
>     }
> 
> }
> 
> @Test
> void testScope(){
>     AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(MainConfig2.class);
> 
>     System.out.println("已创建ioc容器");
>     //多实例情况下测试 bean所调用的方法
>     Object magic1 = applicationContext.getBean("Magic");
>     Object magic2 = applicationContext.getBean("Magic");
>     System.out.println(magic1 == magic2); //false	
> 
> }
> ```

#### (5)@Lazy

- 概念：懒加载 

  ​	懒加载 - 针对于单实例使用，在第一次获取 bean 实例时创建对应的 bean 实例

#### (6)@Conditional

- 概念：按照一定条件进行判断，满足条件给容器注册bean

- 使用：

  - ```java
    Class<? extends Condition>[] value();
    ```

  参数为类名

- 测试：

  - 编写条件condition类实现condition接口

    ```java
    //实现条件Condition 接口
    public class LinuxCondition implements Condition {
        /**
     * ConditionContext：判断条件能使用的上下文（环境）
     * AnnotatedTypeMetadata：注释的信息
     * @param context
     * @param metadata
     * @return
     */
        @Override
        public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
            //判断环境是否为linux系统
            //  context上下文
            //1.可以获取到当前ioc容器的beanFactory工厂
            ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();
            //2.获取当前类加载器
            ClassLoader classLoader = context.getClassLoader();
            //3.获取当前系统环境信息
            Environment environment = context.getEnvironment();
            //4.获取到bean定义的注册类
            BeanDefinitionRegistry registry = context.getRegistry();
    
            //获取系统信息
            String property = environment.getProperty("os.name");
    
            //判断容器是否包含某个bean
            boolean definition = registry.containsBeanDefinition("Magic");
            //property中不包含window就可以通过
            if(!property.contains("Windows")) {
                return true;
            }
            return false;
        }
    }
    ```

  -  @Conditional(WindowsCondition.class)//满足当前条件这个类才生效

    注解添加到类或方法上

#### (7)@Import

- 概念：快速导入组件到容器

- 使用：

  - 三种方式
    - (1).@Import(要导入到容器的组件)：容器自动注册这个组件 id默认为组件的全类名
    - (2).ImportSelector:返回需要导入的组件的全类名数组
    - (3).ImportBeanDefinitionRegistrar：手动注册bean到容器

  ```java
  @Import({Planet.class, MyImportSelector.class,MyImportBeanDefinitionRegistrar.class})
  ```

  对应三种方式的导入

  > - MyImportSelector.class 自定义编辑返回需要导入的组件 实现ImportSelect接口
  >
  > ```java
  > /**
  >  * @Author Robert
  >  * @create 2021/5/17 15:59
  >  * @Version 1.0
  >  * @Description: import 导入选择器
  >  */
  > //自定义编辑返回需要导入的组件
  > public class MyImportSelector implements ImportSelector{
  >     //返回值为要返回的组件全类名 数组
  >     //AnnotationMetadata :当前
  >     @Override
  >     public String[] selectImports(AnnotationMetadata importingClassMetadata) {
  >         //importingClassMetadata
  >         //方法不能返回null值
  >         return new String[]{"com.geek.bean.Star"};
  >     }
  > 
  >     @Override
  >     public Predicate<String> getExclusionFilter() {
  >         return null;
  >     }
  > }
  > ```
  >
  > - MyImportBeanDefinitionRegistrar.class 手动注册bean
  >
  > ```java
  > public class MyImportBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {
  > 
  > 
  >     /**
  >      * AnnotationMetadata 当前类的注解信息
  >      * BeanDefinitionRegistry bean定义的注册类
  >      *         把所有要添加到容器中的bean，调用 registry.registerBeanDefinition();方法手动注册
  >      * @param importingClassMetadata
  >      * @param registry
  >      */
  >     @Override
  >     public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
  > 
  >         //指定bean定义类型
  >         RootBeanDefinition beanDefinition = new RootBeanDefinition(School.class);
  >         //注册一个bean，指定bean名 手动注册
  >         registry.registerBeanDefinition("school",beanDefinition);
  >     }
  > }
  > ```

### 扩展类



