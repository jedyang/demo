package com.yunsheng.mix.http;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * @description:
 * @author: uncleY
 * @createDate: 2020/3/13
 * @version: 1.0
 */
public class UrlConnectionTest {
    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url
     *            发送请求的 URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    public static String sendPost(String url, Map param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        //对参数中的sign进行url处理

        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(createLinkString(param));
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

    /**
     *
     * @param url 请求url
     * @param params 请求参数
     * @param filePath 上传文件路径
     * @return
     * @throws Exception
     */
    public static String sendPostFile(String url, Map params, String filePath) throws Exception {
        File file = new File(filePath);
        String BOUNDARY = "------WebKitFormBoundaryAl9CIOBJ1jfQWTl8";
        Proxy proxy = new Proxy(Proxy.Type.HTTP,
                new InetSocketAddress("127.0.0.1", 8888));
        URL realUrl = new URL(url);
        HttpURLConnection urlConnection = (HttpURLConnection) realUrl.openConnection(proxy);
        urlConnection.setUseCaches(false);
        urlConnection.setRequestMethod("POST");
        urlConnection.setDoOutput(true);
        urlConnection.setDoInput(true);
        urlConnection.setRequestProperty("Connection", "Keep-Alive");
        urlConnection.setRequestProperty("Uuser-Agent",
                "Mozilla/5.0 (Windows NT 6.2; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.99 Safari/537.36");
        urlConnection.setRequestProperty("Charset", "UTF-8");
        urlConnection.setRequestProperty("Content-Type",
                "multipart/form-data; boundary=" + "----WebKitFormBoundaryAl9CIOBJ1jfQWTl8");
        urlConnection.connect();
        StringBuilder contentBody1 = new StringBuilder();
        StringBuilder contentBody2 = new StringBuilder();
        String boundary = BOUNDARY + "\r\n";
        DataOutputStream out = new DataOutputStream(urlConnection.getOutputStream());
        byte[] end_data = ("------WebKitFormBoundaryAl9CIOBJ1jfQWTl8--".getBytes());
        try {
            if (file != null) {
                // 上传文件
                contentBody1.append(boundary);
                contentBody1
                        .append("Content-Disposition: form-data; name=\"file\"; filename=\"" + file.getName() + "\""
                                + "\r\n");
                contentBody1.append("\r\n");
                out.write(contentBody1.toString().getBytes());
                // 读取文件
                DataInputStream dis = new DataInputStream(new FileInputStream(file));
                int bytes = 0;
                byte[] bufferOut = new byte[(int) file.length()];
                bytes = dis.read(bufferOut);
                out.write(bufferOut, 0, bytes);
                out.write("\r\n".getBytes());
                dis.close();
                // 传送参数
                Iterator<Map.Entry<Integer, Integer>> entries = params.entrySet().iterator();
                while (entries.hasNext()) {
                    Map.Entry<Integer, Integer> entry = entries.next();
                    contentBody2.append(boundary);
                    contentBody2.append("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"" + "\r\n");
                    contentBody2.append("\r\n");
                    contentBody2.append(entry.getValue() + "\r\n");
                }
                out.write(contentBody2.toString().getBytes());
                out.write(end_data);
                out.flush();
                // 从服务器获得回答的内容
                InputStream inputStream = null;
                try {
                    inputStream = urlConnection.getInputStream();
                } catch (Exception e) {
                    // TODO: handle exception
                    inputStream = urlConnection.getErrorStream();

                }
                InputStreamReader reader = new InputStreamReader(inputStream);
                BufferedReader in = new BufferedReader(reader);
                String strLine = "";
                String strResponse = "";
                while ((strLine = in.readLine()) != null) {
                    strResponse += strLine + "\n";
                }
                return strResponse;
            }
        } finally {
            out.close();
        }
        return "";
    }

    /**
     * 把Map中所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
     *
     * @param params
     * @param encode
     * @return
     */
    public static String createLinkString(Map<String, String> params) {
        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);// 按首字母排序
        StringBuilder prestrSB = new StringBuilder();
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);
            if (i == keys.size() - 1) {// 拼接时，不包括最后一个&字符
                prestrSB.append(key).append("=").append(value);
            } else {
                prestrSB.append(key).append("=").append(value).append("&");
            }
        }
        return prestrSB.toString();
    }
}
