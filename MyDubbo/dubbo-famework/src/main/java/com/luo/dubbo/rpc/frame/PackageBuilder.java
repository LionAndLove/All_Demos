package com.luo.dubbo.rpc.frame;

import java.lang.reflect.Method;
import com.luo.dubbo.exception.RpcInvokeException;

public class PackageBuilder {

    /***
     * 构建一个发送tcp包
     * 
     * @param uuid
     * @param targetInter
     * @param targetMethod
     * @return
     * @author HadLuo 2017年11月22日 新建
     */
    public static TcpPackage buildTcp(String uuid,Class<?> clazz, Method method,Object[] parameters) {
        return new TcpPackage(uuid, clazz, method, parameters);
    }

    /**
     * 构建一个 异常包
     * @param uuid
     * @param exception
     * @return
     * @author HadLuo  2017年11月22日 新建
     */
    public static ExceptionPackage buildExceptionPackage(String uuid, RpcInvokeException exception) {
        return new ExceptionPackage(uuid, exception);
    }
    
    /**
     * 反序列化 包
     * @param src
     * @return
     * @author HadLuo  2017年11月22日 新建
     */
    public static Package buildDeserialize(String src){
        return Package.deserialize(src);
    }
}
