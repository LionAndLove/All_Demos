package com.luo.dubbo.context2;

import java.util.Map;
import org.springframework.context.support.FileSystemXmlApplicationContext;

/***
 * Main 启动方式的 spring
 * 
 * @author HadLuo
 * @since JDK1.7
 * @history 2017年12月15日 新建
 */
public class FileSystemXmlAppSpringContext extends SpringContext {

    /**
     * FileSystemXmlAppSpringContext new之后, 就会调用该方法
     */
    public void onCreate() {
        // TODO Auto-generated method stub
        // 读取配置文件, 初始化BeanFactory
        setBeanFactory(new FileSystemXmlApplicationContext(StaticResource.springConfigLocations()));
    }

    public <T> Map<String, T> getBeansOfType(Class<T> type) {
        // TODO Auto-generated method stub
        return ((FileSystemXmlApplicationContext) spring()).getBeansOfType(type);
    }

}
