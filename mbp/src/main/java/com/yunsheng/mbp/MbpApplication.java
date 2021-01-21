package com.yunsheng.mbp;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.yunsheng.mbp.demo.mapper")
@SpringBootApplication
public class MbpApplication {


    public static void main(String[] args) {
        SpringApplication.run(MbpApplication.class, args);
    }

}
