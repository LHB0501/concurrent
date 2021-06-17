package com.concurrent.test5;

public final class Singleton4 {
    private Singleton4() { }
    // Q1：解释为什么要加 volatile ?
    // A: 因为构造方法的指令有可能跟赋值语句指令重排序
    private static volatile Singleton4 INSTANCE = null;

    //Q:对比实现3, 说出这样做的意义
    //A:只需第一次加锁，后续进来都不需要加锁并发的效率更高
    public static Singleton4 getInstance() {
        if (INSTANCE != null) {
            return INSTANCE;
        }
        synchronized (Singleton4.class) {//t2
            //Q：为什么还要在这里加为空判断, 之前不是判断过了吗
            //A: 为了解决首次创建对象时多个线程并发访问的问题
            if (INSTANCE != null) { //如果没有这个当t1执行完成之后也会创建对象
                return INSTANCE;
            }
            INSTANCE = new Singleton4();//t1
            return INSTANCE;
        }
    }
}
