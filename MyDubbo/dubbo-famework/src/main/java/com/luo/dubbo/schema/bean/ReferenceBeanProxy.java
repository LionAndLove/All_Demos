package com.luo.dubbo.schema.bean;

import org.springframework.beans.factory.FactoryBean;

/****
 * rpc的 动态代理 对象，需要动态注入 spring  
 * 
 * @author HadLuo
 * @since JDK1.7
 * @history 2017年12月15日 新建
 */
public class ReferenceBeanProxy extends ApplicationBean implements FactoryBean<Object> {
    /***
     * 远程代理对象， 用来 进行rpc调用
     */
    private Object proxy;

    public Object getProxy() {
        return proxy;
    }

    public void setProxy(Object proxy) {
        this.proxy = proxy;
    }

    /***
     * 直接 返回代理对象，引用 spring 对象 就等于 直接找这个 代理对象
     */
    public Object getObject() throws Exception {
        // TODO Auto-generated method stub
        return proxy;
    }

    public Class<?> getObjectType() {
        // TODO Auto-generated method stub
        return proxy.getClass();
    }

    public boolean isSingleton() {
        // TODO Auto-generated method stub
        return false;
    }

    public Class<? extends DubboBean> getBeanClass() {
        // TODO Auto-generated method stub
        return ReferenceBeanProxy.class;
    }

    public String getName() {
        // TODO Auto-generated method stub
        return getId();
    }
}
