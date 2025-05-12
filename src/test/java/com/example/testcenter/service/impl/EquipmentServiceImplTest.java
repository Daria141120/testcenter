package com.example.testcenter.service.impl;

import com.example.testcenter.exception.CommonBackendException;
import com.example.testcenter.model.db.entity.EquipExam2;
import com.example.testcenter.model.db.entity.Equipment;
import com.example.testcenter.model.db.repository.EquipmentRepository;
import com.example.testcenter.model.dto.request.EquipmentInfoReq;
import com.example.testcenter.model.dto.response.EquipExam2InfoResp;
import com.example.testcenter.model.dto.response.EquipmentInfoResp;
import com.example.testcenter.model.dto.response.ExamInfoResp;
import com.example.testcenter.model.enums.EquipStatus;
import com.example.testcenter.model.enums.TypeEquipment;
import com.example.testcenter.service.EquipExam2Service;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class EquipmentServiceImplTest {

    @InjectMocks
    EquipmentServiceImpl equipmentService;

    @Mock
    private EquipmentRepository equipmentRepository;

    @Spy
    private ObjectMapper objectMapper;

    @Mock
    private EquipExam2Service equipExam2Service;


    @Test
    public void getEquipmentFromDB() {
        Equipment equipment = new Equipment();
        equipment.setId(1L);

        when(equipmentRepository.findById(anyLong())).thenReturn(Optional.of(equipment));
        Equipment equipmentFromDB = equipmentService.getEquipmentFromDB(equipment.getId());
        assertEquals(equipment.getId(), equipmentFromDB.getId());
    }

    @Test(expected = CommonBackendException.class)
    public void getEquipmentFromDBNotFound() {
        equipmentService.getEquipmentFromDB(1L);
    }

    @Test
    public void getEquipment() {
        Equipment equip = new Equipment();
        equip.setId(1L);

        when(equipmentRepository.findById(equip.getId())).thenReturn(Optional.of(equip));
        EquipmentInfoResp equipResp = equipmentService.getEquipment(equip.getId());
        assertEquals(equip.getId(), equipResp.getId());
    }

    @Test
    public void addEquipment() {
        EquipmentInfoReq req = new EquipmentInfoReq();
        req.setName("name");
        Equipment equipment = new Equipment();
        equipment.setName(req.getName());

        when(equipmentRepository.save(any(Equipment.class))).thenReturn(equipment);
        EquipmentInfoResp equipResp = equipmentService.addEquipment(req);
        assertEquals(req.getName(), equipResp.getName());
    }

    @Test(expected = CommonBackendException.class)
    public void addEquipmentExist() {
        EquipmentInfoReq req = new EquipmentInfoReq();
        req.setName("name");

        when(equipmentRepository.findFirstByName(req.getName())).thenReturn(Optional.of(new Equipment()));
        equipmentService.addEquipment(req);
    }

    @Test
    public void updateEquipment() {
        EquipmentInfoReq req = new EquipmentInfoReq();
        req.setName("newName");
        req.setTypeEquipment(TypeEquipment.ELECTRICAL);
        Equipment equipFromDB = new Equipment();
        equipFromDB.setId(1L);

        when(equipmentRepository.findById(anyLong())).thenReturn(Optional.of(equipFromDB));
        when(equipmentRepository.save(any(Equipment.class))).thenReturn(equipFromDB);
        EquipmentInfoResp equipResp = equipmentService.updateEquipment(equipFromDB.getId(), req);
        assertEquals(req.getName(), equipResp.getName());
        assertEquals(EquipStatus.UPDATED, equipFromDB.getStatus());
    }

    @Test
    public void deleteEquipment() {
        Equipment equipFromDB = new Equipment();
        equipFromDB.setId(1L);

        when(equipmentRepository.findById(anyLong())).thenReturn(Optional.of(equipFromDB));
        equipmentService.deleteEquipment(equipFromDB.getId());
        assertEquals(EquipStatus.DELETED, equipFromDB.getStatus());
        verify(equipmentRepository, times(1)).save(any(Equipment.class));
    }

    @Test
    public void getAllEquipment() {
        Equipment equip1 = new Equipment();
        Equipment equip2 = new Equipment();
        equip1.setId(1L);
        equip2.setId(1L);
        List<Equipment> equipList = List.of(equip1, equip2);

        when(equipmentRepository.findAll()).thenReturn(equipList);
        List<EquipmentInfoResp> equipRespList = equipmentService.getAllEquipment();
        assertEquals(equipList.size(), equipRespList.size());
    }

    @Test
    public void getEquipmentExams() {
        List<EquipExam2> equipExamList = List.of(new EquipExam2(), new EquipExam2());

        Equipment equip = new Equipment();
        equip.setId(1L);
        equip.setEquipExam2List(equipExamList);

        when(equipmentRepository.findById(anyLong())).thenReturn(Optional.of(equip));
        List<ExamInfoResp> respList = equipmentService.getEquipmentExams(equip.getId());
        assertEquals(equip.getEquipExam2List().size(), respList.size());
    }


    @Test
    public void addEquipmentExams() {
        ExamInfoResp examResp1 = new ExamInfoResp();
        ExamInfoResp examResp2 = new ExamInfoResp();
        examResp1.setId(1L);
        examResp2.setId(1L);
        List<ExamInfoResp> examRespList = List.of(examResp1, examResp2);

        Equipment equip = new Equipment();
        equip.setId(1L);

        when(equipmentRepository.findById(anyLong())).thenReturn(Optional.of(equip));
        List<EquipExam2InfoResp> equipExamRespList = equipmentService.addEquipmentExams(equip.getId(), examRespList);

        assertEquals(examRespList.size(), equipExamRespList.size());
        equipExamRespList.forEach(equipExamResp -> {
        });
    }
}















