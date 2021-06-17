package com.concurrent.tset3;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j(topic = "c.Thread09:")
/**
 * 第十个课堂例子
 分析
    因为主线程和线程 t1 是并行执行的，t1 线程需要 1 秒之后才能算出 r=10
    而主线程一开始就要打印 r 的结果，所以只能打印出 r=0
 解决方法
    用 sleep 行不行？为什么？
    用 join，加在 t1.start() 之后即可
 */
//join等待多个线程的结果
//最后结果想要r1=10,r1=20
public class Thread09 {
    static int r1 = 0;
    static int r2 = 0;
    public static void main(String[] args) throws InterruptedException {
        //这三个方法分别运行，不然会影响结果
        //test1();//join等待多个线程的结果 因为t1和t2同时并发执行，t1睡眠1秒，t2睡眠2秒，因此总等待时间为2秒不是3秒，两个线程等待完毕，拿到正确结果
        //test2(); // 因为t1睡眠2秒，想等待1秒之后拿到结果，因为没等够时间 返回错误结果
        //test3(); //  因为t1睡眠1秒，想等待1.5秒之后拿到结果，但是因为t1在1秒时候已经执行完毕，此时不用等待1.5秒，线程运行结束，拿到正确结果

    }
    private static void test1() throws InterruptedException {
        Thread t1 = new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            r1 = 10;
        });
        Thread t2 = new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            r2 = 20;
        });
        long start = System.currentTimeMillis();
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        long end = System.currentTimeMillis();
        log.debug("r1: {} r2: {} cost: {}", r1, r2, end - start);
    }

    public static void test2() throws InterruptedException {
        Thread t1 = new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
                r1 = 10;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        long start = System.currentTimeMillis();
        t1.start();
        // 线程执行结束会导致 join 结束
        t1.join(1000);
        long end = System.currentTimeMillis();
        log.debug("r1: {} r2: {} cost: {}", r1, r2, end - start); //没等够时间哎，所以返回0
    }

    public static void test3() throws InterruptedException {
        Thread t1 = new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
                r1 = 10;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        long start = System.currentTimeMillis();
        t1.start();
        // 线程执行结束会导致 join 结束
        t1.join(1500);
        long end = System.currentTimeMillis();
        log.debug("r1: {} r2: {} cost: {}", r1, r2, end - start); //等够时间返回正确
    }
}
