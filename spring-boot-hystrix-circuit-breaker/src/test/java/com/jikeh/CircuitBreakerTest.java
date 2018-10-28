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
	 * 	1、前10次请求为请求(正常)
	 * 	打印结果为：
	 * 		System.out.println("第"+count+"次调用接口，开始查询广告数据，adId=" + adId);
	 * 		System.out.println("调用接口，成功查询广告数据，res=" + response);
	 *
	 * 	2、 后面3次请求(异常)，结果：熔断器 open
	 * 	打印结果为：
	 * 		System.out.println("第"+count+"次调用接口，开始查询广告数据，adId=" + adId);
	 * 		System.out.println("降级：从本地缓存获取过期的广告数据，adId=" + adId);
	 *
	 * 	3、后面3次请求(正常)，结果：直接被熔断器给过滤降级
	 * 	打印结果为：
	 * 		System.out.println("降级：从本地缓存获取过期的广告数据，adId=" + adId);
	 *
	 * 	4、经过一段时间，断路器 half-open
	 *
	 * 	5、再后面3次请求(正常)，断路器 close
	 * 	打印结果为：
	 * 		System.out.println("第"+count+"次调用接口，开始查询广告数据，adId=" + adId);
	 * 		System.out.println("调用接口，成功查询广告数据，res=" + response);
	 *
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		System.out.println("前10次请求为请求(正常)=================================================");
		for(int i = 0; i < 10; i++) {
			String response = HttpClientUtils.sendGetRequest("http://localhost:8022/get/ad?adId=1");
		}

		System.out.println("后面3次请求(异常)，熔断器开始统计异常信息===============================");
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
		}

		System.out.println("后面3次请求(正常)：此时熔断器已经处于 open begin====================================================");

		//熔断器统计也需要一些时间，我们再多加500ms
		Thread.sleep(500);

		//降级处理
		for(int j = 0; j < 3; j++) {
			String response = HttpClientUtils.sendGetRequest("http://localhost:8022/get/ad?adId=1");
		}

		/**
		 * 从open状态开始到close状态经历的时间 >= withCircuitBreakerSleepWindowInMilliseconds
		 * 断路器处理half-open状态：试探一条请求过去，如果能正常返回，则断路器打开，否则一直处于打开状态
		 */
		Thread.sleep(6000);
		System.out.println("再后面3次请求(正常)：此时断路器会经过两种状态的变化half-open begin and open close");

		for(int i = 0; i < 3; i++) {
			String response = HttpClientUtils.sendGetRequest("http://localhost:8022/get/ad?adId=1");
		}
	}
	
}
