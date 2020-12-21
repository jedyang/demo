package com.example.demo.oom;

import java.util.ArrayList;
import java.util.List;

public class TestOOM {
    /**
     *  -Xms8m -Xmx8m -XX:+PrintGC -XX:+PrintGCTimeStamps -Xloggc:gc.log -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=./
     * 打印内存dump -XX:HeapDumpPath=./ 也可以指定文件名，不指定就是java_pid进程号.hprof
     */
    public static void main(String[] args) {
        TestOOM testOOM = new TestOOM();
        testOOM.oom();
    }

    private void oom() {
        List<User> list = new ArrayList<>();
        long i = 0;
        while (true) {
            System.out.println("==========" + i);
            list.add(new User());
            i++;
        }
    }

    class User {
        private byte[] img = new byte[1024 * 1024]; // 1M
    }
}
