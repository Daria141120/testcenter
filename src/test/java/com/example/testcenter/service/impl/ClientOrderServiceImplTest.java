package com.example.testcenter.service.impl;

import com.example.testcenter.exception.CommonBackendException;
import com.example.testcenter.mapper.ClientOrderMapper;
import com.example.testcenter.mapper.OrderItemMapper;
import com.example.testcenter.model.db.entity.Client;
import com.example.testcenter.model.db.entity.ClientOrder;
import com.example.testcenter.model.db.repository.ClientOrderRepository;
import com.example.testcenter.model.dto.request.ClientOrderInfoReq;
import com.example.testcenter.model.dto.response.ClientInfoResp;
import com.example.testcenter.model.dto.response.ClientOrderInfoResp;
import com.example.testcenter.service.ClientService;
import com.example.testcenter.service.EmailSenderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

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
    private OrderItemMapper orderItemMapper;

    @Spy
    private ClientOrderMapper clientOrderMapper;

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
        orderService.getClientOrder(1L);
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
    }

    @Test
    public void getAllClientOrder() {
    }

    @Test
    public void getAllItemsOfOrder() {
    }

    @Test
    public void getAllOrderStatus() {
    }

    @Test
    public void getStatusByNumber() {
    }
}