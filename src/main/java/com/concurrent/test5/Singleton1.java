package com.concurrent.test5;

import java.io.Serializable;

// 问题1：为什么加 final   防止子类继承他破坏他的单例属性
// 问题2：如果实现了序列化接口, 还要做什么来防止反序列化破坏单例 通过readResolve来防止反序列化破坏单例
public final class Singleton1 implements Serializable {
    // 问题3：为什么设置为私有? 是否能防止反射创建新的实例?  防止无限创建它的实例，所以设置成私有，不能防止反射来创建实例
    private Singleton1() {
    }

    // 问题4：这样初始化是否能保证单例对象创建时的线程安全? 是线程安全的
    private static final Singleton1 INSTANCE = new Singleton1();

    // 问题5：为什么提供静态方法而不是直接将 INSTANCE 设置为 public, 说出你知道的理由
    //使用方法可以提供更好的封装，而且可以支持泛型，而使用属性不行
    public static Singleton1 getInstance() {
        return INSTANCE;
    }

    public Object readResolve() {
        return INSTANCE;
    }
}
