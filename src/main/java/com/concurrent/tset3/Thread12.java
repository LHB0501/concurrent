package com.concurrent.tset3;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

@Slf4j(topic = "c.Thread11:")

/**
 第十三个例子
 用 interrupt 打断 park（阻塞）线程
 * isInterrupted() 判断是否被打断， 不会清除 打断标记不会清除 打断标记不会清除 打断标记
 * interrupted      判断当前线程是否被打断 会清除 打断标记
 */
public class Thread12 {
    public static void main(String[] args) throws InterruptedException {
        test3();
    }
    private static void test3() throws InterruptedException {
        Thread t1 = new Thread(() -> {
            log.debug("park...");
            LockSupport.park();
            log.debug("unpark...");
        }, "t1");
        t1.start();
        TimeUnit.SECONDS.sleep(2);
        t1.interrupt();
        log.debug("打断状态：{}", t1.isInterrupted());
        log.debug("打断状态：{}", t1.interrupted());
    }

}