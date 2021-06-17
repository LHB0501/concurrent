package com.concurrent.test4;

import lombok.extern.slf4j.Slf4j;

/**
 * 其它干活的线程，都要一直阻塞，效率太低
 * 小南线程必须睡足 2s 后才能醒来，就算烟提前送到，也无法立刻醒来
 * 加了 synchronized (room) 后，就好比小南在里面反锁了门睡觉，烟根本没法送进门，main 没加
 * synchronized 就好像 main 线程是翻窗户进来的
 * 解决方法，使用 wait - notify 机制
 */
@Slf4j(topic = "c.test19:")
public class Test19 {
    static final Object room = new Object(); //相当于共享资源
    static boolean hasCigarette = false;

    public static void main(String[] args) throws InterruptedException {
        //这个线程不仅要拥有资源而且必须有烟才能干活
        new Thread(() -> {
            synchronized (room) {
                log.debug("有烟没？[{}]", hasCigarette);
                if (!hasCigarette) {
                    log.debug("没烟，先歇会！");
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                log.debug("有烟没？[{}]", hasCigarette);
                if (hasCigarette) {
                    log.debug("有烟可以开始干活了");
                }
            }
        }, "小南").start();
        //其他线程有共享资源就可以干活
        for (int i = 0; i < 5; i++) {
            new Thread(() -> {
                synchronized (room) {
                    log.debug("可以开始干活了");
                }
            }, "其它人").start();
        }
        Thread.sleep(1000);
        new Thread(() -> {
            // 这里能不能加 synchronized (room)？
            hasCigarette = true;
            log.debug("烟到了噢！");
        }, "送烟的").start();
    }
}