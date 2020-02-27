## itext使用demo
本文源码路径：https://github.com/jedyang/demo

#### 在指定页码的指定位置插入文字

​       在指定页码的指定位置插入文字属于常规操作，比较简单。但是如果要支持中文，需要多做一点操作。而且有坑要踩。

  首先引入汉语系语言包。（非必须，后面讲）

  ```java
<dependency>
   <groupId>com.itextpdf</groupId>
   <artifactId>font-asian</artifactId>
   <version>${itext.version}</version>
</dependency>
  ```

  代码如下：

  ```java
    public void addTextAbsolutePosition() {
        PdfReader template = null;
        PdfWriter writer = null;
        try {
            // 模拟从模板读取，并在指定位置增加内容
            template = new PdfReader("src/test/resources/pdf/合同多页.pdf");
            writer = new PdfWriter(new   FileOutputStream("src/test/resources/pdf/xin.pdf"));
            PdfDocument pdf = new PdfDocument(template, writer);
            Document document = new Document(pdf, PageSize.A4);

            // 在指定位置加文本
//            pageNumber:要设置绝对位置所在的页码
//            left：添加文本的左下角相对原点的x坐标
//            bottom:添加文本的左下角相对原点的y坐标
//            width:添加文本的横向宽度
            Text text = new Text("hello xxx这是我yyy凉凉");
            // 支持中文字体
//            PdfFont font = PdfFontFactory.createFont("STSong-Light", "UniGB-UCS2-H", false);
//            PdfFont sysFont = PdfFontFactory.createFont("c://windows//fonts//simsun.ttc,1", PdfEncodings.IDENTITY_H, false);
            PdfFont sysFont = PdfFontFactory.createFont("src/test/resources/pdf/simsun.ttc,1", PdfEncodings.IDENTITY_H, false);
            text.setBackgroundColor(ColorConstants.WHITE);
            // 这个text主要是设置背景色为白色，如果text的位置上面有内容就可以覆盖掉内容
            document.add(new Paragraph(text)
                    .setFixedPosition(2, 100, 500, 100) // 在第二页加
                    .setFont(sysFont)); // 设置字体


            document.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
  ```

  使用setFixedPosition直接可以指定第几页，然后设置绝对位置。我注释里写的比较详细，就不重复了。

  注意我这里设置了宽带是100。一会可以看下如果内容太多会怎样。

  然后就是字体。**说一说遇到的坑**

  网上很多都说配置`STSong-Light`字体，这是`font-asian`包中的字体。

  第一次测试没有问题。但是后来发现这种字体对中文的只是很不完善。比如`凉`字，就会导致NPE，真是凉凉。

  后来改用windows系统的字体，位置是`c://windows//fonts`下，我这里用的是宋体。

  测试一下，效果很不错。没有发现异常。

  然后，把字体直接拷贝到项目里。这样以后发布到linux下就没问题了。

​	效果图：

  ![image-20200226195219748](D:\github\demo\src\test\java\com\example\demo\pdf\image-20200226195219748.png)

  可以看到，通过设置背景色为白色，遮蔽原有的文字。当内容过长时，会自动换行。





#### 在指定页码的指定位置插入图片

搜索了网上的很多文章，都不能实现我的需求，通过自己的一点摸索，

下面的示例代码可在第二页的指定位置打上印章

```java
public void addImgtAbsolutePosition() {
        PdfReader reader = null;
        PdfWriter writer = null;
        try {
            // 模拟从模板读取，并在指定位置增加内容
            reader = new PdfReader("src/test/resources/pdf/合同多页.pdf");
            writer = new PdfWriter("src/test/resources/pdf/xin.pdf");
            PdfDocument pdf = new PdfDocument(reader, writer);

            // 例如在第二页插入
            PdfPage page = pdf.getPage(2);

            PdfCanvas pdfCanvas = new PdfCanvas(page);
            Rectangle[] columns = {new Rectangle(6, 650, 100, 100)};
            //几个Rectangle就对应几个位置,在这里没什么用
            Canvas canvas = new Canvas(pdfCanvas, pdf, columns[0]);
            Image img = new Image(ImageDataFactory.create(
                    "src/test/resources/pdf/公章.png"), 100, 100, 100);
            canvas.add(img);

            pdf.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
```

效果：

![image-20200226172035971](D:\github\demo\src\test\java\com\example\demo\pdf\image-20200226172035971.png)

**遇到问题点：**

一开始写的是插入文字，直接使用document.add方法就可以，指定页码和位置。

所以以为图片也可以使用document，通过获取指定页的document，然后直接add图片，发现是只能插入到第一页。

经过几次尝试，最终使用canvas成功。但是感觉Rectangle在这有点奇怪，存在不需要的代码。

欢迎指教。

