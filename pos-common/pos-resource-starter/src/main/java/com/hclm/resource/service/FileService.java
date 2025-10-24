package com.hclm.resource.service;

import com.hclm.mybatis.entity.FileEntity;
import com.hclm.resource.FileOwner;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    /**
     * 上传文件
     *
     * @param merchantId 商户id
     * @param file       文件
     * @param fileOwner  文件所有者
     * @return {@link FileEntity }
     */
    FileEntity uploadFIle(String merchantId, MultipartFile file, FileOwner fileOwner);
}
