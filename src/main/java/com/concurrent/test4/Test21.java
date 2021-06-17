package com.concurrent.test4;

import lombok.extern.slf4j.Slf4j;

/**
 解决了其它干活的线程阻塞的问题
 但如果有其它线程也在等待条件呢？
 关联Test 38
 */
@Slf4j(topic = "c.test21:")
public class Test21 {
    static final Object room = new Object(); //相当于共享资源
    static boolean hasCigarette = false;//hasCigarette和hasTakeout都是某一个线程执行所必须的结果
    static boolean hasTakeout = false;

    public static void main(String[] args) throws InterruptedException {
        //这个线程不仅要拥有资源而且必须有烟才能干活
        new Thread(() -> {
            synchronized (room) {
                log.debug("有烟没？啊[{}]", hasCigarette);
                if (!hasCigarette) {
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
        //其他线程有共享资源和外卖才可以干活
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
                //room.notify();
//如果此时采用notify唤醒的是要烟的线程，但是现在有的资源是外卖，导致双方都干不了活
//notify 只能随机唤醒一个 WaitSet 中的线程，这时如果有其它线程也在等待，那么就可能唤醒不了正确的线程，称之为【虚假唤醒】
//解决方法，改为 notifyAll
                room.notifyAll();
//用 notifyAll 仅解决某个线程的唤醒问题，但使用 if + wait 判断仅有一次机会，一旦条件不成立，就没有重新判断的机会了
//解决方法，用 while + wait，当条件不成立，再次 wait
            }
        }, "送外卖").start();
    }
}