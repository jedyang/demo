package com.example.yunsheng.mysql;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class MysqlApplicationTests {

    @Autowired
    private FileRepository fileRepository;

    @Test
    public void save() {
        for (int i = 0; i < 1000; i++) {
            FileEntity s = new FileEntity();
            File file = new File("D:" + File.separator + "demo.docx");
            try {
                byte[] bytes = Files.readAllBytes(file.toPath());
                s.setFile(bytes);
                FileEntity save = fileRepository.save(s);
                log.info(save.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void get() {
        while (true) {

            long count = fileRepository.count();
            log.info("==========" + count);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
