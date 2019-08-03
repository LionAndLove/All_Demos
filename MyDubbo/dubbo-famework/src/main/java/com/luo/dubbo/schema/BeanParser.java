package com.luo.dubbo.schema;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

import com.luo.dubbo.context2.ContextFactory;
import com.luo.dubbo.rpc.proxy.ProxyFactory;
import com.luo.dubbo.schema.bean.ApplicationBean;
import com.luo.dubbo.schema.bean.ReferenceBean;
import com.luo.dubbo.schema.bean.annotation.Inject;
import com.luo.dubbo.schema.bean.annotation.SingleBean;
import com.luo.dubbo.util.ReflectUtils;
import com.luo.dubbo.util.UUIDUtils;

public class BeanParser extends AbstractBeanParser {

    public static final String DUBBO = "dubbo:";

    public BeanParser(Class<?> beanClass) {
        super(beanClass);
        // TODO Auto-generated constructor stub
    }

    public BeanDefinition parse(Element element, ParserContext parserContext) {
        List<Field> fields = ReflectUtils.getAllFieldsByAnnotation(getBeanClass(), Inject.class);
        Map<String, Object> properties = new HashMap<String, Object>();
        RootBeanDefinition beanDefinition = springBeanDefinition();
        String val;
        for (Field field : fields) {
            String xmlFieldName = field.getAnnotation(Inject.class).alias();
            if (StringUtils.isEmpty(xmlFieldName)) {
                xmlFieldName = field.getName();
            }
            val = element.getAttribute(xmlFieldName);
            properties.put(field.getName(), val);
            beanDefinition.getPropertyValues().addPropertyValue(field.getName(), val);
        }
        String id = generateId();

        // ----------------对于  ReferenceBean 只是注册一个动态接口的空对象 占位到spring中
        if (getBeanClass().equals(ReferenceBean.class)) {
            id = element.getAttribute("id");
            try {
                beanDefinition = springBeanDefinition();
                Class<?> impl = ProxyFactory.buildImplClasses(Class.forName(element.getAttribute("interface")) , ApplicationBean.class) ;
                beanDefinition.setBeanClass(impl);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                throw new RuntimeException();
            }
        }
//
//        RootBeanDefinition SpringUtils = new RootBeanDefinition();
//        beanDefinition.setBeanClass(SpringUtils.class);
//        beanDefinition.setLazyInit(false);
//        parserContext.getRegistry().registerBeanDefinition("SpringUtils",SpringUtils);
        
        
        
        // 将值 设置到dubbo容器中
        ContextFactory.dubbo().setBean(id, getBeanClass(), properties);

        // 向spring注册bean
        parserContext.getRegistry().registerBeanDefinition(id, beanDefinition);
        
        return beanDefinition;
    }

    private String generateId() {
        if (getBeanClass().isAnnotationPresent(SingleBean.class)) {
            return DUBBO + getBeanClass().getName();
        } else {
            return DUBBO + getBeanClass().getName() + UUIDUtils.shortUuid();
        }
    }

}
