package com.concurrent.test8;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author maric
 * @Description: 定时任务
 * @date 2021/6/27 19:18
 */
public class Test8 {
    //每周四 18:00:00定时执行任务
    public static void main(String[] args) {
        //获取当前时间
        LocalDateTime now = LocalDateTime.now();
        //获取周四时间
        LocalDateTime time = now.withHour(18).withMinute(0).withSecond(0).withNano(0).with(DayOfWeek.THURSDAY);
        //如果当前时间 > 本周四 必须找到下周周四
        if(now.compareTo(time) > 0){
            time = time.plusWeeks(1);
        }
        //initialDelay 代表当前时间和周四执行时间的差值
        long initialDelay = Duration.between(now,time).toMillis();
        //period 一周的执行间隔时间
        long period = 1000 * 60 * 60 * 24 * 7;
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
        scheduledExecutorService.scheduleAtFixedRate(() -> {
        }, initialDelay, period, TimeUnit.MILLISECONDS);

    }
}
