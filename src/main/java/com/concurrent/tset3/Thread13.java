package com.concurrent.tset3;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j(topic = "c.Thread11:")

/**
 第十四个例子
 默认情况下，Java 进程需要等待所有线程都运行结束，才会结束。有一种特殊的线程叫做守护线程（通过setDaemon方法）
 ，只要其它非守护线程运行结束了，即使守护线程的代码没有执行完，也会强制结束。

 垃圾回收器线程就是一种守护线程
 Tomcat 中的 Acceptor 和 Poller 线程都是守护线程，所以 Tomcat 接收到 shutdown 命令后，不会等
 待它们处理完当前请求

 */
public class Thread13 {
    public static void main(String[] args) throws InterruptedException {
        log.debug("main开始运行...");
        Thread t1 = new Thread(() -> {
            log.debug("t1开始运行...");
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.debug("t1运行结束...");
        }, "daemon");
        // 设置该线程为守护线程
        t1.setDaemon(true);
        t1.start();
        TimeUnit.SECONDS.sleep(1);
        log.debug("main运行结束...");

    }
}