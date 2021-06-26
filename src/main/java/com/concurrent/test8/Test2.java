package com.concurrent.test8;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author maric
 * @Description: 创建固定大小的线程池
 * @date 2021/6/20 17:44
 */
@Slf4j(topic = "c.Test2")
public class Test2 {
    public static void main(String[] args) {
        //创建固定大小的线程池
        //ExecutorService executorService = Executors.newFixedThreadPool(2);
        //创建固定大小的线程池 带名字
        ExecutorService executorService = Executors.newFixedThreadPool(2, new ThreadFactory() {
            private final AtomicInteger atomicInteger = new AtomicInteger(1);
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r,"myThread"+atomicInteger.getAndIncrement());
            }
        });
        executorService.execute(() ->{
           log.info("1");
        });
        executorService.execute(() ->{
            log.info("2");
        });
        executorService.execute(() ->{
            log.info("3");
        });
    }
}
