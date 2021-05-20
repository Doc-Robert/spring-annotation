package com.geek.bean;

/**
 * @Author Robert
 * @create 2021/5/12 15:32
 * @Version 1.0
 * @Description:
 */

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
        return "Magic[" +
                "name='" + name + '\'' +
                ", level=" + level +
                ']';
    }
}
