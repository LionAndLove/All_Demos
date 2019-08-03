package com.luo.dubbo.main;

import org.apache.log4j.Logger;
import com.luo.dubbo.context2.ContextFactory;
import com.luo.dubbo.util.ThreadUtils;

/****
 * 容器启动类
 * 
 * @author HadLuo
 * @since JDK1.7
 * @history 2017年12月4日 新建
 */
public class Main {

    private static Logger log = Logger.getLogger(Main.class);
    /***
     * 主线程 阻塞 锁
     */
    private static Object lock = new Object();

    public static void main(String[] args) {
        main0(args);
    }

    public static void main() {
        main0(null);
    }

    /***
     * 初始化
     * 
     * @param args
     * @author HadLuo 2017年12月4日 新建
     */
    private static void main0(String[] args) {
        // 1. 初始化 环境
        ContextFactory.init();
        // 2. 阻塞
        loopMain();
    }

    private static void loopMain() {
        log.error("罗氏 Dubbo 容器 is start ok !!!!!");
        ThreadUtils.waitFor(lock);
    }

    /***
     * 结束 主线程
     * 
     * @author HadLuo 2017年11月15日 新建
     */
    public static void finishMain() {
        ThreadUtils.notifyWait(lock);
    }
}
