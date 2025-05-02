package com.example.testcenter.service.impl;

import com.example.testcenter.exception.CommonBackendException;
import com.example.testcenter.mapper.OrderItemMapper;
import com.example.testcenter.mapper.OrderItemMapperImpl;
import com.example.testcenter.model.db.entity.ClientOrder;
import com.example.testcenter.model.db.entity.OrderItem;
import com.example.testcenter.model.db.repository.OrderItemRepository;
import com.example.testcenter.model.dto.request.OrderItemInfoReq;
import com.example.testcenter.model.dto.request.TaskInfoReq;
import com.example.testcenter.model.dto.response.ClientOrderInfoResp;
import com.example.testcenter.model.dto.response.OrderItemInfoResp;
import com.example.testcenter.model.enums.OrderStatus;
import com.example.testcenter.service.ClientOrderService;
import com.example.testcenter.service.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class OrderItemServiceImplTest {

    @InjectMocks
    OrderItemServiceImpl orderItemService;

    @Mock
    private OrderItemRepository orderItemRepository;

    @Spy
    private ObjectMapper objectMapper;

    @Spy
    private OrderItemMapper orderItemMapper = new OrderItemMapperImpl();

    @Mock
    private ClientOrderService clientOrderService;

    @Mock
    private TaskService taskService;

    @Test
    public void getOrderItemFromDB() {
        OrderItem orderItem = new OrderItem();
        orderItem.setId(1L);

        when(orderItemRepository.findById(orderItem.getId())).thenReturn(Optional.of(orderItem));
        OrderItem orderItemFromDB = orderItemService.getOrderItemFromDB(orderItem.getId());
        assertEquals(orderItem.getId(), orderItemFromDB.getId());
    }

    @Test(expected = CommonBackendException.class)
    public void getOrderItemFromDBNotFound() {
        orderItemService.getOrderItemFromDB(1L);
    }

    @Test
    public void getOrderItem() {
        OrderItem orderItem = new OrderItem();
        orderItem.setId(1L);

        when(orderItemRepository.findById(orderItem.getId())).thenReturn(Optional.of(orderItem));
        OrderItemInfoResp orderItemResp = orderItemService.getOrderItem(orderItem.getId());
        assertEquals(orderItem.getId(), orderItemResp.getId());
    }

    @Test(expected = CommonBackendException.class)
    public void addOrderItemOrderCOMPLETED() {
        ClientOrderInfoResp orderResp = new ClientOrderInfoResp();
        orderResp.setStatus(OrderStatus.COMPLETED);

        OrderItemInfoReq req = new OrderItemInfoReq();
        req.setClientOrder(orderResp);
        orderItemService.addOrderItem(req);
    }

    @Test
    public void addOrderItem() {
        ClientOrderInfoResp orderResp = new ClientOrderInfoResp();
        orderResp.setStatus(OrderStatus.CONFIRMED);
        OrderItemInfoReq req = new OrderItemInfoReq();
        req.setInfo("info");
        req.setClientOrder(orderResp);

        ClientOrder order = new ClientOrder();
        order.setId(1L);
        order.setOrderItemList(new ArrayList<>());
        OrderItem orderItem = new OrderItem();
        orderItem.setInfo(req.getInfo());
        orderItem.setClientOrder(order);


        when(orderItemRepository.save(any(OrderItem.class))).thenReturn(orderItem);
        when(clientOrderService.getClientOrderFromDB(anyLong())).thenReturn(order);
        OrderItemInfoResp orderItemResp = orderItemService.addOrderItem(req);

        assertEquals(1, order.getOrderItemList().size());
        assertEquals(orderItem.getInfo(), orderItemResp.getInfo());
        verify(taskService, times(1)).addTask(any(TaskInfoReq.class));
    }


    @Test
    public void updateOrderItem() {
        ClientOrderInfoResp orderResp = new ClientOrderInfoResp();
        orderResp.setStatus(OrderStatus.CONFIRMED);
        orderResp.setId(1L);
        OrderItemInfoReq req = new OrderItemInfoReq();
        req.setClientOrder(orderResp);
        req.setInfo("info");
        req.setQuantity(10);
        req.setExamResult("res");

        ClientOrder order = new ClientOrder();
        order.setId(1L);
        order.setOrderItemList(new ArrayList<>());

        OrderItem orderItem = new OrderItem();
        orderItem.setId(1L);
        orderItem.setClientOrder(order);

        when(orderItemRepository.findById(orderItem.getId())).thenReturn(Optional.of(orderItem));
        when(orderItemRepository.save(any(OrderItem.class))).thenReturn(orderItem);
        when(clientOrderService.getClientOrderFromDB(anyLong())).thenReturn(order);
        OrderItemInfoResp orderItemResp = orderItemService.updateOrderItem(orderItem.getId(), req);

        assertEquals(req.getInfo(), orderItemResp.getInfo());
        assertEquals(req.getQuantity(), orderItemResp.getQuantity());
        assertEquals(req.getExamResult(), orderItemResp.getExamResult());
        assertEquals(req.getClientOrder().getId(), orderItemResp.getClientOrder().getId());
    }

    @Test
    public void updateOrderItemEmptyReq() {
        ClientOrderInfoResp orderResp = new ClientOrderInfoResp();
        orderResp.setStatus(OrderStatus.CONFIRMED);
        OrderItemInfoReq req = new OrderItemInfoReq();
        req.setClientOrder(orderResp);

        ClientOrder order = new ClientOrder();
        order.setId(1L);
        order.setOrderItemList(new ArrayList<>());

        OrderItem orderItemFromDB = new OrderItem();
        orderItemFromDB.setId(1L);
        orderItemFromDB.setInfo("info");
        orderItemFromDB.setQuantity(10);
        orderItemFromDB.setExamResult("old res");
        orderItemFromDB.setClientOrder(order);

        when(orderItemRepository.findById(orderItemFromDB.getId())).thenReturn(Optional.of(orderItemFromDB));
        when(orderItemRepository.save(any(OrderItem.class))).thenReturn(orderItemFromDB);
        when(clientOrderService.getClientOrderFromDB(anyLong())).thenReturn(order);
        OrderItemInfoResp orderItemResp = orderItemService.updateOrderItem(orderItemFromDB.getId(), req);

        assertEquals(orderItemFromDB.getInfo(), orderItemResp.getInfo());
        assertEquals(orderItemFromDB.getQuantity(), orderItemResp.getQuantity());
        assertEquals(orderItemFromDB.getExamResult(), orderItemResp.getExamResult());

    }

    @Test(expected = CommonBackendException.class)
    public void updateOrderItemOrderCOMPLETED() {
        ClientOrderInfoResp orderResp = new ClientOrderInfoResp();
        orderResp.setStatus(OrderStatus.COMPLETED);
        OrderItemInfoReq req = new OrderItemInfoReq();
        req.setClientOrder(orderResp);

        orderItemService.updateOrderItem(1L, req);
    }

    @Test
    public void getAllOrderItem() {
        OrderItem orderItem1 = new OrderItem();
        OrderItem orderItem2 = new OrderItem();
        orderItem1.setId(1L);
        orderItem2.setId(2L);
        List<OrderItem> orderItemList = List.of(orderItem1, orderItem2);

        when(orderItemRepository.findAll()).thenReturn(orderItemList);
        List<OrderItemInfoResp> orderItemRespList = orderItemService.getAllOrderItem(null);
        assertEquals(orderItemList.size(), orderItemRespList.size());
    }

    @Test
    public void getAllOrderItemByOrderId() {
        OrderItem orderItem1 = new OrderItem();
        OrderItem orderItem2 = new OrderItem();
        orderItem1.setId(1L);
        orderItem2.setId(2L);
        List<OrderItem> orderItemList = List.of(orderItem1, orderItem2);

        when(orderItemRepository.findAllByClientOrder_Id(anyLong())).thenReturn(orderItemList);
        List<OrderItemInfoResp> orderItemRespList = orderItemService.getAllOrderItem(1L);
        assertEquals(orderItemList.size(), orderItemRespList.size());
    }

}