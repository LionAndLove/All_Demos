package com.jikeh;

import com.jikeh.http.HttpClientUtils;

/**
 * 断路器测试：
 *
 * @author 极客慧 www.jikeh.cn
 *
 */
public class CircuitBreakerTest {

	/**
	 * 实验实录：
	 * 	1、前10次请求为正常请求
	 * 	打印结果为：
	 * 		System.out.println("第"+count+"次调用接口，开始查询广告数据，adId=" + adId);
	 * 		System.out.println("调用接口，成功查询广告数据，res=" + response);
	 *
	 * 	2、 后面3次请求，断路器 open
	 *
	 * 	3、经过一段时间，断路器 half-open
	 *
	 * 	4、再后面3次请求，断路器 close
	 *
	 *
	 *
	 *
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		for(int i = 0; i < 10; i++) {
			String response = HttpClientUtils.sendGetRequest("http://localhost:8022/get/ad?adId=1");
			System.out.println("第" + (i + 1) + "次请求，结果为：" + response);  
		}

		Long start = System.currentTimeMillis();

		/**
		 * 当消耗时间 > withMetricsRollingStatisticalWindowInMilliseconds的时候
		 *
		 * 滑动时间窗范围内：
		 * 	1、请求次数 >= withCircuitBreakerRequestVolumeThreshold
		 * 	2、错误比例 >= withCircuitBreakerErrorThresholdPercentage
		 */
		for(int i = 0; i < 3; i++) {
			String response = HttpClientUtils.sendGetRequest("http://localhost:8022/get/ad?adId=-1");
			Thread.sleep(2000);
			Long end = System.currentTimeMillis();
			System.out.printf("消耗时间："+ (end - start));
			System.out.println("\n第" + (i + 1) + "次请求，结果为：" + response);
		}

		Long end = System.currentTimeMillis();
		System.out.printf("断路器 open begin，消耗时间=" + (end - start));

		Thread.sleep(2000);

		//降级处理
		for(int j = 0; j < 3; j++) {
			String response = HttpClientUtils.sendGetRequest("http://localhost:8022/get/ad?adId=1");
			System.out.println("\n第" + (j + 1) + "次请求，结果为：" + response);
		}

		/**
		 * 从open状态开始到close状态经历的时间 >= withCircuitBreakerSleepWindowInMilliseconds
		 * 断路器处理half-open状态：试探一条请求过去，如果能正常返回，则断路器打开，否则一直处于打开状态
		 */
		Thread.sleep(4000);
		Long end2 = System.currentTimeMillis();
		System.out.printf("断路器 half-open begin，消耗时间=" + (end2 - end));

		for(int i = 0; i < 3; i++) {
			String response = HttpClientUtils.sendGetRequest("http://localhost:8022/get/ad?adId=1");
			System.out.println("第" + (i + 1) + "次请求，结果为：" + response);  
		}
	}
	
}
