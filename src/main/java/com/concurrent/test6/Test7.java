package com.concurrent.test6;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 原子字段更新器
 */
@Slf4j(topic = "c.Test5")
public class Test7 {
    public static void main(String[] args) throws InterruptedException {
       Student student = new Student();
        AtomicReferenceFieldUpdater updater =
                AtomicReferenceFieldUpdater.newUpdater(Student.class, String.class, "name");
        //假如在主线程给student赋值之前有一个线程已经对这个对象完成了赋值
        new Thread(() -> {
            while (!updater.compareAndSet(student, null, "马化腾")) {}
        }).start();
        Thread.sleep(1000);
        //那么主线程将赋值失败
        System.out.println(updater.compareAndSet(student, null, "马云"));
        System.out.println(student);

    }
}
class Student{
    //使用原子更新器字段必须使用volatile来修饰
    volatile String name;

    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                '}';
    }
}
