package com.luo.dubbo.rpc;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import com.luo.dubbo.exception.RpcInvokeException;
import com.luo.dubbo.rpc.frame.Package;
import com.luo.dubbo.rpc.frame.PackageBuilder;
import com.luo.dubbo.rpc.frame.RpcMethod;
import com.luo.dubbo.rpc.proxy.ProxyFactory;
import com.luo.dubbo.rpc.resultset.DefaultResultSet;
import com.luo.dubbo.rpc.resultset.ResultSet;
import com.luo.dubbo.rpc.transport.ConnectMonitor;
import com.luo.dubbo.rpc.transport.MessageListener;
import com.luo.dubbo.rpc.transport.NettyClient;
import com.luo.dubbo.rpc.transport.NettyServer;
import com.luo.dubbo.rpc.transport.NettyBootstrap;
import com.luo.dubbo.rpc.transport.Socket;
import com.luo.dubbo.util.ArrayUtils;
import com.luo.dubbo.util.Assert;
import com.luo.dubbo.util.ThreadUtils;
import com.luo.dubbo.util.UUIDUtils;

public class NettyRpc implements Rpc {

    private NettyBootstrap bootstrap;

    private ProxyPool proxyPool;

    private final Object resultSetLock = new Object();

    private ResultSet<String, Object> resultSet;

    public synchronized void getServer(String host, int port, Map<Class<?>, Object> proxysOptions, ConnectMonitor connectMonitor) {
        Assert.isNull(proxysOptions, "远程代理接口不能为空");
        proxyPool().reset(proxysOptions);
        initializeNettyServer(host, port,connectMonitor);
    }

    private ProxyPool proxyPool() {
        if (proxyPool == null) {
            proxyPool = new ProxyPool();
        }
        return proxyPool;
    }

    private ResultSet<String, Object> resultSet() {
        if (null == resultSet) {
            resultSet = new DefaultResultSet<String, Object>();
        }
        return resultSet;
    }

    /***
     * 执行 服务器 的 实现类
     * 
     * @param transmitData
     * @return
     * @author HadLuo 2017年11月22日 新建
     * @throws ClassNotFoundException
     * @throws SecurityException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    private Package invoke0(Package tcpPackage) throws ClassNotFoundException, NoSuchMethodException,
            SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Object target = proxyPool().get(tcpPackage.getTargetClass().getClassName());
        if (null == target) {
            throw new RuntimeException("没有找到接口:" + tcpPackage.getTargetClass().getClassName() + "的实现类对象");
        }
        // 找到执行方法
        RpcMethod rpcMethod = tcpPackage.getTargetClass().getTargetMethod();
        // 有参数
        if (!ArrayUtils.isEmpty(rpcMethod.getParameters())) {
            Class<?>[] ps = new Class<?>[rpcMethod.getParameters().length];
            for (int i = 0; i < rpcMethod.getParameters().length; i++) {
                ps[i] = rpcMethod.getParameters()[i].getClass();
            }
            Method method = target.getClass().getDeclaredMethod(rpcMethod.getName(), ps);
            // 设置 返回值
            rpcMethod.setReturnValue(method.invoke(target, rpcMethod.getParameters()));
        } else {
            // 没有参数
            Method method = target.getClass().getDeclaredMethod(rpcMethod.getName());
            // 设置 返回值
            rpcMethod.setReturnValue(method.invoke(target));
        }
        return tcpPackage;
    }

    private void initializeNettyServer(String host, int port , ConnectMonitor connectMonitor) {
        try {
            // 构建netty服务器
            bootstrap = new NettyServer(new MessageListener<String>() {
                public void onMessage(String msg, Socket remoteSocket) {
                    Package tcpPackage = PackageBuilder.buildDeserialize(msg);
                    try {
                        tcpPackage = invoke0(tcpPackage);
                    } catch (Exception e) {
                        e.printStackTrace();
                        // 执行 方法出错 ,构建异常包
                        tcpPackage = PackageBuilder.buildExceptionPackage(tcpPackage.getPackageUuid(),
                                new RpcInvokeException(e.getMessage()));
                    }
                    // 回送远端 执行结果
                    remoteSocket.writeMessage(tcpPackage.serialize());
                }
            });
            bootstrap.bind(host, port,connectMonitor);
        } catch (Exception e) {
            e.printStackTrace();
            // 清除 代理池
            proxyPool.clear();
            System.exit(-1);
            throw new RuntimeException("netty服务器开启失败，   cause:" + e.getMessage());
        }
    }

    private synchronized void initializeNettyClient(String host, int port, ConnectMonitor connectMonitor) {
        if (bootstrap != null) { // 已经连接过了
            return;
        }
        try {
            bootstrap = new NettyClient(new MessageListener<String>() {
                public void onMessage(String msg, Socket remoteSocket) {
                    Package tcpPackage = PackageBuilder.buildDeserialize(msg);
                    // 存入临时结果集
                    resultSet().putResultSet(tcpPackage.getPackageUuid(),
                            tcpPackage.getTargetClass().getTargetMethod().getReturnValue());
                    ThreadUtils.notifyWait(resultSetLock);
                }
            });
            bootstrap.connect(host, port,connectMonitor);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("连接netty服务器失败，   cause:" + e.getMessage());
        }
    }

    public <T> T getProxy(final Class<T> inter, final String host, final int port, ConnectMonitor connectMonitor) {
        initializeNettyClient(host, port,connectMonitor);
        // 生成代理
        return ProxyFactory.newProxy(inter, new InvocationHandler() {
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

                if ("toString".equals(method.getName())) { // toString 方法
                    return "$" + inter.getName();
                }
                // 构建一个tcp包 发送到 服务器端
                String uuid = UUIDUtils.shortUuid();
                ((NettyClient) bootstrap).sendMessage(PackageBuilder.buildTcp(uuid, inter, method, args)
                        .serialize());
                // 等待 结果
                ThreadUtils.waitFor(resultSetLock);
                // 如果有返回值。从结果集中取出返回值
                return resultSet.fromResultSet(uuid);
            }
        });
    }
}
