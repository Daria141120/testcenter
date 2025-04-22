package com.example.testcenter.service.impl;

import com.example.testcenter.exception.CommonBackendException;
import com.example.testcenter.mapper.OrderItemMapper;
import com.example.testcenter.model.db.entity.OrderItem;
import com.example.testcenter.model.db.repository.OrderItemRepository;
import com.example.testcenter.model.dto.response.OrderItemInfoResp;
import com.example.testcenter.service.EquipExam2Service;
import com.example.testcenter.service.OrderItemService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Slf4j
@Service
@RequiredArgsConstructor
public class OrderItemServiceImpl implements OrderItemService {

    private final OrderItemRepository orderItemRepository;
    private final ObjectMapper objectMapper;
    private final EquipExam2Service equipExam2Service;
    private final OrderItemMapper orderItemMapper;

    public OrderItem getOrderItemFromDB(Long id) {
          Optional<OrderItem> orderItemFromDB = orderItemRepository.findById(id);
          final String errMsg = String.format("orderItem with id : %s not found", id);
          return orderItemFromDB.orElseThrow(()-> new CommonBackendException(errMsg, HttpStatus.NOT_FOUND));
    }

    @Override
    @SneakyThrows
    public OrderItemInfoResp getOrderItem(Long id) {
        OrderItem orderItemFromDB = getOrderItemFromDB(id);

        OrderItemInfoResp resp = orderItemMapper.toOrderItemInfoResp(orderItemFromDB);
        //OrderItemInfoResp resp = objectMapper.convertValue(orderItemFromDB, OrderItemInfoResp.class); // not work
        return resp;
    }





}
