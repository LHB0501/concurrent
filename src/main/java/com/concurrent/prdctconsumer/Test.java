package com.concurrent.prdctconsumer;

import com.concurrent.utils.Downloader;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;
@Slf4j(topic = "c.Test:")
/**
 * 异步模式之生产者/消费者
 * 1. 定义
 * 要点
 * 与前面的保护性暂停中的 GuardObject 不同，不需要产生结果和消费结果的线程一一对应
 * 消费队列可以用来平衡生产和消费的线程资源
 * 生产者仅负责产生结果数据，不关心数据该如何处理，而消费者专心处理结果数据
 * 消息队列是有容量限制的，满时不会再加入数据，空时不会再消耗数据
 * JDK 中各种阻塞队列，采用的就是这种模式
 */
public class Test {
    public static void main(String[] args) {
        MessageQueue messageQueue = new MessageQueue(2);
        // 4 个生产者线程, 下载任务
        for (int i = 0; i < 4; i++) {
            int id = i;
            new Thread(() -> {
                try {
                    //log.debug("download...");
                    List<String> response = Downloader.download();
                            log.debug("try put message({})", id);
                    messageQueue.put(new Message(id, response));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }, "生产者" + i).start();
        }
        // 1 个消费者线程, 处理结果
        new Thread(() -> {
            while (true) {
                Message message = messageQueue.consume();
                List<String> response = (List<String>) message.getMessage();
                log.debug("consume message({}): [{}] lines", message.getId(), response.size());
            }
        }, "消费者").start();
    }
}
