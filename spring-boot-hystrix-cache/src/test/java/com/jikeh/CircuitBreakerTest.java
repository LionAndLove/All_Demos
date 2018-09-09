package com.jikeh;

import com.jikeh.http.HttpClientUtils;

/**
 * 熔断器测试：无非就掌握三种状态的转换(open——half——open)
 * 	工作原理：在滑动时间窗口内(10s)，到达熔断器的流量达到一定值(20)，且在该时间窗口内的错误比例达到一定值(50%)，熔断器打开(open)
 * 	从open开始，再过一定时间(5s)，熔断器处于half-open状态，此时允许某个请求过去，如果请求正常，则熔断器关闭(close)
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
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws InterruptedException {

		//测试熔断器原理：
//		testCircuitBreaker();

		//测试资源池被占满的情况：
//		testResourceIsFull();

		//测试timeout机制引起的降级
		testTimeout();

	}

	/**
	 * 测试熔断器原理：
	 *
	 */
	private static void testCircuitBreaker() throws InterruptedException {

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

	/**
	 * 测试资源线程池被占满了：
	 * 	假设，一个线程池，大小是10个，队列大小是6个
	 * 	 先进去线程池的是10个请求，然后有6个请求进入等待队列，线程池里有空闲，等待队列中的请求如果还没有timeout，那么就进去线程池去执行
	 * 	 10 + 6 = 16个请求之外，2个请求，直接会被reject掉，限流，直接走fallback
	 *
	 */
	private static void testResourceIsFull(){
		for(int i = 0; i < 18; i++) {
			new TestThread(i).start();
		}
	}

	private static class TestThread extends Thread {

		private int index;

		public TestThread(int index) {
			this.index = index;
		}

		@Override
		public void run() {
			String response = HttpClientUtils.sendGetRequest("http://localhost:8022/get/ad?adId=-2");
			System.out.println("第" + (index + 1) + "次请求，结果为：" + response);
		}

	}

	/**
	 * 测试执行超时引起的降级策略：
	 *
	 */
	private static void testTimeout(){
		String response = HttpClientUtils.sendGetRequest("http://localhost:8022/get/ad?adId=-2");
	}
	
}
