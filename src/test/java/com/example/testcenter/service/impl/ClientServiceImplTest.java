package com.example.testcenter.service.impl;

import com.example.testcenter.exception.CommonBackendException;
import com.example.testcenter.model.db.entity.Client;
import com.example.testcenter.model.db.entity.ClientOrder;
import com.example.testcenter.model.db.repository.ClientRepository;
import com.example.testcenter.model.dto.request.ClientInfoReq;
import com.example.testcenter.model.dto.response.ClientInfoResp;
import com.example.testcenter.model.dto.response.ClientOrderInfoResp;
import com.example.testcenter.model.enums.ClientStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.checkerframework.checker.units.qual.C;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ClientServiceImplTest {
    @InjectMocks
    private ClientServiceImpl clientService;

    @Spy
    private  ObjectMapper objectMapper;

    @Mock
    private  ClientRepository clientRepository;

    @Test
    public void getClientFromDB() {
        Client client = new Client();
        client.setId(1L);
        client.setEmail("restClient@mail.ru");

        when(clientRepository.findById(client.getId())).thenReturn(Optional.of(client));
        Client clientFromDB = clientService.getClientFromDB(client.getId());
        assertEquals(client.getEmail(), clientFromDB.getEmail());
    }

    @Test(expected = CommonBackendException.class)
    public void getClientFromDBNotFound() {
        clientService.getClientFromDB(1L);
    }

    @Test
    public void getClient() {
        Client client = new Client();
        client.setId(1L);
        client.setEmail("restClient@mail.ru");

        when(clientRepository.findById(client.getId())).thenReturn(Optional.of(client));
        ClientInfoResp clientInfoResp = clientService.getClient(client.getId());
        assertEquals(client.getId(), clientInfoResp.getId());
    }

    @Test
    public void addClient() {
        ClientInfoReq req = new ClientInfoReq();
        req.setEmail("testClient@mail.ru");
        Client client = new Client();
        client.setId(1L);
        client.setEmail(req.getEmail());
        client.setStatus(ClientStatus.CREATED);

        when(clientRepository.save(any(Client.class))).thenReturn(client);
        ClientInfoResp clientResp = clientService.addClient(req);

        assertEquals(req.getEmail(), clientResp.getEmail());
        assertEquals(client.getStatus(), clientResp.getStatus());
    }

    @Test(expected = CommonBackendException.class)
    public void addClientExist() {
        ClientInfoReq req = new ClientInfoReq();
        req.setEmail("testClient@mail.ru");
        req.setPhone("12345678");

        when(clientRepository.findFirstByEmailOrPhone(anyString(), anyString())).thenReturn(Optional.of(new Client()));
        clientService.addClient(req);
    }

    @Test
    public void updateClient() {
        ClientInfoReq req = new ClientInfoReq();
        req.setEmail("newTest@mail.ru");
        req.setPhone("00000000000");
        req.setFirstName("FN");
        req.setLastName("LN");
        req.setMiddleName("MN");

        Client clientFromDB = new Client();
        clientFromDB.setId(1L);
        clientFromDB.setFirstName("OldName");

        when(clientRepository.findById(clientFromDB.getId())).thenReturn(Optional.of(clientFromDB));
        when(clientRepository.save(any(Client.class))).thenReturn(clientFromDB);

        ClientInfoResp clientResp = clientService.updateClient(clientFromDB.getId(), req);

        assertEquals(clientFromDB.getId(), clientResp.getId());
        assertEquals(req.getEmail(), clientResp.getEmail());
        assertEquals(req.getPhone(), clientResp.getPhone());
        assertEquals(req.getFirstName(), clientResp.getFirstName());
        assertEquals(req.getLastName(), clientResp.getLastName());
        assertEquals(req.getMiddleName(), clientResp.getMiddleName());
        assertEquals(ClientStatus.UPDATED, clientResp.getStatus());
    }


    @Test
    public void updateClientEmpty() {
        ClientInfoReq req = new ClientInfoReq();

        Client clientFromDB = new Client();
        clientFromDB.setId(1L);
        clientFromDB.setFirstName("OldName");

        when(clientRepository.findById(clientFromDB.getId())).thenReturn(Optional.of(clientFromDB));
        when(clientRepository.save(any(Client.class))).thenReturn(clientFromDB);

        ClientInfoResp clientResp = clientService.updateClient(clientFromDB.getId(), req);

        assertEquals(clientFromDB.getId(), clientResp.getId());
        assertEquals(clientFromDB.getFirstName(), clientResp.getFirstName());
        assertEquals(clientFromDB.getLastName(), clientResp.getLastName());
        assertEquals(clientFromDB.getMiddleName(), clientResp.getMiddleName());
        assertEquals(ClientStatus.UPDATED, clientResp.getStatus());
    }

    @Test
    public void deleteClient() {
        Client clientFromDB = new Client();
        clientFromDB.setId(1L);

        when(clientRepository.findById(clientFromDB.getId())).thenReturn(Optional.of(clientFromDB));
        clientService.deleteClient(clientFromDB.getId());

        verify(clientRepository,times(1)).save(any(Client.class));
        assertEquals(ClientStatus.DELETED, clientFromDB.getStatus());
    }

    @Test
    public void getAllClient() {
        Client client1 = new Client();
        Client client2 = new Client();
        client1.setId(1L);
        client2.setId(2L);

        List<Client> clientList = List.of(client1, client2);
        when(clientRepository.findAll()).thenReturn(clientList);
        List<ClientInfoResp> respList = clientService.getAllClient();
        assertEquals(clientList.size(), respList.size());
    }

}