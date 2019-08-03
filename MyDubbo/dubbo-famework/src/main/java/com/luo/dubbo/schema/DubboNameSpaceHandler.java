package com.luo.dubbo.schema;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;
import com.luo.dubbo.schema.bean.ProtocolBean;
import com.luo.dubbo.schema.bean.ReferenceBean;
import com.luo.dubbo.schema.bean.RegistryBean;
import com.luo.dubbo.schema.bean.ServiceBean;

/***
 * dubbo自定义标签处理器
 * 
 * @author HadLuo
 * @since JDK1.7
 * @history 2017年11月15日 新建
 */
public class DubboNameSpaceHandler extends NamespaceHandlerSupport {
    /**
     * dubbo 标签初始化
     */
    public void init() {
        registerBeanDefinitionParser("service", new BeanParser(ServiceBean.class));

        registerBeanDefinitionParser("registry", new BeanParser(RegistryBean.class));

        registerBeanDefinitionParser("reference", new BeanParser(ReferenceBean.class));

        registerBeanDefinitionParser("protocol", new BeanParser(ProtocolBean.class));
    }
}
