package com.hadluo.dubbo.transport.wrapper;

import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import com.hadluo.dubbo.common.Constants;
import com.hadluo.dubbo.common.URL;
import com.hadluo.dubbo.transport.Channel;
import com.hadluo.dubbo.transport.Client;

/***
 * 心跳处理的 客户端
 * 
 * @author HadLuo
 * @since JDK1.7
 * @history 2018年5月9日 新建
 */
public class HeaderExchangeClient implements Client {

    private static final ScheduledThreadPoolExecutor scheduled = new ScheduledThreadPoolExecutor(2);

    // 心跳定时器
    private ScheduledFuture<?> heatbeatTimer;

    private final Client client;
    // 心跳超时，毫秒。缺省0，不会执行心跳。
    private final int heartbeat;

    private final int heartbeatTimeout;

    public HeaderExchangeClient(Client client) {
        this.client = client;
        this.heartbeat = client.getUrl().getParameter(Constants.HEARTBEAT_KEY, 0);
        this.heartbeatTimeout = client.getUrl().getParameter(Constants.HEARTBEAT_TIMEOUT_KEY, heartbeat * 3);

        startHeatbeatTimer();
    }

    private void startHeatbeatTimer() {
        stopHeartbeatTimer();
        if (heartbeat > 0) {
            heatbeatTimer = scheduled.scheduleWithFixedDelay(new HeartBeatTask(
                    new HeartBeatTask.ChannelProvider() {
                        public Collection<Channel> getChannels() {
                            return Collections.<Channel> singletonList(HeaderExchangeClient.this);
                        }
                    }, heartbeat, heartbeatTimeout), heartbeat, heartbeat, TimeUnit.MILLISECONDS);
        }
    }

    private void stopHeartbeatTimer() {
        if (heatbeatTimer != null && !heatbeatTimer.isCancelled()) {
            try {
                heatbeatTimer.cancel(true);
                scheduled.purge();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        heatbeatTimer = null;
    }

    @Override
    public void send(Object message) {
        client.send(message);
    }

    @Override
    public void close() {
        stopHeartbeatTimer();
        client.close();
    }

    @Override
    public void reconnect() {
        client.reconnect();
    }

    @Override
    public Object getAttribute(String key) {
        return client.getAttribute(key);
    }

    @Override
    public void setAttribute(String key, Object value) {
        client.setAttribute(key, value);
    }

    @Override
    public boolean isConnected() {
        return client.isConnected();
    }

    @Override
    public InetSocketAddress getRemoteAddress() {
        return client.getRemoteAddress();
    }

    @Override
    public InetSocketAddress getLocalAddress() {
        return client.getLocalAddress();
    }

    @Override
    public URL getUrl() {
        return client.getUrl();
    }
}
