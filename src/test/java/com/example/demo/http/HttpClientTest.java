package com.example.demo.http;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.io.*;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
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
            String pdfUrl = "src/test/resources/pdf/xin.pdf";
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

    /**
     * 测试另一种写法
     * 大同小异
     * 使用addPart加文件
     * 编码使用ContentType contentType = ContentType.create("text/html", "UTF-8");
     * 测试也是可以的
     */
    @Test
    public void test2() {
        Map<String, Object> body = new HashMap<>();
        body.put("version", "1.0");
        body.put("mid", UUID.randomUUID().toString());
        body.put("companyname", "测试科技有限公司");
        body.put("uniscid", "ZZJGD1583485845742");
        body.put("pagenum", "1"); // TODO
        body.put("keyword", "#甲方盖章#"); // TODO
        body.put("num", "-1");
        body.put("sealtype", "1");
        body.put("timestamp", String.valueOf(System.currentTimeMillis()));

        String pdfUrl = "src/test/resources/pdf/一个合同.pdf";
        File file = new File(pdfUrl);
        String url = "http://xxx/v1/getSignWithKey";

        postMultipartFiles(url, file, body, 20000);
    }

    public static void postMultipartFiles(String url, File file, Map<String, Object> params, Integer timeout) {
//        RestResult result = new RestResult();
        CloseableHttpClient httpClient = HttpClients.createDefault();

        try {
            HttpPost httpPost = new HttpPost(url);
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.setCharset(Charset.forName("UTF-8"));
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            builder.addPart("file", new FileBody(file));
            ContentType contentType = ContentType.create("text/html", "UTF-8");
            Iterator var10 = params.entrySet().iterator();

            while(var10.hasNext()) {
                Map.Entry<String, Object> entry = (Map.Entry)var10.next();
                if (entry.getValue() != null) {
                    builder.addTextBody((String)entry.getKey(), entry.getValue().toString(), contentType);
                }
            }

            HttpEntity entity = builder.build();
            httpPost.setEntity(entity);
            httpPost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.90 Safari/537.36");
            HttpResponse response = httpClient.execute(httpPost);
            if (timeout == null) {
                timeout = 180000;
            }

            RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(timeout.intValue()).setConnectionRequestTimeout(timeout.intValue()).setSocketTimeout(timeout.intValue()).build();
            httpPost.setConfig(requestConfig);
            HttpEntity responseEntity = response.getEntity();
            String.valueOf(response.getStatusLine().getStatusCode());
            if (response.getStatusLine().getStatusCode() != 200) {
                System.out.println(response);
            }

            if (responseEntity != null) {
                String resultStr = EntityUtils.toString(responseEntity, Charset.forName("UTF-8"));
                System.out.println(resultStr);
            }
        } catch (Exception var22) {
            Writer w = new StringWriter();
            var22.printStackTrace(new PrintWriter(w));
        } finally {
            try {
                httpClient.close();
            } catch (IOException var21) {
                var21.printStackTrace();
            }

        }

    }
}
