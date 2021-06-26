package com.concurrent.test8;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author maric
 * @Description: 单线程的线程池
 * @date 2021/6/22 21:21
 */
@Slf4j(topic = "c.Test3")
public class Test3 {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
                log.debug("1");
                int i = 1/0;
        });
        executorService.execute(() -> {
                log.debug("2");
        });
        executorService.execute(() -> {
                log.debug("3");
        });
    }
}
