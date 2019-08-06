package com.hadluo.dubbo.transport.wrapper;

import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.hadluo.dubbo.common.URL;
import com.hadluo.dubbo.transport.Channel;
import com.hadluo.dubbo.transport.netty.TCP;

/***
 * netty channel（通道） 的包装器
 * 
 * @author HadLuo
 * @since JDK1.7
 * @history 2018年5月8日 新建
 */
public class NettyChannel implements Channel {

    /** 所有 已经连接的 通道 缓存 <netty通道 , 包装器通道> */
    private static final ConcurrentMap<io.netty.channel.Channel, NettyChannel> channelMap = new ConcurrentHashMap<io.netty.channel.Channel, NettyChannel>();
    /** 真实的 netty通道 */
    private final io.netty.channel.Channel channel;
    /** 包装器 的 一些 属性 */
    private final Map<String, Object> attributes = new ConcurrentHashMap<String, Object>();

    private final URL url;

    private NettyChannel(io.netty.channel.Channel channel, URL url) {
        this.channel = channel;
        this.url = url;
    }

    public void close() {
        try {
            removeChannelIfDisconnected(channel);
            attributes.clear();
            channel.close();
            System.err.println("Close netty channel " + channel);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 如果netty通道断开连接 调用同步移除缓存 通道包装器
     * 
     * @param ch
     * @author HadLuo 2018年5月8日 新建
     */
    public static synchronized void removeChannelIfDisconnected(io.netty.channel.Channel ch) {
        if (ch != null && !ch.isActive()) {
            channelMap.remove(ch);
        }
    }

    /***
     * 有就从缓存中拿，否则就new 包装通道对象(前提 netty通道是连接状态)
     * 
     * @param ch
     * @return
     * @author HadLuo 2018年5月8日 新建
     */
    public static synchronized NettyChannel getOrAddChannel(io.netty.channel.Channel ch, URL url) {
        NettyChannel ret = channelMap.get(ch);
        if (ret == null) {
            ret = new NettyChannel(ch, url);
            if (ch.isActive()) {
                channelMap.putIfAbsent(ch, ret);
            }
        }
        return ret;
    }

    public static Collection<NettyChannel> channels() {
        return channelMap.values();
    }

    public InetSocketAddress getLocalAddress() {
        return (InetSocketAddress) channel.localAddress();
    }

    public InetSocketAddress getRemoteAddress() {
        return (InetSocketAddress) channel.remoteAddress();
    }

    public void send(Object message) {
        channel.writeAndFlush(TCP.encode(message));
    }

    public Object getAttribute(String key) {
        return attributes.get(key);
    }

    public void setAttribute(String key, Object value) {
        attributes.put(key, value);
    }

    @Override
    public boolean isConnected() {
        // TODO Auto-generated method stub
        return !(channel == null) || channel.isActive();
    }

    @Override
    public URL getUrl() {
        // TODO Auto-generated method stub
        return url;
    }

}
