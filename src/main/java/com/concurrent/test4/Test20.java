package com.concurrent.test4;

import lombok.extern.slf4j.Slf4j;

/**
 解决了其它干活的线程阻塞的问题
 但如果有其它线程也在等待条件呢？
 */
@Slf4j(topic = "c.test20:")
public class Test20 {
    static final Object room = new Object(); //相当于共享资源
    static boolean hasCigarette = false;

    public static void main(String[] args) throws InterruptedException {
        //这个线程不仅要拥有资源而且必须有烟才能干活
        new Thread(() -> {
            synchronized (room) {
                log.debug("有烟没？啊[{}]", hasCigarette);
                if (!hasCigarette) {
                    log.debug("没烟，先歇会！");
                    try {
                       room.wait(2000);
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
        Thread.sleep(10000);
        new Thread(() -> {
            synchronized (room) {
                hasCigarette = true;
                log.debug("烟到了噢！");
                room.notify();
            }
        }, "送烟的").start();

    }
}