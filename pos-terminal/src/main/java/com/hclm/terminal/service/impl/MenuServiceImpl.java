package com.hclm.terminal.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hclm.mybatis.entity.MenuCatEntity;
import com.hclm.mybatis.entity.MenuEntity;
import com.hclm.mybatis.entity.MenuItemEntity;
import com.hclm.mybatis.mapper.MenuItemMapper;
import com.hclm.mybatis.mapper.MenuMapper;
import com.hclm.terminal.converter.MenuConverter;
import com.hclm.terminal.pojo.response.MenuCatResponse;
import com.hclm.terminal.pojo.response.MenuResponse;
import com.hclm.terminal.service.MenuCatService;
import com.hclm.terminal.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, MenuEntity> implements MenuService {
    private final MenuCatService catService;
    private final MenuItemMapper menuItemMapper;

    @Override
    public Collection<MenuResponse> queryAllMenus() {
        List<MenuEntity> menuList = list();//查询所有的菜单
        if (menuList == null) {
            return List.of();
        }
        Map<Long, MenuResponse> menuResponseMap = MenuConverter.INSTANCE.toResponse(menuList)
                .stream()
                .peek(r -> r.setCats(new LinkedList<>())) //初始化分类,方便后续添加
                .collect(Collectors.toMap(MenuResponse::getMenuId, r -> r));
        //查询分类
        List<MenuCatEntity> catEntityList = catService
                .lambdaQuery()
                .in(MenuCatEntity::getMenuId, menuResponseMap.keySet())
                .list();
        if (catEntityList == null) {
            return menuResponseMap.values();//分类数据为空 就不用 处理了
        }
        //将分类数据转换成响应，并添加到菜单中
        Map<Long, MenuCatResponse> catMap = MenuConverter.INSTANCE.toCatResponse(catEntityList)
                .stream()
                .peek(cat -> menuResponseMap.get(cat.getMenuId()).getCats().add(cat)) //添加到菜单中
                .collect(Collectors.toMap(MenuCatResponse::getCategoryId, r -> r));
        //查询菜单项
        for (Map.Entry<Long, MenuCatResponse> entry : catMap.entrySet()) {
            List<MenuItemEntity> menuItemEntityList = menuItemMapper.findByCategoryId(entry.getKey());//查询该分类下的菜单项
            entry.getValue().setItems(MenuConverter.INSTANCE.toMenuItemResponse(menuItemEntityList));//设置菜单项
        }
        return menuResponseMap.values();
    }
}
