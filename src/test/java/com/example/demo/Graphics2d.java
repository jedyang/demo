package com.example.demo;

import org.junit.Test;
import sun.font.FontDesignMetrics;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * @description:
 * @author: yunsheng
 * @createDate: 2020/3/6
 * @version: 1.0
 */
public class Graphics2d {

    @Test
    public void base() throws IOException {
        int width = 375, height = 750;
        //创建图片对象
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        //基于图片对象打开绘图
        Graphics2D graphics = (Graphics2D) image.getGraphics();

        // 设置背景色
        graphics.setBackground(Color.WHITE);
        graphics.clearRect(0, 0, width, height);//通过使用当前绘图表面的背景色进行填充来清除指定的矩形。
        //绘图逻辑 START （基于业务逻辑进行绘图处理）
        BufferedImage content = ImageIO.read(new File("D:\\y.jpg"));
        graphics.drawImage(content.getScaledInstance(375, 200, Image.SCALE_DEFAULT), 0, 0, null);

        Font font = new Font("宋体", Font.BOLD, 22);
        String zhibo_name = "直播名";
        graphics.setFont(font);
        graphics.setColor(Color.BLACK);
        graphics.drawString(zhibo_name, 30, 210);//图片上写文字

//        Font font = new Font("宋体", Font.BOLD, 22);
        String zhibo_time = "直播名时间";
        graphics.setFont(font);
        graphics.setColor(Color.BLACK);
        graphics.drawString(zhibo_time, 30, 240);

        String zhibo_author = "直播主持人";
        graphics.setFont(font);
        graphics.setColor(Color.BLACK);
        graphics.drawString(zhibo_author, 30, 270);


        BufferedImage img2 = ImageIO.read(new File("D:\\y.jpg"));
        graphics.drawImage(img2.getScaledInstance(150, 150, Image.SCALE_DEFAULT), 10, 600, null);

        BufferedImage img3 = ImageIO.read(new File("D:\\y.jpg"));
        graphics.drawImage(img3.getScaledInstance(200, 200, Image.SCALE_DEFAULT), 200, 500, null);
        //将绘制好的图片写入到图片
        try {
            ImageIO.write(image, "png", new File("abc.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }


        // 绘图逻辑 END
        //处理绘图
        graphics.dispose();
    }
}
