package com.example.yunsheng.mysql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description:
 * @author: yunsheng
 * @time: 2021/8/10 15:27
 */
@RestController
public class FileController {

    @Autowired
    private FileService fileService;

    @GetMapping("/test")
    public String test() {
        return "ok";
    }
}
