package com.concurrent.test8;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author maric
 * @Description: ExecutorService方法使用
 * @date 2021/6/22 21:21
 */
@Slf4j(topic = "c.Test4")
public class Test4 {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        //测试submit方法
        //m1();
        //测试invokeAll方法 获取所有返回结果
        //m2();
        //测试invokeAny方法 获取哪个任务先成功执行完毕，返回此任务执行结果，其它任务取消
        //m3();
        //测试invokeAny方法 获取哪个任务先成功执行完毕，返回此任务执行结果，其它任务取消  带超时
        m4();
    }

    public static void m4(){
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        String reslut = null;
        try {
            reslut = executorService.invokeAny(
                    Arrays.asList(
                            () -> {
                                Thread.sleep(1000);
                                return "1";
                            },
                            () -> {
                                Thread.sleep(900);
                                return "2";
                            },
                            () -> {
                                Thread.sleep(1100);
                                return "3";
                            }
                    ),1000, TimeUnit.MILLISECONDS
            );
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            e.printStackTrace();
        }
        log.debug(reslut);
    }
    public static void m3(){
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        String reslut = null;
        try {
            reslut = executorService.invokeAny(
                    Arrays.asList(
                            () -> {
                                Thread.sleep(1000);
                                return "1";
                            },
                            () -> {
                                Thread.sleep(900);
                                return "2";
                            },
                            () -> {
                                Thread.sleep(1100);
                                return "3";
                            }
                    )
            );
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        log.debug(reslut);
    }

    public static void m2(){
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        List<Future<Object>> futures = null;
        try {
            futures = executorService.invokeAll(
                    Arrays.asList(
                            () -> {
                                Thread.sleep(1000);
                                return "1";
                            },
                            () -> {
                                Thread.sleep(900);
                                return "2";
                            },
                            () -> {
                                Thread.sleep(1100);
                                return "3";
                            }
                    )
            );
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        futures.forEach(f->{
            try {
                log.debug((String) f.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        });
    }

    public static void m1() throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        Future<Integer> future = executorService.submit(() -> {
            Thread.sleep(1000);
            return 0;
        });
        log.debug(String.valueOf(future.get()));
    }
}
