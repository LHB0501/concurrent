package com.concurrent.tset3;

import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "c.Thread03:")
/**
 * 第五个课堂例子
    start与run方法的区别
 */
public class Thread04 {
    public static void main(String[] args) {
        Thread t1 = new Thread("t1"){
            @Override
            public void run() {
                log.debug("t1线程调用run");
            }
        };
        /**
         * t1.run(); 此如果调用run方法执行线程，这里相当于普通的java new了一个对象而已
         并不是真正的线程异步调用，还是同步
         */
        t1.run();
        System.out.println(t1.getState());
        t1.start();
        /**
         小节：
         直接调用 run 是在主线程中执行了 run，没有启动新的线程
         使用 start 是启动新的线程，通过新的线程间接执行 run 中的代码
         一个线程只能使用一次start方法，否则会报错IllegalThreadStateException
         */
        System.out.println(t1.getState());
        log.debug("main do otherThings");
    }
}
