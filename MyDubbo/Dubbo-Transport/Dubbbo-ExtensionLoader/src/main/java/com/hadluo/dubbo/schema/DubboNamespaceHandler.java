package com.hadluo.dubbo.schema;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;
import com.hadluo.dubbo.schema.bean.ApplicationBean;
import com.hadluo.dubbo.schema.bean.ProtocolBean;
import com.hadluo.dubbo.schema.bean.RegistryBean;
import com.hadluo.dubbo.schema.bean.ServiceBean;

public class DubboNamespaceHandler extends NamespaceHandlerSupport {

    @Override
    public void init() {

        registerBeanDefinitionParser("application", new DubboBeanDefinitionParser(ApplicationBean.class));

        registerBeanDefinitionParser("registry", new DubboBeanDefinitionParser(RegistryBean.class));

        registerBeanDefinitionParser("service", new DubboBeanDefinitionParser(ServiceBean.class));

        registerBeanDefinitionParser("protocol", new DubboBeanDefinitionParser(ProtocolBean.class));
    }

}
