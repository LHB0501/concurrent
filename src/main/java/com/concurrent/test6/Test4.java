package com.concurrent.test6;

import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * 原子引用AtomicStampedReference 带修改版本的  可以知道，引用变量中途被更改了几次
 */
@Slf4j(topic = "c.Test4")
public class Test4 {
    //构造方法第一个是原始值，第二个是版本号
    static AtomicStampedReference<String> reference = new AtomicStampedReference<>("A", 0);

    public static void main(String[] args) throws InterruptedException {
        log.debug("main start...");
        // 获取值 A
        String prev = reference.getReference();
        // 获取版本号
        int stamp = reference.getStamp();
        log.debug("main修改之前的 stamped:{}", stamp);
        // 如果中间有其它线程干扰，发生了 ABA 现象
        other();
        Thread.sleep(1000);
        // 尝试改为 C
        log.debug("change A->C {}", reference.compareAndSet(prev, "C", stamp, stamp + 1));

    }
    private static void other() throws InterruptedException {
        log.debug("other第一次修改之前的 stamped:{}",reference.getStamp());
        new Thread(()->{
            log.debug("change A->B:{}",reference.compareAndSet(reference.getReference(), "B", reference.getStamp(),reference.getStamp()+1));
        },"t1").start();
        Thread.sleep(500);
        log.debug("other第二次修改之前的 stamped:{}",reference.getStamp());
        new Thread(()->{
            log.debug("change B->A:{}",reference.compareAndSet(reference.getReference(), "A", reference.getStamp(),reference.getStamp()+1));
            },"t2").start();
        log.debug("other第二次修改之后的 stamped:{}",reference.getStamp());

    }

}
