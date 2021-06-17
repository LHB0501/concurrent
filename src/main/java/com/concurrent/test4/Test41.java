package com.concurrent.test4;

/**
 * 使用wait notify交替输出
 * 比如，必须先 2 后 1 打印
 */
public class Test41 {
    /**
     * 等待标记
     */
    private int falg;

    /**
     * 打印次数
     */
    private int printNumber;

    public Test41(int falg, int printNumber) {
        this.falg = falg;
        this.printNumber = printNumber;
    }

    /**
     * @param string   要打印的字符串
     * @param nowFalg  本次打印的标记
     * @param nextFlag 下次打印的标记
     * @throws InterruptedException
     */
    private void print(String string, int nowFalg, int nextFlag) throws InterruptedException {

        for (int i = 0; i < printNumber; i++) {
            synchronized (this) {
                while (this.falg != nowFalg) {
                    this.wait();
                }
                System.out.print(string+",");
                this.falg = nextFlag;
                this.notifyAll();
            }
        }
    }

    public static void main(String[] args) {
        Test41 syncWaitNotify = new Test41(1, 5);
        new Thread(() -> {
            try {
                syncWaitNotify.print("a", 1, 2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        new Thread(() -> {
            try {
                syncWaitNotify.print("b", 2, 3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        new Thread(() -> {
            try {
                syncWaitNotify.print("c", 3, 1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

}