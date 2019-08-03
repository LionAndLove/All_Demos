package com.luo.dubbo.schema.bean;

import com.luo.dubbo.schema.bean.annotation.Inject;
import com.luo.dubbo.schema.bean.annotation.MultipleBean;

/***
 * <dubbo:service>标签的 bean
 * 
 * @author HadLuo
 * @since JDK1.7
 * @history 2017年11月15日 新建
 */
@MultipleBean
public class ServiceBean extends ApplicationBean {
    @Inject
    private String ref;
    @Inject(alias="interface")
    private String inter;
    @Inject
    private String version;
    @Inject
    private String timeout;

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getInter() {
        return inter;
    }

    public void setInter(String inter) {
        this.inter = inter;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getTimeout() {
        return timeout;
    }

    public void setTimeout(String timeout) {
        this.timeout = timeout;
    }

    public Class<? extends DubboBean> getBeanClass() {
        // TODO Auto-generated method stub
        return ServiceBean.class;
    }

    public String getName() {
        // TODO Auto-generated method stub
        return getId();
    }
}
