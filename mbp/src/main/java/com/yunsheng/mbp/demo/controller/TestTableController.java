package com.yunsheng.mbp.demo.controller;


import com.yunsheng.mbp.demo.entity.TestTable;
import com.yunsheng.mbp.demo.service.ITestTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
@RequestMapping("/demo/test-table")
public class TestTableController {

    @Autowired
    private ITestTableService testTableService;

    @GetMapping("/getName/{id}")
    public String getName(@PathVariable("id") Integer id) {
        TestTable byId = testTableService.getById(id);
        if (null == byId) {
            return "unknown";
        } else {
            return byId.getName();
        }
    }
}

