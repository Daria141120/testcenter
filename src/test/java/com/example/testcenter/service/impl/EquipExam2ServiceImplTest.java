package com.example.testcenter.service.impl;

import com.example.testcenter.exception.CommonBackendException;
import com.example.testcenter.mapper.EquipExamMapper;
import com.example.testcenter.mapper.EquipExamMapperImpl;
import com.example.testcenter.model.db.entity.EquipExam2;
import com.example.testcenter.model.db.entity.EquipExam2Key;
import com.example.testcenter.model.db.entity.Equipment;
import com.example.testcenter.model.db.entity.Exam;
import com.example.testcenter.model.db.repository.EquipExam2Repository;
import com.example.testcenter.model.dto.request.EquipExam2InfoReq;
import com.example.testcenter.model.dto.response.EquipExam2InfoResp;
import com.example.testcenter.model.dto.response.EquipmentInfoResp;
import com.example.testcenter.model.dto.response.ExamInfoResp;
import com.example.testcenter.model.enums.Availability;
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
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EquipExam2ServiceImplTest {
    @InjectMocks
    EquipExam2ServiceImpl equipExamService;

    @Mock
    private EquipExam2Repository equipExam2Repository;

    @Spy
    private ObjectMapper objectMapper;

    @Spy
    private EquipExamMapperImpl equipExamMapper;// = new EquipExamMapperImpl();

    @Test
    public void getEquipExamFromDB() {
        EquipExam2Key key = new EquipExam2Key(1L,1L);
        EquipExam2 equipExam = new EquipExam2();
        equipExam.setId(key);

        when(equipExam2Repository.findById(any(EquipExam2Key.class))).thenReturn(Optional.of(equipExam));
        EquipExam2 equipExam2FromDB = equipExamService.getEquipExamFromDB(key.getEquipmentId(), key.getExamId());
        assertEquals(equipExam.getId(), equipExam2FromDB.getId());
    }

    @Test(expected = CommonBackendException.class)
    public void getEquipExamFromDBNotFound() {
        equipExamService.getEquipExamFromDB(1L, 1L);
    }

    @Test
    public void getEquipExam() {
        EquipExam2Key key = new EquipExam2Key(1L,1L);
        EquipExam2 equipExam = new EquipExam2();
        equipExam.setId(key);

        when(equipExam2Repository.findById(any(EquipExam2Key.class))).thenReturn(Optional.of(equipExam));
        EquipExam2InfoResp equipExamResp = equipExamService.getEquipExam(1L, 1L);
        assertEquals(equipExam.getId(), equipExamResp.getId());
    }

    @Test
    public void addEquipExam() {
        ExamInfoResp examResp = new ExamInfoResp();
        examResp.setId(1L);
        EquipmentInfoResp equipResp = new EquipmentInfoResp();
        examResp.setId(1L);
        EquipExam2InfoReq req = new EquipExam2InfoReq(equipResp, examResp);

        Exam exam = new Exam();
        exam.setId(examResp.getId());
        Equipment equipment = new Equipment();
        equipment.setId(equipResp.getId());

        EquipExam2 equipExam = new EquipExam2();
        equipExam.setId(new EquipExam2Key(equipResp.getId(),examResp.getId()));
        equipExam.setExam(exam);
        equipExam.setEquipment(equipment);

        when(equipExam2Repository.save(any(EquipExam2.class))).thenReturn(equipExam);
        EquipExam2InfoResp equipExamResp = equipExamService.addEquipExam(req);
        assertEquals(equipExam.getId(), equipExamResp.getId());
    }

    @Test(expected = CommonBackendException.class)
    public void addEquipExamExist() {
        ExamInfoResp examResp = new ExamInfoResp();
        examResp.setId(1L);
        EquipmentInfoResp equipResp = new EquipmentInfoResp();
        examResp.setId(1L);
        EquipExam2InfoReq req = new EquipExam2InfoReq(equipResp, examResp);

        when(equipExam2Repository.findById(any(EquipExam2Key.class))).thenReturn(Optional.of(new EquipExam2()));
        equipExamService.addEquipExam(req);
    }

    @Test
    public void updateEquipExamStatus() {
        String status = Availability.AVAILABLE.name();
        EquipExam2 equipExam = new EquipExam2();
        equipExam.setId(new EquipExam2Key(1L, 1L));

        when(equipExam2Repository.findById(any(EquipExam2Key.class))).thenReturn(Optional.of(equipExam));
        when(equipExam2Repository.save(any(EquipExam2.class))).thenReturn(equipExam);

        EquipExam2InfoResp equipExamResp = equipExamService.updateEquipExamStatus(1L, 1L, status);
        assertEquals(equipExam.getId(), equipExamResp.getId());
        assertEquals(Availability.valueOf(status), equipExamResp.getAvailability());
    }

    @Test(expected = CommonBackendException.class)
    public void updateEquipExamStatusNotCorrect() {
        String status = "QWERTY";
        equipExamService.updateEquipExamStatus(1L, 1L, status);
    }


    @Test
    public void getEquipExamAll() {
        EquipExam2 equipExam1 = new EquipExam2();
        EquipExam2 equipExam2 = new EquipExam2();
        List<EquipExam2> equipExamList = List.of(equipExam1, equipExam2);

        when(equipExam2Repository.findAll()).thenReturn(equipExamList);

        List<EquipExam2InfoResp> equipExamRespList = equipExamService.getEquipExamAll();
        assertEquals(equipExamList.size(), equipExamRespList.size());
    }


}