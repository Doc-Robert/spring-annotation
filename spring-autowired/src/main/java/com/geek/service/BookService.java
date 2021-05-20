package com.geek.service;

import com.geek.dao.BookDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author Robert
 * @create 2021/5/20 14:11
 * @Version 1.0
 * @Description:
 */

@Service
public class BookService {

//    @Qualifier("bookDao2") //指定装配 bookDao2
//    @Autowired(required = false)//装配是否必须 默认为true
//    @Autowired
//    @Resource(name = "bookDao2")
    @Resource
    private BookDao bookDao;

    @Override
    public String toString() {
        return "BookService[" +
                "bookDao=" + bookDao +
                ']';
    }
}
