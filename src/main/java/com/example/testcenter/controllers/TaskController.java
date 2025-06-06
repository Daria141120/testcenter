package com.example.testcenter.controllers;

import com.example.testcenter.model.dto.request.TaskInfoReq;
import com.example.testcenter.model.dto.response.EmployeeInfoResp;
import com.example.testcenter.model.dto.response.TaskInfoResp;
import com.example.testcenter.model.enums.TaskStatus;
import com.example.testcenter.service.TaskService;
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
@RequestMapping("/tasks")
@RequiredArgsConstructor
@Tag(name = "Задачи")
@SecurityScheme(type = SecuritySchemeType.HTTP, scheme = "bearer", bearerFormat = "JWT", name = "Authorization")
@SecurityRequirement(name = AUTHORIZATION)
public class TaskController {

    private final TaskService taskService;

    @GetMapping("/{id}")
    @Operation(summary = "Получить задачу по id (элемента заявки)")
    public TaskInfoResp getTask(@PathVariable ("id") Long id){
        return taskService.getTask(id);
    }


// для внутреннего использования в OrderItem, задача создается автоматически в orderItemService.addOrderItem
//    @PostMapping
//    @Operation(summary = "Добавить задачу")
//    public TaskInfoResp addTask(@RequestBody @Valid TaskInfoReq taskInfoReq){
//        return taskService.addTask(taskInfoReq);
//    }


    @PutMapping("/{id}/changeEmployee")
    @Operation(summary = "Назначить/сменить сотрудника для задачи")
    public TaskInfoResp changeTaskEmployee(@PathVariable("id") Long id, @RequestBody EmployeeInfoResp employeeResp){
        return taskService.changeTaskEmployee(id, employeeResp);
    }


    @PutMapping("/{id}/changeStatus")
    @Operation(summary = "Сменить статус задачи")
    public TaskInfoResp changeTaskStatus(@PathVariable("id") Long id, @RequestBody String status){
        return taskService.changeTaskStatus(id, status);
    }


    @GetMapping("/allStatus")
    @Operation(summary = "Получить все возможные статусы задач")
    public List<TaskStatus> getAllTaskStatus(){
        return taskService.getAllTaskStatus();
    }


    @GetMapping("/all")
    @Operation(summary = "Получить все задачи")
    public List<TaskInfoResp> getAllTasks(){
        return taskService.getAllTasks();
    }


    @GetMapping("/allNewTask")
    @Operation(summary = "Получить все задачи у которых не назначен сотрудник")
    public List<TaskInfoResp> getAllNewTasks(){
        return taskService.getAllNewTasks();
    }


    @GetMapping("/allNotCompletedTask")
    @Operation(summary = "Получить все незавершенные задачи (по номеру заявки - опционально)")
    public List<TaskInfoResp> getAllNotCompletedTask(@RequestParam (required = false) Long idOrder){
        return taskService.getAllNotCompletedTask(idOrder);
    }


}
