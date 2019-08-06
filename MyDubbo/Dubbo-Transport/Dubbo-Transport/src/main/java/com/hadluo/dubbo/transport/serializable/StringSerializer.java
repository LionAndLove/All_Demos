package com.hadluo.dubbo.transport.serializable;

import com.hadluo.dubbo.extension.Adaptive;
import com.hadluo.dubbo.extension.SPI;

/***
 * string序列化器 (默认使用Hessian解析 实现)
 * 
 * @author HadLuo
 * @since JDK1.7
 * @history 2018年5月8日 新建
 */
@SPI("hessian")
public interface StringSerializer {

    /***
     * 将string反序列化成对象
     * 
     * @param bytes
     * @return
     * @throws SerializeException
     * @author HadLuo 2018年5月8日 新建
     */
    @Adaptive
    Object deserialize(String resource) throws SerializeException;

    /***
     * 将对象序列化 成string
     * 
     * @param object
     * @return
     * @throws SerializeException
     * @author HadLuo 2018年5月8日 新建
     */
    @Adaptive
    String serialize(Object object) throws SerializeException;

}
