package com.yunsheng.mix.file;

import org.junit.Test;

import java.io.File;

/**
 * @作者 技术笔记与开源分享
 * @时间 2020/3/20
 * @描述
 */
public class SplitAndMergeTest {
    @Test
    public void split() throws Exception {
        File src = new File("src/test/resources/file/src.pdf");
        SplitAndMerge.split(src);
    }

    @Test
    public void merge() throws Exception {
        SplitAndMerge.merge();
    }

}