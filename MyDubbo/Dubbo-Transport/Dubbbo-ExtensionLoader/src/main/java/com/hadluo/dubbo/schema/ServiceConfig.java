package com.hadluo.dubbo.schema;

import com.hadluo.dubbo.schema.bean.ApplicationBean;
import com.hadluo.dubbo.schema.bean.ProtocolBean;

public class ServiceConfig extends AbstractConfig {
    /** 服务接口全路径 */
    @Tag("interface")
    private String _interface;

    /** 服务对象实现引用 */
    @Tag("ref")
    private String ref;

    /** 远程服务调用超时时间（毫秒） */
    @Tag("timeout")
    private long timeout = 1000;
    /** 负载均衡策略，可选值为：random（随机）、roundrobin（轮询）、leastactive（最少活跃调用） */
    @Tag("loadbalance")
    private String loadbalance = "random";
    /** 是否缺省异步执行，不可靠的异步，只是忽略返回值，不阻塞执行线程 */
    @Tag("async")
    private boolean async = false;
    @Tag
    private Holder<ApplicationBean> applicationHolder;
    @Tag
    private Holder<ProtocolBean> protocolHolder;

    public String getInterface() {
        return _interface;
    }

    public String getRef() {
        return ref;
    }

    public long getTimeout() {
        return timeout;
    }

    public String getLoadbalance() {
        return loadbalance;
    }

    public boolean isAsync() {
        return async;
    }

    // dubbo://10.112.6.12:20880/cn.javacoder.test.dubbo.IHelloWorldService?application=test-provider
    // &dubbo=2.5.3&interface=cn.javacoder.test.dubbo.IHelloWorldService&methods=say&pid=6816&side=provider&timestamp=1522284835101
    public void export() {
        if (getApplicationContext() == null) {
            throw new RuntimeException("spring 还没有初始化完成");
        }
        wakeupHolder();
        // 这里 模拟 ======================
        String url = "dubbo://" + "10.112.6.12:" + protocolHolder.get().getPort() + _interface + "?"
                + "application=" + applicationHolder.get().getName() + "&dubbo=2.5.3&interface=" + _interface
                + "&methods=say&pid=6816&side=provider&timestamp=1522284835101";

        System.err.println("准备开始 导出 >>" + url);

    }

    private void wakeupHolder() {
        applicationHolder.setValue(getApplicationContext());
        protocolHolder.setValue(getApplicationContext());
    }
}
