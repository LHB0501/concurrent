package com.concurrent.test8;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author maric
 * @Description: 异步模式之工作线程：让有限的工作线程来轮流处理无限多的任务，他典型的实现就是线程池，
 * 也体现了经典设计模式中的享元模式
 *
 * 这个例子中，有两个线程，当只有一个客人的时候，两个线程一个负责点餐，另外一个负责做菜 不会造成饥饿
 * 当有两个客人时就会造成饥饿现象
 * @date 2021/6/23 15:37
 */
@Slf4j(topic = "c.Test5")
public class Test5 {
    static final List<String> MENU = Arrays.asList("地三鲜", "宫保鸡丁", "辣子鸡丁", "烤鸡翅");
    static Random RANDOM = new Random();

    static String cooking() {
        return MENU.get(RANDOM.nextInt(MENU.size()));
    }

    public static void main(String[] args) {
        ExecutorService waitPool = Executors.newFixedThreadPool(2);
        /**
         * 假如来了一个客人，两个线程一个负责点餐，一个负责做菜
         */
        waitPool.execute(() -> {
            log.debug("处理点餐...");
            Future<String> f = waitPool.submit(() -> {
                log.debug("做菜");
                return cooking();
            });
            try {
                log.debug("上菜: {}", f.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });
        /**
         * 假如来了俩客人，两个线程一个负责点餐，一个负责做菜
         */
         waitPool.execute(() -> {
         log.debug("处理点餐...");
         Future<String> f = waitPool.submit(() -> {
         log.debug("做菜");
         return cooking();
         });
         try {
         log.debug("上菜: {}", f.get());
         } catch (InterruptedException | ExecutionException e) {
             e.printStackTrace();
         }
         });
    }
}

