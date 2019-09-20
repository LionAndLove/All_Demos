package com.jikeh.temp;

import com.alibaba.fastjson.JSONObject;

/**
 * 反序列化问题：
 */
public class User {

    private String name;

    private User subUser;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getUser() {
        return subUser;
    }

    public void setUser(User user) {
        this.subUser = user;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", subUser=" + subUser +
                '}';
    }

    public static void main(String[] args) {
        User u = new User();
        u.setName("han");
        User subU = new User();
        subU.setName("son");
        u.setUser(subU);
        String str = JSONObject.toJSONString(u);
        System.out.println(str);
        User parseUser = JSONObject.parseObject(str, User.class);
        System.out.println(parseUser);
    }

}
