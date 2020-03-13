package com.example.demo.http;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @description:
 * @author: yunsheng
 * @createDate: 2020/3/13
 * @version: 1.0
 */
public class HttpClientTest {
    /**
     * 演示同时发送文件和带中文的form表单
     */
    @Test
    public void testFileAndChn() {
        Map<String, String> body = new HashMap<>();
        body.put("version", "1.0");
        body.put("mid", UUID.randomUUID().toString());
        body.put("companyname", "测试科技有限公司");
        body.put("keyword", "#甲方盖章#"); // TODO
        body.put("num", "-1");
        body.put("sealtype", "1");
        body.put("timestamp", String.valueOf(System.currentTimeMillis()));

        //设置代理IP、端口、协议（请分别替换）
        HttpHost proxy = new HttpHost("127.0.0.1", 8888, "http");

        //把代理设置到请求配置
        RequestConfig defaultRequestConfig = RequestConfig.custom()
                .setProxy(proxy)
                .build();
        CloseableHttpClient client = HttpClients.custom().setDefaultRequestConfig(defaultRequestConfig).build();
//        CloseableHttpClient client = HttpClients.createDefault();
        try {
            String result = "";
            //提取到文件名
            String fileName = "xin.pdf";
            //转换成文件流
            String pdfUrl = "src/test/resources/xin.pdf";
            File file = new File(pdfUrl);
            InputStream is = new FileInputStream(file);
            String url = "http://xxxx";

            HttpPost httpPost = new HttpPost(url);

            MultipartEntityBuilder builder = MultipartEntityBuilder.create().setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            builder.setCharset(Charset.forName("UTF-8"));

            builder.addBinaryBody("file", is, ContentType.MULTIPART_FORM_DATA, fileName);
            // 处理其他参数
            ContentType contentType = ContentType.create("text/plain", Charset.forName("UTF-8"));
            for (Map.Entry<String, String> entry : body.entrySet()) {
                builder.addTextBody(entry.getKey(), entry.getValue(), contentType);
            }
            HttpEntity httpEntity = builder.build();
            httpPost.setEntity(httpEntity);
            HttpResponse response = client.execute(httpPost);
            HttpEntity responseEntity = response.getEntity();
            if (responseEntity != null) { // 将响应内容转换为字符串                                   
                result = EntityUtils.toString(responseEntity, Charset.forName("UTF-8"));
                System.out.println(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
