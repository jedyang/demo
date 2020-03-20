
package com.example.demo.pdf;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.StampingProperties;
import com.itextpdf.layout.element.Image;
import com.itextpdf.signatures.*;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javax.swing.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;

public class SigningTest {

    public static final String sourceFolder = "src/test/resources/pdf/";
    public static final String destinationFolder = "src/test/resources/pdf/sign/";
    //    public static final String keystorePath = "./src/test/resources/com/itextpdf/signatures/sign/SigningTest/test.p12";
    public static final String keystorePath = "D:\\keystore\\server.p12";
    public static final char[] password = "111111".toCharArray();

    public static final String stamperSrc = "src/test/resources/pdf/公章.png";//印章路径
    private Certificate[] chain; // 证书链
    private PrivateKey pk;

    @Rule
    public ExpectedException junitExpectedException = ExpectedException.none();

    @BeforeClass
    public static void before() {
        Security.addProvider(new BouncyCastleProvider());
        PdfSignatureUtil.createOrClearDestinationFolder(destinationFolder);
    }

    @Before
    public void init() throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException, UnrecoverableKeyException {
        pk = PdfSignatureUtil.readFirstKey(keystorePath, password, password);
        chain = PdfSignatureUtil.readFirstChain(keystorePath, password);
    }


    @Test
    public void testPic() {
        String src = sourceFolder + "一个合同.pdf";
        String fileName = "dest.pdf";
        String dest = destinationFolder + fileName;
        try {
            // 读取keystore ，获得私钥和证书链 jks
//            KeyStore ks = KeyStore.getInstance("jks");
//            ks.load(new FileInputStream(keystorePath), password);
//            String alias = (String) ks.aliases().nextElement();
//            PrivateKey pk = (PrivateKey) ks.getKey(alias, password);
//            Certificate[] chain = ks.getCertificateChain(alias);
            // new一个上边自定义的方法对象，调用签名方法

            ImageData img = ImageDataFactory.create(stamperSrc);
            //读取图章图片，这个image是itext包的image
            Image image = new Image(img);
            float height = image.getImageHeight();
            float width = image.getImageWidth();
            Rectangle rectangle = new Rectangle(150, 200, width, height);

            int pageNum = 1;
            sign(src, String.format(dest, 1), img, pageNum, rectangle, chain, pk, DigestAlgorithms.SHA256, null, PdfSigner.CryptoStandard.CADES, "云胜测试",
                    "青岛");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            e.printStackTrace();
        }
    }

    public void sign(String src  //需要签章的pdf文件路径
            , String dest  // 签完章的pdf文件路径
            , ImageData img // 印章图片
            , int pageNum // 印在第几页
            , Rectangle rectangle // 印章显示的位置
            , Certificate[] chain //证书链
            , PrivateKey pk //签名私钥
            , String digestAlgorithm  //摘要算法名称，例如SHA-1
            , String provider  // 密钥算法提供者，可以为null
            , PdfSigner.CryptoStandard subfilter //数字签名格式，itext有2种
            , String reason  //签名的原因，显示在pdf签名属性中，随便填
            , String location) //签名的地点，显示在pdf签名属性中，随便填
            throws GeneralSecurityException, IOException {
        //下边的步骤都是固定的，照着写就行了，没啥要解释的
        PdfReader reader = new PdfReader(src);
        PdfDocument document = new PdfDocument(reader);
        document.setDefaultPageSize(PageSize.TABLOID);
        //目标文件输出流
        FileOutputStream os = new FileOutputStream(dest);
        //创建签章工具PdfSigner ，最后一个boolean参数
        //false的话，pdf文件只允许被签名一次，多次签名，最后一次有效
        //true的话，pdf可以被追加签名，验签工具可以识别出每次签名之后文档是否被修改
        PdfReader reader2 = new PdfReader(src);
//        PdfSigner stamper = new PdfSigner(reader2, os, true);
        StampingProperties stampingProperties = new StampingProperties();
        stampingProperties.useAppendMode();
        PdfSigner stamper = new PdfSigner(reader2, os, stampingProperties);
        // 获取数字签章属性对象，设定数字签章的属性
        PdfSignatureAppearance appearance = stamper.getSignatureAppearance();
        appearance.setReason(reason);
        appearance.setLocation(location);

        //设置签名的位置，页码，签名域名称，多次追加签名的时候，签名与名称不能一样
        //签名的位置，是图章相对于pdf页面的位置坐标，原点为pdf页面左下角
        //四个参数的分别是，图章左下角x，图章左下角y，图章宽度，图章高度
        appearance.setPageNumber(pageNum);
        appearance.setPageRect(rectangle);
        //插入盖章图片
        appearance.setSignatureGraphic(img);
        //设置图章的显示方式，如下选择的是只显示图章（还有其他的模式，可以图章和签名描述一同显示）
        appearance.setRenderingMode(PdfSignatureAppearance.RenderingMode.GRAPHIC);
        // 这里的itext提供了2个用于签名的接口，可以自己实现，后边着重说这个实现
        // 摘要算法
        IExternalDigest digest = new BouncyCastleDigest();
        // 签名算法
        IExternalSignature signature = new PrivateKeySignature(pk, digestAlgorithm, BouncyCastleProvider.PROVIDER_NAME);
        // 调用itext签名方法完成pdf签章
        stamper.setCertificationLevel(1);
        stamper.signDetached(digest, signature, chain, null, null, null, 0, PdfSigner.CryptoStandard.CADES);
    }


}
