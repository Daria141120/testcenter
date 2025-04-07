package com.example.testcenter.controllers;


import com.example.testcenter.model.dto.response.ClientInfoResp;
import com.example.testcenter.service.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("clients")
@RequiredArgsConstructor
@Tag(name = "Клиенты")
public class ClientController {
    private final ClientService clientService;

    @GetMapping("/{id}")
    @Operation(summary = "Получить клиента по id")
    public ClientInfoResp getClient (@PathVariable Long id){
        return clientService.getClient(id);
    }










}
