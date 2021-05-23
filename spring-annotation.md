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

#### 1) AnnotationConfigApplicationContext

- 概念：ApplicationContext 接口的实现类，用于创建配置类的 IOC 容器对象

- 方法

  1. getBean(Class)：传入 Class 类型获取对应的 bean 实例
  2. getBeanNamesForType(Class)：传入 Class 类型获取 IOC 容器中所有对应的 bean 实例的 id
  3. getBeanDefinitionNames()：获取当前 IOC 容器对象中所有 bean 实例的 id
  4. getBeansForType(Class)：可以获取容器中指定类型(Class)的 bean 实例和 id 组成的 Map
  5. getEnvironment()：获取当前容器运行的环境信息对象(Environment)

- 实例

  ```
  // 1. 调用 AnnotationConfigApplicationContext 的构造函数，创建配置类，创建 ApplicationContext 类对象
  ApplicationContext applicationContext = new AnnotationConfigApplicationContext(MainConfig.class);
  
  // 2. 方法调用
  // 2.1 通过 getBean(Class) 传入 Class 类型获取对应的 bean 实例
  Person person = applicationContext.getBean(Person.class);
  // 2.2 通过 getBeanNamesForType(Class) 传入 Class 类型获取 IOC 容器中所有对应的 bean 实例的 id
  String[] names = applicationContext.getBeanNamesForType(Person.class);
  // 2.3 通过 getBeanDefinitionNames() 获取 IOC 容器中所有 bean 实例的 id
  String[] beanDefinitionNames = context.getBeanDefinitionNames();
  // 2.4 getBeansOfType(Class)：可以获取容器中指定类型的 bean 实例和 id 组成的 Map
  Map<String, Person> personMap = context.getBeansOfType(Person.class);
  // 2.5 getEnvironment()：获取当前容器运行的环境信息对象(Environment)
  Environment environment = context.getEnvironment();
  
  // 3. 输出测试
  log.info(person.toString()); // Person(name=巴御前, age=16)
  for (String name : names) {
      System.out.println(name); // person
  }
  ```

#### 2) Environment

- 概念：Spring IOC 容器的运行环境

- 方法：

  1. getProperty("key")：根据 key 获取对应的环境信息

- 实例

  ```
  // getEnvironment()：获取当前容器运行的环境信息对象(Environment)
  Environment environment = context.getEnvironment();
  // environment.getProperty("key")：根据 key 获取对应的环境信息
  String osName = environment.getProperty("os.name"); // os,name -> 运行时的操作系统
  System.out.println(osName); // Windows 10
  ```

#### 3) FactoryBean

- 概念：接口，也是一个 Bean，用户可以通过实现该接口用于定制实例化 Bean 的逻辑

- 使用：

  1. getObject(): FactoryBean 会将该方法的返回结果作为 bean 实例装配到 IOC 容器中
  2. getObjectType(): 获取对应的 bean 实例的类型
  3. isSingleton(): 是否单例，返回 true 则是，反之相反

- 实例：

  1. 实现 FactoryBean 接口

     ```
     /**
      * 通过实现 Factory 用于控制 Person bean 的实例化
      * @author EMTKnight
      * @create 2021-02-26
      */
     public class PersonFactoryBean implements FactoryBean<Person> {
     
         /**
          * 该方法的返回结果会作为 bean 实例装配到 IOC 容器中
          * @return
          * @throws Exception
          */
         @Override
         public Person getObject() throws Exception {
             return new Person("巴御前",17);
         }
     
         @Override
         public Class<?> getObjectType() {
             return Person.class;
         }
     
         @Override
         public boolean isSingleton() {
             return false;
         }
     }
     ```

  2. 使用 @Bean 配置 FactoryBean

  3. 测试

     ```
     @Test
     public void test06(){
         Person person = context.getBean("personFactoryBean", Person.class);
         // 通过注册 FactoryBean 实例时使用的 id 得到的是其通过 getObject() 方法返回的 bean 实例
         System.out.println(person); // Person(name=巴御前, age=17)
         // 如果需要获取的是 FactoryBean 实例，可以在 id 前面加上 &
         Object bean = context.getBean("&personFactoryBean");
         System.out.println(bean); // pers.dreamer07.springAoon.bean.PersonFactoryBean@1532c619
     }
     ```

- 注意：

  1. 通过注册 FactoryBean 的 id 在 IOC 容器中默认获取的是其 getObject() 方法返回后装配的 bean 实例
  2. 如果需要获取 FactoryBean 实例，可以在对应的 id 前加上 **&** 即可

### SpringIOC

