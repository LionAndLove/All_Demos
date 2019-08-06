package com.hadluo.dubbo.schema;

import java.lang.reflect.Field;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;
import com.hadluo.dubbo.common.ReflectUtils;

public class DubboBeanDefinitionParser implements BeanDefinitionParser {

    private Class<?> beanClass;
    private static final DubboIdStrategy ID_STRATEGY = new DubboIdStrategy();

    public DubboBeanDefinitionParser(Class<?> beanClass) {
        this.beanClass = beanClass;
    }

    @Override
    public BeanDefinition parse(Element element, ParserContext parserContext) {
        RootBeanDefinition beanDefinition = new RootBeanDefinition();
        beanDefinition.setBeanClass(beanClass);
        beanDefinition.setLazyInit(false);
        String id = element.getAttribute("id");

        if (id == null || id.isEmpty()) {
            id = IdGenerator.generateId(ID_STRATEGY, beanClass);
        }

        if (parserContext.getRegistry().containsBeanDefinition(id)) {
            throw new IllegalStateException("Duplicate spring bean id " + id);
        }
        parserContext.getRegistry().registerBeanDefinition(id, beanDefinition);

        // 设置属性值
        for (Field field : ReflectUtils.getAllFieldsByAnnotation(beanClass, Tag.class)) {
            String tag = field.getAnnotation(Tag.class).value();
            beanDefinition.getPropertyValues().addPropertyValue(field.getName(),
                    getValueByType(field, element.getAttribute(tag)));
        }
        return beanDefinition;
    }

    /***
     * 设置字段值
     * 
     * @param field
     * @param target
     * @param val
     * @throws NumberFormatException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @author HadLuo 2018年4月13日 新建
     */
    @SuppressWarnings("rawtypes")
    private static Object getValueByType(Field field, String val) {
        if (val == null) {
            return null;
        }
        if (field.getType().equals(Integer.class) || field.getType().equals(int.class)) {
            return Integer.parseInt(val);
        } else if (field.getType().equals(Boolean.class) || field.getType().equals(boolean.class)) {
            return Boolean.parseBoolean(val);
        } else if (field.getType().equals(Byte.class) || field.getType().equals(byte.class)) {
            return Byte.parseByte(val);
        } else if (field.getType().equals(Character.class) || field.getType().equals(char.class)) {
            return val.charAt(0);
        } else if (field.getType().equals(Double.class) || field.getType().equals(double.class)) {
            return Double.parseDouble(val);
        } else if (field.getType().equals(Float.class) || field.getType().equals(float.class)) {
            return Float.parseFloat(val);
        } else if (field.getType().equals(Long.class) || field.getType().equals(long.class)) {
            return Long.parseLong(val);
        } else if (field.getType().equals(Short.class) || field.getType().equals(short.class)) {
            return Short.parseShort(val);
        } else if (field.getType().equals(String.class)) {
            return val;
        } else if (ReflectUtils.isChild(field.getType(), Holder.class)) {
            // 代表 是 对象 类型 , 从spring里面 找
            String id = ID_STRATEGY.getFixBeanId(field.getType());
            if (id == null || id.isEmpty()) {
                throw new UnsupportedOperationException(field.getType().getName() + " 不能赋值为 :" + val);
            }
            return new Holder(id);
        }
        throw new UnsupportedOperationException(field.getType().getName() + " 不能赋值为 :" + val);
    }
}
