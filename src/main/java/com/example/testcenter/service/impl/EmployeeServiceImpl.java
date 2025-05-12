package com.example.testcenter.service.impl;


import com.example.testcenter.exception.CommonBackendException;
import com.example.testcenter.mapper.EmployeeMapper;
import com.example.testcenter.model.db.entity.Employee;
import com.example.testcenter.model.db.entity.Laboratory;
import com.example.testcenter.model.db.repository.EmployeeRepository;
import com.example.testcenter.model.dto.request.EmployeeInfoReq;
import com.example.testcenter.model.dto.response.EmployeeInfoResp;
import com.example.testcenter.model.dto.response.LaboratoryInfoResp;
import com.example.testcenter.model.dto.response.TaskInfoResp;
import com.example.testcenter.model.enums.EmployeeStatus;
import com.example.testcenter.model.enums.LaboratoryStatus;
import com.example.testcenter.service.EmployeeService;
import com.example.testcenter.service.LaboratoryService;
import com.example.testcenter.service.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final ObjectMapper objectMapper;
    private final LaboratoryService laboratoryService;
    private final EmployeeMapper employeeMapper;
    private final TaskService taskService;

    @Override
    public Employee getEmployeeFromDB(Long id) {
        Optional<Employee> employeeFromDB = employeeRepository.findById(id);
        final String errMsg = String.format("employee with id : %s not found", id);
        return employeeFromDB.orElseThrow(() -> new CommonBackendException(errMsg, HttpStatus.NOT_FOUND));
    }

    @Override
    public EmployeeInfoResp getEmployee(Long id) {
        Employee employeeFromDB = getEmployeeFromDB(id);
        return employeeMapper.toEmployeeInfoResp(employeeFromDB);
    }

    @Override
    public EmployeeInfoResp addEmployee(EmployeeInfoReq req) {
        employeeRepository.findFirstByEmailAndLastName(req.getEmail(), req.getLastName()).ifPresent(
                employee -> {
                    throw new CommonBackendException("Employee already exist", HttpStatus.CONFLICT);
                });

        if (laboratoryService.getLaboratoryFromDB(req.getLaboratory().getId()).getStatus() == LaboratoryStatus.LIQUIDATED) {
            throw new CommonBackendException("Laboratory LIQUIDATED ", HttpStatus.CONFLICT);
        }

        Employee employee = objectMapper.convertValue(req, Employee.class);
        employee.setStatus(EmployeeStatus.ACTIVE);
        Employee employeeSaved = employeeRepository.save(employee);

        Laboratory laboratoryFromDB = laboratoryService.getLaboratoryFromDB(req.getLaboratory().getId());
        laboratoryFromDB.getEmployeeList().add(employeeSaved);
        laboratoryService.updateLabListEmployee(laboratoryFromDB);

        return employeeMapper.toEmployeeInfoResp(employeeSaved);
    }


    @Override
    public EmployeeInfoResp updateEmployee(Long id, EmployeeInfoReq req) {

        Employee employeeFromDB = getEmployeeFromDB(id);
        Employee employeeForUpdate = objectMapper.convertValue(req, Employee.class);

        employeeFromDB.setEmail(employeeForUpdate.getEmail() == null ? employeeFromDB.getEmail() : employeeForUpdate.getEmail());
        employeeFromDB.setFirstName(employeeForUpdate.getFirstName() == null ? employeeFromDB.getFirstName() : employeeForUpdate.getFirstName());
        employeeFromDB.setLastName(employeeForUpdate.getLastName() == null ? employeeFromDB.getLastName() : employeeForUpdate.getLastName());
        employeeFromDB.setMiddleName(employeeForUpdate.getMiddleName() == null ? employeeFromDB.getMiddleName() : employeeForUpdate.getMiddleName());
        employeeFromDB.setPost(employeeForUpdate.getPost() == null ? employeeFromDB.getPost() : employeeForUpdate.getPost());

        Employee employeeSaved = employeeRepository.save(employeeFromDB);

        return employeeMapper.toEmployeeInfoResp(employeeSaved);
    }

    @Override
    public void deleteEmployee(Long id) {
        Employee employeeFromDB = getEmployeeFromDB(id);
        employeeFromDB.setStatus(EmployeeStatus.DISMISSED);
        employeeRepository.save(employeeFromDB);
    }


    @Override
    public List<EmployeeInfoResp> getAllEmployee(String status) {
        List<EmployeeInfoResp> respList;

        if (StringUtils.hasText(status)) {

            List<String> listStatus = getAllEmployeeStatus().stream().map(Enum::name).collect(Collectors.toList());
            if (!listStatus.contains(status)) {
                throw new CommonBackendException("Error in the status, there is no such status.", HttpStatus.BAD_REQUEST);
            } else {
                EmployeeStatus employeeStatus = EmployeeStatus.valueOf(status);
                respList = employeeMapper.toEmployeeInfoRespList(employeeRepository.findAllByStatus(employeeStatus));
            }

        } else {
            respList = employeeMapper.toEmployeeInfoRespList(employeeRepository.findAll());
        }
        return respList;
    }


    @Override
    public EmployeeInfoResp changeLab(Long id, LaboratoryInfoResp lab) {
        if (laboratoryService.getLaboratoryFromDB(lab.getId()).getStatus() == LaboratoryStatus.LIQUIDATED) {
            throw new CommonBackendException("Laboratory LIQUIDATED ", HttpStatus.CONFLICT);
        }

        Laboratory laboratoryForUpdate = objectMapper.convertValue(lab, Laboratory.class);
        Employee employeeFromDB = getEmployeeFromDB(id);

        Laboratory oldLaboratory = employeeFromDB.getLaboratory();
        oldLaboratory.getEmployeeList().remove(employeeFromDB);
        laboratoryService.updateLabListEmployee(oldLaboratory);

        employeeFromDB.setLaboratory(laboratoryForUpdate);
        Employee employeeSaved = employeeRepository.save(employeeFromDB);

        Laboratory newLab = laboratoryService.getLaboratoryFromDB(lab.getId());
        newLab.getEmployeeList().add(employeeSaved);
        laboratoryService.updateLabListEmployee(newLab);

        return employeeMapper.toEmployeeInfoResp(employeeSaved);
    }


    @Override
    public List<EmployeeStatus> getAllEmployeeStatus() {
        return Arrays.stream(EmployeeStatus.values()).collect(Collectors.toList());
    }

    @Override
    public List<TaskInfoResp> getAllAssignedTasks(Long id) {
        return taskService.getAllEmployeeAssignedTasks(id);
    }


}
