package com.hclm.terminal.service;

import com.hclm.terminal.pojo.request.OrderCreateRequest;
import jakarta.validation.Valid;

public interface OrderService {
    void create(@Valid OrderCreateRequest request);
}
