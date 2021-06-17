package com.concurrent.test4;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;

@Slf4j(topic = "c.test11:")
/**
 将 Test11中的list修改为局部变量
 分析：
 list 是局部变量，每个线程调用时会创建其不同实例，没有共享
 而 method2 的参数是从 method1 中传递过来的，与 method1 中引用同一个对象
 method3 的参数分析与 method2 相同
 */
//从这个例子可以看出 private 或 final 提供【安全】的意义所在，请体会开闭原则中的【闭】
public class Test12 {
    public final void method1(int loopNumber) {
        for (int i = 0; i < loopNumber; i++) {
            ArrayList<String> list = new ArrayList<>();
            //临界区, 会产生竞态条件
            method2(list);
            method3(list);
            //临界区
        }
    }
    private void method2(ArrayList<String> list) {
        list.add("1");
    }
    private void method3(ArrayList<String> list) {
        list.remove(0);
    }
//测试
    public static void main(String[] args) {
        Test12 test = new Test12();
        for (int i = 0; i < 2; i++) {
            new Thread(() -> {
                test.method1(200);
            }, "Thread" + i).start();
        }
    }
}

