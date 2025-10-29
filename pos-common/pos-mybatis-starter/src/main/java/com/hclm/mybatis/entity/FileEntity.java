package com.hclm.mybatis.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.hclm.mybatis.TableNameConstant;
import com.hclm.mybatis.enums.FileOwnerEnum;
import lombok.Data;

@TableName(TableNameConstant.FILES)
@Data
public class FileEntity {
    /**
     * 文件ID
     */
    @TableId(type = IdType.AUTO)
    private Long fileId;
    /**
     * 所有者
     */
    private String ownerId;
    /**
     * 所有者类型
     */
    private FileOwnerEnum ownerType;
    /**
     * 商户id
     */
    private String merchantId;
    /**
     * 文件类型
     */
    private String contentType;
    /**
     * 文件名
     */
    private String fileName;
    /**
     * minio对象名
     */
    private String objectName;
    /**
     * 文件大小
     */
    private Long fileSize;
    /**
     * 预览地址URL
     */
    private String previewUrl;
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Long createAt;
    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.UPDATE)
    private Long updateAt;
}
