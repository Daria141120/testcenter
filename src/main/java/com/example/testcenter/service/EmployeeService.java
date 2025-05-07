package com.example.testcenter.service;

import com.example.testcenter.model.db.entity.Employee;
import com.example.testcenter.model.dto.request.EmployeeInfoReq;
import com.example.testcenter.model.dto.response.EmployeeInfoResp;
import com.example.testcenter.model.dto.response.LaboratoryInfoResp;
import com.example.testcenter.model.dto.response.TaskInfoResp;
import com.example.testcenter.model.enums.EmployeeStatus;

import java.util.List;

public interface EmployeeService {

    Employee getEmployeeFromDB(Long id);

    EmployeeInfoResp getEmployee(Long id);

    EmployeeInfoResp addEmployee(EmployeeInfoReq employeeInfoReq);

    EmployeeInfoResp updateEmployee(Long id, EmployeeInfoReq employeeInfoReq);

    void deleteEmployee(Long id);

    List<EmployeeInfoResp> getAllEmployee(String status);

    EmployeeInfoResp changeLab(Long id, LaboratoryInfoResp lab);

    List<EmployeeStatus> getAllEmployeeStatus();

    List<TaskInfoResp> getAllAssignedTasks(Long id);
}
