package com.hadluo.dubbo;

import com.hadluo.dubbo.extension.ExtensionLoader;
import com.hadluo.dubbo.http.Http;

/**
 * 将该jar包打包发布出去：调用者即可使用相应的实现类(按key调用指定的实现类)
 */
public class Test {

    public static void main(String[] args) {
        // 调用 key 为 post 的http组件
        Http postHttp = ExtensionLoader.getExtensionLoader(Http.class).getAdaptiveExtension("post");
        postHttp.http();
        // 这个 直接使用注解配置的 key
        Http getHttp = ExtensionLoader.getExtensionLoader(Http.class).getAdaptiveExtension();
        getHttp.http();
    }

}
