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

package com.jikeh.job.simple;

import com.alibaba.fastjson.JSONObject;
import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.jikeh.http.HttpClientUtils;
import com.jikeh.model.Ad;
import com.jikeh.redis.RedisManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 意为简单实现，未经任何封装的类型。需实现SimpleJob接口。该接口仅提供单一方法用于覆盖，此方法将定时执行。与Quartz原生接口相似，但提供了弹性扩缩容和分片等功能。
 */
public class SpringSimpleJob implements SimpleJob {

    /**
     * 定时任务：全量刷新db源数据到redis
     * 缓存预热：可以定时触发，也可以直接登录管控台进行手动触发
     * 缓存更新：定时全量更新redis指定key的数据，并重新刷入数据，因为redis中的key有缓存失效时间(无效的key会自动删除，没有无效的key变会更新)
     * @param shardingContext
     */
    @Override
    public void execute(final ShardingContext shardingContext) {

        System.out.println(String.format("Item: %s | Time: %s | Thread: %s | %s",
                shardingContext.getShardingItem(), new SimpleDateFormat("HH:mm:ss").format(new Date()), Thread.currentThread().getId(), "SIMPLE"));

        String url = "http://localhost:1111/ad-manage/api/getAds";
        String res = HttpClientUtils.sendGetRequest(url);
        List<Ad> ads = JSONObject.parseArray(res, Ad.class);

        RedisManager manager = RedisManager.getInstance();

        //重新刷
        for(Ad ad : ads){
            String key = "ad_info_" + ad.getId();
            //刷入数据到redis：
            manager.setStrWithExpire(key, JSONObject.toJSONString(ad), 24*3600);
        }

    }
}
