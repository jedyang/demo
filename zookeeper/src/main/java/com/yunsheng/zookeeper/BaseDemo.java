package com.yunsheng.zookeeper;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

public class BaseDemo {
    private static final String ZK_ADDRESS = "127.0.0.1:2181";
    private static final String ZK_LOCK_PATH = "/zktest/demo";

    public static void main(String[] args) throws Exception {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework client = CuratorFrameworkFactory.newClient(ZK_ADDRESS, retryPolicy);
        client.start();

        /**
         client.create().creatingParentContainersIfNeeded() // 递归创建所需父节点
         .withMode(CreateMode.PERSISTENT) // 创建类型为持久节点
         .forPath("/nodeA", "init".getBytes()); // 目录及内容
         **/
        byte[] bytes = client.getData().forPath("/nodeA");
        System.out.println(new String(bytes));
    }
}
