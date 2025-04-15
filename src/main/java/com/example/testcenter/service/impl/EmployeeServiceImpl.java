package com.example.testcenter.service.impl;


import com.example.testcenter.exception.CommonBackendException;
import com.example.testcenter.model.db.entity.Employee;
import com.example.testcenter.model.db.repository.EmployeeRepository;
import com.example.testcenter.model.dto.request.EmployeeInfoReq;
import com.example.testcenter.model.dto.response.EmployeeInfoResp;
import com.example.testcenter.model.enums.EmployeeStatus;
import com.example.testcenter.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final ObjectMapper objectMapper;
  //  private final LaboratoryService laboratoryService;

    private Employee getEmployeeFromDB (Long id){
        Optional <Employee> employeeFromDB = employeeRepository.findById(id);
        final String errMsg = String.format("employee with id : %s not found", id);
        return employeeFromDB.orElseThrow(() ->  new CommonBackendException(errMsg, HttpStatus.NOT_FOUND));
    }

    @Override
    public EmployeeInfoResp getEmployee(Long id) {
        Employee employeeFromDB = getEmployeeFromDB(id);
        return objectMapper.convertValue(employeeFromDB, EmployeeInfoResp.class);
    }


    @Override
    public EmployeeInfoResp addEmployee(EmployeeInfoReq employeeInfoReq) {

        employeeRepository.findFirstByEmailAndLastName(employeeInfoReq.getEmail(), employeeInfoReq.getLastName()).ifPresent(
                employee -> {
                    throw new CommonBackendException("Employee already exist", HttpStatus.CONFLICT);
                });
//        if (laboratoryService.getLaboratoryFromDB(employeeInfoReq.getLaboratory().getId()).getStatus() == LaboratoryStatus.LIQUIDATED ){
//            throw new CommonBackendException("Laboratory LIQUIDATED ", HttpStatus.CONFLICT);
//        }

        Employee employee = objectMapper.convertValue(employeeInfoReq, Employee.class);
        employee.setStatus(EmployeeStatus.CREATED);

        Employee employeeSaved = employeeRepository.save(employee);
        return objectMapper.convertValue(employeeSaved, EmployeeInfoResp.class);
    }


    @Override
    public EmployeeInfoResp updateEmployee(Long id, EmployeeInfoReq employeeInfoReq) {
//        if (laboratoryService.getLaboratoryFromDB(employeeInfoReq.getLaboratory().getId()).getStatus() == LaboratoryStatus.LIQUIDATED ){
//            throw new CommonBackendException("Laboratory LIQUIDATED ", HttpStatus.CONFLICT);
//        }

        Employee employeeFromDB = getEmployeeFromDB(id);
        Employee employeeForUpdate = objectMapper.convertValue(employeeInfoReq, Employee.class);

        employeeFromDB.setEmail( employeeForUpdate.getEmail() == null ? employeeFromDB.getEmail() : employeeForUpdate.getEmail());
        employeeFromDB.setFirstName( employeeForUpdate.getFirstName() == null ? employeeFromDB.getFirstName() : employeeForUpdate.getFirstName());
        employeeFromDB.setLastName( employeeForUpdate.getLastName() == null ? employeeFromDB.getLastName() : employeeForUpdate.getLastName());
        employeeFromDB.setMiddleName( employeeForUpdate.getMiddleName() == null ? employeeFromDB.getMiddleName() : employeeForUpdate.getMiddleName());
        employeeFromDB.setPost( employeeForUpdate.getPost() == null ? employeeFromDB.getPost() : employeeForUpdate.getPost());
        employeeFromDB.setLaboratory(employeeForUpdate.getLaboratory() == null ? employeeFromDB.getLaboratory() : employeeForUpdate.getLaboratory());

        employeeFromDB.setStatus(EmployeeStatus.UPDATED);
        employeeFromDB = employeeRepository.save(employeeFromDB);
        return objectMapper.convertValue(employeeFromDB, EmployeeInfoResp.class);
    }


    @Override
    public void deleteEmployee(Long id) {
        Employee employeeFromDB = getEmployeeFromDB(id);
        employeeFromDB.setStatus(EmployeeStatus.DISMISSED);
        employeeRepository.save(employeeFromDB);
    }


    @Override
    public List<EmployeeInfoResp> getAllEmployee() {
        return employeeRepository.findAll().stream().map(employee -> objectMapper.convertValue(employee, EmployeeInfoResp.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<Employee> getEmployeesByLabId(Long id) {
        List <Employee> listEmpl =  employeeRepository.findEmployeesByLaboratory_Id(id);
        return listEmpl;
    }


}
