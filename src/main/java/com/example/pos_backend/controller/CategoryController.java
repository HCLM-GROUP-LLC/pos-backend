package com.example.pos_backend.controller;

import com.example.pos_backend.common.ApiResponse;
import com.example.pos_backend.dto.CategoryRequestDTO;
import com.example.pos_backend.dto.CategoryResponseDTO;
import com.example.pos_backend.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * 商品分类控制器
 * 提供商品分类的CRUD操作接口
 * 
 * @author POS System
 * @version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    /**
     * 创建商品分类
     * 
     * @param dto 分类创建请求DTO
     * @return 创建的分类信息
     */
    @PostMapping
    public ResponseEntity<ApiResponse<CategoryResponseDTO>> create(@Valid @RequestBody CategoryRequestDTO dto) {
        log.info("创建商品分类请求: storeId={}, categoryName={}", dto.getStoreId(), dto.getCategoryName());
        
        CategoryResponseDTO response = categoryService.create(dto);
        
        log.info("商品分类创建成功: categoryId={}", response.getCategoryId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "商品分类创建成功"));
    }

    /**
     * 根据ID获取商品分类详情
     * 
     * @param id 分类ID
     * @return 分类详情
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryResponseDTO>> getById(@PathVariable UUID id) {
        log.info("获取商品分类详情: categoryId={}", id);
        
        CategoryResponseDTO response = categoryService.getById(id);
        
        log.info("商品分类详情获取成功: categoryId={}", id);
        return ResponseEntity.ok(ApiResponse.success(response, "获取分类详情成功"));
    }

    /**
     * 获取门店下的所有商品分类列表
     * 
     * @param storeId 门店ID
     * @return 分类列表
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryResponseDTO>>> list(@RequestParam UUID storeId) {
        log.info("获取商品分类列表: storeId={}", storeId);
        
        List<CategoryResponseDTO> response = categoryService.list(storeId);
        
        log.info("商品分类列表获取成功: storeId={}, count={}", storeId, response.size());
        return ResponseEntity.ok(ApiResponse.success(response, "获取分类列表成功"));
    }

    /**
     * 更新商品分类信息
     * 
     * @param id 分类ID
     * @param dto 分类更新请求DTO
     * @return 更新后的分类信息
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryResponseDTO>> update(@PathVariable UUID id, 
                                                                  @Valid @RequestBody CategoryRequestDTO dto) {
        log.info("更新商品分类: categoryId={}, categoryName={}", id, dto.getCategoryName());
        
        CategoryResponseDTO response = categoryService.update(id, dto);
        
        log.info("商品分类更新成功: categoryId={}", id);
        return ResponseEntity.ok(ApiResponse.success(response, "商品分类更新成功"));
    }

    /**
     * 删除商品分类（软删除）
     * 
     * @param id 分类ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
        log.info("删除商品分类: categoryId={}", id);
        
        categoryService.delete(id);
        
        log.info("商品分类删除成功: categoryId={}", id);
        return ResponseEntity.ok(ApiResponse.success(null, "商品分类删除成功"));
    }
}
