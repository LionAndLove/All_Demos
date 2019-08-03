package com.luo.dubbo.rpc.frame;

import java.lang.reflect.Method;
import com.luo.dubbo.util.HessianUtils;

public class TcpPackage extends Package {

    /** xx */
    private static final long serialVersionUID = -2367599534726397661L;

    /***
     * 要调用的接口
     */
    private RpcClass targetClass;

    private String packageUuid;

    TcpPackage(String packageUuid, Class<?> clazz, Method method, Object[] parameters) {
        setTargetClass(RpcClass.toClass(clazz, method, parameters));
        this.packageUuid = packageUuid;
    }

    TcpPackage() {
    }

    public void setTargetClass(RpcClass targetClass) {
        this.targetClass = targetClass;
    }

    public String serialize() {
        return HessianUtils.serialize(this);
    }

    public void setPackageUuid(String packageUuid) {
        this.packageUuid = packageUuid;
    }

    public String getPackageUuid() {
        return packageUuid;
    }

    @Override
    public RpcClass getTargetClass() {
        return targetClass;
    }
}
