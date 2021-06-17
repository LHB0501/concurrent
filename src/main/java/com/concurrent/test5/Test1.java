package com.concurrent.test5;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 *使用volatile优化两阶段打断
 */
@Slf4j(topic = "c.Test1:")
public class Test1 {
    public static void main(String[] args) throws InterruptedException {
        TwoPhaseTermination twoPhaseTermination = new TwoPhaseTermination();
        twoPhaseTermination.start();
        TimeUnit.SECONDS.sleep(3);
        log.debug("主线程执行了打断");
        twoPhaseTermination.stop();
    }
}
@Slf4j(topic = "c.TwoPhaseTermination:")
class TwoPhaseTermination {
    Thread monitor; //监控线程
    private volatile static boolean falg= false;
    //启动监控线程
    public void start() {
        monitor = new Thread(() -> {
            while (true) {
                if (falg) {
                    log.debug("被打断了料理后事");
                    break;
                }
                try {
                    TimeUnit.SECONDS.sleep(1);
                    log.debug("执行监控记录,线程此时正在正常运行中");
                } catch (InterruptedException e) {}
            }
        });
        monitor.setName("monitor");
        monitor.start();
    }

    //停止监控线程
    public void stop() {
        falg= true;
        monitor.interrupt();
    }
}

