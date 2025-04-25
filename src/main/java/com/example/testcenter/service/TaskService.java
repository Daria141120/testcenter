package com.example.testcenter.service;

import com.example.testcenter.model.dto.request.TaskInfoReq;
import com.example.testcenter.model.dto.response.EmployeeInfoResp;
import com.example.testcenter.model.dto.response.TaskInfoResp;
import com.example.testcenter.model.enums.TaskStatus;

import java.util.List;

public interface TaskService {
    TaskInfoResp getTask(Long id);

    List<TaskInfoResp> getAllTasks();

    TaskInfoResp addTask(TaskInfoReq taskInfoReq);

    TaskInfoResp changeTaskEmployee(Long id, EmployeeInfoResp employeeResp);

    TaskInfoResp changeTaskStatus(Long id, String status);

    List<TaskStatus> getAllTaskStatus();

    List<TaskInfoResp> getAllNewTasks();

    List<TaskInfoResp> getAllNotCompletedTask(Long idOrder);
}
