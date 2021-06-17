package com.concurrent.test4;

import lombok.extern.slf4j.Slf4j;

/**
 * API 介绍
 * obj.wait() 让进入 object 监视器的线程到 waitSet 等待
 * obj.notify() 在 object 上正在 waitSet 等待的线程中挑一个唤醒
 * obj.notifyAll() 让 object 上正在 waitSet 等待的线程全部唤醒
 *
 * wait() 方法会释放对象的锁，进入 WaitSet 等待区，从而让其他线程就机会获取对象的锁。无限制等待，直到
 * notify 为止
 * wait(long n) 有时限的等待, 到 n 毫秒后结束等待，或是未到等待时间被 notify
 *
 *它们都是线程之间进行协作的手段，都属于 Object 对象的方法。必须获得此对象的锁，才能调用这几个方法
 * 使用wait就是重量级锁
 */
@Slf4j(topic = "c.test2:")
public class Test17 {
    final static Object obj = new Object();
    public static void main(String[] args) throws InterruptedException {
        new Thread(() -> {
            synchronized (obj) {
                log.debug("执行....");
                try {
                    obj.wait(); // 让线程在obj上一直等待下去
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.debug("t1执行其它代码....");
            }
        },"t1").start();
        new Thread(() -> {
            synchronized (obj) {
                log.debug("执行....");
                try {
                    obj.wait(); // 让线程在obj上一直等待下去
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.debug("t2执行其它代码....");
            }
        },"t2").start();
        // 主线程两秒后执行
        Thread.sleep(4);
        log.debug("唤醒 obj 上其它线程");
        synchronized (obj) {
            //obj.notify(); // 唤醒obj上一个线程
             obj.notifyAll(); // 唤醒obj上所有等待线程
        }
    }

}
