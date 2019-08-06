package com.hadluo.dubbo.schema;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import com.hadluo.dubbo.schema.bean.ApplicationBean;
import com.hadluo.dubbo.schema.bean.ProtocolBean;
import com.hadluo.dubbo.schema.bean.RegistryBean;
import com.hadluo.dubbo.schema.bean.ServiceBean;

public class DubboIdStrategy implements IdStrategy {
    private static AtomicLong ID_COUNTER = new AtomicLong(0);

    private static ConcurrentHashMap<Class<?>, String> FIX_ID_CACHE = new ConcurrentHashMap<Class<?>, String>();

    static {
        FIX_ID_CACHE.putIfAbsent(ApplicationBean.class, "dubbo_application$");
        FIX_ID_CACHE.putIfAbsent(ProtocolBean.class, "dubbo_protocol$");
        FIX_ID_CACHE.putIfAbsent(RegistryBean.class, "dubbo_registry$");
    }

    @Override
    public String id(Class<?> beanClass) {
        if (FIX_ID_CACHE.contains(beanClass)) {
            return FIX_ID_CACHE.get(beanClass);
        }

        if (beanClass == ServiceBean.class) {
            return "dubbo_service_" + ID_COUNTER.getAndIncrement();
        }
        throw new UnsupportedOperationException("dubbo暂时不支持 " + beanClass.getSimpleName() + " bean");
    }

    public String getFixBeanId(Class<?> beanClass) {
        if (!FIX_ID_CACHE.containsKey(beanClass)) {
            return "";
        }
        return FIX_ID_CACHE.get(beanClass);
    }
}
