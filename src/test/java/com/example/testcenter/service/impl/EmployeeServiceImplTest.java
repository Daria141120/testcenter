package com.example.testcenter.service.impl;

import com.example.testcenter.exception.CommonBackendException;
import com.example.testcenter.mapper.EmployeeMapper;
import com.example.testcenter.mapper.EmployeeMapperImpl;
import com.example.testcenter.model.db.entity.Employee;
import com.example.testcenter.model.db.entity.Laboratory;
import com.example.testcenter.model.db.repository.EmployeeRepository;
import com.example.testcenter.model.dto.request.EmployeeInfoReq;
import com.example.testcenter.model.dto.response.EmployeeInfoResp;
import com.example.testcenter.model.dto.response.LaboratoryInfoResp;
import com.example.testcenter.model.enums.EmployeeStatus;
import com.example.testcenter.model.enums.LaboratoryStatus;
import com.example.testcenter.model.enums.Post;
import com.example.testcenter.service.LaboratoryService;
import com.example.testcenter.service.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class EmployeeServiceImplTest {

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    @Spy
    private ObjectMapper objectMapper;

    @Spy
    private EmployeeMapper employeeMapper = new EmployeeMapperImpl();

    @Mock
    private LaboratoryService laboratoryService;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private TaskService taskService;

    @Test
    public void getEmployeeFromDB() {
        Employee employee = new Employee();
        employee.setId(1L);
        employee.setEmail("testEmployee@mail.ru");

        when(employeeRepository.findById(employee.getId())).thenReturn(Optional.of(employee));
        Employee employeeFromDB = employeeService.getEmployeeFromDB(employee.getId());
        assertEquals(employee.getEmail(), employeeFromDB.getEmail());
    }

    @Test(expected = CommonBackendException.class)
    public void getEmployeeFromDBNotFound() {
        employeeService.getEmployeeFromDB(1L);
    }

    @Test
    public void getEmployee() {
        Employee employee = new Employee();
        employee.setId(1L);
        employee.setEmail("testEmployee@mail.ru");

        when(employeeRepository.findById(employee.getId())).thenReturn(Optional.of(employee));
        EmployeeInfoResp employeeResp = employeeService.getEmployee(employee.getId());
        assertEquals(employee.getEmail(), employeeResp.getEmail());
    }

    @Test
    public void addEmployee() {
        LaboratoryInfoResp labResp = new LaboratoryInfoResp();
        labResp.setId(1L);

        EmployeeInfoReq req = new EmployeeInfoReq();
        req.setEmail("testEmployee@mail.ru");
        req.setLaboratory(labResp);

        Laboratory lab = new Laboratory();
        lab.setId(1L);
        lab.setEmployeeList(new ArrayList<>());
        lab.setStatus(LaboratoryStatus.CREATED);

        Employee employeeFromDB = new Employee();
        employeeFromDB.setEmail(req.getEmail());
        employeeFromDB.setLaboratory(lab);

        when(employeeRepository.save(any(Employee.class))).thenReturn(employeeFromDB);
        when(laboratoryService.getLaboratoryFromDB(lab.getId())).thenReturn(lab);
        EmployeeInfoResp employeeResp = employeeService.addEmployee(req);

        assertEquals(req.getEmail(), employeeResp.getEmail());
    }

    @Test(expected = CommonBackendException.class)
    public void addEmployeeExist() {
        EmployeeInfoReq req = new EmployeeInfoReq();
        req.setEmail("testEmployee@mail.ru");
        req.setLastName("Petrov");

        when(employeeRepository.findFirstByEmailAndLastName(anyString(), anyString())).thenReturn(Optional.of(new Employee()));
        employeeService.addEmployee(req);
    }

    @Test(expected = CommonBackendException.class)
    public void addEmployeeLabLiq() {
        LaboratoryInfoResp labResp = new LaboratoryInfoResp();
        labResp.setId(1L);
        labResp.setName("testLab");

        EmployeeInfoReq req = new EmployeeInfoReq();
        req.setLaboratory(labResp);

        Laboratory lab = new Laboratory();
        lab.setStatus(LaboratoryStatus.LIQUIDATED);

        when(laboratoryService.getLaboratoryFromDB(any(Long.class))).thenReturn(lab);
        employeeService.addEmployee(req);
    }

    @Test
    public void updateEmployee() {
        EmployeeInfoReq req = new EmployeeInfoReq();
        req.setFirstName("FN");
        req.setMiddleName("MN");
        req.setEmail("testEmployee@mail.ru");
        req.setPost(Post.ENGINEER);
        req.setLastName("LN");

        Employee employeeFromDB = new Employee();
        employeeFromDB.setId(1L);
        employeeFromDB.setFirstName("OldName");

        when(employeeRepository.findById(employeeFromDB.getId())).thenReturn(Optional.of(employeeFromDB));
        when(employeeRepository.save(any(Employee.class))).thenReturn(employeeFromDB);

        EmployeeInfoResp employeeResp = employeeService.updateEmployee(employeeFromDB.getId(), req);

        assertEquals(req.getFirstName(), employeeResp.getFirstName());
        assertEquals(req.getLastName(), employeeResp.getLastName());
        assertEquals(req.getMiddleName(), employeeResp.getMiddleName());
        assertEquals(req.getPost(), employeeResp.getPost());
        assertEquals(req.getEmail(), employeeResp.getEmail());
    }

    @Test
    public void updateEmployeeEmpty() {
        EmployeeInfoReq req = new EmployeeInfoReq();

        Employee employeeFromDB = new Employee();
        employeeFromDB.setId(1L);
        employeeFromDB.setFirstName("OldName");
        employeeFromDB.setLastName("LN");
        employeeFromDB.setMiddleName("MN");
        employeeFromDB.setEmail("testEmloyee@mail.ru");
        employeeFromDB.setPost(Post.ENGINEER);

        when(employeeRepository.findById(employeeFromDB.getId())).thenReturn(Optional.of(employeeFromDB));
        when(employeeRepository.save(any(Employee.class))).thenReturn(employeeFromDB);
        EmployeeInfoResp employeeResp = employeeService.updateEmployee(employeeFromDB.getId(), req);

        assertEquals(employeeFromDB.getFirstName(), employeeResp.getFirstName());
        assertEquals(employeeFromDB.getLastName(), employeeResp.getLastName());
        assertEquals(employeeFromDB.getMiddleName(), employeeResp.getMiddleName());
        assertEquals(employeeFromDB.getPost(), employeeResp.getPost());
        assertEquals(employeeFromDB.getEmail(), employeeResp.getEmail());
    }

    @Test
    public void deleteEmployee() {
        Employee employeeFromDB = new Employee();
        employeeFromDB.setId(1L);

        when(employeeRepository.findById(any(Long.class))).thenReturn(Optional.of(employeeFromDB));
        employeeService.deleteEmployee(employeeFromDB.getId());
        verify(employeeRepository, times(1)).save(any(Employee.class));
        assertEquals(EmployeeStatus.DISMISSED, employeeFromDB.getStatus());
    }


    @Test
    public void getAllEmployee() {
        Employee employee1 = new Employee();
        Employee employee2 = new Employee();
        employee1.setId(1L);
        employee2.setId(2L);
        List<Employee> employeeList = List.of(employee1, employee2);

        when(employeeRepository.findAll()).thenReturn(employeeList);
        List<EmployeeInfoResp> respList = employeeService.getAllEmployee("");
        assertEquals(employeeList.size(), respList.size());
    }

    @Test
    public void getAllEmployeeWithStatus() {
        String status = "ACTIVE";
        EmployeeStatus employeeStatus = EmployeeStatus.valueOf(status);
        Employee employee1 = new Employee();
        Employee employee2 = new Employee();
        employee1.setId(1L);
        employee2.setId(2L);
        employee1.setStatus(employeeStatus);
        employee2.setStatus(employeeStatus);
        List<Employee> employeeList = List.of(employee1, employee2);

        when(employeeRepository.findAllByStatus(any(EmployeeStatus.class))).thenReturn(employeeList);
        List<EmployeeInfoResp> respList = employeeService.getAllEmployee(status);
        assertEquals(employeeList.size(), respList.size());
        respList.forEach(employeeInfoResp -> assertEquals(employeeStatus, employeeInfoResp.getStatus()));
    }

    @Test(expected = CommonBackendException.class)
    public void getAllEmployeeNotCorrectStatus() {
        String status = "QAZXSW";
        employeeService.getAllEmployee(status);
    }

    @Test(expected = CommonBackendException.class)
    public void changeLabLiq() {
        LaboratoryInfoResp labResp = new LaboratoryInfoResp();
        labResp.setName("TestLab");
        labResp.setId(1L);

        Laboratory lab = new Laboratory();
        lab.setName(labResp.getName());
        lab.setId(labResp.getId());
        lab.setStatus(LaboratoryStatus.LIQUIDATED);

        when(laboratoryService.getLaboratoryFromDB(any(Long.class))).thenReturn(lab);
        employeeService.changeLab(lab.getId(), labResp);
    }

    @Test
    public void changeLab() {
        LaboratoryInfoResp labRespNew = new LaboratoryInfoResp();
        labRespNew.setId(1L);

        Laboratory labOld = new Laboratory();
        labOld.setEmployeeList(new ArrayList<>());

        Employee employeeFromDBOlb = new Employee();
        employeeFromDBOlb.setId(1L);
        employeeFromDBOlb.setLaboratory(labOld);

        Laboratory labNew = new Laboratory();
        List<Employee> employeeList = new ArrayList<>();
        labNew.setId(labRespNew.getId());
        labNew.setStatus(LaboratoryStatus.CREATED);
        labNew.setEmployeeList(employeeList);

        Employee employeeFromDBNew = new Employee();
        employeeFromDBNew.setId(employeeFromDBOlb.getId());
        employeeFromDBNew.setLaboratory(labNew);

        when(employeeRepository.findById(any(Long.class))).thenReturn(Optional.of(employeeFromDBOlb));
        when(laboratoryService.getLaboratoryFromDB(any(Long.class))).thenReturn(labNew);
        when(employeeRepository.save(any(Employee.class))).thenReturn(employeeFromDBNew);
        EmployeeInfoResp employeeResp = employeeService.changeLab(labNew.getId(), labRespNew);
        assertEquals(employeeResp.getLaboratory().getId(), labRespNew.getId());
    }

}