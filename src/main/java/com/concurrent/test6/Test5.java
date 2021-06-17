package com.concurrent.test6;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicMarkableReference;

/**
 * 原子引用AtomicMarkableReference可以判断引用是否被修改过 带标记
 */
@Slf4j(topic = "c.Test5")
public class Test5 {
    public static void main(String[] args) throws InterruptedException {
        AtomicMarkableReference<String> ref = new AtomicMarkableReference<>("A", true);
        String s= ref.getReference();
        //假如在将A替换成B的过程中有一个线程已经将他换成了C
        new Thread(() -> {
            while (!ref.compareAndSet(s, "C", true, false)) {}
        }).start();
        Thread.sleep(1000);
        //那么主线程将替换失败
        boolean success = ref.compareAndSet(s, "B", true, false);
        log.debug("替换成功了么？" + success);
    }
}