package com.example.testcenter.service.impl;

import com.example.testcenter.exception.CommonBackendException;
import com.example.testcenter.model.db.entity.Client;
import com.example.testcenter.model.db.repository.ClientRepository;
import com.example.testcenter.model.dto.request.ClientInfoReq;
import com.example.testcenter.model.dto.response.ClientInfoResp;
import com.example.testcenter.model.enums.ClientStatus;
import com.example.testcenter.service.ClientService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ObjectMapper objectMapper;
    private final ClientRepository clientRepository;

    @Override
    public Client getClientFromDB(Long id){
        Optional <Client> client = clientRepository.findById(id);
        final String errMsg = String.format("client with id : %s not found", id);
        return client.orElseThrow(() -> new CommonBackendException(errMsg, HttpStatus.NOT_FOUND));
    }


    @Override
    public ClientInfoResp getClient(Long id) {
        Client client = getClientFromDB(id);
        return objectMapper.convertValue(client, ClientInfoResp.class);
    }


    @Override
    public ClientInfoResp addClient(ClientInfoReq clientInfoReq) {

        clientRepository.findFirstByEmailOrPhone(clientInfoReq.getEmail(), clientInfoReq.getPhone()).ifPresent(
                client -> {throw new CommonBackendException("Client already exist", HttpStatus.CONFLICT);
                });

        Client client = objectMapper.convertValue(clientInfoReq, Client.class);
        client.setStatus(ClientStatus.CREATED);

        Client clientSaved = clientRepository.save(client);
        return objectMapper.convertValue(clientSaved, ClientInfoResp.class);
    }


    @Override
    public ClientInfoResp updateClient(Long id, ClientInfoReq clientInfoReq) {
        Client clientFromDB = getClientFromDB(id);
        Client clientForUpdate = objectMapper.convertValue(clientInfoReq, Client.class);

        clientFromDB.setEmail( clientForUpdate.getEmail() == null ? clientFromDB.getEmail() : clientForUpdate.getEmail());
        clientFromDB.setPhone( clientForUpdate.getPhone() == null ? clientFromDB.getPhone() : clientForUpdate.getPhone());
        clientFromDB.setFirstName( clientForUpdate.getFirstName() == null ? clientFromDB.getFirstName() : clientForUpdate.getFirstName());
        clientFromDB.setLastName( clientForUpdate.getLastName() == null ? clientFromDB.getLastName() : clientForUpdate.getLastName());
        clientFromDB.setMiddleName( clientForUpdate.getMiddleName() == null ? clientFromDB.getMiddleName() : clientForUpdate.getMiddleName());

        clientFromDB.setStatus(ClientStatus.UPDATED);
        clientFromDB = clientRepository.save(clientFromDB);
        return objectMapper.convertValue(clientFromDB, ClientInfoResp.class);
    }

    @Override
    public void deleteClient(Long id) {
        Client clientFromDB = getClientFromDB(id);

        clientFromDB.setStatus(ClientStatus.DELETED);
        clientRepository.save(clientFromDB);
    }

    @Override
    public List<ClientInfoResp> getAllClient() {
        return clientRepository.findAll().stream().map(client -> objectMapper.convertValue(client, ClientInfoResp.class))
                .collect(Collectors.toList());
    }


}
