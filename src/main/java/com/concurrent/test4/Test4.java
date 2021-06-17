package com.concurrent.test4;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j(topic = "c.test4:")
/**
 * 线程八锁 会打印1 2 或 2 1
 * sleep不会释放锁，但是会让出cpu执行权
 */
public class Test4 {
    public static void main(String[] args) {
    Number1 n1 = new Number1();
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
class Number1{
    public synchronized void a() throws InterruptedException {
        TimeUnit.SECONDS.sleep(10);
        log.debug("1");
    }
    public synchronized void b() {
        log.debug("2");
    }
}

