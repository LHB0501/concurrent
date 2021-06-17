package com.concurrent.tset3;

import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "c.Thread05:")
/**
 * 第八个课堂例子
 在项目中我们有时通过while (true) 来循环接受消息，但如果在单核cpu上这样使用的话
 会造成cpu使用率达到100%
 所以我们通过sleep让线程间隔性的执行
 这样避免while(true) 空转浪费 cpu，这时可以使用 yield 或 sleep 来让出 cpu 的使用权给其他程序

 */
public class Thread07 {
    public static void main(String[] args){
        while (true) {
            System.out.println("========+++========");
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
