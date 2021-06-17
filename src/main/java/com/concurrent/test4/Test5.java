package com.concurrent.test4;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j(topic = "c.test5:")
/**
 * 线程八锁 会打印 3 1 2 或3  2 1 或 2 3 1
 * sleep不会释放锁，但是会让出cpu执行权
 */
public class Test5 {
    public static void main(String[] args) {
        Number2 n1 = new Number2();
        new Thread(()->{
            try {
                log.debug("t1 begin");
                n1.a();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"t1").start();
        new Thread(()->{ log.debug("t2 begin");n1.b();},"t2").start();
        new Thread(()->{ log.debug("t3 begin");n1.c(); },"t3").start();
    }
}

@Slf4j(topic = "c.Number")
class Number2{
    public synchronized void a() throws InterruptedException {
        TimeUnit.SECONDS.sleep(1);
        log.debug("1");
    }
    public synchronized void b() {
        log.debug("2");
    }
    public void c() {
        log.debug("3");
    }
}

