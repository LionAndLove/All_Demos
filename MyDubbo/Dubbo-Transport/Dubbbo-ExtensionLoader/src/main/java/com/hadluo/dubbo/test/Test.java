package com.hadluo.dubbo.test;

import com.hadluo.dubbo.extension.ExtensionLoader;

public class Test {

    public static void main(String[] args) {
        // 调用 key 为 post 的http组件
        ExtensionLoader.getExtensionLoader(Http.class).getAdaptiveExtension("post").http();
        // 这个 直接使用注解配置的 key
        ExtensionLoader.getExtensionLoader(Http.class).getAdaptiveExtension().http();
    }
}
