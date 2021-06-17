package com.concurrent.tset3;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j(topic = "c.Thread11:")
/**
 * 第十二个例子
 * 设计模式 -两阶段打断
 */
public class Thread11 {
    public static void main(String[] args) throws InterruptedException {
        TwoPhaseTermination twoPhaseTermination = new TwoPhaseTermination();
        twoPhaseTermination.start();
        TimeUnit.SECONDS.sleep(3);
        twoPhaseTermination.stop();
    }
}
@Slf4j(topic = "c.Thread12:")
class TwoPhaseTermination {
    Thread monitor; //监控线程

    //启动监控线程
    public void start() {
        monitor = new Thread(() ->{
            while (true){
                Thread current = Thread.currentThread();
                if(current.isInterrupted()){
                    log.debug("料理后事，处理后续流程");
                    break;
                }
                try {
                    /**
                     *  在情况1这种情况下被打断，线程会清除打断标记，并且会抛出异常
                     * 因此需要我们在catch进行处理，将打断标记置为true
                     */
                    TimeUnit.SECONDS.sleep(1); //情况1
                    /**
                     * 情况2下打断不会清除线程打断标记
                     */
                    log.debug("执行监控记录,线程此时正在正常运行中");//情况2
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    //重新设置打断标记
                    current.interrupt();
                }
            }
        });
        monitor.setName("monitor");
        monitor.start();
    }

    //停止监控线程
    public void stop() {
        monitor.interrupt();
    }
}