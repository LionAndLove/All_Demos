package com.hadluo.dubbo.transport.serializable;

import com.hadluo.dubbo.common.HessianUtils;

/***
 * 使用 Hessian 实现
 * 
 * @author HadLuo
 * @since JDK1.7
 * @history 2018年5月8日 新建
 */
public class HessianStringSerializer implements StringSerializer {

    public Object deserialize(String resource) throws SerializeException {
        try {
            return HessianUtils.deserialize(resource);
        } catch (Throwable e) {
            throw new SerializeException(e);
        }
    }

    public String serialize(Object object) throws SerializeException {
        if (object == null) {
            return null;
        }
        try {
            return HessianUtils.serialize(object);
        } catch (Throwable e) {
            throw new SerializeException(e);
        }
    }

}
