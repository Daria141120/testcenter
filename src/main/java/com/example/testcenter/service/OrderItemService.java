package com.example.testcenter.service;

import com.example.testcenter.model.db.entity.OrderItem;
import com.example.testcenter.model.dto.request.OrderItemInfoReq;
import com.example.testcenter.model.dto.response.OrderItemInfoResp;

import java.util.List;

public interface OrderItemService {

    OrderItem getOrderItemFromDB(Long id);

    OrderItemInfoResp getOrderItem(Long id);

    OrderItemInfoResp addOrderItem(OrderItemInfoReq orderItemInfoReq);

    OrderItemInfoResp updateOrderItem(Long id, OrderItemInfoReq orderItemInfoReq);

    List<OrderItemInfoResp> getAllOrderItem(Long idOrder);
}
