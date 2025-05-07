package com.example.testcenter.controllers;


import com.example.testcenter.model.dto.request.ClientOrderInfoReq;
import com.example.testcenter.model.dto.response.ClientOrderInfoResp;
import com.example.testcenter.model.dto.response.OrderItemInfoResp;
import com.example.testcenter.model.enums.OrderStatus;
import com.example.testcenter.service.ClientOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@Tag(name = "Заявки")
@SecurityScheme(type = SecuritySchemeType.HTTP, scheme = "bearer", bearerFormat = "JWT", name = "Authorization")
public class ClientOrderController {

    private final ClientOrderService clientOrderService;

    @GetMapping("/{id}")
    @Operation(summary = "Получить заявку по id", security = @SecurityRequirement(name = AUTHORIZATION))
    public ClientOrderInfoResp getClientOrder(@PathVariable("id") Long id) {
        return clientOrderService.getClientOrder(id);
    }

    @PostMapping
    @Operation(summary = "Добавить заявку", security = @SecurityRequirement(name = AUTHORIZATION))
    public ClientOrderInfoResp addClientOrder(@RequestBody @Valid ClientOrderInfoReq clientOrderInfoReq) {
        return clientOrderService.addClientOrder(clientOrderInfoReq);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить даннные заявки", security = @SecurityRequirement(name = AUTHORIZATION))
    public ClientOrderInfoResp updateClientOrder(@PathVariable("id") Long id, @RequestBody ClientOrderInfoReq clientOrderInfoReq) {
        return clientOrderService.updateClientOrder(id, clientOrderInfoReq);
    }


    @PutMapping("/{id}/changeStatus")
    @Operation(summary = "Изменить статус заявки", security = @SecurityRequirement(name = AUTHORIZATION))
    public ClientOrderInfoResp updateClientOrderStatus(@PathVariable("id") Long id, @RequestBody String status) {
        return clientOrderService.updateClientOrderStatus(id, status);
    }

    @GetMapping("/all")
    @Operation(summary = "Получить все заявки(по статусу - опционально)", security = @SecurityRequirement(name = AUTHORIZATION))
    public List<ClientOrderInfoResp> getAllClientOrder(@RequestParam (required = false) String status) {
        return clientOrderService.getAllClientOrder(status);
    }


    @GetMapping("/allStatus")
    // используется для выбора статуса при обновлении информации о состоянии заявки
    @Operation(summary = "Получить все возможные статусы заявок", security = @SecurityRequirement(name = AUTHORIZATION))
    public List<OrderStatus> getAllOrderStatus() {
        return clientOrderService.getAllOrderStatus();
    }

    @GetMapping("/{id}/orderItems")
    @Operation(summary = "Получить все элементы заявки", security = @SecurityRequirement(name = AUTHORIZATION))
    public List<OrderItemInfoResp> getAllItemsOfOrder(@PathVariable("id") Long id) {
        return clientOrderService.getAllItemsOfOrder(id);
    }

    @GetMapping("/checkStatus")
    @Operation(summary = "Получить статус заявки по уникальному номеру")
    public String getStatusByNumber(@RequestParam(required = true) String orderNumber) {
        return clientOrderService.getStatusByNumber(orderNumber);
    }

}
