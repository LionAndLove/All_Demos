package com.hadluo.dubbo.invoker;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import com.hadluo.dubbo.common.ClassGenerator;

public abstract class Wrapper {
    private static AtomicLong WRAPPER_CLASS_COUNTER = new AtomicLong(0);

    private static ConcurrentHashMap<Class<?>, Wrapper> WRAPPERS = new ConcurrentHashMap<Class<?>, Wrapper>();

    public static Wrapper getWrapper(Class<?> type) {
        Wrapper wrapper = WRAPPERS.get(type);
        if (wrapper == null) {
            wrapper = makeWrapper(type);
            WRAPPERS.putIfAbsent(type, wrapper);
        }
        return wrapper;
    }

    abstract public String[] getPropertyNames();

    abstract public Class<?> getPropertyType(String pn);

    abstract public boolean hasProperty(String name);

    abstract public Object getPropertyValue(Object instance, String pn) throws Throwable;

    abstract public void setPropertyValue(Object instance, String pn, Object pv) throws Throwable;

    abstract public Object invokeMethod(Object instance, String methodName, Class<?>[] types, Object[] args)
            throws Throwable;

    private static Wrapper makeWrapper(Class<?> type) {
        ClassGenerator classGenerator = ClassGenerator.newInstance(Wrapper.class.getClassLoader());
        // extends Wrapper.class
        classGenerator.setSuperClass(Wrapper.class);
        // class 名称
        classGenerator.setClassName(Wrapper.class.getName() + WRAPPER_CLASS_COUNTER.getAndIncrement() + "$");

        // public static Map<String, Class<?>> pts; // key: 属性名称 value:属性类型
        classGenerator.addField("public static " + Map.class.getName() + " pts;");
        // 属性名称 集合
        classGenerator.addField("public static String[] pNames;");

        // getPropertyNames 抽象方法实现
        classGenerator.addMethod("public String[] getPropertyNames(){return pNames;}");
        // getPropertyType 抽象方法实现
        classGenerator
                .addMethod("public Class getPropertyType(String pName) { return (Class)pts.get(pName); }");
        // hasProperty 抽象方法实现
        classGenerator
                .addMethod("public boolean hasProperty(String name){  return pts.containsKey(name) ; }");
        // getPropertyValue 抽象方法实现
        classGenerator
                .addMethod("public Object getPropertyValue(Object instance, String pName) throws Throwable { "
                        + "try {"
                        + "java.lang.Class clazz = instance.getClass();"
                        + "java.lang.reflect.Field f = clazz.getDeclaredField(pName);"
                        + "return f.get(instance);" + "} catch (Exception e) {throw e;} }");
        // setPropertyValue 抽象方法实现
        classGenerator
                .addMethod("public void setPropertyValue(Object instance, String pName, Object pValue) throws Throwable {"
                        + "try {"
                        + "java.lang.reflect.Field f = instance.getClass().getDeclaredField(pName);"
                        + "f.set(instance, pValue);" + "} catch (Exception e) { throw e;}}");
        // invokeMethod 抽象方法实现
        classGenerator
                .addMethod("public Object invokeMethod(Object instance, String methodName, Class[] types, Object[] args) throws Throwable {"
                        + "try {"
                        + "java.lang.Class clazz = instance.getClass();"
                        + "java.lang.reflect.Method method = clazz.getDeclaredMethod(methodName, types);"
                        + "if (method == null) { throw new NullPointerException(\"没有 在 \" + clazz.getName() + \" 中 找到 方法: \" + methodName);}"
                        + "return method.invoke(instance, args);" + "} catch (Exception e) {throw e;}}");

        try {
            Class<?> proxyClass = classGenerator.toClass();
            if (proxyClass == null) {
                throw new RuntimeException("生成动态类型错误");
            }
            Map<String, Class<?>> pts = new HashMap<String, Class<?>>();
            for (Field f : type.getDeclaredFields()) {
                pts.put(f.getName(), f.getType());
            }
            // 跟动态类 的静态字段设置值
            proxyClass.getDeclaredField("pts").set(null, pts);
            proxyClass.getDeclaredField("pNames").set(null, pts.keySet().toArray(new String[0]));

            return (Wrapper) proxyClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}