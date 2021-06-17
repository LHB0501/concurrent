package com.concurrent.test4;

import lombok.extern.slf4j.Slf4j;

/**
 解决了其它干活的线程阻塞的问题
 但如果有其它线程也在等待条件呢？

 总结
 synchronized(lock) {
 while(条件不成立) {
 lock.wait();
 }
 // 干活
 }
 //另一个线程
 synchronized(lock) {
 lock.notifyAll();
 }
 */
@Slf4j(topic = "c.test21:")
public class Test22 {
    static final Object room = new Object(); //相当于共享资源
    static boolean hasCigarette = false;//hasCigarette和hasTakeout都是某一个线程执行所必须的结果
    static boolean hasTakeout = false;

    public static void main(String[] args) throws InterruptedException {
        //这个线程不仅要拥有资源而且必须有烟才能干活
        new Thread(() -> {
            synchronized (room) {
                log.debug("有烟没？啊[{}]", hasCigarette);
                while (!hasCigarette) {
                    log.debug("没烟，先歇会！");
                    try {
                       room.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                log.debug("有烟没？[{}]", hasCigarette);
                if (hasCigarette) {
                    log.debug("有烟可以开始干活了");
                }else {
                    log.debug("没干成活...");
                }
            }
        }, "小南").start();
        //其他线程有共享资源就可以干活
        new Thread(() -> {
            synchronized (room) {
                log.debug("外卖送到没？[{}]", hasTakeout);
                if (!hasTakeout) {
                    log.debug("没外卖，先歇会！");
                    try {
                        room.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                log.debug("外卖送到没？[{}]", hasTakeout);
                if (hasTakeout) {
                    log.debug("有外卖可以开始干活了");
                } else {
                    log.debug("没干成活...");
                }
            }
        }, "小女").start();

        Thread.sleep(1000);
        new Thread(() -> {
            synchronized (room) {
                hasTakeout = true;
                log.debug("外卖到了噢！");
                room.notifyAll();
            }
        }, "送外卖").start();

        new Thread(() -> {
            synchronized (room) {
                hasCigarette = true;
                log.debug("烟到了！");
                room.notifyAll();
            }
        }, "送烟").start();

    }
}