package com.concurrent.test4;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j(topic = "c.test6:")
/**
 * 线程八锁 会打印 2  1  根本不会冲突
 * sleep不会释放锁，但是会让出cpu执行权
 */
public class Test6 {
    public static void main(String[] args) {
        Number3 n1 = new Number3();
        Number3 n2 = new Number3();
        new Thread(()->{
            try {
                log.debug("t1 begin");
                n1.a();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"t1").start();
        new Thread(()->{ log.debug("t2 begin");n2.b();},"t2").start();
    }
}

@Slf4j(topic = "c.Number")
class Number3{
    public synchronized void a() throws InterruptedException {
        TimeUnit.SECONDS.sleep(1);
        log.debug("1");
    }
    public synchronized void b() {
        log.debug("2");
    }
}

