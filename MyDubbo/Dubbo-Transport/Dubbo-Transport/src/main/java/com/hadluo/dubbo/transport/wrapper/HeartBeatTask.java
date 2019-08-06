package com.hadluo.dubbo.transport.wrapper;

import java.util.Collection;
import com.hadluo.dubbo.common.Constants;
import com.hadluo.dubbo.transport.Channel;
import com.hadluo.dubbo.transport.Client;

/***
 * 心跳 任务 <br>
 * 主要是 向 远端隔 heartbeat 发送HeartBeat包 ,并检查 如果 对方在 heartbeatTimeout没回复就重连远端
 * 
 * @author HadLuo
 * @since JDK1.7
 * @history 2018年5月9日 新建
 */
public class HeartBeatTask implements Runnable {
    private ChannelProvider channelProvider;
    private int heartbeat;
    private int heartbeatTimeout;

    HeartBeatTask(ChannelProvider provider, int heartbeat, int heartbeatTimeout) {
        this.channelProvider = provider;
        this.heartbeat = heartbeat;
        this.heartbeatTimeout = heartbeatTimeout;
    }

    @Override
    public void run() {
        long now = System.currentTimeMillis();
        for (Channel channel : channelProvider.getChannels()) {
            Long lastRead = (Long) channel.getAttribute(Constants.KEY_READ_TIMESTAMP);
            if (lastRead != null && now - lastRead > heartbeat) {
                // 大于了心跳时间, 发送心跳
                channel.send(new HeartBeat());
            }
            if (lastRead != null && now - lastRead > heartbeatTimeout) {
                System.err.println("Close channel " + channel + ", because heartbeat read idle time out: "
                        + heartbeatTimeout + "ms");
                // 大于了 心跳超时时间 , 重连
                if (channel instanceof Client) {
                    ((Client) channel).reconnect();
                } else {
                    channel.close();
                }
            }
        }
    }

    interface ChannelProvider {
        Collection<Channel> getChannels();
    }
}
