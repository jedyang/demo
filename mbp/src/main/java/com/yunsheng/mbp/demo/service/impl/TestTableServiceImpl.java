package com.yunsheng.mbp.demo.service.impl;

import com.yunsheng.mbp.demo.entity.TestTable;
import com.yunsheng.mbp.demo.mapper.TestTableMapper;
import com.yunsheng.mbp.demo.service.ITestTableService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用来测试的表 服务实现类
 * </p>
 *
 * @author yunsheng
 * @since 2021-01-21
 */
@Service
public class TestTableServiceImpl extends ServiceImpl<TestTableMapper, TestTable> implements ITestTableService {

}
