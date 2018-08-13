package com.jikeh.elasticjob.lite.autoconfigure;

import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperConfiguration;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({ZookeeperConfigurationProperties.class})
public class RegistryCenterConfiguration {

    @Bean(initMethod = "init")
    @ConditionalOnMissingBean
    public ZookeeperRegistryCenter regCenter(ZookeeperConfigurationProperties properties) {
        ZookeeperConfiguration zookeeperConfiguration = new ZookeeperConfiguration(properties.getServerLists(), properties.getNamespace());
        zookeeperConfiguration.setBaseSleepTimeMilliseconds(properties.getBaseSleepTimeMilliseconds());
        zookeeperConfiguration.setMaxSleepTimeMilliseconds(properties.getMaxSleepTimeMilliseconds());
        zookeeperConfiguration.setMaxRetries(properties.getMaxRetries());
        return new ZookeeperRegistryCenter(zookeeperConfiguration);
    }
}
