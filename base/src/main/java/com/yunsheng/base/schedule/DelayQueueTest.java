package com.yunsheng.base.schedule;

import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public class DelayQueueTest {

    public static void main(String[] args) throws Exception {

        BlockingQueue<SampleTask> delayQueue = new DelayQueue<>();

        long now = System.currentTimeMillis();

        delayQueue.put(new SampleTask(now + 1000));

        delayQueue.put(new SampleTask(now + 2000));

        delayQueue.put(new SampleTask(now + 3000));

        for (int i = 0; i < 3; i++) {

            System.out.println(new Date(delayQueue.take().getTime()));

        }

    }

    static class SampleTask implements Delayed {

        long time;

        public SampleTask(long time) {

            this.time = time;

        }

        public long getTime() {

            return time;

        }

        @Override

        public int compareTo(Delayed o) {

            return Long.compare(this.getDelay(TimeUnit.MILLISECONDS), o.getDelay(TimeUnit.MILLISECONDS));

        }

        @Override

        public long getDelay(TimeUnit unit) {

            return unit.convert(time - System.currentTimeMillis(), TimeUnit.MILLISECONDS);

        }

    }

}
