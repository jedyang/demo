package com.yunsheng.base.juc;

/**
 * @description:
 * @author: yunsheng
 * @time: 2021/12/8 9:27
 */
public class ThreadTest {

    public void sayGreeting(String greeting, int times) {
        for (int i = 0; i < times; i++) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(greeting + i);
        }
    }

    public static void main(String[] args) {
        ThreadTest demo = new ThreadTest();
        new Thread(() -> {
            demo.sayGreeting("hi", 10);
        }).start();

        new Thread(() -> {
            demo.sayGreeting("hello", 10);
        }).start();
    }

}