- 给 Spring IOC 容器中注册组件的方式
  - 对应自定义的组件：包扫描+组件注解(@Controller/@Service/@Repository/@Component) - id 为对应的类名首字母小写
  - 对应第三方的组件：使用 @Bean 进行注册 - id 为对应的方法名
  - 快速给容器中导入一个 bean：使用 @Import 注解 - id为对应的全类名
  - 使用 Spring 提供的 FactoryBean(工厂 Bean) - id 为对应的方法名

## 1.2 Bean的生命周期

### 一、概念

- bean 的生命周期：创建 -> 初始化 -> 销毁
- 而 Spring 中由 IOC 容器辅助帮我们进行管理
- 我们也可以自定义初始化和销毁方法，容器在bean进行到当前生命周期的时候来调用我们自定义初始化和销毁方法

> **Bean的生命周期**：
>
>  构造（创建对象）
> *      单实例：在容器启动的时候创建对象
> *      多实例；在每次获取的时候才创建对象
>
> 初始化
>
> *      在对象创建后 并赋值完成，调用初始化方法
>
> 销毁
>
> *      单实例：在容器关闭的时候 调用销毁方法
> *      多实例：容器并不会管理这个bean 因为多实例只在获取它时才进行创建，容器不会调用销毁方法 可以手动调用

### 二、自定义bean初始化和销毁方法

#### 1. 通过 @Bean 注解

- 通过@Bean注解 ； （xml内相当于指定  init-method="" destroy-method=""）
- 指定 `@Bean` 注解的 `initNethod` 和 `destroyMethod` 属性为对应的 bean 实例对象中的方法
- 实例：

```Java
/**
 * @Author Robert
 * @create 2021/5/19 10:23
 * @Version 1.0
 * @Description: @bean实现 bean生命周期
 */
public class Real {

    public Real() {
        System.out.println("Real Constructor....");
    }

    public void init(){
        System.out.println("Real init method");
    }
    public void destroy(){
        System.out.println("Real destroy method");
    }
}


    //注入bean   Real
	//@Scope("prototype") 多实例情况下
    @Bean(initMethod = "init",destroyMethod = "destroy")//可以指定 初始化方法 和销毁方法
    public Real real(){
        return new Real();
    }

```

#### 2.Bean类实现InitializingBean&**DisposableBean** 接口

- 可以通过让Bean 实现InitializingBean接口，定义初始化逻辑；实现DisposableBean接口，定义销毁逻辑。
- 实例：

```java
/**
 * @Author Robert
 * @create 2021/5/19 10:49
 * @Version 1.0
 * @Description: 通过 实现InitializingBean  DisposableBean来实现bean的生命周期
 */

@Component
public class Lie implements InitializingBean, DisposableBean {

    public Lie() {
        System.out.println("Lie constructor");
    }

    //实现接口 得到的初始化 方法
    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("Lie afterPropertiesSet...");
    }
    //销毁方法
    @Override
    public void destroy() throws Exception {
        System.out.println("Lie destroy...");
    }
}
```

测试：注册到ioc 容器 进行测试

#### 3.Bean类可以使用JSR250规范

- JSR250规范中的两个注解

  - `@PostConstruct` :在bean创建完成并且属性赋值完成；来执行初始化方法

  *      `@PreDestory` : 在容器销毁bean之前 通知我们进行清理工作

- 实例：

```java
/**
 * @Author Robert
 * @create 2021/5/19 11:05
 * @Version 1.0
 * @Description:    第三种方式实现bean 生命周期 两个注解
 */

@Component//将其加入到容器 
public class Voids {


    public Voids() {
        System.out.println("Voids Constructor....");
    }

    //在对象创建 并赋值完成后调用
    @PostConstruct
    public void init(){
        System.out.println("Real @PostConstruct");
    }

    //在容器移除对象之前通知我们
    @PreDestroy
    public void destroy(){
        System.out.println("Real @PreDestroy");
    }

}
```

#### 4.配置类实现BeanPostProcessor接口

- `BeanPostProcessor` [interface]: bean的后置处理器

- 使用 - 重写的两个方法

  1. postProcessBeforeInitialization()：在所有初始化方法之前执行
  2. postProcessAfterInitialization()：在所有初始化方法执行之后执行

- 实例：

  ```java
  /**
   * @Author Robert
   * @create 2021/5/19 14:09
   * @Version 1.0
   * @Description:  后置处理器 初始化前后进行处理
   * 将后置处理器加入到容器中 
   */
  
  @Component
  public class MyBeanPostProcessor implements BeanPostProcessor {
  
      @Override
      public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
          System.out.println("postProcessBeforeInitialization。。。"+beanName+"->"+bean);
          return bean;
      }
  
      @Override
      public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
          System.out.println("postProcessAfterInitialization。。。"+beanName+"->"+bean);
          return bean;
      }
  }
  ```

#### 5.BeanPostProcessor原理

