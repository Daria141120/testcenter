package com.example.testcenter.controllers;

import com.example.testcenter.model.dto.request.OrderItemInfoReq;
import com.example.testcenter.model.dto.response.OrderItemInfoResp;
import com.example.testcenter.service.OrderItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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

    @PostMapping
    @Operation(summary = "Добавить элемент заявки")
    public OrderItemInfoResp addOrderItem(@RequestBody @Valid OrderItemInfoReq orderItemInfoReq){
        return orderItemService.addOrderItem(orderItemInfoReq);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить даннные (количество, результат, доп.информация) элемента заявки по id")
    public OrderItemInfoResp updateOrderItem(@PathVariable ("id") Long id, @RequestBody @Valid OrderItemInfoReq orderItemInfoReq){
        return orderItemService.updateOrderItem(id, orderItemInfoReq);
    }

    @GetMapping("/all")
    @Operation(summary = "Получить все записи из таблицы элементы заявок (по id заявки - опционально)")
    public List<OrderItemInfoResp> getAllOrderItem(@RequestParam (required = false) Long idOrder){
        return orderItemService.getAllOrderItem(idOrder);
    }


}
