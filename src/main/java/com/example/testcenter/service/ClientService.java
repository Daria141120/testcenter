package com.example.testcenter.service;

import com.example.testcenter.model.db.entity.Client;
import com.example.testcenter.model.dto.request.ClientInfoReq;
import com.example.testcenter.model.dto.response.ClientInfoResp;

import java.util.List;


public interface ClientService {

    Client getClientFromDB(Long id);

    ClientInfoResp getClient(Long id);

    ClientInfoResp addClient(ClientInfoReq clientInfoReq);

    ClientInfoResp updateClient(Long id, ClientInfoReq clientInfoReq);

    void deleteClient(Long id);

    List<ClientInfoResp> getAllClient();
}
