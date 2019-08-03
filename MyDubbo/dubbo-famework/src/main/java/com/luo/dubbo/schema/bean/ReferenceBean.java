package com.luo.dubbo.schema.bean;

import com.luo.dubbo.schema.bean.annotation.Inject;
import com.luo.dubbo.schema.bean.annotation.MultipleBean;

/***
 * <dubbo:reference>标签的 bean
 * 
 * @author HadLuo
 * @since JDK1.7
 * @history 2017年11月15日 新建
 */
@MultipleBean
public class ReferenceBean extends ApplicationBean {
    @Inject(alias = "interface")
    private String inter;

    public void setInter(String inter) {
        this.inter = inter;
    }

    public String getInter() {
        return inter;
    }

    public Class<? extends DubboBean> getBeanClass() {
        return ReferenceBean.class;
    }

    public String getName() {
        // TODO Auto-generated method stub
        return getId();
    }

}
