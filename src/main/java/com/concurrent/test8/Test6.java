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
 * @Description: 解决Test5中的饥饿问题
 * 思路：不同类型的任务使用不同的线程池
 * @date 2021/6/23 15:37
 */
@Slf4j(topic = "c.Test6")
public class Test6 {
    static final List<String> MENU = Arrays.asList("地三鲜", "宫保鸡丁", "辣子鸡丁", "烤鸡翅");
    static Random RANDOM = new Random();

    static String cooking() {
        return MENU.get(RANDOM.nextInt(MENU.size()));
    }

    public static void main(String[] args) {
        ExecutorService waitPool = Executors.newFixedThreadPool(1);
        ExecutorService cookPool = Executors.newFixedThreadPool(1);
        /**
         * 假如来了一个客人，两个线程一个负责点餐，一个负责做菜
         */
        waitPool.execute(() -> {
            log.debug("处理点餐...");
            Future<String> f = cookPool.submit(() -> {
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
         Future<String> f = cookPool.submit(() -> {
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

