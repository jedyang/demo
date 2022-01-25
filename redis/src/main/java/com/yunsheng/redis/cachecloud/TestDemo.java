package com.yunsheng.redis.cachecloud;

import com.sohu.tv.builder.ClientBuilder;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.params.SetParams;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @description:
 * @author: yunsheng
 * @time: 2021/12/27 13:54
 */
public class TestDemo {
    public static void main(String[] args) {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        System.out.println(date.getTime());
        long appId = 100104;
        /**
         * 使用自定义配置：
         * 1. setTimeout：redis操作的超时设置；
         * 2. setMaxRedirections：节点定位重试的次数；
         */
        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        JedisCluster redisCluster = ClientBuilder.redisCluster(appId)
                .setJedisPoolConfig(poolConfig)
                .setConnectionTimeout(1000)
                .setSoTimeout(1000)
                .setMaxRedirections(5)
                .build("wW1tUY8n");
        while (true) {
            try {
                long date = new Date().getTime();
                String key = "key" + date;
                SetParams setParams = new SetParams();
                // 100秒过期时间
                redisCluster.set(key, "" + date, setParams.ex(100));
//            redisCluster.set(key, "" + date);
                System.out.println(redisCluster.get(key));

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

}
