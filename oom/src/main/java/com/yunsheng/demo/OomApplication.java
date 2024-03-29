package com.yunsheng.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SpringBootApplication
public class OomApplication {

    public static void main(String[] args) {
        System.out.println("===============");
        SpringApplication.run(OomApplication.class, args);
        System.out.println("********************");
//        oom();
        stringOOM();
    }

    /**
     * 不断创建string会导致哪块内存溢出
     * 堆溢出
     * char[]
     */
    private static void stringOOM() {
        List<String> s = new ArrayList<>();
        while (true) {
            String r = "asl;dkfl;askdjfl;askjdfg;lajsfopijhasodifjh;akhsdfgihjaopsdijgoapsihgdoasidhfgopijshofgdsjfgopiahgfoiajhopiujgfroiwehgfhsdioguhsfdihpiudfghfsadiufhioasudhgiuahfoiusbfiuashdciudhasfuijdhiaucshiaushfbiudhsciauhdsiufhiasduhfoisauhfiuashdifuhaisudhf" + System.currentTimeMillis() + new Random().nextInt(10);
            System.out.println(r);
            s.add(r);
        }
    }

    private static void oom() {
        Runtime rt = Runtime.getRuntime();
        //返回java虚拟机中的初始内存总量Xms，默认应该是机器内存的1/64
        long totalMemory = rt.totalMemory();
        //返回java虚拟机可以使用的最大内存量Xms,默认应该是机器的1/4
        long maxMemory = rt.maxMemory();
        System.out.println("Total_Memory(-Xms ) =  " + totalMemory + " 字节  " + (totalMemory / (double) 1024 / 1024) + "MB");
        System.out.println("Max_Memory(-Xmx ) =  " + maxMemory + " 字节  " + (maxMemory / (double) 1024 / 1024) + "MB");

        // 看一下获取的环境的cpu核数
        System.out.println("cpu核数:" + rt.availableProcessors());
        try {
            Thread.sleep(24 * 60 * 60);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        List l = new ArrayList<>();
//        while (true) {
//            byte b[] = new byte[1048576];
//            l.add(b);
//            System.out.println("使用的内存："+ rt.totalMemory() / (double) 1024 / 1024 + "M，free memory: " + rt.freeMemory() / (double) 1024 / 1024);
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
    }

}
