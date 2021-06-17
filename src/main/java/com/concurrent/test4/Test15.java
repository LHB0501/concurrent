package com.concurrent.test4;

import lombok.extern.slf4j.Slf4j;
import java.util.Random;

@Slf4j(topic = "c.test11:")
/**
 *买票问题
 */
public class Test15 {
    public static void main(String[] args) throws InterruptedException {
        //创建两个账户相互转账，每人开始拥有1000快
        Account a = new Account(1000);
        Account b = new Account(1000);
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                //a给b转账1000次，每次金额随机
                a.transfer(b, randomAmount());
            }
        }, "t1");
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                //b给a转账1000次，每次金额随机
                b.transfer(a, randomAmount());
            }
        }, "t2");
        t1.start();
        t2.start();
        t1.join();
        t2.join();
// 查看转账2000次后的总金额
        log.debug("total:{}",(a.getMoney() + b.getMoney()));
    }
    // Random 为线程安全
    static Random random = new Random();
    // 随机 1~100
    public static int randomAmount() {
        return random.nextInt(100) +1;
    }
}
class Account {//账户类
    private int money;//初始化金额
    public Account(int money) {
        this.money = money;
    }
    public int getMoney() {
        return money;
    }
    public void setMoney(int money) {
        this.money = money;
    }
    public synchronized void transfer(Account target, int amount) {
        //如果加synchronized还是不安全的，因为加在方法上等于保护的是自己的共享变量
        //但是此处有两个共享变量this.money和target.money
        //所以这儿应该用synchronized锁住类对象，因为这俩共享变量是一个类中
        synchronized (Account.class){
            if (this.money > amount) {
                this.setMoney(this.getMoney() - amount);
                target.setMoney(target.getMoney() + amount);
            }
        }
    }
}
