package com.concurrent.test4;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

@Slf4j(topic = "c.test11:")
/**
 *买票问题
 */
public class Test14 {
    public static void main(String[] args) {
        TicketWindow ticketWindow = new TicketWindow(2000);
        //所有线程集合，用于等待所有线程执行完毕
        List<Thread> list = new ArrayList<>();
        // 用来存储买出去多少张票
        List<Integer> sellCount = new Vector<>();
        for (int i = 0; i < 5000; i++) {
            Thread t = new Thread(() -> {
                // 分析这里的竞态条件
                int count = ticketWindow.sell(randomAmount());
                //此出sellCount和ticketWindow都是共享变量这俩分开不需要保护，因为我们要求每个共享
                //变量中他的临界区代码收到保护就行
                sellCount.add(count);
            });
            list.add(t);//此出的list只会被主线程调用，所以不存在线程安全问题，因此我们用了ArrayList
            t.start();
        }
        list.forEach(t -> {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        // 买出去的票求和
        log.debug("卖出去的票总和:{}",sellCount.stream().mapToInt(c -> c).sum());
        // 剩余票数
        log.debug("剩余票数:{}", ticketWindow.getCount());
    }
    // Random 为线程安全
    static Random random = new Random();
    // 随机 1~5
    public static int randomAmount() {
        return random.nextInt(5) + 1;
    }
}
class TicketWindow {
    private int count;
    public TicketWindow(int count) {
        this.count = count;
    }
    public int getCount() {
        return count;
    }
    public synchronized int sell(int amount) { //如果不加synchronized就是线程不安全的
        if (this.count >= amount) {
            this.count -= amount;
            return amount;
        } else {
            return 0;
        }
    }
}

