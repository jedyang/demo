package com.yunsheng.mix.lock;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileLock;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class FileLockTest {
    public static void main(String[] args) throws IOException {
        File file = new File("D:\\tmp\\lock.txt");
        RandomAccessFile lockFile = new RandomAccessFile(file, "rw");

        FileLock lock = lockFile.getChannel().tryLock(0, 1, false);
        if (lock == null || lock.isShared() || !lock.isValid()) {
            System.out.println("get locked failed");
        } else {
            System.out.println("get locked:" + lock.isValid());
        }

        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            System.out.println("running");
        }, 10, 60, TimeUnit.SECONDS);
    }
}
