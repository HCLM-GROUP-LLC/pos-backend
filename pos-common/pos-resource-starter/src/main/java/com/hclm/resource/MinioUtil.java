package com.hclm.resource;

import io.minio.*;
import io.minio.errors.ErrorResponseException;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * minio工具
 *
 * @author 吴涵华
 * @since 2024/12/17
 */
@AllArgsConstructor
@Data
public class MinioUtil {
    private MinioClient minioClient;
    /**
     * 默认桶
     */
    private String defaultBucket;
    private String previewHost;
    

    /**
     * put文件
     *
     * @param bucketName 桶名字
     * @param objectName 对象名字
     * @param stream     输入流
     * @throws Exception 异常
     */
    public void putFile(String bucketName, String objectName, InputStream stream) throws Exception {
        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .stream(stream, -1, 10485760)
                        .build()
        );

    }

    /**
     * 上传文件
     *
     * @param objectName 对象名称
     * @param stream     输入流
     * @throws Exception 异常
     */
    public void putFile(String objectName, InputStream stream) throws Exception {
        putFile(getDefaultBucket(), objectName, stream);
    }

    /**
     * 上传文件
     *
     * @param bucketName 桶名字
     * @param objectName 对象名称
     * @param bytes      字节
     * @throws Exception 异常
     */
    public void putFile(String bucketName, String objectName, byte[] bytes) throws Exception {
        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .stream(new ByteArrayInputStream(bytes), bytes.length, -1)
                        .build()
        );
    }

    /**
     * 上传文件
     *
     * @param objectName 对象名称
     * @param bytes      字节
     * @throws Exception 异常
     */
    public void putFile(String objectName, byte[] bytes) throws Exception {
        putFile(getDefaultBucket(), objectName, bytes);
    }

    /**
     * 复制
     *
     * @param inputStream  输入流
     * @param outputStream 输出流
     */
    private static void copy(InputStream inputStream, OutputStream outputStream) {
        try (inputStream; outputStream) {
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * 获取文件
     *
     * @param bucketName   桶名字
     * @param objectName   对象名字
     * @param outputStream 输出流
     * @throws Exception 异常
     */
    public void getFile(String bucketName, String objectName, OutputStream outputStream) throws Exception {
        copy(getFile(bucketName, objectName), outputStream);
    }

    /**
     * 获取文件
     *
     * @param bucketName 桶名字
     * @param objectName 对象名字
     * @return {@link InputStream }
     * @throws Exception 例外
     */
    public InputStream getFile(String bucketName, String objectName) throws Exception {
        return minioClient
                .getObject(GetObjectArgs.builder().bucket(bucketName).object(objectName).build());
    }

    /**
     * 下载文件
     *
     * @param objectName   对象名称
     * @param outputStream 输出流
     * @throws Exception 异常
     */
    public void getFile(String objectName, OutputStream outputStream) throws Exception {
        getFile(getDefaultBucket(), objectName, outputStream);
    }

    /**
     * 得到文件
     *
     * @param objectName 对象名称
     * @return {@link InputStream}
     * @throws Exception 异常
     */
    public InputStream getFile(String objectName) throws Exception {
        return getFile(getDefaultBucket(), objectName);
    }

    /**
     * 存在
     *
     * @param bucketName 桶名字
     * @param objectName 对象名字
     * @return boolean
     * @throws RuntimeException 运行时异常
     */
    public boolean exist(String bucketName, String objectName) throws RuntimeException {
        try {
            StatObjectArgs args = StatObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .build();
            minioClient.statObject(args);
            return true; // 对象存在
        } catch (ErrorResponseException e) {
            if ("Object does not exist".equalsIgnoreCase(e.getMessage())) {
                // 对象不存在
                return false;
            }
            throw new RuntimeException(e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 存在
     *
     * @param objectName 对象名字
     * @return boolean
     * @throws RuntimeException 运行时异常
     */
    public boolean exist(String objectName) throws RuntimeException {
        return exist(getDefaultBucket(), objectName);
    }

    /**
     * 静态预览url,需要桶允许公开访问，并提前配置好访问域名
     *
     * @param bucketName 桶
     * @param objectName 对象名称
     * @return {@link String }
     */
    public String staticPreviewURL(String bucketName, String objectName) {
        if (previewHost == null || previewHost.isEmpty()) {
            return null;
        }
        return normalizeURL(previewHost + "/" + bucketName + "/" + objectName);
    }

    /**
     * 规范化url
     *
     * @param url url
     * @return {@link String }
     */
    private static String normalizeURL(String url) {
        try {
            URI uri = new URI(url);
            return uri.normalize().toString();
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Invalid URL: " + url, e);
        }
    }

    /**
     * 静态预览url,需要桶允许公开访问，并提前配置好访问域名
     *
     * @param objectName 对象名称
     * @return 静态预览url
     */
    public String staticPreviewURL(String objectName) {
        return staticPreviewURL(getDefaultBucket(), objectName);
    }

    /**
     * 删除文件
     *
     * @param bucketName 桶姓名
     * @param objectName 对象名称
     * @throws Exception 例外
     */
    public void deleteFile(String bucketName, String objectName) throws Exception {
        minioClient.removeObject(
                RemoveObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .build()
        );
    }

    /**
     * 删除文件
     *
     * @param objectName 对象名称
     * @throws Exception 例外
     */
    public void deleteFile(String objectName) throws Exception {
        deleteFile(getDefaultBucket(), objectName);
    }
}
