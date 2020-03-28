## 文件拆分与合并

项目中遇到大文件上传，前端会将大文件切分，后台进行文件合并。为实现这个功能，先实现文件合并功能。

我选择使用RandomAccessFile，相比FileInputStream。RandomAccessFile多了很多功能，非常方便，具体可查看API。

### 拆分文件代码

```
/**
 * 拆分文件
 *
 * @param f
 */
public static void split(File f) throws IOException {

    // 切分为100K大小的文件
    long fileLength = 1024 * 100;

    RandomAccessFile src = new RandomAccessFile(f, "r");
    int numberOfPieces = (int) (src.length() / fileLength) + 1;
    int len = -1;
    byte[] b = new byte[1024];
    for (int i = 0; i < numberOfPieces; i++) {

        String name = "src/test/resources/file/" + f.getName() + "." + (i + 1);
        File file = new File(name);
        file.createNewFile();
        RandomAccessFile dest = new RandomAccessFile(file, "rw");
        while ((len = src.read(b)) != -1) {
            dest.write(b, 0, len);
            //如果太大了就不在这个子文件写了 换下一个
            if (dest.length() > fileLength) {
                break;
            }
        }
        dest.close();
    }
    src.close();
}
```

#### 效果

![image-20200320174749615](D:\github\demo\article\image-20200320174749615.png)

可见生成了三个小文件



### 文件合并代码

```
/**
 * 文件合并
 * @throws IOException
 */
public static void merge() throws IOException {
    
    int length = 3;
    String name = "src/test/resources/file/src.pdf.";
    File file = new File("src/test/resources/file/new.pdf");
    file.createNewFile();
    RandomAccessFile in = new RandomAccessFile(file, "rw");
    in.setLength(0);
    in.seek(0);
    byte[] bytes = new byte[1024];
    int len = -1;
    for (int i = 0; i < length; i++) {
        File src = new File(name + (i + 1));
        RandomAccessFile out = new RandomAccessFile(src, "rw");
        while ((len = out.read(bytes)) != -1) {
            in.write(bytes, 0, len);
        }
        out.close();
    }
    in.close();
}
```

![image-20200320174728865](D:\github\demo\article\image-20200320174728865.png)

看一下文件大小

![image-20200320174912653](D:\github\demo\article\image-20200320174912653.png)

打开文件试一下，没问题，可以正常打开