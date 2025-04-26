package com.example.testcenter.controllers;


import com.example.testcenter.model.dto.request.ClientOrderInfoReq;
import com.example.testcenter.model.dto.response.ClientOrderInfoResp;
import com.example.testcenter.model.dto.response.OrderItemInfoResp;
import com.example.testcenter.model.enums.OrderStatus;
import com.example.testcenter.service.ClientOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@Tag(name = "Заявки")
public class ClientOrderController {

    private final ClientOrderService clientOrderService;

    @GetMapping("/{id}")
    @Operation(summary = "Получить заявку по id")
    public ClientOrderInfoResp getClientOrder(@PathVariable("id") Long id) {
        return clientOrderService.getClientOrder(id);
    }

    @PostMapping
    @Operation(summary = "Добавить заявку")
    public ClientOrderInfoResp addClientOrder(@RequestBody @Valid ClientOrderInfoReq clientOrderInfoReq) {
        return clientOrderService.addClientOrder(clientOrderInfoReq);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить даннные заявки")
    public ClientOrderInfoResp updateClientOrder(@PathVariable("id") Long id, @RequestBody ClientOrderInfoReq clientOrderInfoReq) {
        return clientOrderService.updateClientOrder(id, clientOrderInfoReq);
    }


    @PutMapping("/{id}/changeStatus")
    @Operation(summary = "Изменить статус заявки")
    public ClientOrderInfoResp updateClientOrderStatus(@PathVariable("id") Long id, @RequestBody String status) {
        return clientOrderService.updateClientOrderStatus(id, status);
    }

    @GetMapping("/all")
    @Operation(summary = "Получить все заявки(по статусу - опционально)")
    public List<ClientOrderInfoResp> getAllClientOrder(@RequestParam (required = false) String status) {
        return clientOrderService.getAllClientOrder(status);
    }


    @GetMapping("/allStatus")
    // используется для выбора статуса при обновлении информации о состоянии заявки
    @Operation(summary = "Получить все возможные статусы заявок")
    public List<OrderStatus> getAllOrderStatus() {
        return clientOrderService.getAllOrderStatus();
    }

    @GetMapping("/{id}/orderItems")
    @Operation(summary = "Получить все элементы заявки")
    public List<OrderItemInfoResp> getAllItemsOfOrder(@PathVariable("id") Long id) {
        return clientOrderService.getAllItemsOfOrder(id);
    }

    @GetMapping("/checkStatus")
    @Operation(summary = "Получить статус заявки по уникальному номеру")
    public String getStatusByNumber(@RequestParam(required = true) String orderNumber) {
        return clientOrderService.getStatusByNumber(orderNumber);
    }

}
