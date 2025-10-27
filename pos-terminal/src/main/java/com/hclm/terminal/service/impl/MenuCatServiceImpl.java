package com.hclm.terminal.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hclm.mybatis.entity.MenuCatEntity;
import com.hclm.mybatis.mapper.MenuCatMapper;
import com.hclm.terminal.service.MenuCatService;
import org.springframework.stereotype.Service;

@Service
public class MenuCatServiceImpl extends ServiceImpl<MenuCatMapper, MenuCatEntity> implements MenuCatService {
}
