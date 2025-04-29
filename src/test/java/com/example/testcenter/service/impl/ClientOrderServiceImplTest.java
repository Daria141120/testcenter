package com.example.testcenter.service.impl;

import com.example.testcenter.exception.CommonBackendException;
import com.example.testcenter.mapper.ClientOrderMapper;
import com.example.testcenter.mapper.ClientOrderMapperImpl;
import com.example.testcenter.mapper.OrderItemMapper;
import com.example.testcenter.mapper.OrderItemMapperImpl;
import com.example.testcenter.model.db.entity.Client;
import com.example.testcenter.model.db.entity.ClientOrder;
import com.example.testcenter.model.db.entity.OrderItem;
import com.example.testcenter.model.db.repository.ClientOrderRepository;
import com.example.testcenter.model.dto.request.ClientOrderInfoReq;
import com.example.testcenter.model.dto.response.ClientInfoResp;
import com.example.testcenter.model.dto.response.ClientOrderInfoResp;
import com.example.testcenter.model.dto.response.OrderItemInfoResp;
import com.example.testcenter.model.enums.OrderStatus;
import com.example.testcenter.service.ClientService;
import com.example.testcenter.service.EmailSenderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ClientOrderServiceImplTest {

    @InjectMocks
    private ClientOrderServiceImpl orderService;

    @Mock
    private ClientOrderRepository orderRepository;

    @Mock
    private ClientService clientService;

    @Spy
    private ObjectMapper objectMapper;

    @Spy
    private OrderItemMapper orderItemMapper = new OrderItemMapperImpl();

    @Spy
    private ClientOrderMapper clientOrderMapper = new ClientOrderMapperImpl();

    @Mock
    EmailSenderService emailSenderService;

    @Test
    public void getClientOrderFromDB() {
        Client client = new Client();
        client.setId(1L);
        client.setEmail("test@mail.ru");

        ClientOrder order = new ClientOrder();
        order.setId(1L);
        order.setClient(client);

        when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));

        ClientOrder orderFromDB = orderService.getClientOrderFromDB(order.getId());
        assertEquals(order.getClient(), orderFromDB.getClient());
    }

    @Test(expected = CommonBackendException.class)
    public void getClientOrderFromDBNotFound(){
        orderService.getClientOrderFromDB(1L);
    }

    @Test
    public void getClientOrder() {
        ClientOrder order = new ClientOrder();
        order.setId(1L);

        when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));

        ClientOrderInfoResp orderInfoResp = orderService.getClientOrder(order.getId());
        assertEquals(order.getId(), orderInfoResp.getId());
    }


    @Test
    public void addClientOrder() {
        ClientInfoResp clientResp = new ClientInfoResp();
        clientResp.setId(1L);
        clientResp.setEmail("test@mail.ru");

        Client client = new Client();
        client.setEmail(clientResp.getEmail());
        client.setId(clientResp.getId());

        ClientOrderInfoReq req = new ClientOrderInfoReq();
        req.setClient(clientResp);

        ClientOrder order = new ClientOrder();
        order.setId(1L);
        order.setClient(client);

        when(orderRepository.save(any(ClientOrder.class))).thenReturn(order);
        //doNothing().when(emailSenderService).sendEmail(isA(String.class),isA(String.class),isA(String.class));

        ClientOrderInfoResp orderInfoResp = orderService.addClientOrder(req);
        assertEquals(order.getId(),orderInfoResp.getId());
        assertEquals(order.getClient(),client);
    }

    @Test
    public void updateClientOrder() {
        ClientInfoResp clientResp = new ClientInfoResp();
        clientResp.setId(1L);
        clientResp.setEmail("test@mail.ru");

        Client client = new Client();
        client.setEmail(clientResp.getEmail());
        client.setId(clientResp.getId());

        ClientOrderInfoReq req = new ClientOrderInfoReq();
        req.setClient(clientResp);

        ClientOrder order = new ClientOrder();
        order.setId(1L);

        when(clientService.getClientFromDB(clientResp.getId())).thenReturn(client);
        when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));
        when(orderRepository.save(any(ClientOrder.class))).thenReturn(order);

        ClientOrderInfoResp orderUpdatedResp = orderService.updateClientOrder(order.getId(), req);
        assertEquals(req.getClient().getEmail(), orderUpdatedResp.getClient().getEmail());
    }

    @Test
    public void updateClientOrderEmpty() {
        ClientOrderInfoReq emptyReq = new ClientOrderInfoReq();

        Client clientOld = new Client();
        clientOld.setEmail("test@mail.ru");
        clientOld.setId(1L);

        ClientOrder orderOld = new ClientOrder();
        orderOld.setId(1L);
        orderOld.setClient(clientOld);

        when(orderRepository.findById(orderOld.getId())).thenReturn(Optional.of(orderOld));

        ClientOrderInfoResp orderUpdatedResp = orderService.updateClientOrder(orderOld.getId(), emptyReq);
        assertEquals(clientOld.getEmail(), orderUpdatedResp.getClient().getEmail());
    }

    @Test
    public void updateClientOrderStatus() {
        String status = OrderStatus.CONFIRMED.name();
        ClientOrder order = new ClientOrder();
        order.setId(1L);

        when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));
        when(orderRepository.save(any(ClientOrder.class))).thenReturn(order);

        ClientOrderInfoResp orderResp = orderService.updateClientOrderStatus(1L, status);
        assertEquals(OrderStatus.valueOf(status), orderResp.getStatus());
    }

    @Test
    public void updateClientOrderStatusCompleted() {
        Client client = new Client();
        client.setEmail("testUpdate@yandex.ru");
        ClientOrder order = new ClientOrder();
        order.setId(1L);
        order.setClient(client);

        String status = OrderStatus.COMPLETED.name();

        when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));
        when(orderRepository.save(any(ClientOrder.class))).thenReturn(order);

        ClientOrderInfoResp orderResp = orderService.updateClientOrderStatus(1L, status);
        assertEquals(OrderStatus.valueOf(status), orderResp.getStatus());
    }

    @Test(expected = CommonBackendException.class)
    public void updateStatusNotCorrect() {
        String status = "QWERTY";
        ClientOrderInfoResp orderResp = orderService.updateClientOrderStatus(1L, status);
        assertEquals(OrderStatus.valueOf(status), orderResp.getStatus());
    }


    @Test
    public void getAllClientOrder() {
        ClientOrder order1 = new ClientOrder();
        ClientOrder order2 = new ClientOrder();
        order1.setId(1L);
        order2.setId(2L);

        List<ClientOrder> orderList = List.of(order1, order2);
        when(orderRepository.findAll()).thenReturn(orderList);

        List<ClientOrderInfoResp> respList = orderService.getAllClientOrder("");
        assertEquals(orderList.size(), respList.size());
       // assertEquals(orderList.get(1).getId(), respList.get(1).getId());
    }

    @Test
    public void getAllClientOrderWithStatus() {
        String status = "CONFIRMED";
        OrderStatus orderStatus = OrderStatus.valueOf(status);
        ClientOrder order1 = new ClientOrder();
        ClientOrder order2 = new ClientOrder();
        order1.setId(1L);
        order2.setId(2L);
        order1.setStatus(orderStatus);
        order2.setStatus(orderStatus);
        List<ClientOrder> orderList = List.of(order1, order2);

        when(orderRepository.findAllByStatus(any(OrderStatus.class))).thenReturn(orderList);
        List<ClientOrderInfoResp> respList = orderService.getAllClientOrder(status);

        assertEquals(orderList.size(), respList.size());
        respList.forEach(orderResp -> assertEquals(orderStatus, orderResp.getStatus()));
    }

    @Test(expected = CommonBackendException.class)
    public void getAllClientOrderNotCorrectStatus() {
        String status = "QAZXSW";
        orderService.getAllClientOrder(status);
    }

    @Test
    public void getAllItemsOfOrder() {
        List<OrderItem> orderItemList = List.of(new OrderItem(), new OrderItem());
        ClientOrder order = new ClientOrder();
        order.setId(1L);
        order.setOrderItemList(orderItemList);

        when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));
        List<OrderItemInfoResp> respOrderItemList = orderService.getAllItemsOfOrder(order.getId());

        assertEquals(order.getOrderItemList().size(), respOrderItemList.size());
    }

    @Test
    public void getStatusByNumber() {
        String status = OrderStatus.CONFIRMED.name();
        String number = "K-2025-11";
        ClientOrder order = new ClientOrder();
        order.setId(1L);
        order.setStatus(OrderStatus.CONFIRMED);
        order.setOrderNumber(number);

        when(orderRepository.findByOrderNumber(number)).thenReturn(Optional.of(order));

        String resultStatus = orderService.getStatusByNumber(number);
        assertEquals(status, resultStatus);
    }

    @Test
    public void updateOrderItemList() {
        ClientOrder order = new ClientOrder();
        order.setId(1L);
        orderService.updateOrderItemList(order);
        verify(orderRepository,times(1)).save(any(ClientOrder.class));
    }




}