package com.example.demo.html2img;


import gui.ava.html.image.generator.HtmlImageGenerator;

import java.io.*;

/**
 * @description:
 * @author: yunsheng
 * @createDate: 2020/3/6
 * @version: 1.0
 */
public class Html2Img {

    /**
     *
     * @Description 读取HTML文件，获取字符内容
     * @param filePath
     * @param charset
     * @return
     */
    public static String getHtmlContent(String filePath, String charset){

        String line = null;
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(filePath)),charset));
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("读取HTML文件，获取字符内容异常");
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("关闭流异常");
            }
        }
        return sb.toString();
    }

    public static String html2Img(String src, String dest) {
        HtmlImageGenerator imageGenerator = new HtmlImageGenerator();
        try {
            imageGenerator.loadHtml(src);
            Thread.sleep(100);
            imageGenerator.getBufferedImage();
            Thread.sleep(200);
            imageGenerator.saveAsImage(dest);
            //imageGenerator.saveAsHtmlWithMap("hello-world.html", saveImageLocation);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("将HTML文件转换成图片异常");
        }
        return "done";
    }
}
