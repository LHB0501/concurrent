package com.concurrent.test8;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author maric
 * @Description: 任务调度线程:在『任务调度线程池』功能加入之前，
 * 可以使用 java.util.Timer 来实现定时功能，Timer 的优点在于简单易用，但
 * 由于所有任务都是由同一个线程来调度，因此所有任务都是串行执行的，
 * 同一时间只能有一个任务在执行，前一个任务的延迟或异常都将会影响到之后的任务。
 * @date 2021/6/26 22:39
 */
@Slf4j(topic = "c.Test7")
public class Test7 {
    public static void main(String[] args) {
        //测试Timer
        //m1();
        //使用ScheduledExecutorService改进Timer
        //m2();
        /**
         * 使用ScheduledExecutorService的scheduleAtFixedRate方法
         * 来定时执行(注意任务时间大于间隔时间任务被执行完再调用下一个方法)
         * 注意scheduleAtFixedRate和scheduleWithFixedDelay的区别
         * （scheduleWithFixedDelay() 无论任务执行多久，间隔时间都会间隔）
         */

        m3();
    }

    public static void m1() {
        Timer timer = new Timer();
        TimerTask task1 = new TimerTask() {
            @SneakyThrows
            @Override
            public void run() {
                log.debug("task 1");
                Thread.sleep(2000);
            }
        };
        TimerTask task2 = new TimerTask() {
            @Override
            public void run() {
                log.debug("task 2");
            }
        };
        // 使用 timer 添加两个任务，希望它们都在 1s 后执行
        // 但由于 timer 内只有一个线程来顺序执行队列中的任务，因此『任务1』的延时，影响了『任务2』的执行
        timer.schedule(task1, 1000);
        timer.schedule(task2, 1000);

    }

    public static void m2() {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        // 添加两个任务，希望它们都在 1s 后执行
        executor.schedule(() -> {
            System.out.println("任务1，执行时间：" + new Date());
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) { }
        }, 1000, TimeUnit.MILLISECONDS);
        executor.schedule(() -> {
            System.out.println("任务2，执行时间：" + new Date());
        }, 1000, TimeUnit.MILLISECONDS);
    }

    public static void m3() {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        System.out.println("start");
        executor.scheduleAtFixedRate(()->{
            System.out.println("被执行。。");
        },3,1,TimeUnit.SECONDS);

    }
}
