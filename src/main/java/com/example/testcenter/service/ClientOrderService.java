package com.example.testcenter.service;

import com.example.testcenter.model.dto.request.ClientOrderInfoReq;
import com.example.testcenter.model.dto.response.ClientOrderInfoResp;
import com.example.testcenter.model.enums.OrderStatus;

import java.util.List;


public interface ClientOrderService {

    ClientOrderInfoResp getClientOrder(Long id);

    ClientOrderInfoResp addClientOrder(ClientOrderInfoReq clientOrderInfoReq);

    ClientOrderInfoResp updateClientOrder(Long id, ClientOrderInfoReq clientOrderInfoReq);

    ClientOrderInfoResp updateClientOrderStatus(Long id, String status);

    List<ClientOrderInfoResp> getAllClientOrder();

    List<OrderStatus> getAllOrderStatus();



}
