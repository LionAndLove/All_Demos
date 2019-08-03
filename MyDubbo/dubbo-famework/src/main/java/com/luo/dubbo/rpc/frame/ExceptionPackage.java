package com.luo.dubbo.rpc.frame;

import com.luo.dubbo.exception.RpcInvokeException;
import com.luo.dubbo.util.HessianUtils;

/***
 * 异常包
 * 
 * @author HadLuo
 * @since JDK1.7
 * @history 2017年11月22日 新建
 */
public class ExceptionPackage extends TcpPackage {
    /** xx */
    private static final long serialVersionUID = -4411852400681591429L;
    /***
     * 异常类型
     */
    private RpcInvokeException exception;

    public ExceptionPackage(String packageUuid, RpcInvokeException exception) {
        setPackageUuid(packageUuid);
        this.exception = exception;
    }

    public RpcInvokeException getException() {
        return exception;
    }

    public void setException(RpcInvokeException exception) {
        this.exception = exception;
    }

    @Override
    public String serialize() {
        return HessianUtils.serialize(this);
    }
}
