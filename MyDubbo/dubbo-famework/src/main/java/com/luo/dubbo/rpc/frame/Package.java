package com.luo.dubbo.rpc.frame;

import java.io.Serializable;

import com.luo.dubbo.util.HessianUtils;

/***
 * 传输的包
 * 
 * @author HadLuo
 * @since JDK1.7
 * @history 2017年11月22日 新建
 */
public abstract class Package implements Serializable {
    /**xx*/
    private static final long serialVersionUID = -7778186403301428767L;
    /***
     * 序列化 成网络传输
     * 
     * @return
     * @author HadLuo 2017年11月22日 新建
     */
    public abstract String serialize();
    
    /**
     * 获取包的uuid
     * @return
     * @author HadLuo  2017年11月22日 新建
     */
    public abstract String getPackageUuid();
    
    /***
     * 获取目标执行接口
     * @return
     * @author HadLuo  2017年11月22日 新建
     */
    public abstract RpcClass getTargetClass();
    
    /***
     * 反序列化
     * @param src
     * @return
     * @author HadLuo  2017年11月22日 新建
     */
    public static Package deserialize(String src){
        return (Package) HessianUtils.deserialize(src);
    }
}
