package com.hadluo.dubbo.transport.netty;

import java.io.Serializable;

import com.hadluo.dubbo.common.Constants;
import com.hadluo.dubbo.extension.ExtensionLoader;
import com.hadluo.dubbo.transport.serializable.StringSerializer;

/***
 * netty 发送底层包
 * 
 * @author HadLuo
 * @since JDK1.7
 * @history 2018年5月8日 新建
 */
public class TCP implements Serializable {
    /** xx */
    private static final long serialVersionUID = -8494600564325104163L;

    private Object attach;

    private static StringSerializer serializer = ExtensionLoader.getExtensionLoader(StringSerializer.class)
            .getAdaptiveExtension();

    public TCP(Object attach) {
        this.attach = attach;
    }

    public TCP() {
    }

    public Object getAttach() {
        return attach;
    }

    public static String encode(Object attach) {
        // 构造netty发送包， 并加上换行符，以便netty正确处理粘包问题
        return serializer.serialize(new TCP(attach)) + Constants.LINE_DELIMITER;

    }

    public static Object decode(String json) {
        // 反序列化成对象
        Object object = serializer.deserialize(json);
        if (object instanceof TCP) {
            return ((TCP) object).getAttach();
        }
        return object;
    }
}
