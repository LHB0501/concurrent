package com.concurrent.test5;

public class Singleton5 {
    private Singleton5() { }
    //Q1：属于懒汉式还是饿汉式
    //A:懒汉式
    private static class LazyHolder {
        static final Singleton5 INSTANCE = new Singleton5();
    }
    //Q2：在创建时是否有并发问题
    //A:没有并发问题是有JVM来保证它的线程安全性的
    public static Singleton5 getInstance() {
        return LazyHolder.INSTANCE;
    }
}
