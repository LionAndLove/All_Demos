package com.hadluo.dubbo.transport;

import com.hadluo.dubbo.common.URL;
import com.hadluo.dubbo.extension.ExtensionLoader;

/***
 * dubbo transport 的入口
 * 
 * @author HadLuo
 * @since JDK1.7
 * @history 2018年5月8日 新建
 */
public class Transporters {

    /**
     * 初始化 服务端
     * 
     * @param url
     * @param handlers
     * @return
     * @throws RemotingException
     * @author HadLuo 2018年5月9日 新建
     */
    public static Server bind(URL url, ChannelHandler... handlers) throws RemotingException {
        if (url == null) {
            throw new NullPointerException("url is null");
        }
        return getTransporter().bind(url, new ChannelHandlerDispatcher(handlers));
    }

    /***
     * 初始化客户端
     * 
     * @param url
     * @param handlers
     * @return
     * @throws RemotingException
     * @author HadLuo 2018年5月9日 新建
     */
    public static Client connect(URL url, ChannelHandler... handlers) throws RemotingException {
        if (url == null) {
            throw new NullPointerException("url is null");
        }
        return getTransporter().connect(url, new ChannelHandlerDispatcher(handlers));
    }

    /***
     * ExtensionLoader 获取
     * 
     * @return
     * @author HadLuo 2018年5月8日 新建
     */
    private static Transporter getTransporter() {
        return ExtensionLoader.getExtensionLoader(Transporter.class).getAdaptiveExtension();
    }
}
