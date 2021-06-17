package com.concurrent.test6;

import java.security.cert.X509Certificate;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 原子整数AtomicInteger
 */
public class Test2 {
    public static void main(String[] args) {

        //测试原子整数 incrementAndGet()，getAndIncrement()，getAndAdd，addAndGet
        AtomicInteger integer = new AtomicInteger(0);
        System.out.println(integer.incrementAndGet());//++i 等于1
        System.out.println(integer.getAndIncrement());//i++ 等于2
        System.out.println(integer.get());
        System.out.println(integer.getAndAdd(5));//i+5 等于7
        System.out.println(integer.get());
        System.out.println(integer.addAndGet(5));//i+5 等于12
        System.out.println(integer.get());
        integer.getAndSet(100);
        System.out.println(integer.get());


        //测试updateAndGet
        AtomicInteger integer1 = new AtomicInteger(5);
        integer1.updateAndGet(x ->x*10);
        System.out.println(integer1.get());

        //updateAndGet的底层代码
        while (true){
            int i = integer1.get();
            int j = i * 10;
            if (integer1.compareAndSet(i, j)){
                break;
            }
        }
    }
}
