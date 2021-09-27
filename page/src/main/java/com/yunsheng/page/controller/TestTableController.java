package com.yunsheng.page.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 用来测试的表 前端控制器
 * </p>
 *
 * @author yunsheng
 * @since 2021-01-21
 */
@RestController
@RequestMapping("/demo")
public class TestTableController {

    @GetMapping("/getName")
    public String getName() {
        return "yunsheng";
    }
}

