package com.concurrent.test4;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

@Slf4j(topic = "c.test11:")
/**
 成员变量
 1.如果没有被共享，则线程安全
 2.如果被共享了，根据它们的状态是否能够改变，又分两种情况
    2.1如果只有读操作，则线程安全
    2.2如果有读写操作，则这段代码是临界区，需要考虑线程安全

 这个例子就证明成员变量被共享后线程不安全

 主要问题是在于list不是线程安全的，
 可以这样理解：
 thread0主存读取当前size 0，add 1 remove 0，但是并未来的及刷新主存，
 线程B主存读取size 0 add 1 刷新主存，此时线程A也刷新主存，导致最终的size是0
 线程B再执行remove操作的时候，有可能先从主存同步数据，拿到size 0。

 注意：线程从主存同步数据和修改后将数据同步回去的时机是随机的
 */
public class Test11 {
    ArrayList<String> list = new ArrayList<>();
    public void method1(int loopNumber) {
        for (int i = 0; i < loopNumber; i++) {
            //临界区, 会产生竞态条件
            method2();
            method3();
            //临界区
        }
    }
    private void method2() {
        list.add("1");
    }
    private void method3() {
        list.remove(0);
    }
//测试
    public static void main(String[] args) {
        Test11 test = new Test11();
        for (int i = 0; i < 2; i++) {
            new Thread(() -> {
                test.method1(200);
            }, "Thread" + i).start();
        }
    }
}

