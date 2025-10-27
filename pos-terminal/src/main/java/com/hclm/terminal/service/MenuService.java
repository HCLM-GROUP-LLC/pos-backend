package com.hclm.terminal.service;

import com.hclm.terminal.pojo.response.MenuResponse;

import java.util.Collection;
import java.util.List;

/**
 * 菜单服务
 *
 * @author hanhua
 * @since 2025/10/27
 */
public interface MenuService {
    /**
     * 查询所有菜单
     *
     * @return {@link List }<{@link MenuResponse }>
     */
    Collection<MenuResponse> queryAllMenus();
}
