package com.example.testcenter.service.impl;


import com.example.testcenter.exception.CommonBackendException;
import com.example.testcenter.mapper.ClientOrderMapper;
import com.example.testcenter.mapper.OrderItemMapper;
import com.example.testcenter.model.db.entity.Client;
import com.example.testcenter.model.db.entity.ClientOrder;
import com.example.testcenter.model.db.entity.OrderItem;
import com.example.testcenter.model.db.repository.ClientOrderRepository;
import com.example.testcenter.model.dto.request.ClientOrderInfoReq;
import com.example.testcenter.model.dto.response.ClientOrderInfoResp;
import com.example.testcenter.model.dto.response.OrderItemInfoResp;
import com.example.testcenter.model.enums.OrderStatus;
import com.example.testcenter.service.ClientOrderService;
import com.example.testcenter.service.ClientService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClientOrderServiceImpl implements ClientOrderService {

    private final ClientOrderRepository clientOrderRepository;
    private final ClientService clientService;
    private final OrderItemMapper orderItemMapper;
    private final ObjectMapper objectMapper;
    private final ClientOrderMapper clientOrderMapper;

    @Override
    public ClientOrder getClientOrderFromDB(Long id) {
        Optional<ClientOrder> clientOrderFromDB = clientOrderRepository.findById(id);
        final String errMsg = String.format("order with id : %s not found", id);
        return clientOrderFromDB.orElseThrow(() ->  new CommonBackendException(errMsg, HttpStatus.NOT_FOUND));
    }


    @Override
    public ClientOrderInfoResp getClientOrder(Long id) {
        ClientOrder clientOrderFromDB = getClientOrderFromDB(id);
        return objectMapper.convertValue(clientOrderFromDB, ClientOrderInfoResp.class);
    }

    @Override
    public ClientOrderInfoResp addClientOrder(ClientOrderInfoReq clientOrderInfoReq) {
        clientService.getClientFromDB(clientOrderInfoReq.getClient().getId());   // проверка что клиент заявки существует

        ClientOrder clientOrder = objectMapper.convertValue(clientOrderInfoReq, ClientOrder.class);
        clientOrder.setStatus(OrderStatus.CREATED);

        clientOrder = clientOrderRepository.save(clientOrder);
        return objectMapper.convertValue(clientOrder, ClientOrderInfoResp.class);
    }


    @Override
    public ClientOrderInfoResp updateClientOrder(Long id, ClientOrderInfoReq clientOrderInfoReq) {


        ClientOrder clientOrderFromDB = getClientOrderFromDB(id);
     //   ClientOrder clientOrderForUpdate = objectMapper.convertValue(clientOrderInfoReq, ClientOrder.class);

       if (clientOrderInfoReq.getClient() != null){
           clientService.getClientFromDB(clientOrderInfoReq.getClient().getId());   // проверка что клиент заявки существует

           Client client = clientService.getClientFromDB(clientOrderInfoReq.getClient().getId());
           clientOrderFromDB.setClient(client);
           clientOrderFromDB = clientOrderRepository.save(clientOrderFromDB);
       }

        return objectMapper.convertValue(clientOrderFromDB, ClientOrderInfoResp.class);
    }


    @Override
    public ClientOrderInfoResp updateClientOrderStatus(Long id, String status) {
        if (!checkStatusExist(status)) {
            throw new CommonBackendException("Error in the status, there is no such status.", HttpStatus.BAD_REQUEST);
        }

        OrderStatus orderStatus = OrderStatus.valueOf(status);

        ClientOrder clientOrderFromDB = getClientOrderFromDB(id);
        clientOrderFromDB.setStatus(orderStatus);
        clientOrderFromDB = clientOrderRepository.save(clientOrderFromDB);
        return objectMapper.convertValue(clientOrderFromDB, ClientOrderInfoResp.class);
    }

    @Override
    public List<ClientOrderInfoResp> getAllClientOrder(String status) {
        List<ClientOrderInfoResp> respList;

        if (StringUtils.hasText(status)){

            if (!checkStatusExist(status)) {
                throw new CommonBackendException("Error in the status, there is no such status.", HttpStatus.BAD_REQUEST);
            }
            OrderStatus orderStatus = OrderStatus.valueOf(status);
            respList = clientOrderMapper.toClientOrderInfoRespList(clientOrderRepository.findAllByStatus(orderStatus));

        } else {
            respList = clientOrderMapper.toClientOrderInfoRespList(clientOrderRepository.findAll());
        }

        return respList;
    }


    @Override
    public List<OrderItemInfoResp> getAllItemsOfOrder(Long id) {
        ClientOrder clientOrder = getClientOrderFromDB(id);
        List<OrderItem> orderItemList = clientOrder.getOrderItemList();
        return orderItemMapper.toOrderItemInfoRespList(orderItemList);
    }

    @Override
    public void updateOrderItemList(ClientOrder clientOrder){
        clientOrderRepository.save(clientOrder);
    }

    @Override
    public List<OrderStatus> getAllOrderStatus() {
        return Arrays.stream(OrderStatus.values()).collect(Collectors.toList());
    }


    private boolean checkStatusExist(String status){
        List <String> list = getAllOrderStatus().stream().map(Enum::name).collect(Collectors.toList());
        return list.contains(status);
    }

}
