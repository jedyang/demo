package com.yunsheng.demo.dubbo.api;

/**
 * @description:
 * @author: yunsheng
 * @time: 2021/5/24 11:22
 */
public interface DemoService {

    /**
     * dubbo服务测试
     * @param name
     * @return
     */
    public String hello(String name);
}