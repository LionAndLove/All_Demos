package com.luo.dubbo.util;

public class ThreadUtils {

    public static void waitFor(Object lock) {
        synchronized (lock) {
            try {
                lock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void notifyWait(Object lock) {
        synchronized (lock) {
            lock.notify();
        }
    }
}
