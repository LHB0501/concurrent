package com.concurrent.test4;

import com.concurrent.utils.Downloader;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;

/**
 同步模式之保护性暂停
 1. 定义
 即 Guarded Suspension，用在一个线程等待另一个线程的执行结果
 要点
 有一个结果需要从一个线程传递到另一个线程，让他们关联同一个 GuardedObject
 如果有结果不断从一个线程到另一个线程那么可以使用消息队列（见生产者/消费者）
 JDK 中，join 的实现、Future 的实现，采用的就是此模式
 因为要等待另一方的结果，因此归类到同步模式
 */
@Slf4j(topic = "c.test23:")
public class Test23 {
    public static void main(String[] args) {
        GuardedObject guardedObject = new GuardedObject();
        new Thread(() -> {
            try {
                log.debug("执行下载...");
                List<String> response = Downloader.download();
                log.debug("download complete...");
                guardedObject.complete(response);
            } catch (IOException e) {
                e.printStackTrace();
            }
        },"t1").start();
        log.debug("waiting...");
        // 主线程阻塞等待
        Object response = guardedObject.get();
        log.debug("get response: [{}] lines", ((List<String>) response).size());
    }
}
class GuardedObject {
    private Object response;
    private final Object lock = new Object();
    public Object get() {
        synchronized (lock) {
            // 条件不满足则等待
            while (response == null) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
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