package com.concurrent.test4;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * ReentrantLock
 * 可打断
 */
@Slf4j(topic = "c.test34:")
public class Test34 {
    static ReentrantLock lock = new ReentrantLock();

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            log.debug("尝试获取锁");
            lock.lock();
            try {
                log.debug("获取到了锁");
            } finally {
                lock.unlock();
            }
        }, "t1");
        //主线程比t1先获取到了锁
        lock.lock();
        log.debug("main获得了锁");
        t1.start();
        try {
            //主线程休眠1秒之后打断t1的等待
            TimeUnit.SECONDS.sleep(1);
            log.debug("main线程打断t1的等待");
            t1.interrupt();
        } finally {
            log.debug("main线程释放了锁");
            lock.lock();
        }
    }
}