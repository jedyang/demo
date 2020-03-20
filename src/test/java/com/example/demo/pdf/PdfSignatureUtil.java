package com.example.demo.pdf;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.util.Enumeration;

/**
 * @description:
 * @author: yunsheng
 * @createDate: 2020/3/17
 * @version: 1.0
 */
public class PdfSignatureUtil {

    public static PrivateKey readFirstKey(String p12FileName, char[] ksPass, char[] keyPass) throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException, UnrecoverableKeyException {
        PrivateKey pk = null;

        KeyStore p12 = KeyStore.getInstance("pkcs12");
        p12.load(new FileInputStream(p12FileName), ksPass);

        Enumeration<String> aliases = p12.aliases();
        while (aliases.hasMoreElements()) {
            String alias = aliases.nextElement();
            if (p12.isKeyEntry(alias)) {
                pk = (PrivateKey) p12.getKey(alias, keyPass);
                break;
            }
        }

        return pk;
    }

    public static java.security.cert.Certificate[] readFirstChain(String p12FileName, char[] ksPass) throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException {
        Certificate[] certChain = null;

        KeyStore p12 = KeyStore.getInstance("pkcs12");
        p12.load(new FileInputStream(p12FileName), ksPass);

        Enumeration<String> aliases = p12.aliases();
        while (aliases.hasMoreElements()) {
            String alias = aliases.nextElement();
            if (p12.isKeyEntry(alias)) {
                certChain = p12.getCertificateChain(alias);
                break;
            }
        }

        return certChain;
    }

    public static void createOrClearDestinationFolder(String path) {
        File fpath = new File(path);
        fpath.mkdirs();
        deleteDirectoryContents(path, false);
    }

    private static void deleteDirectoryContents(String path, boolean removeParentDirectory) {
        File file = new File(path);
        if (file.exists() && file.listFiles() != null) {
            for (File f : file.listFiles()) {
                if (f.isDirectory()) {
                    deleteDirectoryContents(f.getPath(), false);
                    f.delete();
                } else {
                    f.delete();
                }
            }
            if (removeParentDirectory) {
                file.delete();
            }
        }
    }

}
