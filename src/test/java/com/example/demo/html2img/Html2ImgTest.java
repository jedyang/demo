package com.example.demo.html2img;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @作者 UncleY
 * @时间 2020/3/6
 * @描述
 */
public class Html2ImgTest {
    @Test
    public void html2Img() throws Exception {
        String htmlContent = Html2Img.getHtmlContent("D:\\meyley\\zhibo.html", "UTF-8");

        Html2Img.html2Img(htmlContent, "D:\\meyley\\zhibo.jpg");
    }

}