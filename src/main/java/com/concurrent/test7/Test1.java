package com.concurrent.test7;

/**
 * final原理
 */
public class Test1 {
    final static int A = 10;
    final static int B = Short.MAX_VALUE+1;

    final  int a = 20;
    final  int b = Integer.MAX_VALUE;

    public static void main(String[] args) {
        System.out.println("AA");
    }
}
