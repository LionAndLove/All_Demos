package com.hadluo.dubbo.schema;

/***
 * <dubbo:protocol name="dubbo" port="20880" />
 * 
 * @author HadLuo
 * @since JDK1.7
 * @history 2018年4月28日 新建
 */
public class ProtocolConfig extends AbstractConfig {
    @Tag("name")
    private String name;
    @Tag("port")
    private int port;

    public String getName() {
        return name;
    }

    public int getPort() {
        return port;
    }

}
