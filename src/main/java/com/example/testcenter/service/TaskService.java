package com.example.testcenter.service;

import com.example.testcenter.model.dto.request.TaskInfoReq;
import com.example.testcenter.model.dto.response.TaskInfoResp;

import java.util.List;

public interface TaskService {
    TaskInfoResp getTask(Long id);

    List<TaskInfoResp> getAllTasks();

    TaskInfoResp addTask(TaskInfoReq taskInfoReq);
}
