package com.jikeh.elasticjob.lite.autoconfigure;

import com.dangdang.ddframe.job.api.dataflow.DataflowJob;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.dataflow.DataflowJobConfiguration;
import com.dangdang.ddframe.job.config.simple.SimpleJobConfiguration;
import com.dangdang.ddframe.job.event.rdb.JobEventRdbConfiguration;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.lite.spring.api.SpringJobScheduler;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import com.jikeh.elasticjob.lite.annotation.ElasticDataflowJob;
import com.jikeh.elasticjob.lite.annotation.ElasticSimpleJob;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.Map;

/**
 * 更多免费资料，更多高清内容，更多java技术，欢迎访问网站
 * 极客慧：www.jikeh.cn
 * 如果你希望进一步深入交流，请加入我们的大家庭QQ群：375412858
 */
@Configuration
@EnableConfigurationProperties()
public class ElasticJobAutoConfiguration {

    @Resource
    private ApplicationContext applicationContext;

    @Resource
    private ZookeeperRegistryCenter zookeeperRegistryCenter;

    @PostConstruct
    public void initElasticJob() {
        initElasticSimpleJob();
        initElasticDataflowJob();
    }

    public void initElasticSimpleJob() {
        Map<String, SimpleJob> map = applicationContext.getBeansOfType(SimpleJob.class);

        for (Map.Entry<String, SimpleJob> entry : map.entrySet()) {
            SimpleJob simpleJob = entry.getValue();
            ElasticSimpleJob elasticSimpleJob = simpleJob.getClass().getAnnotation(ElasticSimpleJob.class);
            if (elasticSimpleJob.disabled()) {
                continue;
            }
            String cron = StringUtils.defaultIfBlank(elasticSimpleJob.cron(), elasticSimpleJob.value());
            SimpleJobConfiguration simpleJobConfiguration = new SimpleJobConfiguration(JobCoreConfiguration.newBuilder(simpleJob.getClass().getName(), cron, elasticSimpleJob.shardingTotalCount()).shardingItemParameters(elasticSimpleJob.shardingItemParameters()).build(), simpleJob.getClass().getCanonicalName());
            LiteJobConfiguration liteJobConfiguration = LiteJobConfiguration.newBuilder(simpleJobConfiguration).overwrite(false).build();

            String dataSourceRef = elasticSimpleJob.dataSource();
            if (StringUtils.isNotBlank(dataSourceRef)) {

                if (!applicationContext.containsBean(dataSourceRef)) {
                    throw new RuntimeException("not exist datasource [" + dataSourceRef + "] !");
                }

                DataSource dataSource = (DataSource) applicationContext.getBean(dataSourceRef);
                JobEventRdbConfiguration jobEventRdbConfiguration = new JobEventRdbConfiguration(dataSource);
                SpringJobScheduler jobScheduler = new SpringJobScheduler(simpleJob, zookeeperRegistryCenter, liteJobConfiguration, jobEventRdbConfiguration);
                jobScheduler.init();
            } else {
                SpringJobScheduler jobScheduler = new SpringJobScheduler(simpleJob, zookeeperRegistryCenter, liteJobConfiguration);
                jobScheduler.init();
            }
        }
    }

    public void initElasticDataflowJob() {
        Map<String, DataflowJob> map = applicationContext.getBeansOfType(DataflowJob.class);

        for (Map.Entry<String, DataflowJob> entry : map.entrySet()) {
            DataflowJob dataflowJob = entry.getValue();
            ElasticDataflowJob elasticDataflowJob = dataflowJob.getClass().getAnnotation(ElasticDataflowJob.class);
            if (elasticDataflowJob.disabled()) {
                continue;
            }

            String cron = StringUtils.defaultIfBlank(elasticDataflowJob.cron(), elasticDataflowJob.value());
            DataflowJobConfiguration simpleJobConfiguration = new DataflowJobConfiguration(JobCoreConfiguration.newBuilder(dataflowJob.getClass().getName(), cron, elasticDataflowJob.shardingTotalCount()).shardingItemParameters(elasticDataflowJob.shardingItemParameters()).build(), dataflowJob.getClass().getCanonicalName(), elasticDataflowJob.streamingProcess());
            LiteJobConfiguration liteJobConfiguration = LiteJobConfiguration.newBuilder(simpleJobConfiguration).overwrite(false).build();

            String dataSourceRef = elasticDataflowJob.dataSource();
            if (StringUtils.isNotBlank(dataSourceRef)) {

                if (!applicationContext.containsBean(dataSourceRef)) {
                    throw new RuntimeException("not exist datasource [" + dataSourceRef + "] !");
                }

                DataSource dataSource = (DataSource) applicationContext.getBean(dataSourceRef);
                JobEventRdbConfiguration jobEventRdbConfiguration = new JobEventRdbConfiguration(dataSource);
                SpringJobScheduler jobScheduler = new SpringJobScheduler(dataflowJob, zookeeperRegistryCenter, liteJobConfiguration, jobEventRdbConfiguration);
                jobScheduler.init();
            } else {
                SpringJobScheduler jobScheduler = new SpringJobScheduler(dataflowJob, zookeeperRegistryCenter, liteJobConfiguration);
                jobScheduler.init();
            }
        }
    }
}
