package com.concurrent.test4;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 哲学家就餐问题
 *
 * 有五位哲学家，围坐在圆桌旁。
 * 他们只做两件事，思考和吃饭，思考一会吃口饭，吃完饭后接着思考。
 * 吃饭时要用两根筷子吃，桌上共有 5 根筷子，每位哲学家左右手边各有一根筷子。
 * 如果筷子被身边的人拿着，自己就得等待
 *使用trylock解决死锁问题
 */
@Slf4j(topic = "c.test37:")
public class Test37 {
    public static void main(String[] args) {
        Chopstick1 c1 = new Chopstick1("1");
        Chopstick1 c2 = new Chopstick1("2");
        Chopstick1 c3 = new Chopstick1("3");
        Chopstick1 c4 = new Chopstick1("4");
        Chopstick1 c5 = new Chopstick1("5");
        new Philosopher1("苏格拉底", c1, c2).start();
        new Philosopher1("柏拉图", c2, c3).start();
        new Philosopher1("亚里士多德", c3, c4).start();
        new Philosopher1("赫拉克利特", c4, c5).start();
        new Philosopher1("阿基米德", c5, c1).start();
    }
}

//筷子类
class Chopstick1 extends ReentrantLock {
    String name;
    public Chopstick1(String name) {
        this.name = name;
    }
    @Override
    public String toString() {
        return "筷子{" + name + '}';
    }
}

//哲学家类
@Slf4j(topic = "c.test29:")
class Philosopher1 extends Thread {
    Chopstick1 left;
    Chopstick1 right;
    public Philosopher1(String name, Chopstick1 left, Chopstick1 right) {
        super(name);
        this.left = left;
        this.right = right;
    }
    @Override
    public void run() {
        while (true) {
            // 尝试获得左手筷子
            if (left.tryLock()) {
                try {
                    // 尝试获得右手筷子
                    if (right.tryLock()) {
                        try {
                            eat();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } finally {
                            right.unlock();
                        }
                    }
                } finally {
                    left.unlock();
                }
            }
        }
    }
    private void eat() throws InterruptedException {
        log.debug("eating...");
        TimeUnit.SECONDS.sleep(1);
    }
}