package com.luo.dubbo.schema.bean;

import com.luo.dubbo.schema.bean.annotation.Inject;
import com.luo.dubbo.schema.bean.annotation.SingleBean;

/***
 * 注册中心 信息配置
 * 
 * @author HadLuo
 * @since JDK1.7
 * @history 2017年11月16日 新建
 */
@SingleBean
public class RegistryBean extends ApplicationBean{
    // 注册中心地址
    @Inject
    private String address ;
    
    // 注册中心的 实现类
    @Inject
    private String implClass ;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImplClass() {
        return implClass;
    }

    public void setImplClass(String implClass) {
        this.implClass = implClass;
    }

    public Class<? extends DubboBean> getBeanClass() {
        // TODO Auto-generated method stub
        return RegistryBean.class;
    }

    public String getName() {
        // TODO Auto-generated method stub
        return getId();
    }
}
