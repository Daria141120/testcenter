package com.example.testcenter.service.impl;

import com.example.testcenter.exception.CommonBackendException;
import com.example.testcenter.model.db.entity.Employee;
import com.example.testcenter.model.db.entity.Laboratory;
import com.example.testcenter.model.db.repository.LaboratoryRepository;
import com.example.testcenter.model.dto.request.LaboratoryInfoReq;
import com.example.testcenter.model.dto.response.EmployeeInfoResp;
import com.example.testcenter.model.dto.response.LaboratoryInfoResp;
import com.example.testcenter.model.enums.LaboratoryStatus;
import com.example.testcenter.service.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class LaboratoryServiceImplTest {
    @InjectMocks
    private LaboratoryServiceImpl laboratoryService;

    @Spy
    private ObjectMapper objectMapper;

    @Mock
    private LaboratoryRepository laboratoryRepository;

    @Mock
    private TaskService taskService;

    @Test
    public void getLaboratoryFromDB() {
        Laboratory lab = new Laboratory();
        lab.setName("TestLab");
        lab.setId(1L);

        when(laboratoryRepository.findById(lab.getId())).thenReturn(Optional.of(lab));
        Laboratory labFromDB = laboratoryService.getLaboratoryFromDB(lab.getId());
        assertEquals(lab.getName(), labFromDB.getName());
    }

    @Test(expected = CommonBackendException.class)
    public void getLaboratoryFromDBNotFound() {
        laboratoryService.getLaboratoryFromDB(1L);
    }

    @Test
    public void getLaboratory() {
        Laboratory lab = new Laboratory();
        lab.setName("TestLab");
        lab.setId(1L);

        when(laboratoryRepository.findById(lab.getId())).thenReturn(Optional.of(lab));
        LaboratoryInfoResp labResp = laboratoryService.getLaboratory(lab.getId());
        assertEquals(lab.getName(), labResp.getName());
    }

    @Test
    public void addLaboratory() {
        LaboratoryInfoReq req = new LaboratoryInfoReq();
        req.setName("TestLab");

        Laboratory lab = new Laboratory();
        lab.setId(1L);
        lab.setName(req.getName());

        when(laboratoryRepository.save(any(Laboratory.class))).thenReturn(lab);
        LaboratoryInfoResp labResp = laboratoryService.addLaboratory(req);
        assertEquals(req.getName(), labResp.getName());
    }


    @Test(expected = CommonBackendException.class)
    public void addLaboratoryExist() {
        LaboratoryInfoReq req = new LaboratoryInfoReq();
        req.setName("TestLab");
        when(laboratoryRepository.findFirstByName(anyString())).thenReturn(Optional.of(new Laboratory()));
        laboratoryService.addLaboratory(req);
    }

    @Test
    public void updateLaboratory() {
        LaboratoryInfoReq req = new LaboratoryInfoReq();
        req.setName("NewName");
        req.setDescription("description");

        Laboratory lab = new Laboratory();
        lab.setId(1L);

        when(laboratoryRepository.findById(lab.getId())).thenReturn(Optional.of(lab));
        when(laboratoryRepository.save(any(Laboratory.class))).thenReturn(lab);
        LaboratoryInfoResp labResp = laboratoryService.updateLaboratory(lab.getId(), req);
        assertEquals(req.getName(), labResp.getName());
        assertEquals(req.getDescription(), labResp.getDescription());
    }


    @Test
    public void updateLaboratoryEmpty() {
        LaboratoryInfoReq req = new LaboratoryInfoReq();

        Laboratory lab = new Laboratory();
        lab.setId(1L);
        lab.setName("NewName");
        lab.setDescription("description");

        when(laboratoryRepository.findById(lab.getId())).thenReturn(Optional.of(lab));
        when(laboratoryRepository.save(any(Laboratory.class))).thenReturn(lab);
        LaboratoryInfoResp labResp = laboratoryService.updateLaboratory(lab.getId(), req);
        assertEquals(lab.getName(), labResp.getName());
        assertEquals(lab.getDescription(), labResp.getDescription());
    }

    @Test
    public void deleteLaboratory() {
        Laboratory lab = new Laboratory();
        lab.setId(1L);

        when(laboratoryRepository.findById(lab.getId())).thenReturn(Optional.of(lab));
        laboratoryService.deleteLaboratory(lab.getId());
        assertEquals(LaboratoryStatus.LIQUIDATED, lab.getStatus());
        verify(laboratoryRepository, times(1)).save(any(Laboratory.class));
    }

    @Test
    public void getAllLaboratory() {
        Laboratory lab1 = new Laboratory();
        Laboratory lab2 = new Laboratory();
        lab1.setId(1L);
        lab2.setId(2L);
        List<Laboratory> labList = List.of(lab1, lab2);

        when(laboratoryRepository.findAll()).thenReturn(labList);
        List<LaboratoryInfoResp> labRespList = laboratoryService.getAllLaboratory("");
        assertEquals(labList.size(), labRespList.size());
    }

    @Test
    public void getAllLaboratoryWithStatus() {
        String status = LaboratoryStatus.CREATED.name();
        LaboratoryStatus labStatus = LaboratoryStatus.valueOf(status);

        Laboratory lab1 = new Laboratory();
        Laboratory lab2 = new Laboratory();
        lab1.setId(1L);
        lab2.setId(2L);
        lab1.setStatus(labStatus);
        lab2.setStatus(labStatus);
        List<Laboratory> labList = List.of(lab1, lab2);

        when(laboratoryRepository.findAllByStatus(any(LaboratoryStatus.class))).thenReturn(labList);
        List<LaboratoryInfoResp> labRespList = laboratoryService.getAllLaboratory(status);
        assertEquals(labList.size(), labRespList.size());
        labRespList.forEach(labResp -> assertEquals(labStatus, labResp.getStatus()));
    }

    @Test(expected = CommonBackendException.class)
    public void getAllLaboratoryNotCorrectStatus() {
        String wrongStatus = "ASDFG";
        laboratoryService.getAllLaboratory(wrongStatus);
    }

    @Test
    public void getLaboratoryEmployees() {
        List<Employee> employeeList = List.of(new Employee(), new Employee());
        Laboratory lab = new Laboratory();
        lab.setId(1L);
        lab.setEmployeeList(employeeList);

        when(laboratoryRepository.findById(lab.getId())).thenReturn(Optional.of(lab));
        List<EmployeeInfoResp> employeeRespList = laboratoryService.getLaboratoryEmployees(lab.getId());
        assertEquals(lab.getEmployeeList().size(), employeeRespList.size());
    }

    @Test
    public void updateLabListEmployee() {
        Laboratory lab = new Laboratory();
        laboratoryService.updateLabListEmployee(lab);
        verify(laboratoryRepository, times(1)).save(lab);
    }

}










