package com.concurrent.test6;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * LongAdder 源码
 * LongAdder 类有几个关键域
 * // 累加单元数组, 懒惰初始化
 * transient volatile Cell[] cells;
 * // 基础值, 如果没有竞争, 则用 cas 累加这个域
 * transient volatile long base;
 * // 在 cells 创建或扩容时, 置为 1, 表示加锁
 * transient volatile int cellsBusy;  //使用cas锁来保证Cell创建时的线程安全
 * 下面是会LongAdder 锁的源码解释
 *
 * 另外注意笔记中缓存行的原理，要写笔记来总结
 */
@Slf4j(topic = "c.Test9")
public class Test9 {
    public static void main(String[] args) {
        LockCas lock = new LockCas();
        new Thread(() -> {
            log.debug("begin...");
            lock.lock();
            try {
                log.debug("lock...");
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }).start();
        new Thread(() -> {
            log.debug("begin...");
            lock.lock();
            try {
                log.debug("lock...");
            } finally {
                lock.unlock();
            }
        }).start();

    }
}

@Slf4j(topic = "c.LockCas")
// 不要用于实践！！！
class LockCas {
    private AtomicInteger state = new AtomicInteger(0);

    public void lock() {
        while (true) {
            if (state.compareAndSet(0, 1)) {
                break;
            }
        }
    }

    public void unlock() {
        log.debug("unlock...");
        state.set(0);
    }
}

