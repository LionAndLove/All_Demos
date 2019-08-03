package com.luo.dubbo.util;

import java.lang.reflect.Array;
import java.util.List;

public class ArrayUtils {

    public static boolean isEmpty(Object[] arrays) {
        return arrays == null || arrays.length == 0;
    }

    @SuppressWarnings("unchecked")
    public static <T> T[] listToArray(Class<T> type, List<T> list) {
        T[] array = (T[]) Array.newInstance(type, list.size());
        for (int i = 0; i < list.size(); i++) {
            array[i] = list.get(i);
        }
        return array;
    }
    
    
}
