package com.example.testcenter.service;

import com.example.testcenter.model.dto.response.OrderItemInfoResp;

public interface OrderItemService {
    OrderItemInfoResp getOrderItem(Long id);
}
