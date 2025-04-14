package com.example.testcenter.service;


import com.example.testcenter.model.dto.request.EmployeeInfoReq;
import com.example.testcenter.model.dto.response.EmployeeInfoResp;

import java.util.List;

public interface EmployeeService {

    EmployeeInfoResp getEmployee(Long id);

    EmployeeInfoResp addEmployee(EmployeeInfoReq employeeInfoReq);

    EmployeeInfoResp updateEmployee(Long id, EmployeeInfoReq employeeInfoReq);

    void deleteEmployee(Long id);

    List<EmployeeInfoResp> getAllEmployee();

}
