package com.concurrent.test4;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * ReentrantLock
 * 可打断
 * 使用lockInterruptibly()
 */
@Slf4j(topic = "c.test33:")
public class Test33 {
    static ReentrantLock lock = new ReentrantLock();

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            try {
                //lockInterruptibly 如果没有锁竞争，则成功获取到lock对象的锁
                //否则如果有竞争，则进入阻塞队列，可以被其他线程调用interrupit方法打断，停止等待
                log.debug("尝试获取锁");
                lock.lockInterruptibly();
                log.debug("获取到了锁");
            } catch (InterruptedException e) {
                e.printStackTrace();
                log.debug("没有获取到了锁");
                return;
            } finally {
                lock.unlock();
            }
        }, "t1");
        //主线程比t1先获取到了锁
        lock.lock();
        t1.start();
        //主线程休眠1秒之后打断t1的等待
        TimeUnit.SECONDS.sleep(1);
        log.debug("main线程打断t1的等待");
        t1.interrupt();
    }
}