package com.example.testcenter.controllers;

import com.example.testcenter.model.dto.request.TaskInfoReq;
import com.example.testcenter.model.dto.response.TaskInfoResp;
import com.example.testcenter.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
@Tag(name = "Задачи")
public class TaskController {

    private final TaskService taskService;

    @GetMapping("/{id}")
    @Operation(summary = "Получить задачу по id (элемента заявки)")
    public TaskInfoResp getTask(@PathVariable ("id") Long id){
        return taskService.getTask(id);
    }

    @PostMapping
    @Operation(summary = "Добавить задачу")
    public TaskInfoResp addTask(@RequestBody @Valid TaskInfoReq taskInfoReq){
        return taskService.addTask(taskInfoReq);
    }














    @GetMapping("/all")
    @Operation(summary = "Получить все задачи")
    public List<TaskInfoResp> getAllTasks(){
        return taskService.getAllTasks();
    }


}
