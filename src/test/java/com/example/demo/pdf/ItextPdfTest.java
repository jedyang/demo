package com.example.demo.pdf;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;

/**
 * @description:
 * @author: yunsheng
 * @createDate: 2020/2/26
 * @version: 1.0
 */
public class ItextPdfTest {

    /**
     * 使用绝对定位添加文字
     */
    @Test
    public void addTextAbsolutePosition() {
        PdfReader template = null;
        PdfWriter writer = null;
        try {
            // 模拟从模板读取，并在指定位置增加内容
            template = new PdfReader("src/test/resources/pdf/合同多页.pdf");
            writer = new PdfWriter(new FileOutputStream("src/test/resources/pdf/xin.pdf"));
            PdfDocument pdf = new PdfDocument(template, writer);
            // 默认多页pdf只能再第一页打上图片
//            int numberOfPages = pdf.getNumberOfPages();
//            System.out.println(numberOfPages);
//            PdfPage page = pdf.getPage(2);
//            PdfDocument documentPageNum = page.getDocument();
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

    /**
     * 使用绝对定位添加图片
     */
    @Test
    public void addImgtAbsolutePosition() {
        PdfReader reader = null;
        PdfWriter writer = null;
        try {
            // 模拟从模板读取，并在指定位置增加内容
            reader = new PdfReader("src/test/resources/pdf/合同多页.pdf");
            writer = new PdfWriter("src/test/resources/pdf/xin.pdf");
            PdfDocument pdf = new PdfDocument(reader, writer);

            PdfPage page = pdf.getPage(2);

            PdfCanvas pdfCanvas = new PdfCanvas(page);
            Rectangle[] columns = {new Rectangle(6, 650, 100, 100)};
            //几个Rectangle对应几个位置,在这里没什么用
//            pdfCanvas.rectangle(columns[0]);
//            pdfCanvas.stroke();
            Canvas canvas = new Canvas(pdfCanvas, pdf, columns[0]);
            Image img = new Image(ImageDataFactory.create(
                    "src/test/resources/pdf/公章.png"), 100, 100, 100);
            canvas.add(img);

//            PdfDocument document = page.getDocument();
//            Document document1 = new Document(document);
//            Image img = new Image(ImageDataFactory.create(
//                    "src/test/resources/pdf/公章.png"), 100, 100, 100);
//            document1.add(img);

            pdf.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
