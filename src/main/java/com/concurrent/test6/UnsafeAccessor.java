package com.concurrent.test6;

import org.omg.CORBA.UNKNOWN;
import sun.misc.Unsafe;

import java.lang.reflect.Field;

public class UnsafeAccessor {
    private static Unsafe unSafe = null;

    static {
        //getDeclaredField可以获取私有的成员变量
        Field theUnsafe = null;
        try {
            theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
            theUnsafe.setAccessible(true);
            //获取unsafe对象
            unSafe= (Unsafe) theUnsafe.get(null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }
    public static Unsafe getUnsafe(){return unSafe;}

}
