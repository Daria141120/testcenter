package com.example.testcenter.service.impl;


import com.example.testcenter.exception.CommonBackendException;
import com.example.testcenter.mapper.EmployeeMapper;
import com.example.testcenter.mapper.TaskMapper;
import com.example.testcenter.model.db.entity.Employee;
import com.example.testcenter.model.db.entity.Task;
import com.example.testcenter.model.db.repository.TaskRepository;
import com.example.testcenter.model.dto.request.TaskInfoReq;
import com.example.testcenter.model.dto.response.EmployeeInfoResp;
import com.example.testcenter.model.dto.response.TaskInfoResp;
import com.example.testcenter.model.enums.EmployeeStatus;
import com.example.testcenter.model.enums.OrderStatus;
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
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final ObjectMapper objectMapper;
    private final TaskMapper taskMapper;
    private final EmployeeMapper employeeMapper;

//    private final OrderItemService orderItemService;

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
//        orderItemService.getOrderItemFromDB(req.getOrderItem().getId());  // check  ordItem

        Task task = objectMapper.convertValue(req, Task.class);
        if (req.getEmployee() == null) {
            task.setStatus(TaskStatus.CREATED);
        } else {
            if (req.getEmployee().getStatus() == EmployeeStatus.DISMISSED){
                throw new CommonBackendException("Employee DISMISSED", HttpStatus.BAD_REQUEST);
            }
            task.setStatus(TaskStatus.ASSIGNED);
        }

        //Task taskSaved = taskRepository.save(merge);   // detached entity - если переприсоединять order-item то работает
        // пытается пересохранять в таблицу order-item, не получилось это отключить

        Task mergedTask = entityManager.merge(task);  // обновление связанных сущностей делает
        entityManager.persist(mergedTask);

        return taskMapper.toTaskInfoResp(mergedTask);
    }


    @Override
    public TaskInfoResp changeTaskEmployee(Long id, EmployeeInfoResp employeeResp) {
        if (employeeResp.getStatus() == EmployeeStatus.DISMISSED){
            throw new CommonBackendException("Employee DISMISSED", HttpStatus.BAD_REQUEST);
        }

        Task taskFromDB = getTaskFromDB(id);

        if (taskFromDB.getStatus() == TaskStatus.CLOSED){
            throw new CommonBackendException("Task was CLOSED, you can't change an employee", HttpStatus.BAD_REQUEST);
        }

        Employee employee = objectMapper.convertValue(employeeResp, Employee.class);
        taskFromDB.setEmployee(employee);
        taskFromDB.setStatus(TaskStatus.ASSIGNED);
        Task taskSaved =  taskRepository.save(taskFromDB);

        return taskMapper.toTaskInfoResp(taskSaved);
    }

    @Override
    public TaskInfoResp changeTaskStatus(Long id, String status) {
        List<String> list = getAllTaskStatus().stream().map(Enum::name).collect(Collectors.toList());
        if (!list.contains(status)) {
            throw new CommonBackendException("Error in the status, there is no such status.", HttpStatus.BAD_REQUEST);
        }

        TaskStatus taskStatus = TaskStatus.valueOf(status);

        Task taskFromDB = getTaskFromDB(id);
        taskFromDB.setStatus(taskStatus);
        Task taskSaved = taskRepository.save(taskFromDB);
        return taskMapper.toTaskInfoResp(taskSaved);
    }

    @Override
    public List<TaskStatus> getAllTaskStatus() {
        return Arrays.stream(TaskStatus.values()).collect(Collectors.toList());
    }

    @Override
    public List<TaskInfoResp> getAllNewTasks() {
        List<Task> taskList = taskRepository.findAllByStatus(TaskStatus.CREATED);
        return taskMapper.toTaskInfoRespList(taskList);
    }


    @Override
    public List<TaskInfoResp> getAllTasks() {
        List<Task> taskList = taskRepository.findAll();
        return taskMapper.toTaskInfoRespList(taskList);
    }






}
