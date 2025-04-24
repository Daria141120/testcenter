package com.example.testcenter.service.impl;


import com.example.testcenter.exception.CommonBackendException;
import com.example.testcenter.mapper.TaskMapper;
import com.example.testcenter.model.db.entity.Task;
import com.example.testcenter.model.db.repository.TaskRepository;
import com.example.testcenter.model.dto.request.TaskInfoReq;
import com.example.testcenter.model.dto.response.TaskInfoResp;
import com.example.testcenter.model.enums.EmployeeStatus;
import com.example.testcenter.model.enums.TaskStatus;
import com.example.testcenter.service.OrderItemService;
import com.example.testcenter.service.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final ObjectMapper objectMapper;
    private final TaskMapper taskMapper;
    private final OrderItemService orderItemService;

    @PersistenceContext
    private final EntityManager entityManager;

    private Task getTaskFromDB(Long id) {
        Optional<Task> taskFromDB = taskRepository.findById(id);
        final String errMsg = String.format("task with id : %s not found", id);
        return taskFromDB.orElseThrow(() -> new CommonBackendException(errMsg, HttpStatus.NOT_FOUND));
    }

    @Override
    public TaskInfoResp getTask(Long id) {
        Task taskFromDB = getTaskFromDB(id);
        return taskMapper.toTaskInfoResp(taskFromDB);
    }

    @Transactional
    @Override
    public TaskInfoResp addTask(TaskInfoReq req) {
        orderItemService.getOrderItemFromDB(req.getOrderItem().getId());  // check  ordItem

        if (req.getEmployee().getStatus() == EmployeeStatus.DISMISSED){
            throw new CommonBackendException("Employee DISMISSED", HttpStatus.BAD_REQUEST);
        }

        Task task = objectMapper.convertValue(req, Task.class);
        task.setStatus(TaskStatus.CREATED);

        //Task taskSaved = taskRepository.save(merge);   // detached entity - если переприсоединять order-item то работает
        // пытается пересохранять в таблицу order-item, не получилось это отключить

        Task mergedTask = entityManager.merge(task);  // обновление связанных сущностей делает
        entityManager.persist(mergedTask);

        return taskMapper.toTaskInfoResp(mergedTask);
    }






















    @Override
    public List<TaskInfoResp> getAllTasks() {
        List<Task> taskList = taskRepository.findAll();
        return taskMapper.toTaskInfoRespList(taskList);
    }





}
