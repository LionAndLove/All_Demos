package com.jikeh.filter;

import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;

import javax.servlet.*;
import java.io.IOException;

/**
 * hystrix请求上下文过滤器
 * @author 极客慧 www.jikeh.cn
 *
 */
public class HystrixRequestContextFilter implements Filter {

	public void init(FilterConfig config) throws ServletException {
		
	}
	
	public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
		HystrixRequestContext context = HystrixRequestContext.initializeContext();
		try {
			chain.doFilter(request, response); 
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			context.shutdown();
		}
	}

	public void destroy() {
		
	}

}
