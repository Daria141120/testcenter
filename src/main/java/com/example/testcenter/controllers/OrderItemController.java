package com.example.testcenter.controllers;

import com.example.testcenter.model.dto.response.LaboratoryInfoResp;
import com.example.testcenter.model.dto.response.OrderItemInfoResp;
import com.example.testcenter.service.OrderItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orderItems")
@RequiredArgsConstructor
@Tag(name = "Элементы заявок")
public class OrderItemController {

    private final OrderItemService orderItemService;

    @GetMapping("/{id}")
    @Operation(summary = "Получить элемент заявки по id")
    public OrderItemInfoResp getOrderItem (@PathVariable("id") Long id){
        return orderItemService.getOrderItem(id);
    }




}
