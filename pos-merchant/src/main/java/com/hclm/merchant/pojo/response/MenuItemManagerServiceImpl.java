package com.hclm.merchant.pojo.response;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hclm.merchant.converter.MenuItemConverter;
import com.hclm.merchant.pojo.dto.MenuItemFileDto;
import com.hclm.merchant.pojo.request.MenuItemAddRequest;
import com.hclm.merchant.pojo.request.MenuItemUpdateRequest;
import com.hclm.merchant.service.MenuItemManagerService;
import com.hclm.merchant.utils.MerchantLoginUtil;
import com.hclm.mybatis.entity.FileEntity;
import com.hclm.mybatis.entity.MenuItemEntity;
import com.hclm.mybatis.mapper.MenuItemMapper;
import com.hclm.resource.service.FileService;
import com.hclm.web.BusinessException;
import com.hclm.web.enums.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@Service
public class MenuItemManagerServiceImpl extends ServiceImpl<MenuItemMapper, MenuItemEntity> implements MenuItemManagerService {
    private final FileService fileService;

    private void checkNameUnique(MenuItemEntity menuItemEntity) {
        if (lambdaQuery()
                .eq(MenuItemEntity::getStoreId, menuItemEntity.getStoreId())
                .eq(MenuItemEntity::getItemName, menuItemEntity.getItemName())
                .ne(menuItemEntity.getItemId() != null, MenuItemEntity::getItemId, menuItemEntity.getItemId()) // 排除自身
                .exists()
        ) {
            throw new BusinessException(ResponseCode.MENU_ITEM_NAME_ALREADY_EXISTS);
        }
    }

    /**
     * 查找由门店id
     *
     * @param storeId 门店id
     * @return {@link List }<{@link MenuItemResponse }>
     */
    @Override
    public List<MenuItemResponse> findByStoreId(String storeId) {
        return MenuItemConverter.INSTANCE.toResponse(
                lambdaQuery()
                        .eq(MenuItemEntity::getStoreId, storeId)
                        .list()
        );
    }

    /**
     * 按类别id查找
     *
     * @param categoryId 类别id
     * @return {@link List }<{@link MenuItemResponse }>
     */
    @Override
    public List<MenuItemResponse> findByCategoryId(Long categoryId) {
        return MenuItemConverter.INSTANCE.toResponse(
                getBaseMapper().findByCategoryId(categoryId)
        );
    }

    @Transactional
    @Override
    public MenuItemResponse create(MenuItemAddRequest request, MultipartFile itemImage) {
        MenuItemEntity menuItemEntity = MenuItemConverter.INSTANCE.toEntity(request);
        checkNameUnique(menuItemEntity);
        save(menuItemEntity);
        //上传文件
        FileEntity fileEntity = fileService.uploadFIle(MerchantLoginUtil.getMerchantId(), itemImage, new MenuItemFileDto(menuItemEntity.getItemId()));
        lambdaUpdate()
                .eq(MenuItemEntity::getItemId, menuItemEntity.getItemId())
                .set(MenuItemEntity::getItemImage, fileEntity.getPreviewUrl())
                .update();
        return MenuItemConverter.INSTANCE.toResponse(menuItemEntity);
    }

    @Override
    public MenuItemResponse update(Long menuItemId, MenuItemUpdateRequest request) {
        if (request.isEmpty()) {// 没有修改
            return MenuItemConverter.INSTANCE.toResponse(getById(menuItemId));
        }
        MenuItemEntity menuItemEntity = MenuItemConverter.INSTANCE.toEntity(request);
        menuItemEntity.setItemId(menuItemId);
        checkNameUnique(menuItemEntity);
        updateById(menuItemEntity);
        return MenuItemConverter.INSTANCE.toResponse(menuItemEntity);
    }
}
