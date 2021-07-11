package com.concurrent.test8;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;

/**
 * @author maric
 * @Description: AQS自定义实现锁
 * @date 2021/7/3 12:52
 */
@Slf4j(topic = "c.Test9")
public class Test9 {
    public static void main(String[] args) {
        MyLock lock = new MyLock();

        new Thread(()->{
            lock.lock();
            log.debug("locking...");
            lock.lock();
            try {
                log.debug("locking...");
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                log.debug("unlocking...");
                lock.unlock();
            }
        },"t1").start();

        /*new Thread(()->{
            lock.lock();
            try {
                log.debug("locking...");
            }finally {
                log.debug("unlocking...");
                lock.unlock();
            }
        },"t2").start();*/
    }
}


//自定义锁(不可重入)
class MyLock implements Lock {

    //自定义一个同步器类
    //独占锁
    class Mysync extends AbstractQueuedSynchronizer {
        @Override
        protected boolean tryAcquire(int arg) {
            if (compareAndSetState(0, 1)) {
                //成功加锁并设置owner为当前线程
                setExclusiveOwnerThread(Thread.currentThread());
                return true;
            }
            return false;
        }

        @Override
        protected boolean tryRelease(int arg) {
            //要注意这两个方法的顺序，因为State是volatile的
            setExclusiveOwnerThread(null);
            setState(0);//把setState放在后面，他前面的方法对于成员变量的修改对其他线程可见
            return true;
        }

        @Override //是否持有独占锁
        protected boolean isHeldExclusively() {
            return getState() == 1;
        }

        public Condition newCondition() {
            return new ConditionObject();
        }
    }

    private Mysync mysync = new Mysync();

    @Override
    public void lock() {
        mysync.acquire(1);

    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
        mysync.acquireInterruptibly(1);

    }

    @Override
    public boolean tryLock() {
        return mysync.tryAcquire(1);
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return mysync.tryAcquireNanos(1,unit.toNanos(time));
    }

    @Override
    public void unlock() {
        mysync.release(1);
    }

    @Override
    public Condition newCondition() {
        return mysync.newCondition();
    }


}

