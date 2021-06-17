package com.concurrent.test5;

public class Singleton {
    private static volatile Singleton instance;
    private Singleton() {}
    public static Singleton getInstance(){
        if (instance == null){
            // 首次访问会加锁，而之后的不加锁
            synchronized (Singleton.class){
                if (instance == null){
                    instance = new Singleton();
                }
            }
        }
        return instance;
    }
}
