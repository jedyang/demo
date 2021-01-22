package com.yunsheng.redis.lock.redisson;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.config.Config;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

/**
 * 使用Redisson封装好的锁
 */
public class LockTest {
    private static Config config = new Config();
    //声明redisson对象
    private static Redisson redisson = null;

    //实例化redisson
    static {
        config.useSingleServer().setAddress("redis://x.x.x.x:6422")
                .setPassword("123456");
        //得到redisson对象
        redisson = (Redisson) Redisson.create(config);

    }

    public static void main(String[] args) {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        List<Logger> loggerList = loggerContext.getLoggerList();
        loggerList.forEach(logger -> {
            logger.setLevel(Level.INFO);
        });

//        ThreadPoolExecutor executor = new ThreadPoolExecutor();
        ExecutorService threadPool = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 10; i++) {
            threadPool.submit(() -> {
                String lockKey = "key1";

                RLock lock = redisson.getLock(lockKey);

                try {
                    System.out.println(LocalDateTime.now());
                    lock.lock(30, TimeUnit.SECONDS);
                    int time = (int) (10 * Math.random());
                    Thread.sleep(time * 1000);
                    System.out.println("业务操作进行了" + time + "秒");
                    System.out.println(LocalDateTime.now());
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (lock.isLocked()) {
                        lock.unlock();
                    }
                }
            });
        }


    }
}
