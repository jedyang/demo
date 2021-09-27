package com.yunsheng.oss;

import com.alibaba.fastjson.JSONObject;
import lombok.SneakyThrows;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DownLoadPic {
    QiniuOss qiniuOss = new QiniuOss("", "", "yunsheng-duorou");


    public static void main(String[] args) {
        DownLoadPic downLoadPic = new DownLoadPic();
        downLoadPic.getPicUrls("family");
        downLoadPic.getPicUrls("gens");
        downLoadPic.getPicUrls("spes");

    }

    private void getPicUrls(String name) {
        File destFile = new File("f:\\data\\duorou\\" + name + ".json");
        File file = new File("F:\\github\\小程序相关\\开源资源\\duorou\\duo1-master\\duo1\\data\\" + name + ".js");
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            StringBuilder total = new StringBuilder();
            String tempString = null;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                total.append(tempString);
            }
            // 改成json格式
            String pattern = "\\[(.*?)\\]";
            Pattern p = Pattern.compile(pattern);
            Matcher matcher = p.matcher(total.toString());
            while (matcher.find()) {
                int i = 1;
//                System.out.println(matcher.group(i));
                rebuild(matcher.group(i), name, destFile);
                i++;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
    }

    private void rebuild(String group, String name, File destFile) {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(destFile, true));

            String[] strings = group.split("},");
            for (String s : strings) {
                if (!s.endsWith("}")) {
                    s = s + "}";
                }
                String imgName = "";
                String idName = "";
                switch (name) {
                    case "gens":
                        imgName = "genImg";
                        idName = "genId";
                        break;
                    case "spes":
                        imgName = "speImg";
                        idName = "speId";
                        break;
                    default:
                        imgName = "familyImg";
                        idName = "familyId";
                        break;
                }
                System.out.println(s);
                JSONObject jsonObject = JSONObject.parseObject(s);
                String familyImg = jsonObject.getString(imgName);
                String familyId = jsonObject.getString(idName);
                String newUrl = downloadOne(familyImg, name, familyId);
                jsonObject.put(imgName, newUrl);
                writer.write(jsonObject.toJSONString());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e1) {
                }
            }
        }

    }

    @SneakyThrows
    public String downloadOne(String src, String dir, String fileName) {
        URL target = new URL(src);
        URLConnection urlConnection = target.openConnection();

        //获取输入流
        InputStream inputStream = urlConnection.getInputStream();
        //获取输出流 这里是下载保存图片到本地的路径
        String localPath = "f:\\data\\duorou\\pic\\" + dir + "\\" + fileName + ".jpg";
        OutputStream outputStream = new FileOutputStream(localPath);

        int temp = 0;
        while ((temp = inputStream.read()) != -1) {
            outputStream.write(temp);
        }
        System.out.println(fileName + ".jpg下载完毕!!!");
        outputStream.close();
        inputStream.close();

        // 传到七牛云
        boolean upload = qiniuOss.upload(localPath, fileName + ".jpg");
        if (upload) {
            return fileName + ".jpg";
        }
        return "err";
    }
}
