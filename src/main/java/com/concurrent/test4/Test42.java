package com.concurrent.test4;


import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 *  使用Lock 条件变量交替输出
 */
public class Test42 extends ReentrantLock {
    public static void main(String[] args) throws InterruptedException {
        Test42 as = new Test42(5);
        Condition aWaitSet = as.newCondition();
        Condition bWaitSet = as.newCondition();
        Condition cWaitSet = as.newCondition();
        new Thread(() -> {
            as.print("a", aWaitSet, bWaitSet);
        }).start();
        new Thread(() -> {
            as.print("b", bWaitSet, cWaitSet);
        }).start();
        new Thread(() -> {
            as.print("c", cWaitSet, aWaitSet);
        }).start();
        Thread.sleep(1000);
        as.lock();
        try {
            aWaitSet.signal();
        } finally {
            as.unlock();
        }
    }

    /**
     * 打印次数
     */
    private int loopNumber;

    public Test42(int loopNumber) {
        this.loopNumber = loopNumber;
    }

    /**
     * @param str              打印内容g
     * @param currentCondition 进入哪一间休息室
     * @param nextCondition    下一间休息室
     */
    public void print(String str, Condition currentCondition, Condition nextCondition) {
        for (int i = 0; i < loopNumber; i++) {
            this.lock();
            try {
                currentCondition.await();
                System.out.print(str + ",");
                nextCondition.signal();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                this.unlock();
            }
        }
    }
}
