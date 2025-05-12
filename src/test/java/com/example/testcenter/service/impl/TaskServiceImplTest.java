package com.example.testcenter.service.impl;

import com.example.testcenter.exception.CommonBackendException;
import com.example.testcenter.mapper.TaskMapper;
import com.example.testcenter.mapper.TaskMapperImpl;
import com.example.testcenter.model.db.entity.*;
import com.example.testcenter.model.db.repository.TaskRepository;
import com.example.testcenter.model.dto.request.TaskInfoReq;
import com.example.testcenter.model.dto.response.EmployeeInfoResp;
import com.example.testcenter.model.dto.response.OrderItemInfoResp;
import com.example.testcenter.model.dto.response.TaskInfoResp;
import com.example.testcenter.model.enums.EmployeeStatus;
import com.example.testcenter.model.enums.TaskStatus;
import com.example.testcenter.service.ClientOrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TaskServiceImplTest {

    @InjectMocks
    private TaskServiceImpl taskService;

    @Mock
    private TaskRepository taskRepository;

    @Spy
    private ObjectMapper objectMapper;

    @Spy
    private TaskMapper taskMapper = new TaskMapperImpl();

    @Mock
    private ClientOrderService clientOrderService;

    @PersistenceContext
    @Mock
    private EntityManager entityManager;

    @Test
    public void getTaskFromDB() {
        Task task = new Task();
        task.setId(1L);

        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));
        Task taskFromDB = taskService.getTaskFromDB(task.getId());
        assertEquals(task.getId(), taskFromDB.getId());
    }

    @Test(expected = CommonBackendException.class)
    public void getTaskFromDBNotFound() {
        taskService.getTaskFromDB(1L);
    }

    @Test
    public void getTask() {
        Task task = new Task();
        task.setId(1L);

        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));
        TaskInfoResp taskResp = taskService.getTask(task.getId());
        assertEquals(task.getId(), taskResp.getId());
    }

    @Test
    public void addTaskEmptyEmployee() {
        OrderItemInfoResp orderItemResp = new OrderItemInfoResp();
        orderItemResp.setId(1L);
        TaskInfoReq req = new TaskInfoReq();
        req.setOrderItem(orderItemResp);

        OrderItem orderItem = new OrderItem();
        orderItem.setId(1L);

        Task task = new Task();
        task.setId(1L);
        task.setOrderItem(orderItem);

        when(entityManager.merge(any(Task.class))).thenReturn(task);
        TaskInfoResp taskResp = taskService.addTask(req);
        assertEquals(task.getId(), taskResp.getId());
        assertEquals(req.getOrderItem().getId(), taskResp.getOrderItem().getId());
    }

    @Test
    public void addTaskWithEmployee() {
        EmployeeInfoResp employeeResp = new EmployeeInfoResp();
        employeeResp.setLastName("Petrov");
        TaskInfoReq req = new TaskInfoReq();
        req.setEmployee(employeeResp);

        Employee employee = new Employee();
        employee.setLastName(employeeResp.getLastName());

        Task task = new Task();
        task.setId(1L);
        task.setEmployee(employee);

        when(entityManager.merge(any(Task.class))).thenReturn(task);
        TaskInfoResp taskResp = taskService.addTask(req);
        assertEquals(task.getId(), taskResp.getId());
        assertEquals(req.getEmployee().getLastName(), taskResp.getEmployee().getLastName());
    }

    @Test(expected = CommonBackendException.class)
    public void addTaskWithEmployeeDISMISSED() {
        EmployeeInfoResp employeeResp = new EmployeeInfoResp();
        employeeResp.setStatus(EmployeeStatus.DISMISSED);
        TaskInfoReq req = new TaskInfoReq();
        req.setEmployee(employeeResp);

        taskService.addTask(req);
    }

    @Test
    public void changeTaskEmployee() {
        EmployeeInfoResp employeeResp = new EmployeeInfoResp();
        employeeResp.setId(1L);
        Task task = new Task();
        task.setId(1L);

        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(task);
        TaskInfoResp taskResp = taskService.changeTaskEmployee(task.getId(), employeeResp);
        assertEquals(employeeResp.getId(), taskResp.getEmployee().getId());
        assertEquals(TaskStatus.ASSIGNED, taskResp.getStatus());
    }

    @Test(expected = CommonBackendException.class)
    public void changeTaskEmployeeDISMISSED() {
        Task task = new Task();
        task.setId(1L);

        EmployeeInfoResp employeeResp = new EmployeeInfoResp();
        employeeResp.setStatus(EmployeeStatus.DISMISSED);
        taskService.changeTaskEmployee(task.getId(), employeeResp);
    }

    @Test(expected = CommonBackendException.class)
    public void changeTaskEmployeeCLOSED() {
        Task task = new Task();
        task.setId(1L);
        task.setStatus(TaskStatus.CLOSED);

        EmployeeInfoResp employeeResp = new EmployeeInfoResp();
        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));
        taskService.changeTaskEmployee(task.getId(), employeeResp);
    }

    @Test
    public void changeTaskStatus() {
        Task task = new Task();
        task.setId(1L);

        String status = TaskStatus.ASSIGNED.name();

        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));
        when(taskRepository.save(task)).thenReturn(task);
        TaskInfoResp taskResp = taskService.changeTaskStatus(task.getId(), status);
        assertEquals(TaskStatus.valueOf(status), taskResp.getStatus());
    }

    @Test
    public void changeTaskStatusToCLOSED() {
        ClientOrder order = new ClientOrder();
        order.setId(1L);
        OrderItem orderItem = new OrderItem();
        orderItem.setClientOrder(order);

        Task task = new Task();
        task.setId(1L);
        task.setOrderItem(orderItem);

        String status = TaskStatus.CLOSED.name();

        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));
        when(taskRepository.save(task)).thenReturn(task);
        when(taskRepository.countByStatusNotAndOrderItem_ClientOrder_Id(any(TaskStatus.class), anyLong())).thenReturn(0L);
        TaskInfoResp taskResp = taskService.changeTaskStatus(task.getId(), status);

        assertEquals(TaskStatus.CLOSED, taskResp.getStatus());
        assertEquals(task.getId(), taskResp.getId());
        verify(clientOrderService, times(1)).updateClientOrderStatus(anyLong(), anyString());
    }

    @Test(expected = CommonBackendException.class)
    public void changeTaskStatusNotCorrect() {
        String wrongStatus = "ASWDWD";
        taskService.changeTaskStatus(1L, wrongStatus);
    }

    @Test
    public void getAllTasks() {
        Task task1 = new Task();
        Task task2 = new Task();
        task1.setId(1L);
        task2.setId(2L);
        List<Task> taskList = List.of(task1, task2);

        when(taskRepository.findAll()).thenReturn(taskList);
        List<TaskInfoResp> taskRespList = taskService.getAllTasks();
        assertEquals(taskList.size(), taskRespList.size());
    }


    @Test
    public void getAllNewTasks() {
        TaskStatus taskStatus = TaskStatus.CREATED;

        Task task1 = new Task();
        Task task2 = new Task();
        task1.setId(1L);
        task1.setStatus(taskStatus);
        task2.setId(2L);
        task2.setStatus(taskStatus);
        List<Task> taskList = List.of(task1, task2);

        when(taskRepository.findAllByStatus(taskStatus)).thenReturn(taskList);
        List<TaskInfoResp> taskRespList = taskService.getAllNewTasks();
        assertEquals(taskList.size(), taskRespList.size());
        taskRespList.forEach(taskResp ->
                assertEquals(TaskStatus.CREATED, taskResp.getStatus())
        );
    }

    @Test
    public void getAllNotCompletedTask() {
        Task task1 = new Task();
        Task task2 = new Task();
        task1.setId(1L);
        task2.setId(2L);
        List<Task> taskList = List.of(task1, task2);

        when(taskRepository.findAllByStatusNot(any(TaskStatus.class))).thenReturn(taskList);
        List<TaskInfoResp> taskRespList = taskService.getAllNotCompletedTask(null);
        assertEquals(taskList.size(), taskRespList.size());
    }

    @Test
    public void getAllNotCompletedTaskAndOrderId() {
        Task task1 = new Task();
        Task task2 = new Task();
        task1.setId(1L);
        task2.setId(2L);
        List<Task> taskList = List.of(task1, task2);

        when(taskRepository.findAllNotCompletedTask(anyLong())).thenReturn(taskList);
        List<TaskInfoResp> taskRespList = taskService.getAllNotCompletedTask(1L);
        assertEquals(taskList.size(), taskRespList.size());
    }

    @Test
    public void getAllEmployeeAssignedTasks() {
        Employee employee = new Employee();
        employee.setId(1L);
        TaskStatus taskStatus = TaskStatus.ASSIGNED;
        List<Task> taskList = List.of(new Task(), new Task());

        when(taskRepository.findAllByEmployee_IdAndStatus(employee.getId(), taskStatus)).thenReturn(taskList);
        List<TaskInfoResp> taskRespList = taskService.getAllEmployeeAssignedTasks(employee.getId());
        assertEquals(taskList.size(), taskRespList.size());
    }

    @Test
    public void getAllTasksByLaboratory() {
        Laboratory lab = new Laboratory();
        lab.setId(1L);
        TaskStatus taskStatus = TaskStatus.CREATED;
        String status = taskStatus.name();
        List<Task> taskList = List.of(new Task(), new Task());

        when(taskRepository.findAllByStatusAndLab(taskStatus, lab.getId())).thenReturn(taskList);
        List<TaskInfoResp> taskRespList = taskService.getAllTasksByLaboratory(lab.getId(), status);
        assertEquals(taskList.size(), taskRespList.size());
    }

    @Test(expected = CommonBackendException.class)
    public void getAllTasksByLaboratoryNotCorrectStatus() {
        Laboratory lab = new Laboratory();
        lab.setId(1L);
        String status = "ASAAF";

        taskService.getAllTasksByLaboratory(lab.getId(), status);
    }

    @Test
    public void getAllTasksByLaboratoryWithoutStatus() {
        Laboratory lab = new Laboratory();
        lab.setId(1L);

        Task task1 = new Task();
        Task task2 = new Task();
        task1.setId(1L);
        task2.setId(2L);
        List<Task> taskList = List.of(task1, task2);

        when(taskRepository.findAllByLab(lab.getId())).thenReturn(taskList);
        List<TaskInfoResp> taskRespList = taskService.getAllTasksByLaboratory(lab.getId(), "");
        assertEquals(taskList.size(), taskRespList.size());
    }
}