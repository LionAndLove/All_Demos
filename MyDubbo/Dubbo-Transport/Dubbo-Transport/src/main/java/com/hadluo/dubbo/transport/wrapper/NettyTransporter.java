package com.hadluo.dubbo.transport.wrapper;

import com.hadluo.dubbo.common.URL;
import com.hadluo.dubbo.transport.ChannelHandler;
import com.hadluo.dubbo.transport.ChannelHandlerDispatcher;
import com.hadluo.dubbo.transport.Client;
import com.hadluo.dubbo.transport.RemotingException;
import com.hadluo.dubbo.transport.Server;
import com.hadluo.dubbo.transport.Transporter;
import com.hadluo.dubbo.transport.netty.NettyClient;
import com.hadluo.dubbo.transport.netty.NettyServer;

/***
 * netty 实现 transporter
 * 
 * @author HadLuo
 * @since JDK1.7
 * @history 2018年5月8日 新建
 */
public class NettyTransporter implements Transporter {

    @Override
    public Server bind(URL url, ChannelHandler handler) throws RemotingException {
        // 包装一个 服务端心跳处理 handler
        handler = ((ChannelHandlerDispatcher) handler).add(new ServerHeaderExchangeHandler());
        // 构造NettyServer
        return new NettyServer(url, handler);
    }

    @Override
    public Client connect(URL url, ChannelHandler handler) throws RemotingException {
        // 包装一个 客户端心跳处理 handler
        handler = ((ChannelHandlerDispatcher) handler).add(new ClientHeaderExchangeHandler());
        // 包装了一个 发送心跳的 client
        return new HeaderExchangeClient(new NettyClient(url, handler));
    }

}
