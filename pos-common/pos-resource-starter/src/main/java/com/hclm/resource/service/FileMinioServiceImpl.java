package com.hclm.resource.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hclm.mybatis.entity.FileEntity;
import com.hclm.mybatis.mapper.FileMapper;
import com.hclm.resource.FileOwner;
import com.hclm.resource.MinioUtil;
import com.hclm.resource.QuotaExceededException;
import com.hclm.resource.UploadFileException;
import com.hclm.resource.properties.FileProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Paths;

/**
 * 文件minio服务实现
 *
 * @author hanhua
 * @since 2025/10/24
 */
@Slf4j
@RequiredArgsConstructor
public class FileMinioServiceImpl extends ServiceImpl<FileMapper, FileEntity> implements FileService {
    private final FileProperties fileProperties;
    private final MinioUtil minioUtil;

    private void checkSize(String merchantId, MultipartFile file) {
        if (getBaseMapper().getUsedStorage(merchantId) > fileProperties.getTenantQuota() + file.getSize()) {
            throw new QuotaExceededException(merchantId);
        }
    }

    private String normalize(String path) {
        return Paths.get(path).normalize().toString().replace('\\', '/');//强制转换为linux路径
    }

    public String uniqueObjectName(String merchantId, FileOwner owner, String originalFilename) {
        String dir = String.join(
                "/",
                merchantId,
                owner.getOwnerType().name()
        );
        String objectName = normalize(dir + "/" + owner.getOwnerId() + "-" + originalFilename);
        if (minioUtil.exist(objectName)) {
            return normalize(dir + "/" + owner.getOwnerId() + "-" + System.currentTimeMillis() + "-" + originalFilename);
        }
        return objectName;
    }

    private FileEntity buildFileEntity(String merchantId, MultipartFile file, String objectName, FileOwner owner) {
        FileEntity filePO = new FileEntity();
        filePO.setFileName(file.getOriginalFilename());
        filePO.setContentType(file.getContentType());
        filePO.setMerchantId(merchantId);
        //所有者类型
        filePO.setOwnerType(owner.getOwnerType());
        //所有者ID
        filePO.setOwnerId(owner.getOwnerId());
        //生成对象名
        filePO.setObjectName(objectName);
        filePO.setFileSize(file.getSize());
        return filePO;
    }

    @Override
    public FileEntity uploadFIle(String merchantId, MultipartFile file, FileOwner fileOwner) {
        checkSize(merchantId, file);
        String objectName = uniqueObjectName(merchantId, fileOwner, file.getOriginalFilename());
        FileEntity filePo = buildFileEntity(merchantId, file, objectName, fileOwner);
        filePo.setObjectName(objectName);
        try {
            minioUtil.putFile(objectName, file.getInputStream());
        } catch (Exception e) {
            log.error("文件上传失败 {}", e.getMessage());
            throw new UploadFileException(e);
        }
        filePo.setPreviewUrl(minioUtil.staticPreviewURL(objectName));
        //保存文件信息
        save(filePo);
        return filePo;
    }
}
