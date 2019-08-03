package com.luo.dubbo.context2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.CollectionUtils;
import com.luo.dubbo.schema.bean.DubboBean;
import com.luo.dubbo.schema.bean.DubboBeanFactory;
import com.luo.dubbo.schema.bean.annotation.SingleBean;

public abstract class AbstractDubboContext extends AbstractContext {

    /***
     * 能配置多个bean 的 dubbo 配置文件bean<br>
     * key: bean的class ,bean 对象
     */
    private final Map<Class<?>, List<DubboBean>> multipleBeanMap = new HashMap<Class<?>, List<DubboBean>>();

    /***
     * 只能配置一个bean 的 dubbo 配置文件bean<br>
     * key: bean的id ,bean 对象
     */
    private final Map<String, DubboBean> singleBeanMap = new HashMap<String, DubboBean>();

    public void onCreate() {
    }

    public void onDestory() {
        // TODO Auto-generated method stub

    }

    public Object getBean(String name) {
        return singleBeanMap.get(name);
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> type) {
        if (!type.isAnnotationPresent(SingleBean.class)) {
            throw new RuntimeException(
                    "public T getBean(Class<T> type) method must be configure SingleBean.class annotation");
        }
        T t;
        for (String key : singleBeanMap.keySet()) {
            t = (T) singleBeanMap.get(key);
            if (t.getClass().equals(type)) {
                return t;
            }
        }
        return null;
    }

    public void setBean(String name, Class<?> beanClass, Map<String, Object> properties) {
        // 构造bean对象
        DubboBean bean = DubboBeanFactory.createBean(beanClass, properties);
        if (beanClass.isAnnotationPresent(SingleBean.class)) {
            singleBeanMap.put(name, bean);
        } else {
            List<DubboBean> beanList = multipleBeanMap.get(beanClass);
            if (null == beanList) {
                ArrayList<DubboBean> val = new ArrayList<DubboBean>();
                val.add(bean);
                multipleBeanMap.put(beanClass, val);
            } else {
                beanList.add(bean);
            }
        }
    }

    @SuppressWarnings("unchecked")
    public <T> Map<String, T> getBeansOfType(Class<T> type) {
        if (type == null) {
            return new HashMap<String, T>(0);
        }
        Map<String, T> result = new HashMap<String, T>();
        // 查找单个
        if (type.isAnnotationPresent(SingleBean.class)) {
            DubboBean bean;
            for (String str : singleBeanMap.keySet()) {
                bean = singleBeanMap.get(str);
                if (bean.equals(type)) {
                    result.put(str, (T) bean);
                    break;
                }
            }
            return result;
        }
        // 查找 多个bean
        List<DubboBean> beanList = multipleBeanMap.get(type);
        if (CollectionUtils.isEmpty(beanList)) {
            return new HashMap<String, T>(0);
        }
        for (DubboBean bean : beanList) {
            result.put(bean.getName(), (T) bean);
        }
        return result;
    }
}
