package com.example.testcenter.service;

import com.example.testcenter.model.db.entity.ClientOrder;
import com.example.testcenter.model.dto.request.ClientOrderInfoReq;
import com.example.testcenter.model.dto.response.ClientOrderInfoResp;
import com.example.testcenter.model.dto.response.OrderItemInfoResp;
import com.example.testcenter.model.enums.OrderStatus;

import java.util.List;


public interface ClientOrderService {

    ClientOrder getClientOrderFromDB(Long id);

    ClientOrderInfoResp getClientOrder(Long id);

    ClientOrderInfoResp addClientOrder(ClientOrderInfoReq clientOrderInfoReq);

    ClientOrderInfoResp updateClientOrder(Long id, ClientOrderInfoReq clientOrderInfoReq);

    ClientOrderInfoResp updateClientOrderStatus(Long id, String status);

    List<ClientOrderInfoResp> getAllClientOrder(String status);

    List<OrderStatus> getAllOrderStatus();

    List<OrderItemInfoResp> getAllItemsOfOrder(Long id);

    void updateOrderItemList(ClientOrder clientOrder);
}
