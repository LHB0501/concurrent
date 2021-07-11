package com.concurrent.test8;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author maric
 * @Description: 读写锁 ReentrantReadWriteLock
 * @date 2021/7/5 17:26
 */
@Slf4j(topic = "c.Test9")
public class Test10 {
    public static void main(String[] args) {
        Test10 test10= new Test10();
        new Thread(() -> {
            test10.write();
        }, "t1").start();
        new Thread(() -> {
            test10.read();
        }, "t2").start();
    }
    private Object data;
    private ReentrantReadWriteLock rw = new ReentrantReadWriteLock();
    private ReentrantReadWriteLock.ReadLock r = rw.readLock();
    private ReentrantReadWriteLock.WriteLock w = rw.writeLock();

    public Object read() {
        log.debug("获取读锁...");
        r.lock();
        try {
            log.debug("读取");
            Thread.sleep(10000);
            return data;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            log.debug("释放读锁...");
            r.unlock();
        }
        return null;
    }

    public void write() {
        log.debug("获取写锁...");
        w.lock();
        try {
            log.debug("写入");
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            log.debug("释放写锁...");
            w.unlock();
        }
    }
}


