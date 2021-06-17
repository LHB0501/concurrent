package com.concurrent.test4;


import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 使用park unpark 条件变量交替输出
 */
public class Test43 extends ReentrantLock {
    private static Thread t1;
    private static Thread t2;
    private static Thread t3;

    public static void main(String[] args) throws InterruptedException {
        SyncPark syncPark = new SyncPark(5);
        t1 = new Thread(() -> {
            syncPark.print("a", t2);
        });
        t2 = new Thread(() -> {
            syncPark.print("b", t3);
        });
        t3 = new Thread(() -> {
            syncPark.print("c", t1);
        });
        t1.start();
        t2.start();
        t3.start();
        LockSupport.unpark(t1);

    }
}

class SyncPark {
    private int loopNumber;

    public SyncPark(int loopNumber) {
        this.loopNumber = loopNumber;
    }
    public void print(String str, Thread nextThread) {
        for (int i = 0; i < loopNumber; i++) {
            LockSupport.park();
            System.out.print(str + ",");
            LockSupport.unpark(nextThread);
        }
    }
}

