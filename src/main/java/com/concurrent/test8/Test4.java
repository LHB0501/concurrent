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
        //m4();

        //测试shutdown方法:调用shutdown之后，线程池状态变为 SHUTDOWN ，并且不会接收新任务，但已提交到任务队列
        //任务会被执行完 此方法不会阻塞调用线程的执行（调用shutdown之后，他后面的代码还会执行）

        //测试shutdownNow方法:调用shutdownNow之后,线程池状态变为 STOP,并且不会接收新任务 会将队列中的任务返回
        //并用 interrupt 的方式中断正在执行的任务
        m5();
        // 不在 RUNNING 状态的线程池，此方法就返回 true
        //boolean isShutdown();
        // 线程池状态是否是 TERMINATED
        //boolean isTerminated();
        // 调用 shutdown 后，由于调用线程并不会等待所有任务运行结束，因此如果它想在线程池 TERMINATED 后做些事情，可以利用此方法等待
        //boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException;
    }

    public static void m5() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        Future<Integer> result1 = executorService.submit(() -> {
            log.debug("task1 running");
            Thread.sleep(1000);
            log.debug("task1 finish");
            return 1;
        });
        Future<Integer> result2 = executorService.submit(() -> {
            log.debug("task2 running");
            Thread.sleep(1000);
            log.debug("task2 finish");
            return 2;
        });
        Future<Integer> result3 = executorService.submit(() -> {
            log.debug("task3 running");
            Thread.sleep(1000);
            log.debug("task3 finish");
            return 3;
        });
        log.debug("shutdownNow");
        //调用shutdownNow之后,线程池状态变为 STOP,并且不会接收新任务 会将队列中的任务返回
        // 并用 interrupt 的方式中断正在执行的任务
        List<Runnable> runnables = executorService.shutdownNow();
        log.debug("未被执行的任务:{}",runnables);
        //并且不会接收新任务 会报java.util.concurrent.RejectedExecutionException异常
        Future<Integer> result4 = executorService.submit(() -> {
            log.debug("task4 running");
            Thread.sleep(1000);
            log.debug("task4 finish");
            return 4;
        });
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
