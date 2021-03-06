package com.concurrent.tset3;

import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "c.Thread05:")
/**
 * 第七个课堂例子
 sleep 与 yield的区别
 sleep
 1. 调用 sleep 会让当前线程从 Running 进入 Timed Waiting 状态（阻塞）
 2. 其它线程可以使用 interrupt 方法打断正在睡眠的线程，这时 sleep 方法会抛出 InterruptedException
 3. 睡眠结束后的线程未必会立刻得到执行
 4. 建议用 TimeUnit 的 sleep 代替 Thread 的 sleep 来获得更好的可读性
 yield
 1. 调用 yield 会让当前线程从 Running 进入 Runnable 就绪状态，然后调度执行其它线程
 2. 具体的实现依赖于操作系统的任务调度器
 线程优先级
 线程优先级会提示（hint）调度器优先调度该线程，但它仅仅是一个提示，调度器可以忽略它
 如果 cpu 比较忙，那么优先级高的线程会获得更多的时间片，但 cpu 闲时，优先级几乎没作用
 */
public class Thread06 {
    public static void main(String[] args) throws InterruptedException {
        Runnable task1 = () -> {
            int count = 0;
            for (;;) {
                System.out.println("---->1 " + count++);
            }
        };
        Runnable task2 = () -> {
            int count = 0;
            for (;;) {
                // Thread.yield();
                System.out.println(" ---->2 " + count++);
            }
        };
        Thread t1 = new Thread(task1, "t1");
        Thread t2 = new Thread(task2, "t2");
        // t1.setPriority(Thread.MIN_PRIORITY);
        // t2.setPriority(Thread.MAX_PRIORITY);
        t1.start();
        t2.start();

    }
}
