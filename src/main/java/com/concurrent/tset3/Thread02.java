package com.concurrent.tset3;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

@Slf4j(topic = "c.Thread02:")
/**
 * 第三个课堂例子
 */
public class Thread02 {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        FutureTask<String> futureTask = new FutureTask((Callable<String>) () -> {
            log.debug("futureTask");
            Thread.sleep(1000);
            return "over";

        });
        new Thread(futureTask).start();
        log.debug("main");
        log.debug("futureTask返回结果:"+futureTask.get());
    }
}
