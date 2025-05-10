package com.example.testcenter.controllers;


import com.example.testcenter.model.dto.request.EmployeeInfoReq;
import com.example.testcenter.model.dto.response.EmployeeInfoResp;
import com.example.testcenter.model.dto.response.LaboratoryInfoResp;
import com.example.testcenter.model.dto.response.TaskInfoResp;
import com.example.testcenter.model.enums.EmployeeStatus;
import com.example.testcenter.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@RequestMapping("/employees")
@RequiredArgsConstructor
@Tag(name = "Сотрудники")
@SecurityScheme(type = SecuritySchemeType.HTTP, scheme = "bearer", bearerFormat = "JWT", name = "Authorization")
@SecurityRequirement(name = AUTHORIZATION)
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping("/{id}")
    @Operation(summary = "Получить работника по id")
    public EmployeeInfoResp getEmployee(@PathVariable("id") Long id) {
        return employeeService.getEmployee(id);
    }

    //создание сотрудника только из конроллера User (зарегистрированный пользователь заполняет профиль сотрудника) в userService.addEmployee(id, employeeReq);
//    @PostMapping
//    @Operation(summary = "Добавить сотрудника")
//    public EmployeeInfoResp addEmployee(@RequestBody @Valid EmployeeInfoReq employeeInfoReq) {
//        return employeeService.addEmployee(employeeInfoReq);
//    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping("/{id}")
    @Operation(summary = "Обновить даннные сотрудника по id")
    public EmployeeInfoResp updateEmployee(@PathVariable("id") Long id, @RequestBody EmployeeInfoReq employeeInfoReq) {
        return employeeService.updateEmployee(id, employeeInfoReq);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить сотрудника")
    public void deleteEmployee(@PathVariable("id") Long id) {
        employeeService.deleteEmployee(id);
    }


    @GetMapping("/all")
    @Operation(summary = "Получить всех сотрудников (по  статусу работает/уволен - опционально)")
    public List<EmployeeInfoResp> getAllEmployee (@RequestParam (required = false) String status){
        return employeeService.getAllEmployee(status);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping("/{id}/changeLab")
    @Operation(summary = "Сменить лабораторию")
    public EmployeeInfoResp changeLab(@PathVariable("id") Long id, @RequestBody LaboratoryInfoResp lab) {
        return employeeService.changeLab(id, lab);
    }


    @GetMapping("/allStatus")
    @Operation(summary = "Получить статусы сотрудников")
    public List<EmployeeStatus> getAllEmployeeStatus(){
        return employeeService.getAllEmployeeStatus();
    }


    @GetMapping("/{id}/assignedTasks")
    @Operation(summary = "Получить задачи назначенные сотруднику")
    public List<TaskInfoResp> getAllAssignedTasks(@PathVariable("id") Long id){
        return employeeService.getAllAssignedTasks(id);
    }


}
