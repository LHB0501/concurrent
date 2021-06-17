package com.concurrent.tset3;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j(topic = "c.Thread05:")
/**
 * 第六个课堂例子
 sleep
 1. 调用 sleep 会让当前线程从 Running 进入 Timed Waiting 状态（阻塞）
 2. 其它线程可以使用 interrupt 方法打断正在睡眠的线程，这时 sleep 方法会抛出 InterruptedException
 3. 睡眠结束后的线程未必会立刻得到执行
 4. 建议用 TimeUnit 的 sleep 代替 Thread 的 sleep 来获得更好的可读性
 */
public class Thread05 {
    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            try {
                //建议用 TimeUnit 的 sleep 代替 Thread 的 sleep 来获得更好的可读性
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        t1.start();
        log.debug("t1的状态："+t1.getState());
        Thread.sleep(5);//主线程休眠5秒后去调用t1的状态，因为t1此时还在休眠所以t1在阻塞状态
        log.debug("t1的状态："+t1.getState());

    }
}
