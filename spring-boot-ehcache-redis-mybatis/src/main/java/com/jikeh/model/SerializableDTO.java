package com.jikeh.model;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;

/**
 * Created by 极客慧 on 2018/7/6.
 */
public abstract class SerializableDTO implements Serializable {
    private static final long serialVersionUID = -3519742722135691307L;

    public SerializableDTO() {
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
