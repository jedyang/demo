package com.yunsheng.redis.redisTemplate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/redis")
public class TemplateDemo {

    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping("/get/{key}")
    public String getByKey(@PathVariable String key) {
        Object o = redisTemplate.opsForValue().get(key);
        return String.valueOf(o);
    }

    @PostMapping("/post")
    public String postString(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
        return "ok";
    }
}
