/**
 * 极客慧 www.jikeh.cn
 * 头条号：https://www.toutiao.com/c/user/52045142300/
 */

/*
 * Copyright 1999-2015 dangdang.com.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * </p>
 */

package com.jikeh.elasticjob.lite.job.dataflow;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.dataflow.DataflowJob;
import com.jikeh.elasticjob.lite.annotation.ElasticDataflowJob;
import com.jikeh.elasticjob.lite.annotation.ElasticSimpleJob;
import com.jikeh.elasticjob.lite.fixture.entity.Foo;
import com.jikeh.elasticjob.lite.fixture.repository.FooRepository;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Dataflow类型用于处理数据流，需实现DataflowJob接口。该接口提供2个方法可供覆盖，分别用于抓取(fetchData)和处理(processData)数据。
 * 可通过DataflowJobConfiguration配置是否流式处理。
 * 流式处理数据只有fetchData方法的返回值为null或集合长度为空时，作业才停止抓取，否则作业将一直运行下去；
 * 非流式处理数据则只会在每次作业执行过程中执行一次fetchData方法和processData方法，随即完成本次作业。
 * 如果采用流式作业处理方式，建议processData处理数据后更新其状态，避免fetchData再次抓取到，从而使得作业永不停止。
 * 流式数据处理参照TbSchedule设计，适用于不间歇的数据处理。
 */
/**
 * 更多免费资料，更多高清内容，更多java技术，欢迎访问网站
 * 极客慧：www.jikeh.cn
 * 如果你希望进一步深入交流，请加入我们的大家庭QQ群：375412858
 */
@ElasticDataflowJob(cron = "0/5 * * * * ?", shardingTotalCount=2, shardingItemParameters="0=Guangzhou,1=HangZhou")
@Component
public class SpringDataflowJob implements DataflowJob<Foo> {
    
    @Resource
    private FooRepository fooRepository;

    /**
     * 负责抓取数据：
     * @param shardingContext
     * @return
     */
    @Override
    public List<Foo> fetchData(final ShardingContext shardingContext) {
        List<Foo> data = fooRepository.findTodoData(shardingContext.getShardingParameter(), 10);
        if(!data.isEmpty()){
            System.out.println(String.format("Item: %s | Time: %s | Thread: %s | %s",
                    shardingContext.getShardingItem(), new SimpleDateFormat("HH:mm:ss").format(new Date()), Thread.currentThread().getId(), "DATAFLOW FETCH"));
        }
        return data;
    }

    /**
     * 负责处理数据：
     * @param shardingContext
     * @param data
     */
    @Override
    public void processData(final ShardingContext shardingContext, final List<Foo> data) {
        System.out.println(String.format("Item: %s | Time: %s | Thread: %s | %s",
                shardingContext.getShardingItem(), new SimpleDateFormat("HH:mm:ss").format(new Date()), Thread.currentThread().getId(), "DATAFLOW PROCESS"));
        for (Foo each : data) {
            System.out.println(String.format("Item: %s | data: %s ", shardingContext.getShardingItem(), each));
            fooRepository.setCompleted(each.getId());
        }
    }
}
