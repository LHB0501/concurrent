package com.concurrent.test5;

public class Singleton3 {
        private Singleton3() { }
        private static Singleton3 INSTANCE = null;
        //Q:分析这里的线程安全, 并说明有什么缺点
        //A:每次进来都要加锁判断，所有性能较低
        public static synchronized Singleton3 getInstance() {
            if( INSTANCE != null ){
                return INSTANCE;
            }
            INSTANCE = new Singleton3();
            return INSTANCE;
        }
}
