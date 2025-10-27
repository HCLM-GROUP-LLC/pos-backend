package com.hclm.terminal.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hclm.mybatis.entity.MenuItemEntity;
import com.hclm.mybatis.mapper.MenuItemMapper;
import com.hclm.terminal.service.MenuItemService;
import org.springframework.stereotype.Service;

@Service
public class MenuItemServiceImpl extends ServiceImpl<MenuItemMapper, MenuItemEntity> implements MenuItemService {
}
