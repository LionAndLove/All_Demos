package com.luo.dubbo.schema.bean;

import com.luo.dubbo.schema.bean.annotation.Inject;
import com.luo.dubbo.schema.bean.annotation.SingleBean;

/***
 * <dubbo:protocol>标签的 bean
 * 
 * @author HadLuo
 * @since JDK1.7
 * @history 2017年11月15日 新建
 */
@SingleBean
public class ProtocolBean extends ApplicationBean {
    @Inject
    private int port;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public Class<? extends DubboBean> getBeanClass() {
        // TODO Auto-generated method stub
        return ProtocolBean.class;
    }

    public String getName() {
        // TODO Auto-generated method stub
        return getId();
    }

}
