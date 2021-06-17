package com.concurrent.tset3;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j(topic = "c.Thread14:")

/**
 第十五个例子
 测试java的六种状态
 */
public class Thread14 {
    public static void main(String[] args) throws InterruptedException {
       Thread t1 = new Thread(()->{
            log.debug("t1 runging"); //new
        });
        t1.setName("t1");
        Thread t2 = new Thread(()->{
            while (true){} //runnable
        });
        t2.setName("t2");
        t2.start();
        Thread t3 = new Thread(()->{
            log.debug("t3 runging"); //正常运行 TERMINATED
        });
        t3.setName("t3");
        t3.start();

        Thread t4 = new Thread(()->{
           synchronized (Thread14.class){
               try {
                   TimeUnit.SECONDS.sleep(10); // TIMED_WAITING 有时间的等待
               } catch (InterruptedException e) {
                   e.printStackTrace();
               }
           }
        });
        t4.setName("t4");
        t4.start();

        Thread t5 = new Thread(()->{
            try {
                //WAITING
                t2.join();//等待t2执行完  但是t2是while(true)循环，因此t5会一直等待
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        t5.setName("t5");
        t5.start();

        Thread t6 = new Thread(()->{
            synchronized (Thread14.class){
                try {
                    //t6拿不到锁，因为t4比他早拿到锁所以他会初一阻塞状态
                    TimeUnit.SECONDS.sleep(10); // BLOCKED
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t6 .setName("t6");
        t6.start();

        Thread.sleep(500);
        log.debug("t1 state:"+t1.getState());
        log.debug("t2 state:"+t2.getState());
        log.debug("t3 state:"+t3.getState());
        log.debug("t4 state:"+t4.getState());
        log.debug("t5 state:"+t5.getState());
        log.debug("t6 state:"+t6.getState());


    }
}