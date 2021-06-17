package com.concurrent.test4;

import lombok.extern.slf4j.Slf4j;

/**
 * API 介绍
 * sleep(long n) 和 wait(long n) 的区别
 * 1) sleep 是 Thread 方法，而 wait 是 Object 的方法
 * 2) sleep 不需要强制和 synchronized 配合使用，但 wait 需要和 synchronized 一起用
 * 3) sleep 在睡眠的同时，不会释放对象锁的，但 wait 在等待的时候会释放对象锁
 * 4) 它们状态 TIMED_WAITING
 */
@Slf4j(topic = "c.test18:")
public class Test18 {
    final static  Object obj = new Object();
        public static void main(String[] args) throws InterruptedException {
            new Thread(() -> {
                log.debug("t1开始执行....");
                synchronized (obj) {
                    try {
                        //Thread.sleep(5000);
                        obj.wait(30000);
                        log.debug("t1执行中....");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    log.debug("t1执行完....");
                }
            },"t1").start();
            // 主线程一秒后执行
            Thread.sleep(1000);
            synchronized (obj) {
                log.debug("main执行");
                obj.notify(); //测试wait(timeout)
            }
    }
}