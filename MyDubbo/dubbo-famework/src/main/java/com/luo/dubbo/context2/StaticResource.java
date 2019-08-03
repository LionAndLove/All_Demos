package com.luo.dubbo.context2;

import java.util.List;

import org.springframework.util.CollectionUtils;

import com.luo.dubbo.exception.DubboConfigFileNotFound;
import com.luo.dubbo.util.ArrayUtils;
import com.luo.dubbo.util.Log;
import com.luo.dubbo.util.ResourceUtils;

public final class StaticResource {
    
    /***
     * 默认加载 spring配置文件的路径
     */
    private static final String DEFAULT_SPRING_LOCATION = "META-INF/spring";

    private static String[] args = null ;
    
    /***
     * 加载spring 的配置文件
     * @return
     * @author HadLuo  2017年12月15日 新建
     */
    public static String[] springConfigLocations(){
        String path = null;
        try {
            path = Thread.currentThread().getContextClassLoader().getResource(wrapperSpringArgs(args))
                    .getPath();
        } catch (Exception e) {
            Log.e("找不到路径: " + wrapperSpringArgs(args) );
            throw new DubboConfigFileNotFound(e.getMessage());
        }
        List<String> configLocations = ResourceUtils.listSpringXml(path);
        if (CollectionUtils.isEmpty(configLocations)) {
            throw new DubboConfigFileNotFound("未找到一个dubbo xml 配置文件");
        }
        return ArrayUtils.listToArray(String.class, configLocations);
    }
    
    /***
     * 默认加载META-INF 下的 配置文件
     * 
     * @param args
     * @return
     * @author HadLuo 2017年11月15日 新建
     */
    private static String wrapperSpringArgs(String[] args) {
        String path;
        if (ArrayUtils.isEmpty(args)) {
            return DEFAULT_SPRING_LOCATION;
        } else {
            path = args[0];
        }
        if (!path.startsWith("META-INF")) {
            path = "META-INF/" + path;
        }
        return path;
    }
}
