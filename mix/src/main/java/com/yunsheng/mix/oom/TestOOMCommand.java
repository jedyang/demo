package com.yunsheng.mix.oom;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 测试命令行启动jar，能否答应gc.log
 */
@Component
public class TestOOMCommand implements CommandLineRunner {
    /**
     *  -Xms8m -Xmx8m -XX:+PrintGC -XX:+PrintGCTimeStamps -Xloggc:gc.log -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=./
     * 打印内存dump -XX:HeapDumpPath=./ 也可以指定文件名，不指定就是java_pid进程号.hprof
     */

    @Override
    public void run(String... args) throws Exception {
        TestOOMCommand testOOM = new TestOOMCommand();
        testOOM.oom();
    }

    private void oom() {
        List<User> list = new ArrayList<>();
        long i = 0;
        while (true) {
            System.out.println("==========" + i);
            list.add(new User());
            i++;
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }



    class User {
        private byte[] img = new byte[1024 * 1024]; // 1M
    }
}
