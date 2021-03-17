package com.yunsheng.base.schedule;

import java.time.LocalDateTime;
import java.util.Timer;
import java.util.TimerTask;

public class TimerDemo {
    public static void main(String[] args) {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                System.out.println(LocalDateTime.now());
            }
        }, 1000, 1000);
    }
}
