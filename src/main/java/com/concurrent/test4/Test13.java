package com.concurrent.test4;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Random;

@Slf4j(topic = "c.test11:")
/**
 ：在 Test12 的基础上，为Test12类添加子类，子类覆盖 method2 或 method3 方法，即
 */
/**
 * 在这个类中分析下java中的线程安全类
 * String
 * Integer
 * StringBuffer
 * Random
 * Vector
 * Hashtable
 * java.util.concurrent 包下的类
 * 这里说它们是线程安全的是指，多个线程调用它们同一个实例的某以个方法时，是线程安全的。
 * 但是如果这些方法组合使用，就不一定是线程安全的
 *
 * 见图中hashmap两个线程同时调用get 和set方法
 *
 */
class Test13 {
    public final void method1(int loopNumber) {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < loopNumber; i++) {
            method2(list);
            method3(list);
        }
    }
    public void method2(ArrayList<String> list) {
        list.add("1");
    }
    public void method3(ArrayList<String> list) {
        list.remove(0);
    }
}
class Test131 extends Test13{
    @Override
    public void method3(ArrayList<String> list) {
        new Thread(() -> {
            list.remove(0);
        }).start();
    }

    public static void main(String[] args) {
        Test131 test = new Test131();
        for (int i = 0; i < 2; i++) {
            new Thread(() -> test.method1(200), "tson").start();
        }
    }
}

