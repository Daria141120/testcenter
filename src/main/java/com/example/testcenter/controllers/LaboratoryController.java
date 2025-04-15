package com.example.testcenter.controllers;


import com.example.testcenter.model.dto.request.LaboratoryInfoReq;
import com.example.testcenter.model.dto.response.EmployeeInfoResp;
import com.example.testcenter.model.dto.response.LaboratoryInfoResp;
import com.example.testcenter.service.LaboratoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
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
    public LaboratoryInfoResp getLaboratory (@PathVariable ("id") Long id){
        return laboratoryService.getLaboratory(id);
    }

    @PostMapping
    @Operation(summary = "Добавить лабораторию")
    public LaboratoryInfoResp addLaboratory(@RequestBody @Valid LaboratoryInfoReq laboratoryInfoReq){
        return laboratoryService.addLaboratory(laboratoryInfoReq);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить даннные лаборатории по id")
    public LaboratoryInfoResp updateLaboratory(@PathVariable ("id") Long id, @RequestBody LaboratoryInfoReq laboratoryInfoReq){
        return laboratoryService.updateLaboratory(id, laboratoryInfoReq);
    }


    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить лабораторию")
    public void deleteLaboratory(@PathVariable ("id") Long id){
        laboratoryService.deleteLaboratory(id);
    }


    @GetMapping("/all")
    @Operation(summary = "Получить все лаборатории")
    public List<LaboratoryInfoResp> getAllLaboratory (){
        return laboratoryService.getAllLaboratory();
    }

    @GetMapping("/{id}/employees")
    @Operation(summary = "Получить сотрудников лаборатории по id")
    public List<EmployeeInfoResp> getLaboratoryEmployees (@PathVariable ("id") Long id){
        return laboratoryService.getLaboratoryEmployees(id);
    }


}
