package com.yunsheng.mix.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;

/**
 * @author yunsheng
 * @version V1.0
 * @Title: TarUtil.java
 * @Description: tar工具类
 */
public class TarUtil {
    private static List<File> files = new ArrayList<File>();
    public static final String[] paths = new String[]{
            "D:\\图库",
            "D:\\y.jpg",
            "D:\\一个合同.pdf"
    };
    public static final File target = new File("D:\\打包测试.tar");

    /**
     * @return File 返回打包后的文件
     * @throws
     * @Title: pack
     * @Description: 将一组文件打成tar包
     */
    public static File pack() {
        long startTime = System.currentTimeMillis();
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(target);
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }
        TarArchiveOutputStream os = new TarArchiveOutputStream(out);
        os.setLongFileMode(2);
        System.out.println("***************开始打" + target + "包****************");
        int fileCount = 0;
        for (File file : getSourcesFile()) {
            try {
                os.putArchiveEntry(new TarArchiveEntry(file));
                IOUtils.copy(new FileInputStream(file), os);
                os.closeArchiveEntry();
                fileCount++;
            } catch (FileNotFoundException e) {
                System.out.print("****创建" + file.getName() + "******异常");
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (os != null) {
            try {
                os.flush();
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        long endTime = System.currentTimeMillis();
        System.out.println("***************打" + target + "包结束:用时 "
                + (endTime - startTime) + "毫秒，共" + fileCount + "个文件。");

        return target;
    }

    public static List<File> getSourcesFile() {

        for (int i = 0; i < paths.length; i++) {
            try {
                File file = new File(paths[i]);
                getFilesByDirectory(file);
            } catch (Exception e) {
                System.out.println("-----创建文件" + paths[i] + "异常------");
                e.printStackTrace();
            }
        }
        return files;
    }

    public static void getFilesByDirectory(File file) {
        if (file.isDirectory()) {
            File[] fileArrays = file.listFiles();
            for (File f : fileArrays) {
                getFilesByDirectory(f);
            }
        } else {
            files.add(file);
        }
    }

    public static void main(String[] args) {
        pack();
    }
}