package com.concurrent.test6;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**

 */
@Slf4j(topic = "c.Test9")
public class Test10 {
    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
        //getDeclaredField可以获取私有的成员变量
        Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
        theUnsafe.setAccessible(true);
        //获取unsafe对象
        Unsafe unsafe = (Unsafe) theUnsafe.get(null);
        System.out.println(unsafe);

        //1.获取属性的偏移量
        long idOffset = unsafe.objectFieldOffset(Teacher.class.getDeclaredField("id"));
        long nameOffset = unsafe.objectFieldOffset(Teacher.class.getDeclaredField("name"));

        Teacher teacher = new Teacher();
        //2.执行 cas 操作
        unsafe.compareAndSwapInt(teacher, idOffset, 0, 1);
        unsafe.compareAndSwapObject(teacher, nameOffset, null, "丁磊");
        //3.验证
        System.out.println(teacher);
    }
}

@Data
class Teacher {
    volatile int id;
    volatile String name;

}