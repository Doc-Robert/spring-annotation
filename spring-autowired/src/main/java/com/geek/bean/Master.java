package com.geek.bean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

/**
 * @Author Robert
 * @create 2021/5/20 15:13
 * @Version 1.0
 * @Description: @Autowired 测试作用在方法 构造器 参数 上
 */
//默认加在 ioc 容器中的组件，容器启会调用无参构造器创建对象，然后再进行初始化赋值操作
@Component
public class Master {

    //Servant 类中 使用@Component注册到容器
    private Servant servant;

    public void setServant(Servant servant) {
        this.servant = servant;
    }

    public Servant getServant() {
        return servant;
    }

    //构造器需要的参数 servant 是从容器中去取
//    @Autowired
//    public Master(Servant servant) {
//        this.servant = servant;
//        System.out.println("从者 。。。。。有参构造器");
//    }

    //标注在参数上
    public Master(@Autowired Servant servant) {
        this.servant = servant;
//        System.out.println("从者 。。。。。有参构造器");
    }
}
