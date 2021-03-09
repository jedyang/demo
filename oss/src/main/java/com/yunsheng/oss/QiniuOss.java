package com.yunsheng.oss;

import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;

@Slf4j
public class QiniuOss {

    private String endpoint;
    private String accessKey;
    private String secretKey;
    private String bucketName;
    private Auth auth;
    private UploadManager uploadManager;
    private BucketManager bucketManager;

    public QiniuOss(String accessKey, String secretKey, String bucketName) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        this.bucketName = bucketName;
    }

    public boolean upload(String localFilePath, String keyName) {
        if (uploadManager == null) {
            if (auth == null) {
                auth = Auth.create(accessKey, secretKey);
            }
            uploadManager = new UploadManager(new Configuration());
        }

        try {
            String upToken = auth.uploadToken(bucketName);
            Response putResponse = uploadManager.put(localFilePath, keyName, upToken);
            return putResponse.statusCode == 200;
        } catch (QiniuException ex) {
            log.error(ex.getMessage(), ex);
            return false;
        }
    }

    public void upload(InputStream inputStream, String contentType, String keyName) {
        if (uploadManager == null) {
            if (auth == null) {
                auth = Auth.create(accessKey, secretKey);
            }
            uploadManager = new UploadManager(new Configuration());
        }

        try {
            String upToken = auth.uploadToken(bucketName);
            uploadManager.put(inputStream, keyName, upToken, null, contentType);
        } catch (QiniuException ex) {
            log.error(ex.getMessage(), ex);
        }
    }
}
