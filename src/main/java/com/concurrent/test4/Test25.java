package com.concurrent.test4;

import lombok.extern.slf4j.Slf4j;

import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 带超时版 GuardedObject
 如果要控制超时时间呢
 P99节课
 */
@Slf4j(topic = "c.test25:")
public class Test25 {
    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 3; i++) {
            new People().start();
        }
        TimeUnit.SECONDS.sleep(1);
        for (Integer id : Mailboxes.getIds()) {
            new Postman(id, "内容" + id).start();
        }
    }
}
//相当与每一个邮箱
class GuardedObject2 {
    //标识唯一的GuardedObject2对象
    private int id;

    public GuardedObject2(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    private Object response;
    private final Object lock = new Object();
    public Object get(long timeout) {
        long begin = System.currentTimeMillis();//开始等待时间 15:00:00
        long passtime = 0;//经历的等待时间 初始化为0
        synchronized (lock) {
            // 条件不满足则等待
            while (response == null) {
                long waittime = timeout -passtime; //剩余应该等待的时间
                if (waittime <=0){ //经历时间超过了最大等待时间
                    break;
                }
                try {
                    lock.wait(waittime);//虚假唤醒 15:00:01
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                passtime = System.currentTimeMillis() -begin; //经历的时间  15:00:02
            }
            return response;
        }
    }
    public void complete(Object response) {
        synchronized (lock) {
            // 条件满足，通知等待线程
            this.response = response;
            lock.notifyAll();
        }
    }
}
//相当与邮箱格子
class Mailboxes {
    private static Map<Integer, GuardedObject2> boxes = new Hashtable<>(); //hatab是线程安全的
    private static int id = 1;
    // 产生唯一 id
    private static synchronized int generateId() {
        return id++;
    }

    public static GuardedObject2 getGuardedObject(int id) {
        return boxes.remove(id);
    }

    public static GuardedObject2 createGuardedObject() {
        GuardedObject2 go = new GuardedObject2(generateId());
        boxes.put(go.getId(), go);
        return go;
    }
    public static Set<Integer> getIds() {
        return boxes.keySet();
    }
}
//收件人
@Slf4j(topic = "c.People:")
class People extends Thread{
    @Override
    public void run() {
        // 收信
        GuardedObject2 GuardedObject2 = Mailboxes.createGuardedObject();
        log.debug("开始收信 id:{}", GuardedObject2.getId());
        Object mail = GuardedObject2.get(5000);
        log.debug("收到信 id:{}, 内容:{}", GuardedObject2.getId(), mail);
    }
}
//邮递员
@Slf4j(topic = "c.Postman:")
class Postman extends Thread {
    private int id;
    private String mail;
    public Postman(int id, String mail) {
        this.id = id;
        this.mail = mail;
    }
    @Override
    public void run() {
        GuardedObject2 GuardedObject2 = Mailboxes.getGuardedObject(id);
        log.debug("送信 id:{}, 内容:{}", id, mail);
        GuardedObject2.complete(mail);
    }
}
