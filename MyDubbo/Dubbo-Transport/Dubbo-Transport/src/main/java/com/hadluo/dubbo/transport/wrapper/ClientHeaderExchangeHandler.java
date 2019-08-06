package com.hadluo.dubbo.transport.wrapper;

import com.hadluo.dubbo.common.Constants;
import com.hadluo.dubbo.transport.Channel;

/***
 * 客户端 收到服务端 的心跳处理 <br>
 * 设置一下 读的时间
 * 
 * @author HadLuo
 * @since JDK1.7
 * @history 2018年5月9日 新建
 */
public class ClientHeaderExchangeHandler extends HeaderExchangeHandlerAdapter {

    @Override
    public void received(Channel channel, Object message) {
        // 心跳用， 上一次 读事件的时间
        channel.setAttribute(Constants.KEY_READ_TIMESTAMP, System.currentTimeMillis());
    }

    @Override
    public void connected(Channel channel) {
        // 心跳用， 上一次 读事件的时间
        channel.setAttribute(Constants.KEY_READ_TIMESTAMP, System.currentTimeMillis());
    }

}
