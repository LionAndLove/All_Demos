package com.luo.dubbo.util;

import org.springframework.util.StringUtils;

public class Assert {
    public static void isNull(Object object, String errorMessage) {
        if (null == object) {
            throw new NullPointerException("assert error : " + errorMessage);
        }
    }

    public static void isStringNull(String object, String errorMessage) {
        if (StringUtils.isEmpty(object)) {
            throw new NullPointerException("assert error : " + errorMessage);
        }
    }

    public static void arrayIsNull(Object[] array, String errorMessage) {
        if (null == array) {
            throw new NullPointerException("assert error : " + errorMessage);
        }
        for (Object obj : array) {
            if (null == obj) {
                throw new NullPointerException("assert error : " + errorMessage);
            }
        }
    }
}
