package com.luo.dubbo.context2;

import java.util.Map;
import org.jboss.netty.util.internal.ConcurrentHashMap;

public final class ContextFactory {

    private static volatile boolean isWeb = true;

    public enum ContextEnum {
        SPRING, DUBBO
    }

    /***
     * 容器 管理器存储 key: 容器class val: 容器实例
     */
    private static final Map<ContextEnum, Context> contexts = new ConcurrentHashMap<ContextEnum, Context>();

    /***
     * main启动方式调用
     * 
     * @author HadLuo 2017年12月15日 新建
     */
    public static void init() {
        isWeb = false;
        DubboContext dubbo = new DubboContext();
        FileSystemXmlAppSpringContext spring = new FileSystemXmlAppSpringContext();
        // 构造spring
        putContext(ContextEnum.SPRING, spring);
        // 构造dubbo
        putContext(ContextEnum.DUBBO, dubbo);
        spring.onCreate();
        dubbo.onCreate();
    }

    public static void initWeb() {
        if (isWeb) {
            System.err.println("开始加载dubbo容器....");
            spring().onCreate();
            dubbo().onCreate();
        }
    }

    /***
     * 存在 先 关闭容器 ，在重新添加
     * 
     * @param clazz
     * @param container
     * @author HadLuo 2017年11月15日 新建
     */
    private static void putContext(ContextEnum contextEnum, Context context) {
        contexts.put(contextEnum, context);
    }

    /***
     * 获取spring容器
     * 
     * @return
     * @author HadLuo 2017年12月15日 新建
     */
    public static SpringContext spring() {
        SpringContext springContext = (SpringContext) contexts.get(ContextEnum.SPRING);
        if (springContext == null) {
            springContext = new WebAppSpringContext();
            putContext(ContextEnum.SPRING, springContext);
        }
        return springContext;
    }

    /***
     * 获取dubbo容器
     * 
     * @return
     * @author HadLuo 2017年12月15日 新建
     */
    public static DubboContext dubbo() {
        DubboContext dubbo = (DubboContext) contexts.get(ContextEnum.DUBBO);
        if (dubbo == null) {
            dubbo = new DubboContext();
            putContext(ContextEnum.DUBBO, dubbo);
        }
        return dubbo;
    }

    public static boolean isWeb() {
        return isWeb;
    }

}
