package com.jikeh.threadlocal;

/**
 * 当有多个threadLocal的情况下，当前线程里面的threadLocals变量值的变化
 */
public class demo {

    private static final ThreadLocal<Integer> numLocal = ThreadLocal.withInitial(() -> 0);
    private static final ThreadLocal<Integer> numLocal2 = ThreadLocal.withInitial(() -> 1);
    private static final ThreadLocal<Integer> numLocal3 = ThreadLocal.withInitial(() -> 2);

    public static void main(String[] args) {
        Thread tBegin = Thread.currentThread();
        int a = numLocal.get();
        System.out.println(a);
        numLocal.set(5);
        int b = numLocal3.get();
        System.out.println(b);

        int a2 = numLocal.get();
        System.out.println(a2);
        numLocal2.set(5);
        int b2 = numLocal.get();
        System.out.println(b2);

        int a3 = numLocal3.get();
        System.out.println(a3);
        numLocal3.set(5);
        int b3 = numLocal3.get();
        System.out.println(b3);
        Thread tEnd = Thread.currentThread();
    }

}
