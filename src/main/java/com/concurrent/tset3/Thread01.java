package com.concurrent.tset3;

import lombok.extern.slf4j.Slf4j;

/**
 * 第二个课堂例子
 * Runnable与Thread的区别
 * 1.Runnable底层还是调用的Thread的run方法
 * 2.Thread是把线程和任务合并在了一起
 * 3.Runnable是把线程和任务分开了
 * 4.用 Runnable 更容易与线程池等高级 API 配合
 */
@Slf4j(topic = "c.Thread01:")
public class Thread01 {

    public static void main(String[] args) {
        /**
         * 继承Thread实现线程
         */
        Thread thread1 = new Thread("thread1"){
            @Override
            public void run() {
                log.debug("thread1");
            }
        };
        thread1.start();
        /**
         * 第二种 实现Runnable接口
         * 相比继承Thread把【线程】和【任务】（要执行的代码）分开
         */
        Runnable runnable = new Runnable() {
            public void run(){
                log.debug("thread2");
            }
        };
        //使用java8实现后
        //Runnable runnable =() -> log.debug("thread2");

        Thread thread2 = new Thread(runnable,"thread2");
        thread2.start();

        log.debug("main");
    }
}