1. 在 BeanFactory 创建对应的 bean 实例时，会执行 `populateBean()` 方法用于进行属性赋值

   `populateBean`(beanName, mbd, instanceWrapper)

2. 然后执行`initializeBean()` bean实例初始化

3.  `applyBeanPostProcessorsBeforeInitialization(wrappedBean, beanName); `初始化之前调用

    先遍历的得到所有的`getBeanPostProcessors`然后挨个执行`BeforeInitialization`

     一旦返回null，跳出for循环 不会执行后面的`BeanPostProcessors`

4. `invokeInitMethods(beanName, wrappedBean, mbd);` 执行自定义初始化方法

5.  `applyBeanPostProcessorsAfterInitialization(wrappedBean, beanName);`初始化之后执行 与第3步类似

  **扩展：**
*      spring底层对BeanPostProcessor的使用：
*          bean赋值，注入其他组件，@Autowired ,生命周期注解功能，@Async

### bean 生命周期的执行时机

1. 创建：
   - 单实例：在容器启动时创建
   - 多实例：从容器中获取时创建
2. 属性赋值
3. 执行 bean 后置处理器的 postProcessBeforeInitialization()
4. 初始化：在对象创建好之后，并完成赋值之后
5. 执行 bean 后置处理器的 postProcessAfterInitialization()
6. 销毁：容器关闭时



## 1.3 组件赋值

### 注解

#### (1)@Value

- 作用在属性上，在初始化之前完成属性赋值
- 使用@Value注解赋值
  * 1、基本数值
  * 2、使用SpEl表达式：#{}    spring表达式
  * 3、使用${} 取出配置文件的值（在运行环境变量里面的值）

```java
public class Person {
    @Value("咕哒子")
    private String name;

    @Value("#{20-2}")
    private Integer age;

    @Value("${person.nickName}")//取出配置文件的值
    private String nickName;
}
```

#### (2) @PropertySource

- 读取指定配置文件中的内容(k/v)并保存到运行的环境变量中

```java
@PropertySource(value = {"classpath:/person.properties"})//读取外部配置文件 导入类路径 //使用${}取出配置文件的值
@Configuration
public class MainConfigOfPropertyValue {

    @Bean
    public Person person(){
        return new Person();
    }

}


person.nickName=\u8587\u5c14\u8389\u7279
```

- 注意

  1. 保存到环境变量的中，也可以通过 **Environment** 实例对象获取

  2. 和 `@Component` 注解相识，可以通过多个 `@PropertySource` 读取多个配置文件，

     也可以通过使用 `@PropertySources` 中配置多个 `@PropertySource` 读取多个配置文件



## 1.4 组件赋值

### 注解

#### (1) @Autowired

- 自动装配：自动注入容器中对应的 bean 实例 , Spring利用依赖注入（DI）完成对各个组件的依赖关系赋值

- 使用：
  -  (1)、默认按照优先类型去对应的容器中找对应的组件 applicationContext.getBean(BookDao.class);

    ~~~java
    BookService{
        @Autowired
        BookDao bookDao;
    }
    ~~~

  *      (2)、如果找到多个相同类型的组件 再将属性的名称作为属性id 去容器中查找  `applicationContext.getBean(dookDao);`
  *      (3)、使用`@Qualifier("bookDao")`  使用@Qualifier指定需要装配的di，而不是使用属性名
  * (4)、自动装配默认一定要将属性赋值，没有则会报错

    可以使用`@Autowired(required = false)`//装配是否必须 默认为true
  * (5)、可以使用 `@Primary` 指定首选自动装配 让Spring自动装配的时候，默认使用首选的Bean

    也可以继续使用 `@Qualifier` 指定需要转配的bean的名字

#### (2)@Qualifier

- 作用：根据 id 在 IOC 容器中查找对应的 bean 实例

#### (3)@Primary

- 作用：将对应的注册 bean 实例作为 IOC 自动装配时使用的**首选项**

#### (4)@Resource(JSR 250规范)

- 作用：实现自动装配
- 使用
  - 默认通过属性名装配对应的 bean 实例，可以通过指定 name 属性值装配指定的 bean 实例
  - 不支持 `@Qualifier` 和 `@Primary`
  - 无法使用和 `@Autowired(required = false)` 的功能

####  (5)@Inject(JSR 330规范)

- 作用：实现自动装配
- 使用(需要导入javax inject包)
  - 默认通过属性名装配对应的 bean 实例
  - 支持 `@Qualifier` 和 `@Primary`
  - 无法指定任何属性

区别：@Autowired是由spring定义的 而@Resource 和@Inject是java规范

> #### @Autowired & @Resouce & @Inject 的区别

1. @Autowired 在 Spring 中使用的最为广泛，但只能在 Spring 中使用

   而后两者属于 Java 规范，可以在其他的 IOC 框架中使用

