package com.example.testcenter.service;

import com.example.testcenter.model.db.entity.Task;
import com.example.testcenter.model.dto.request.TaskInfoReq;
import com.example.testcenter.model.dto.response.EmployeeInfoResp;
import com.example.testcenter.model.dto.response.TaskInfoResp;
import com.example.testcenter.model.enums.TaskStatus;

import java.security.Principal;
import java.util.List;

public interface TaskService {
    Task getTaskFromDB(Long id);

    TaskInfoResp getTask(Long id);

    List<TaskInfoResp> getAllTasks();

    TaskInfoResp addTask(TaskInfoReq taskInfoReq);

    TaskInfoResp changeTaskEmployee(Long id, EmployeeInfoResp employeeResp);

    TaskInfoResp changeTaskStatus(Long id, String status);

    List<TaskStatus> getAllTaskStatus();

    List<TaskInfoResp> getAllNewTasks();

    List<TaskInfoResp> getAllNotCompletedTask(Long idOrder);

    List<TaskInfoResp> getAllEmployeeAssignedTasks(Long id);

    List<TaskInfoResp> getAllTasksByLaboratory(Long id, String status);
}
