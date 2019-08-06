package com.hadluo.dubbo.extension;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public final class ExtensionLoader<T> {

    private static final String CONTEXT_PATH = "META-INF/services/";
    /** 当前扩展类接口 */
    private Class<T> extensionInterfaceClass;

    private final Holder<Object> dynamicProxy = new Holder<Object>();
    /** 扩展类的所有class实现集合 */
    private ConcurrentHashMap<String, Class<?>> extensionImplClasses = new ConcurrentHashMap<String, Class<?>>();
    /** 扩展类的所有class实现 对象集合 */
    private ConcurrentHashMap<Class<?>, Object> extensionImplInstance = new ConcurrentHashMap<Class<?>, Object>();
    /** 扩展类的所有class实现代理 对象集合 */
    private ConcurrentHashMap<String, Object> extensionImplProxyInstance = new ConcurrentHashMap<String, Object>();

    private static ConcurrentHashMap<Class<?>, ExtensionLoader<?>> EXTENSION_LOADERS = new ConcurrentHashMap<Class<?>, ExtensionLoader<?>>();

    public static <T> ExtensionLoader<T> getExtensionLoader(Class<T> extensionInterfaceClass) {
        if (null == extensionInterfaceClass) {
            throw new NullPointerException();
        }
        aseertAnnotation(extensionInterfaceClass);
        @SuppressWarnings("unchecked")
        ExtensionLoader<T> loader = (ExtensionLoader<T>) EXTENSION_LOADERS.get(extensionInterfaceClass);
        if (null == loader) {
            loader = new ExtensionLoader<T>(extensionInterfaceClass);
            // putIfAbsent : 不存在 ，就插入map ,且返回null
            EXTENSION_LOADERS.putIfAbsent(extensionInterfaceClass, loader);
        }
        return loader;
    }

    private static <T> void aseertAnnotation(Class<T> extensionInterfaceClass) {
        if (!extensionInterfaceClass.isAnnotationPresent(SPI.class)) {
            throw new RuntimeException("extension interface must be has SPI Annotation");
        }
    }

    private ExtensionLoader(Class<T> extensionInterfaceClass) {
        this.extensionInterfaceClass = extensionInterfaceClass;
    }

    @SuppressWarnings("unchecked")
    public synchronized T getAdaptiveExtension() {
        // 生成 extensionImplObject 的代理对象
        if (dynamicProxy.getValue() == null) {
            dynamicProxy.setvValue(ExtensionProxyFactory.newDynamicProxy(extensionInterfaceClass,
                    findClassloader()));
        }
        return (T) dynamicProxy.getValue();
    }

    @SuppressWarnings("unchecked")
    public synchronized T getAdaptiveExtension(String key) {
        Object proxy = extensionImplProxyInstance.get(key);
        if (proxy != null) {
            return (T) proxy;
        }
        // 取得配置文件实现类
        Class<?> extensionClass = extensionImplClasses.get(key);
        if (extensionClass == null) {
            loadExtensionImplClass(key);
            extensionClass = extensionImplClasses.get(key);
            if (extensionClass == null) {
                System.err.println(CONTEXT_PATH + " 路径下找不到Extension实现类, Extension key: " + key
                        + "，Extension interface:" + extensionInterfaceClass.getName());
                return null;
            }
        }
        Object extensionImplObject = extensionImplInstance.get(extensionClass);
        if (null == extensionImplObject) {
            try {
                extensionImplObject = extensionClass.newInstance();
                // putIfAbsent : 不存在 ，就插入map ,且返回null
                extensionImplInstance.putIfAbsent(extensionClass, extensionImplObject);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        // 生成 extensionImplObject 的代理对象
        T t = ExtensionProxyFactory.newFixProxy(extensionInterfaceClass, extensionImplObject,
                findClassloader());
        // 设置进缓存
        extensionImplProxyInstance.putIfAbsent(key, t);
        return t;
    }

    /***
     * 加载 对应key 的 Extension实现类
     * 
     * @param key
     * @author HadLuo 2018年4月20日 新建
     */
    private void loadExtensionImplClass(String key) {
        // 已经加载过了
        if (!extensionImplClasses.isEmpty()) {
            return;
        }
        // 加载 扫描 Extension配置文件
        HashMap<String, Class<?>> results = scanAllExtensioClasses();
        // 存到缓存
        extensionImplClasses.putAll(results);
    }

    /***
     * 扫描 所有的 Extension类
     * 
     * @author HadLuo 2018年4月20日 新建
     */
    private HashMap<String, Class<?>> scanAllExtensioClasses() {
        HashMap<String, Class<?>> instances = new HashMap<String, Class<?>>();
        // 类似 META-INF/dubbo/internal/com.alibaba.dubbo.cache.CacheFactory
        // 文件内容：
        // threadlocal=com.alibaba.dubbo.cache.support.threadlocal.ThreadLocalCacheFactory
        // lru=com.alibaba.dubbo.cache.support.lru.LruCacheFactory
        // jcache=com.alibaba.dubbo.cache.support.jcache.JCacheFactory
        String fileName = CONTEXT_PATH + extensionInterfaceClass.getName();
        ClassLoader classLoader = findClassloader();
        Enumeration<java.net.URL> urls;
        try {
            if (classLoader != null) {
                urls = classLoader.getResources(fileName);
            } else {
                urls = ClassLoader.getSystemResources(fileName);
            }
            if (urls == null) {
                System.err.println("没有扫描到ExtensionLoader 配置");
                return instances;
            }
            while (urls.hasMoreElements()) {
                java.net.URL url = urls.nextElement();
                BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), "utf-8"));
                String line = null;
                while ((line = reader.readLine()) != null) {
                    String[] args = line.split("=");
                    if (args == null || args.length == 0 || args.length != 2) {
                        throw new RuntimeException("Extensio配置文件错误： file:" + fileName);
                    }
                    instances.put(args[0].trim(), findClassloader().loadClass(args[1].trim()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("加载配置失败");
        }
        return instances;
    }

    private ClassLoader findClassloader() {
        return Thread.currentThread().getContextClassLoader();
    }
}