2. 强度：@Autowired > @Inject > @Resouce(在 Spring 中的使用)

3. 但三者在 Spring 中都是通过 **AutowiredAnnotationBeanPostProcessor** bean 后置处理器实现自动装配

#### (6)@Autowired 的其他使用功能

- @Aurowired： 可以标在 构造器 方法 参数 属性上;都是从容器中获取参数组件的值

  (1)、标注在方法上； 

  - @bean+方法参数，参数从容器中取获取；默认不写@autowired效果也一致 ，都能够自动装配

  - 注意：如果是 `@Bean` 标注的方法，其中使用的参数都会从 IOC 容器中获取

  (2)、标注在构造器上

  -  如果组件只有一个有参构造器 那么这个有参构造器的@Autowired 可以省略，参数位置的组件还是可以自动从容器中取

  - 注意：
    1. 当类的构造器**有且只有一个构造器**时，无论使不使用 `@Autowired` 都会调用该构造器，其中自定义类型的参数，依然会通过 IOC 中获取
    2. 一个类中**不能有两个/两个以上**由 `@Autowired` 注解**标注的构造器**

  (3)、放在参数位置

  - 效果和放在方法/构造器上一致；但在**构造器**上时，默认还是会调用无参构造器，除非只有当前一个构造器都会从 IOC 容器中获取对应的类型的组件完成赋值

#### (7)@Profile

> *      是spring为我们提供可以根据当前环境，动态的激活和切换一系列组件的功能
> *      指定组件在哪一种环境下才能被注册到环境中，不指定，则任何环境下都能注册这个组件
> *      环境标识 只有环境被激活的时候 才会被激活. 默认为default环境
> *      当写在配置类上时，只有指定环境的时候，整个配置类里面的所有配置才能开始生效
> *      没有profile 标识的bean 则在任何环境都是加载的

- 例如：根据不同的环境(开发环境/测试环境/生产环境)切换数据源

- 使用：

  1. 可以通过指定 value 值为环境标识，来确定组件在对应的环境下才会被注册到容器中

  2. 使用**命令行动态参数**：在虚拟机参数位置输入 `-Dspring.profiles.acvtive=环境标识`

  3. Spring Boot 中根据命名规则(application-{profile}，profile=dev ：开发环境、test：测试环境、prod：生产环境)，

  4. 也可以在 `application.properties` 中使用 **spring.profiles.active** 项激活一个/多个配置文件

     ```
     spring.profiles.active: prod,proddb,prodmq
     ```

     如果没有指定就会默认启动application-default.properties。

  5. 如果将其**标注在类上**就代表只有在对应的运行环境下其中的所有配置才可以生效

- 注意：

  1. 如果不指定在任何环境下都不会注册组件
  2. 不使用该注解和指定该注解和 value 值为 **default** 一致





### 扩展

​	自定义组件想要使用spring底层的一些组件（ApplicationContext，BeanFactroy）

#### (1) 使用 Spring 底层组件(Aware)

- 说明：如果自定义组件想要使用 Spring 容器底层组件(IOC 容器、BeanFactory等)，是需要实现对应的 `xxxAware` 接口

- **Aware** 接口：实现该接口的各种继承接口可以完成对应的需求

  (例如)

  1. 实现 **AplicationContextAware** 接口，通过 setApplicationContext() 方法**保存 IOC 容器**
  2. 实现 **BeanNameAware** 接口，通过 setBeanName() 方法获取 beanName
  3. 实现 **EmbeddedValueResolverAware** 接口，通过 setEmbeddedValueResolver() 方法使用 Spring 用的占位符解析器(#{}、${})

- 实例

```java
/**
 * @Author Robert
 * @create 2021/5/20 15:57
 * @Version 1.0
 * @Description: Aware注入 实现ApplicationContextAware 测试
 */

@Component
public class Human implements ApplicationContextAware, BeanNameAware, EmbeddedValueResolverAware {

    private ApplicationContext applicationContext;

    //ApplicationContextAware 接口 的setcontext方法
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
//        System.out.println("传入的IOC："+applicationContext);
        this.applicationContext = applicationContext;
    }

    //ioc 创建对象的bean的名字
    @Override
    public void setBeanName(String name) {
//        System.out.println("当前bean的名字："+name);

    }
    //解析string中的占位符
    @Override
    public void setEmbeddedValueResolver(StringValueResolver resolver) {
        String resolveStringValue = resolver.resolveStringValue("你好${os.name}");
//        System.out.println("解析的字符串"+resolveStringValue);
    }
}
```



## 1.5 Aop

- 概念：Aop指在程序运行期间动态的将某段代码切入到指定的方法指定位置运行的编程方式；

