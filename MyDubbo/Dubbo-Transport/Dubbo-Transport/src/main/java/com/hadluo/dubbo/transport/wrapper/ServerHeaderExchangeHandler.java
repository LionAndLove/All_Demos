package com.hadluo.dubbo.transport.wrapper;

import com.hadluo.dubbo.transport.Channel;

/***
 * server 端 心跳 处理 <br>
 * 
 * 接收到 客户端的心跳后，原封不动返回心跳给客户端
 * 
 * @author HadLuo
 * @since JDK1.7
 * @history 2018年5月9日 新建
 */
public class ServerHeaderExchangeHandler extends HeaderExchangeHandlerAdapter {

    @Override
    public void received(Channel channel, Object message) {
        
        
        if (message instanceof HeartBeat) {
            // 返回心跳包
            channel.send(message);
        }
    }

}
