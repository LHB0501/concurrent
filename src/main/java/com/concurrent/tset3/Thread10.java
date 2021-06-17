package com.concurrent.tset3;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j(topic = "c.Thread10:")
/**
 第十一个例子
 * interrupt会打断 sleep，wait，join 的线程
 打断 sleep 的线程, 会清空打断状态（正常打断之后打断标记为true,处于sleep，wait，join打断后打断标记会被清除，
 重置为false)
 */
public class Thread10 {
    public static void main(String[] args) throws InterruptedException{
        test1();  //打断 sleep 的线程, 会清空打断状态，以 sleep 为例
        test2(); //打断正常线程
    }
    private static void test1() throws InterruptedException {
        Thread t1 = new Thread(()->{
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "t1");
        t1.start();
        TimeUnit.SECONDS.sleep(1);
        t1.interrupt();
        log.debug(" 打断状态: {}", t1.isInterrupted());
    }

    private static void test2() throws InterruptedException {
        Thread t2 = new Thread(()->{
            while(true) {
                Thread current = Thread.currentThread();
                boolean interrupted = current.isInterrupted();
                if(interrupted) {
                    log.debug(" 打断状态: {}", interrupted);
                    break;
                }
            }
        }, "t2");
        t2.start();
        TimeUnit.SECONDS.sleep(1);
        t2.interrupt();
    }

}
