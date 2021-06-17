package com.concurrent.test4;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * 多把锁
 */
@Slf4j(topic = "c.test27:")
public class Test27 {
    public static void main(String[] args) {
        BigRoom bigRoom = new BigRoom();
        new Thread(() -> {
            try {
                bigRoom.study();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "小南").start();
        new Thread(() -> {
            try {
                bigRoom.sleep();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "小女").start();
    }
}

@Slf4j(topic = "c.test27:")
/*第一版
class BigRoom {
    public void sleep() throws InterruptedException {
        synchronized (this) {
            log.debug("sleeping 2 小时");
            TimeUnit.SECONDS.sleep(2);
        }
    }
    public void study() throws InterruptedException {
        synchronized (this) {
            log.debug("study 1 小时");
            TimeUnit.SECONDS.sleep(1);
        }
    }
}*/
/**
 * 改进后
 * 将锁的粒度细分
 * 好处，是可以增强并发度
 * 坏处，如果一个线程需要同时获得多把锁，就容易发生死锁
 */

class BigRoom {
    private final Object studyRoom = new Object();
    private final Object bedRoom = new Object();

    public void sleep() throws InterruptedException {
        synchronized (bedRoom) {
            log.debug("sleeping 2 小时");
            TimeUnit.SECONDS.sleep(2);
        }
    }
    public void study() throws InterruptedException {
        synchronized (studyRoom) {
            log.debug("study 1 小时");
            TimeUnit.SECONDS.sleep(1);
        }
    }
}

