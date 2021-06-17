package com.concurrent.test4;

import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "c.test1:")
/**
 * 为了避免临界区的竞态条件发生，有多种手段可以达到目的。
 * 阻塞式的解决方案：synchronized，Lock
 * 非阻塞式的解决方案：原子变量
 * 本次课使用阻塞式的解决方案：synchronized，来解决上述问题，即俗称的【对象锁】，它采用互斥的方式让同一
 * 时刻至多只有一个线程能持有【对象锁】，其它线程再想获取这个【对象锁】时就会阻塞住。这样就能保证拥有锁
 * 的线程可以安全的执行临界区内的代码，不用担心线程上下文切换
 * 语法
 * synchronized(对象) // 线程1， 线程2(blocked)
 * {
 *  临界区
 * }
 * 总结：synchronized 实际是用对象锁保证了临界区内代码的原子性，临界区内的代码对外是不可分割的，不会被线程切
 * 换所打断。
 */
public class Test1 {
    static int counter = 0;
    public static void main(String[] args) throws InterruptedException {
        Object o = new Object();
        Thread t1 = new Thread(() -> {
           synchronized (o){
               for (int i = 0; i < 5000; i++) {
                   counter++;
               }
           }
        }, "t1");
        Thread t2 = new Thread(() -> {
            synchronized (o){
                for (int i = 0; i < 5000; i++) {
                    counter--;
                }
            }
        }, "t2");
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        log.debug("{}",counter);
    }

}
