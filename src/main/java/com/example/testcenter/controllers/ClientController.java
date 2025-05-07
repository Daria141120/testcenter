package com.example.testcenter.controllers;


import com.example.testcenter.model.dto.request.ClientInfoReq;
import com.example.testcenter.model.dto.response.ClientInfoResp;
import com.example.testcenter.service.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@RequestMapping("/clients")
@RequiredArgsConstructor
@Tag(name = "Клиенты")
@SecurityScheme(type = SecuritySchemeType.HTTP, scheme = "bearer", bearerFormat = "JWT", name = "Authorization")
@SecurityRequirement(name = AUTHORIZATION)
public class ClientController {

    private final ClientService clientService;

    @GetMapping("/{id}")
    @Operation(summary = "Получить клиента по id")
    public ClientInfoResp getClient (@PathVariable ("id") Long id){
        return clientService.getClient(id);
    }

    @PostMapping
    @Operation(summary = "Добавить клиента")
    public ClientInfoResp addClient(@RequestBody @Valid ClientInfoReq clientInfoReq){
        return clientService.addClient(clientInfoReq);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить даннные клиента по id")
    public ClientInfoResp updateClient(@PathVariable ("id") Long id, @RequestBody ClientInfoReq clientInfoReq){
        return clientService.updateClient(id,clientInfoReq);
    }


    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить клиента")
    public void deleteClient(@PathVariable ("id") Long id){
        clientService.deleteClient(id);
    }

    @GetMapping("/all")
    @Operation(summary = "Получить всех клиентов")
    public List<ClientInfoResp> getAllClient (){
        return clientService.getAllClient();
    }


}
