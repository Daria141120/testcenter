package com.example.testcenter.controllers;


import com.example.testcenter.model.dto.request.ClientInfoReq;
import com.example.testcenter.model.dto.response.ClientInfoResp;
import com.example.testcenter.service.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clients")
@RequiredArgsConstructor
@Tag(name = "Клиенты")
public class ClientController {

    private final ClientService clientService;

    @GetMapping("/{id}")
    @Operation(summary = "Получить клиента по id")
    public ClientInfoResp getClient (@PathVariable Long id){
        return clientService.getClient(id);
    }

    @PostMapping
    @Operation(summary = "Добавить клиента")
    public ClientInfoResp addClient(@RequestBody @Valid ClientInfoReq clientInfoReq){
        return clientService.addClient(clientInfoReq);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить даннные клиента по id")
    public ClientInfoResp updateClient(@PathVariable Long id, @RequestBody ClientInfoReq clientInfoReq){
        return clientService.updateClient(id,clientInfoReq);
    }


    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить клиента")
    public void deleteClient(@PathVariable Long id){
        clientService.deleteClient(id);
    }

    @GetMapping("/all")
    @Operation(summary = "Получить всех клиентов")
    public List<ClientInfoResp> getAllClient (){
        return clientService.getAllClient();
    }


}
