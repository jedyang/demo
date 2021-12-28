package com.yunsheng.base.juc;


import org.junit.Test;

import java.util.concurrent.CountDownLatch;

public class ThreadTest {

    CountDownLatch countDownLatch = new CountDownLatch(2);

    @Test
    public void test() {
        ThreadTest demo = new ThreadTest();
        new Thread(() -> {
            demo.sayGreeting("hi", 10);
        }).start();

        new Thread(() -> {
            demo.sayGreeting("hello", 10);
        }).start();
    }

}