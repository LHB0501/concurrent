package com.concurrent.tset3;

import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "c.Thread14:")

/**
 第十六个例子
 华罗庚《统筹方法》，给出烧水泡茶的多线程解决方案，
 喝茶：开水没有；水壶要洗，茶壶、茶杯要洗；火已生了，茶叶有
 提示
    用两个线程（两个人协作）模拟烧水泡茶过程
    文中办法乙、丙都相当于任务串行
    而图一相当于启动了 4 个线程，有点浪费
 */
public class Thread15 {

    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            log.debug("洗水壶");
            try {
                Thread.sleep(1);
                log.debug("烧开水");
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        },"老王");

        Thread t2 = new Thread(() -> {
            try {
                log.debug("洗茶壶");
                Thread.sleep(1);
                log.debug("洗茶杯");
                Thread.sleep(2);
                log.debug("拿茶叶");
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                t1.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.debug("泡茶");
        },"小王");

        t1.start();
        t2.start();
    }
}