package com.example.testcenter.service.impl;


import com.example.testcenter.model.db.entity.Client;
import com.example.testcenter.model.db.repository.ClientRepository;
import com.example.testcenter.model.dto.response.ClientInfoResp;
import com.example.testcenter.service.ClientService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ObjectMapper objectMapper;
    private final ClientRepository clientRepository;

    @Override
    public ClientInfoResp getClient(Long id) {



        return null;
    }

    private Client getClientFromDB (Long id){
        Optional <Client> client = clientRepository.findById(id);

        return null;


    }


//
//    public Car getCarFromDB(Long id){
//        Optional<Car> car = carRepository.findById(id);
//        final String errMsg = String.format("car with id : %s not found", id);
//        return car.orElseThrow(() -> new CommonBackendException(errMsg, HttpStatus.NOT_FOUND));
//    }


}
