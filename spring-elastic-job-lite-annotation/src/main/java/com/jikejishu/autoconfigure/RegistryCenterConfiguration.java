package com.jikejishu.autoconfigure;

import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperConfiguration;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 更多免费资料，更多高清内容，更多java技术，欢迎访问网站
 * 极客慧：www.jikeh.cn
 * 如果你希望进一步深入交流，请加入我们的大家庭QQ群：375412858
 */
@Configuration
public class RegistryCenterConfiguration {

    @Value("${serverLists}")
    private String serverLists;

    @Value("${namespace}")
    private String nameSpace;

    @Bean(initMethod = "init")
    public ZookeeperRegistryCenter regCenter() {
        ZookeeperConfiguration zookeeperConfiguration = new ZookeeperConfiguration(serverLists, nameSpace);
        return new ZookeeperRegistryCenter(zookeeperConfiguration);
    }
}
