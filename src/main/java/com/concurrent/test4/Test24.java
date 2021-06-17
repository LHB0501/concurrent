package com.concurrent.test4;

import com.concurrent.utils.Downloader;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 带超时版 GuardedObject
 如果要控制超时时间呢
 P99节课
 */
@Slf4j(topic = "c.test24:")
public class Test24 {
    public static void main(String[] args) {
        GuardedObject1 guardedObject = new GuardedObject1();
        new Thread(() -> {
            try {
                log.debug("执行下载...");
                TimeUnit.SECONDS.sleep(1); //开始下载时先等待一秒
                List<String> response = Downloader.download();
                //List<String> response = null;
                log.debug("download complete...");
                guardedObject.complete(response);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        },"t1").start();
        log.debug("waiting...");
        // 主线程阻塞等待
        Object response = guardedObject.get(1000); //这里的等待时间决定这它是否得到的结果为null
        log.debug("get response: [{}] lines", response);
    }
}

class GuardedObject1 {
    private Object response;
    private final Object lock = new Object();
    public Object get(long timeout) {
        long begin = System.currentTimeMillis();//开始等待时间 15:00:00
        long passtime = 0;//经历的等待时间 初始化为0
        synchronized (lock) {
            // 条件不满足则等待
            while (response == null) {
                long waittime = timeout -passtime; //剩余应该等待的时间
                if (waittime <=0){ //经历时间超过了最大等待时间
                    break;
                }
                try {
                    lock.wait(waittime);//虚假唤醒 15:00:01
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                passtime = System.currentTimeMillis() -begin; //经历的时间  15:00:02
            }
            return response;
        }
    }
    public void complete(Object response) {
        synchronized (lock) {
            // 条件满足，通知等待线程
            this.response = response;
            lock.notifyAll();
        }
    }
}