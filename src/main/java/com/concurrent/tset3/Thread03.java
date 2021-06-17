package com.concurrent.tset3;

import lombok.extern.slf4j.Slf4j;


@Slf4j(topic = "c.Thread03:")
/**
 * 第四个课堂例子
 * 原理之线程运行 查看线程的栈与栈帧
 * 注意运用debug模式
 */
public class Thread03 {
    public static void main(String[] args) {
        Thread thread = new Thread(() -> method1(20));
        thread.setName("t1");
        thread.start();
        method1(10);
    }
    private static void method1(int x){
        int y = x+1;
        Object o= method2();
        System.out.println(o);
    }
    private static Object method2(){
        Object o= new Object();
        return o;
    }
}
