package com.example.pos_backend.service;

import com.example.pos_backend.dto.ProductRequestDTO;
import com.example.pos_backend.dto.ProductResponseDTO;
import com.example.pos_backend.entity.Product;
import com.example.pos_backend.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    // 创建商品
    public ProductResponseDTO createProduct(ProductRequestDTO dto) {
        Product product = Product.builder()
                .merchantId(dto.getMerchantId())
                .storeId(dto.getStoreId())
                .categoryId(dto.getCategoryId())
                .productName(dto.getProductName())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .imageUrl(dto.getImageUrl())
                .isActive(dto.getIsActive())
                .createdBy(dto.getCreatedBy())
                .isDeleted(false)
                .build();
        return toResponse(productRepository.save(product));
    }

    // 更新商品
    public ProductResponseDTO updateProduct(UUID productId, ProductRequestDTO dto) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("商品不存在"));

        product.setCategoryId(dto.getCategoryId());
        product.setProductName(dto.getProductName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setImageUrl(dto.getImageUrl());
        product.setIsActive(dto.getIsActive());

        return toResponse(productRepository.save(product));
    }

    // 删除商品（逻辑删除）
    public void deleteProduct(UUID productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("商品不存在"));
        product.setIsDeleted(true);
        productRepository.save(product);
    }

    // 获取商品详情
    public ProductResponseDTO getProduct(UUID productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("商品不存在"));
        return toResponse(product);
    }

    // 查询商品列表（可选按分类过滤）
    public List<ProductResponseDTO> listProducts(UUID storeId, UUID categoryId) {
        List<Product> products;
        if (categoryId != null) {
            products = productRepository.findByStoreIdAndCategoryIdAndIsDeletedFalse(storeId, categoryId);
        } else {
            products = productRepository.findByStoreIdAndIsDeletedFalse(storeId);
        }
        return products.stream().map(this::toResponse).collect(Collectors.toList());
    }

    // Entity -> DTO 转换
    private ProductResponseDTO toResponse(Product product) {
        return ProductResponseDTO.builder()
                .productId(product.getProductId())
                .merchantId(product.getMerchantId())
                .storeId(product.getStoreId())
                .categoryId(product.getCategoryId())
                .productName(product.getProductName())
                .description(product.getDescription())
                .price(product.getPrice())
                .imageUrl(product.getImageUrl())
                .isActive(product.getIsActive())
                .isDeleted(product.getIsDeleted())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }
}
