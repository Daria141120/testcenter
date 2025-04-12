package com.example.testcenter.controllers;


import com.example.testcenter.model.dto.request.EquipmentInfoReq;
import com.example.testcenter.model.dto.response.EquipmentInfoResp;
import com.example.testcenter.service.EquipmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/equipments")
@RequiredArgsConstructor
@Tag(name = "Оборудование")
public class EquipmentController {

    private final EquipmentService equipmentService;

    @GetMapping("/{id}")
    @Operation(summary = "Получить оборудование по id")
    public EquipmentInfoResp getEquipment (@PathVariable("id") Long id){
        return equipmentService.getEquipment(id);
    }

    @PostMapping
    @Operation(summary = "Добавить оборудование")
    public EquipmentInfoResp addEquipment(@RequestBody @Valid EquipmentInfoReq equipmentInfoReq){
        return equipmentService.addEquipment(equipmentInfoReq);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить даннные оборудования по id")
    public EquipmentInfoResp updateEquipment(@PathVariable ("id") Long id, @RequestBody EquipmentInfoReq equipmentInfoReq){
        return equipmentService.updateEquipment(id, equipmentInfoReq);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить оборудование")
    public void deleteEquipment(@PathVariable ("id") Long id){
        equipmentService.deleteEquipment(id);
    }

    @GetMapping("/all")
    @Operation(summary = "Получить всё оборудование")
    public List<EquipmentInfoResp> getAllEquipment (){
        return equipmentService.getAllEquipment();
    }



}
