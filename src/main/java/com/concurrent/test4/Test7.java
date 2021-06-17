package com.concurrent.test4;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j(topic = "c.test7:")
/**
 * 线程八锁 会打印 2  1  根本不会冲突
 * sleep并不会释放锁
 */
public class Test7 {
    public static void main(String[] args) {
        Number4 n1 = new Number4();
        new Thread(()->{
            try {
                log.debug("t1 begin");
                n1.a();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"t1").start();
        new Thread(()->{ log.debug("t2 begin");n1.b();},"t2").start();
    }
}

@Slf4j(topic = "c.Number")
class Number4{
    public static synchronized void a() throws InterruptedException {
        TimeUnit.SECONDS.sleep(1);
        log.debug("1");
    }
    public synchronized void b() {
        log.debug("2");
    }
}

