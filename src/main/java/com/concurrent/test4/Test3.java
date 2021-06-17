package com.concurrent.test4;

import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "c.test3:")
/**
 * 线程八锁 会打印1 2 或 2 1
 */
public class Test3 {
    public static void main(String[] args) {
        Number n1 = new Number();
        new Thread(() -> {
            n1.a();
        }).start();
        new Thread(() -> {
            n1.b();
        }).start();
    }
}
@Slf4j(topic = "c.Number")
class Number{
    public synchronized void a() {
        log.debug("1");
    }
    public synchronized void b() {
        log.debug("2");
    }
}
