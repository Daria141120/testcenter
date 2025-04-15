package com.example.testcenter.controllers;


import com.example.testcenter.model.dto.request.ClientInfoReq;
import com.example.testcenter.model.dto.request.EmployeeInfoReq;
import com.example.testcenter.model.dto.response.ClientInfoResp;
import com.example.testcenter.model.dto.response.EmployeeInfoResp;
import com.example.testcenter.model.dto.response.LaboratoryInfoResp;
import com.example.testcenter.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/employees")
@RequiredArgsConstructor
@Tag(name = "Сотрудники")
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping("/{id}")
    @Operation(summary = "Получить работника по id")
    public EmployeeInfoResp getEmployee(@PathVariable("id") Long id) {
        return employeeService.getEmployee(id);
    }


    @PostMapping
    @Operation(summary = "Добавить сотрудника")
    public EmployeeInfoResp addEmployee(@RequestBody @Valid EmployeeInfoReq employeeInfoReq) {
        return employeeService.addEmployee(employeeInfoReq);
    }


    @PutMapping("/{id}")
    @Operation(summary = "Обновить даннные сотрудника по id")
    public EmployeeInfoResp updateEmployee(@PathVariable("id") Long id, @RequestBody EmployeeInfoReq employeeInfoReq) {
        return employeeService.updateEmployee(id, employeeInfoReq);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить сотрудника")
    public void deleteEmployee(@PathVariable("id") Long id) {
        employeeService.deleteEmployee(id);
    }


    @GetMapping("/all")
    @Operation(summary = "Получить всех сотрудников")
    public List<EmployeeInfoResp> getAllEmployee (){
        return employeeService.getAllEmployee();
    }

    @PutMapping("/{id}/changeLab")
    @Operation(summary = "Сменить лабораторию")
    public EmployeeInfoResp changeLab(@PathVariable("id") Long id, @RequestBody LaboratoryInfoResp lab) {
        return employeeService.changeLab(id, lab);
    }

}
