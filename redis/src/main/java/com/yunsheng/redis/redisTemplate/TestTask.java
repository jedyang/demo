package com.yunsheng.redis.redisTemplate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * @description:
 * @author: yunsheng
 * @time: 2021/6/9 11:15
 */
@Component
@EnableScheduling
public class TestTask {

    @Autowired
    private RedisTemplate redisTemplate;

    @Scheduled(fixedDelay = 10)  //间隔10ms
    public void first() throws InterruptedException {
        redisTemplate.opsForValue().set("yunsheng" + System.currentTimeMillis(), System.currentTimeMillis());

//        predixy不支持keys命令
//        Set keys = redisTemplate.keys("yunsheng*");

        Object abc = redisTemplate.opsForValue().get("abc");

        System.out.println(String.valueOf(abc));
    }
}
