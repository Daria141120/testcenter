package com.example.testcenter.service.impl;

import com.example.testcenter.exception.CommonBackendException;
import com.example.testcenter.mapper.OrderItemMapper;
import com.example.testcenter.model.db.entity.ClientOrder;
import com.example.testcenter.model.db.entity.OrderItem;
import com.example.testcenter.model.db.repository.OrderItemRepository;
import com.example.testcenter.model.dto.request.OrderItemInfoReq;
import com.example.testcenter.model.dto.request.TaskInfoReq;
import com.example.testcenter.model.dto.response.OrderItemInfoResp;
import com.example.testcenter.model.enums.OrderStatus;
import com.example.testcenter.service.ClientOrderService;
import com.example.testcenter.service.OrderItemService;
import com.example.testcenter.service.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderItemServiceImpl implements OrderItemService {

    private final OrderItemRepository orderItemRepository;
    private final ObjectMapper objectMapper;
    private final OrderItemMapper orderItemMapper;
    private final ClientOrderService clientOrderService;
    private final TaskService taskService;

    @Override
    public OrderItem getOrderItemFromDB(Long id) {
        Optional<OrderItem> orderItemFromDB = orderItemRepository.findById(id);
        final String errMsg = String.format("orderItem with id : %s not found", id);
        return orderItemFromDB.orElseThrow(() -> new CommonBackendException(errMsg, HttpStatus.NOT_FOUND));
    }

    @Override
    public OrderItemInfoResp getOrderItem(Long id) {
        OrderItem orderItemFromDB = getOrderItemFromDB(id);
        return orderItemMapper.toOrderItemInfoResp(orderItemFromDB);
    }

    @Override
    public OrderItemInfoResp addOrderItem(OrderItemInfoReq req) {
        if (req.getClientOrder().getStatus() == OrderStatus.COMPLETED) {
            throw new CommonBackendException("Order has already been COMPLETED", HttpStatus.CONFLICT);
        }

        OrderItem orderItem = objectMapper.convertValue(req, OrderItem.class);
        OrderItem orderItemSaved = orderItemRepository.save(orderItem);

        ClientOrder clientOrder = clientOrderService.getClientOrderFromDB(orderItemSaved.getClientOrder().getId());
        List<OrderItem> orderItemList = clientOrder.getOrderItemList();
        orderItemList.add(orderItemSaved);
        clientOrderService.updateOrderItemList(clientOrder);

        OrderItemInfoResp orderItemResp = orderItemMapper.toOrderItemInfoResp(orderItemSaved);
        TaskInfoReq taskInfoReq = new TaskInfoReq();
        taskInfoReq.setOrderItem(orderItemResp);
        taskService.addTask(taskInfoReq);

        return orderItemResp;
    }

    @Override
    public OrderItemInfoResp updateOrderItem(Long id, OrderItemInfoReq req) {
        if (req.getClientOrder().getStatus() == OrderStatus.COMPLETED) {
            throw new CommonBackendException("Order has already been COMPLETED", HttpStatus.CONFLICT);
        }
        OrderItem orderItemFromDB = getOrderItemFromDB(id);
        OrderItem orderItemForUpdate = objectMapper.convertValue(req, OrderItem.class);

        orderItemFromDB.setQuantity(orderItemForUpdate.getQuantity() == null ? orderItemFromDB.getQuantity() : orderItemForUpdate.getQuantity());
        orderItemFromDB.setInfo(orderItemForUpdate.getInfo() == null ? orderItemFromDB.getInfo() : orderItemForUpdate.getInfo());
        orderItemFromDB.setExamResult(orderItemForUpdate.getExamResult() == null ? orderItemFromDB.getExamResult() : orderItemForUpdate.getExamResult());

        OrderItem orderItemSaved = orderItemRepository.save(orderItemFromDB);

        ClientOrder clientOrder = clientOrderService.getClientOrderFromDB(orderItemSaved.getClientOrder().getId());
        List<OrderItem> orderItemList = clientOrder.getOrderItemList();
        orderItemList.add(orderItemSaved);
        clientOrderService.updateOrderItemList(clientOrder);

        return orderItemMapper.toOrderItemInfoResp(orderItemSaved);
    }

    @Override
    public List<OrderItemInfoResp> getAllOrderItem(Long idOrder) {
        List<OrderItem> orderItemList;

        if (idOrder != null) {
            orderItemList = orderItemRepository.findAllByClientOrder_Id(idOrder);
        } else {
            orderItemList = orderItemRepository.findAll();
        }

        return orderItemMapper.toOrderItemInfoRespList(orderItemList);
    }


}
