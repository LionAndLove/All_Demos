package com.jikeh.bolt;

import com.jikeh.zk.ZooKeeperSession;
import org.apache.storm.shade.org.json.simple.JSONArray;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.trident.util.LRUMap;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 热门商品广告访问次数统计bolt：
 * @author 极客慧：www.jikeh.cn
 *
 */
public class AdCountBolt extends BaseRichBolt {

	private OutputCollector collector;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AdCountBolt.class);

	//<商品id, 商品次数>
	private LRUMap<Long, Long> adCountMap = new LRUMap<Long, Long>(1000);

	private static final int topN = 3;

	private ZooKeeperSession zkSession;

	private int taskid;

	@Override
	public void prepare(Map conf, TopologyContext context, OutputCollector collector) {
		this.collector = collector;
		this.zkSession = ZooKeeperSession.getInstance();
		this.taskid = context.getThisTaskId();

		//topN算法：将排名前N的广告商品存入zookeeper中
		new Thread(new AdCountThread()).start();

		//记录缓存了商品数据的所有taskIds
		initTaskId(context.getThisTaskId());
	}

	/**
	 * 记录缓存了商品数据的所有taskIds
	 * @param taskid
	 */
	private void initTaskId(int taskid) {
		
		zkSession.acquireDistributedLock();
		
		zkSession.createNode("/taskid-list");

		String taskidList = zkSession.getNodeData();

		LOGGER.info("【AdCountBolt获取到taskid list】taskidList=" + taskidList);

		if(!"".equals(taskidList)) {
			taskidList += "," + taskid;
		} else {
			taskidList += taskid;
		}
		
		zkSession.setNodeData("/taskid-list", taskidList);

		LOGGER.info("【AdCountBolt设置taskid list】taskidList=" + taskidList);
		
		zkSession.releaseDistributedLock();
	}

	/**
	 * topN算法：将排名前N的广告商品存入zookeeper中
	 */
	private class AdCountThread implements Runnable {
		
		public void run() {
			List<Map.Entry<Long, Long>> topnAdList = new ArrayList<Map.Entry<Long, Long>>();
			List<Long> adidList = new ArrayList<Long>();
			
			while(true) {
				try {
					topnAdList.clear();
					adidList.clear();
					
					if(adCountMap.size() == 0) {
						Utils.sleep(100);
						continue;
					}
					
					LOGGER.info("【AdCountThread打印adCountMap的长度】size=" + adCountMap.size());
					
					for(Map.Entry<Long, Long> adCountEntry : adCountMap.entrySet()) {
						if(topnAdList.size() == 0) {
							topnAdList.add(adCountEntry);
						} else {

							boolean bigger = false;
							
							for(int i = 0; i < topnAdList.size(); i++){
								Map.Entry<Long, Long> topnAdCountEntry = topnAdList.get(i);
								
								if(adCountEntry.getValue() > topnAdCountEntry.getValue()) {
									int lastIndex = topnAdList.size() < topN ? topnAdList.size() - 1 : topN - 2;
									for(int j = lastIndex; j >= i; j--) {
										if(j + 1 == topnAdList.size()) {
											topnAdList.add(null);
										}
										topnAdList.set(j + 1, topnAdList.get(j));
									}
									topnAdList.set(i, adCountEntry);
									bigger = true;
									break;
								}
							}
							
							if(!bigger) {
								if(topnAdList.size() < topN) {
									topnAdList.add(adCountEntry);
								}
							}
						}
					}
					
					// 获取到一个topn list
					for(Map.Entry<Long, Long> topnAdEntry : topnAdList) {
						adidList.add(topnAdEntry.getKey());
					}
					
					String topnAdListJSON = JSONArray.toJSONString(adidList);
					zkSession.createNode("/task-hot-ad-list-" + taskid);
					zkSession.setNodeData("/task-hot-ad-list-" + taskid, topnAdListJSON);
					LOGGER.info("【AdCountThread计算出一份top3热门广告商品列表】zk path=" + ("/task-hot-ad-list-" + taskid) + ", topnAdListJSON=" + topnAdListJSON);
					
					Utils.sleep(5000);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
	}

	@Override
	public void execute(Tuple tuple) {

		try {
			Long adId = tuple.getLongByField("adId");

			LOGGER.info("【AdCountBolt接收到一个商品id】 adId=" + adId);

			Long count = adCountMap.get(adId);
			if(count == null) {
				count = 0L;
			}
			count++;

			adCountMap.put(adId, count);

			LOGGER.info("【AdCountBolt完成热门广告商品访问次数统计】adId=" + adId + ", count=" + count);
			this.collector.ack(tuple);
		} catch (Exception e) {
			this.collector.fail(tuple);
		}

	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		
	}

}
