package com.concurrent.tset3;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j(topic = "c.Thread05:")
/**
 * 第九个课堂例子
 分析
    因为主线程和线程 t1 是并行执行的，t1 线程需要 1 秒之后才能算出 r=10
    而主线程一开始就要打印 r 的结果，所以只能打印出 r=0
 解决方法
    用 sleep 行不行？为什么？
    用 join，加在 t1.start() 之后即可
 */

//join等待单个线程的结果
public class Thread08 {
    static int r = 0;
    public static void main(String[] args) throws InterruptedException {
        test1();
    }
    private static void test1() throws InterruptedException {
        log.debug("test1开始");
        Thread t1 = new Thread(() -> {
            log.debug("t1开始");
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.debug("t1结束");
            r = 10;
        });
        t1.start();
        //t1.join();//join会等待调用它的线程执行完
        log.debug("结果为:{}", r);
        log.debug("结束");
    }
}
