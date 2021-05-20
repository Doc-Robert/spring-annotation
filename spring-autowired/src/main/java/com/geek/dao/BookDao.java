package com.geek.dao;

import org.springframework.stereotype.Repository;

/**
 * @Author Robert
 * @create 2021/5/20 14:11
 * @Version 1.0
 * @Description:
 */

@Repository
public class BookDao {

    private String label = "1";

    public void setLabel(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public String toString() {
        return "BookDao{" +
                "label='" + label + '\'' +
                '}';
    }
}
