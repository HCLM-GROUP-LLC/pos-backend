package com.hclm.merchant.service;

import com.hclm.merchant.mapper.MenuMapper;
import com.hclm.merchant.pojo.request.MenuRequest;
import com.hclm.merchant.pojo.response.MenuResponse;
import com.hclm.web.entity.Menu;
import com.hclm.web.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;

    @Transactional
    public MenuResponse createMenu(MenuRequest request) {
        Menu menu = MenuMapper.INSTANCE.toEntity(request);
        menu.setMenuId(UUID.randomUUID().toString());
        menu.setCreatedAt(System.currentTimeMillis());
        menu.setUpdatedAt(System.currentTimeMillis());
        menuRepository.save(menu);
        return MenuMapper.INSTANCE.toResponse(menu);
    }
    @Transactional
    public MenuResponse updateMenu(String menuId, MenuRequest request) {
        Menu existing = menuRepository.findById(menuId)
                .orElseThrow(() -> new RuntimeException("菜单不存在"));
        existing.setName(request.getName());
        existing.setDescription(request.getDescription());
        existing.setUpdatedAt(System.currentTimeMillis());
        menuRepository.save(existing);
        return MenuMapper.INSTANCE.toResponse(existing);
    }
    @Transactional
    public void deleteMenu(String menuId) {
        menuRepository.softDeleteById(menuId, System.currentTimeMillis());
    }

    @Transactional
    public void updateIsActive(String menuId, Boolean isActive) {
        menuRepository.updateIsActive(menuId, isActive, System.currentTimeMillis());
    }
}
