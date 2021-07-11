package com.concurrent.test8;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author maric
 * @Description 自定义线程池阻塞队列的实现 线程池原理
 * @date 2021/6/19 16:32
 */
@Slf4j(topic = "c.Test1")
public class Test1 {
    public static void main(String[] args) {
        ThreadPool threadPool = new ThreadPool(1, 1000, TimeUnit.MILLISECONDS, 1, (queue, task) -> {
            //queue.put(task);//1.死等
            //queue.put(task,500,TimeUnit.MILLISECONDS);//2.带超时的等待
            //log.debug("放弃{}",task);//3.主线程放弃任务的执行，
            //throw new RuntimeException("任务执行失败！"+task);//4.抛出异常
            task.run(); //5.让调用者自己执行
        });
        for (int i = 0; i < 4; i++) {
            int j = i;
            threadPool.execute(() -> {
                log.debug("{}", j);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
    }
}

@Slf4j(topic = "c.ThreadPool")
class ThreadPool {
    //任务队列
    private BlockingQueue<Runnable> taskQueue;
    //队列上限大小
    private int queuecapcity;
    //线程集合
    private HashSet<worker> workes = new HashSet();
    //线程数
    private int coresize;
    //获取任务的超时时间
    private long timeOut;
    //时间单位
    private TimeUnit timeUnit;
    //拒绝策略
    private RejectPolicy<Runnable> rejectPolict;

    public ThreadPool(int coresize, long timeOut, TimeUnit timeUnit, int queuecapcity, RejectPolicy<Runnable> rejectPolict) {
        this.coresize = coresize;
        this.timeOut = timeOut;
        this.timeUnit = timeUnit;
        this.taskQueue = new BlockingQueue<>(queuecapcity);
        this.rejectPolict = rejectPolict;
    }

    //执行任务
    public void execute(Runnable task) {
        //当任务队列没有超过线程池的大小coresize，直接交给worker去执行
        //当任务队列超过线程池的大小coresize，将任务添加到阻塞队列
        synchronized (workes) {
            if (workes.size() < coresize) {
                worker worker = new worker(task);
                log.debug("新增worker{} ,{}", worker, task);
                workes.add(worker);
                worker.start();
            } else {
                //添加到任务队列有下面五种方法可以执行
                //1.死等
                //2.带超时的等待
                //3.主线程放弃任务的执行，
                //4.抛出异常
                //5.让调用者自己执行
                taskQueue.tryPut(rejectPolict, task);
            }
        }
    }

    class worker extends Thread {
        private Runnable task;

        public worker(Runnable task) {
            this.task = task;
        }

        /**
         * 执行任务
         * 情况1：当task不为空执行任务
         * 情况2：当task执行完毕 再从任务队列列的获取任务执行
         */
        @Override
        public void run() {
            //while (task != null || (task = taskQueue.get()) != null) {
            while (task != null || (task = taskQueue.get(timeOut, timeUnit)) != null) {
                try {
                    log.debug("正在执行任...{}", task);
                    task.run();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    task = null;
                }
            }
            synchronized (workes) {
                log.debug("任务被移除...{}", this);
                workes.remove(this);
            }
        }

    }
}

//拒绝策略
@FunctionalInterface
interface RejectPolicy<T> {
    void reject(BlockingQueue<T> queue, T task);
}

@Slf4j(topic = "c.BlockingQueue")
class BlockingQueue<T> {
    //任务队列
    private Deque<T> queue = new ArrayDeque<>();
    //锁
    private ReentrantLock lock = new ReentrantLock();
    //队列容量
    private int capcity;
    //生产者条件变量
    private Condition fullWaitSet = lock.newCondition();
    //消费者条件变量
    private Condition empWaitSet = lock.newCondition();

    public BlockingQueue(int capcity) {
        this.capcity = capcity;
    }

    //带超时的阻塞获取
    public T get(long timeOut, TimeUnit unit) {
        lock.lock();
        try {
            //将超时时间统一转换成纳秒
            long nanos = unit.toNanos(timeOut);
            while (queue.isEmpty()) {
                try {
                    //返回的事他剩余时间
                    if (nanos <= 0) {
                        return null;
                    }
                    nanos = empWaitSet.awaitNanos(nanos);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            T t = queue.removeFirst();
            fullWaitSet.signal();
            return t;
        } finally {
            lock.unlock();
        }
    }

    //阻塞获取
    public T get() {
        lock.lock();
        try {
            while (queue.isEmpty()) {
                try {
                    empWaitSet.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            T t = queue.removeFirst();
            fullWaitSet.signal();
            return t;
        } finally {
            lock.unlock();
        }
    }

    //阻塞添加
    public void put(T t) {
        lock.lock();
        try {
            while (queue.size() == capcity) {
                try {
                    log.debug("等待插入任务队列 ,{}", t);
                    fullWaitSet.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            log.debug("插入任务队列 ,{}", t);
            queue.addLast(t);
            empWaitSet.signal();
        } finally {
            lock.unlock();
        }

    }

    //带超时的阻塞添加
    public boolean put(T t, long timeOut, TimeUnit timeUnit) {
        lock.lock();
        try {
            long nanos = timeUnit.toNanos(timeOut);
            while (queue.size() == capcity) {
                try {
                    log.debug("等待插入任务队列 ,{}", t);
                    if (nanos <= 0) {
                        return false;
                    }
                    fullWaitSet.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            log.debug("插入任务队列 ,{}", t);
            queue.addLast(t);
            empWaitSet.signal();
            return true;
        } finally {
            lock.unlock();
        }
    }

    //获取大小
    public int getCapcity() {
        lock.lock();
        try {
            return queue.size();
        } finally {
            lock.unlock();
        }
    }

    public void tryPut(RejectPolicy<T> rejectPolicy, T t) {
        lock.lock();
        try {
            if (queue.size() == capcity) {
                rejectPolicy.reject(this, t);
            } else {
                log.debug("插入任务队列 ,{}", t);
                queue.addLast(t);
                empWaitSet.signal();
            }
        } finally {
            lock.unlock();
        }
    }
}
