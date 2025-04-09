package com.example.testcenter.controllers;


import com.example.testcenter.model.dto.request.LaboratoryInfoReq;
import com.example.testcenter.model.dto.response.LaboratoryInfoResp;
import com.example.testcenter.service.LaboratoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/laboratories")
@RequiredArgsConstructor
@Tag(name = "Лаборатории")
public class LaboratoryController {

    private final LaboratoryService laboratoryService;

    @GetMapping("/{id}")
    @Operation(summary = "Получить лабораторию по id")
    public LaboratoryInfoResp getLaboratory (@PathVariable Long id){
        return laboratoryService.getLaboratory(id);
    }

    @PostMapping
    @Operation(summary = "Добавить лабораторию")
    public LaboratoryInfoResp addClient(@RequestBody @Valid LaboratoryInfoReq laboratoryInfoReq){
        return laboratoryService.addLaboratory(laboratoryInfoReq);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить даннные лаборатории по id")
    public LaboratoryInfoResp updateClient(@PathVariable Long id, @RequestBody LaboratoryInfoReq laboratoryInfoReq){
        return laboratoryService.updateLaboratory(id, laboratoryInfoReq);
    }


    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить лабораторию")
    public void deleteLaboratory(@PathVariable Long id){
        laboratoryService.deleteLaboratory(id);
    }

    @GetMapping("/all")
    @Operation(summary = "Получить все лаборатории")
    public List<LaboratoryInfoResp> getAllLaboratory (){
        return laboratoryService.getAllLaboratory();
    }




}
