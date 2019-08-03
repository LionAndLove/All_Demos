package com.luo.provider.bean;

import java.io.Serializable;

public class User implements Serializable{
    
    /**xx*/
    private static final long serialVersionUID = -3991376803961333145L;

    private String name;
    
    private int age ;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "User [name=" + name + ", age=" + age + "]";
    }
}
