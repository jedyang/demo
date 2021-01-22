package com.yunsheng.zookeeper;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ZkLockDemo {
    private static final String ZK_ADDRESS = "127.0.0.1:2181";
    private static final String ZK_LOCK_PATH = "/zktest/lock";

    public static void main(String[] args) throws Exception {
        // 连接时的重试策略
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework client = CuratorFrameworkFactory.newClient(ZK_ADDRESS, retryPolicy);
        client.start();

        ExecutorService threadPool = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 10; i++) {
            threadPool.submit(() -> {
                try {
                    doWork(client);
                } catch (Exception e) {
                    e.printStackTrace();
                }


            });
        }
    }

    private static void doWork(CuratorFramework client) throws Exception {
        InterProcessMutex lock = new InterProcessMutex(client, ZK_LOCK_PATH);

//        if (!lock.acquire(5, TimeUnit.SECONDS)) {
//            throw new IllegalStateException("get the lock failed");
//        }
        try {
            lock.acquire();
            System.out.println("get the lock");
            Thread.sleep(2 * 1000);
        } finally {
            System.out.println("release the lock");
            lock.release(); // always release the lock in a finally block
        }
    }

}