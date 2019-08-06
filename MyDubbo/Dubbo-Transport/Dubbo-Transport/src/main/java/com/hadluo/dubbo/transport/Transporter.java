package com.hadluo.dubbo.transport;

import com.hadluo.dubbo.common.URL;
import com.hadluo.dubbo.extension.Adaptive;
import com.hadluo.dubbo.extension.SPI;

/***
 * dubbo transport通信协议接口 （dubbo里面存在
 * GrizzlyTransporter,MinaTransporter,NettyTransporter 三种），这里我们只实现
 * NettyTransporter
 * 
 * @author HadLuo
 * @since JDK1.7
 * @history 2018年5月8日 新建
 */
@SPI("netty")
public interface Transporter {
    /***
     * 初始化绑定服务端
     * 
     * @param url
     * @param handler
     * @return
     * @author HadLuo 2018年5月8日 新建
     */
    @Adaptive
    Server bind(URL url, ChannelHandler handler) throws RemotingException;

    /***
     * 初始化客户端
     * 
     * @param url
     * @param handler
     * @return
     * @author HadLuo 2018年5月8日 新建
     */
    @Adaptive
    Client connect(URL url, ChannelHandler handler) throws RemotingException;
}
