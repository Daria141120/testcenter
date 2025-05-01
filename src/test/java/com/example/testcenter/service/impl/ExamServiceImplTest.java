package com.example.testcenter.service.impl;

import com.example.testcenter.exception.CommonBackendException;
import com.example.testcenter.model.db.entity.Exam;
import com.example.testcenter.model.db.entity.Laboratory;
import com.example.testcenter.model.db.repository.ExamRepository;
import com.example.testcenter.model.dto.request.ExamInfoReq;
import com.example.testcenter.model.dto.response.ExamInfoResp;
import com.example.testcenter.model.dto.response.LaboratoryInfoResp;
import com.example.testcenter.model.enums.ExamStatus;
import com.example.testcenter.model.enums.LaboratoryStatus;
import com.example.testcenter.service.LaboratoryService;
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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ExamServiceImplTest {

    @InjectMocks
    ExamServiceImpl examService;

    @Mock
    private ExamRepository examRepository;

    @Spy
    private ObjectMapper objectMapper;

    @Mock
    private LaboratoryService laboratoryService;


    @Test
    public void getExamFromDB() {
        Exam exam = new Exam();
        exam.setId(1L);

        when(examRepository.findById(exam.getId())).thenReturn(Optional.of(exam));
        Exam examFromDB = examService.getExamFromDB(exam.getId());
        assertEquals(exam.getId(), examFromDB.getId());
    }

    @Test(expected = CommonBackendException.class)
    public void getExamFromDBNotFound() {
        examService.getExamFromDB(1L);
    }

    @Test
    public void getExam() {
        Exam exam = new Exam();
        exam.setId(1L);

        when(examRepository.findById(exam.getId())).thenReturn(Optional.of(exam));
        ExamInfoResp examResp = examService.getExam(exam.getId());
        assertEquals(exam.getId(), examResp.getId());
    }

    @Test
    public void addExam() {
        LaboratoryInfoResp labResp = new LaboratoryInfoResp();
        labResp.setId(1L);

        ExamInfoReq req = new ExamInfoReq();
        req.setName("testLab");
        req.setLaboratory(labResp);

        Laboratory lab = new Laboratory();
        lab.setId(labResp.getId());
        lab.setStatus(LaboratoryStatus.CREATED);

        Exam exam = new Exam();
        exam.setId(1L);
        exam.setLaboratory(lab);

        when(laboratoryService.getLaboratoryFromDB(anyLong())).thenReturn(lab);
        when(examRepository.save(any(Exam.class))).thenReturn(exam);
        ExamInfoResp examResp = examService.addExam(req);
        assertEquals(exam.getName(), examResp.getName());
    }

    @Test(expected = CommonBackendException.class)
    public void addExamExist() {
        ExamInfoReq req = new ExamInfoReq();
        req.setName("testLab");

        when(examRepository.findFirsByName(anyString())).thenReturn(Optional.of(new Exam()));
        examService.addExam(req);
    }

    @Test(expected = CommonBackendException.class)
    public void addExamLabLIQUIDATED() {
        LaboratoryInfoResp labResp = new LaboratoryInfoResp();
        labResp.setId(1L);

        ExamInfoReq req = new ExamInfoReq();
        req.setName("testLab");
        req.setLaboratory(labResp);

        Laboratory lab = new Laboratory();
        lab.setId(labResp.getId());
        lab.setStatus(LaboratoryStatus.LIQUIDATED);

        when(laboratoryService.getLaboratoryFromDB(anyLong())).thenReturn(lab);
        examService.addExam(req);
    }

    @Test
    public void updateExam() {
        LaboratoryInfoResp labResp = new LaboratoryInfoResp();
        labResp.setId(1L);
        labResp.setName("TestLab");

        ExamInfoReq req = new ExamInfoReq("ExamName", labResp);

        Exam examFromDB = new Exam();
        examFromDB.setId(1L);

        Laboratory lab = new Laboratory();
        lab.setId(labResp.getId());
        lab.setStatus(LaboratoryStatus.CREATED);

        when(laboratoryService.getLaboratoryFromDB(anyLong())).thenReturn(lab);
        when(examRepository.findById(examFromDB.getId())).thenReturn(Optional.of(examFromDB));
        when(examRepository.save(any(Exam.class))).thenReturn(examFromDB);
        ExamInfoResp examResp = examService.updateExam(examFromDB.getId(), req);
        assertEquals(req.getName(), examResp.getName());
        assertEquals(req.getLaboratory().getName(), examResp.getLaboratory().getName());
    }

    @Test
    public void updateExamEmpty() {
        LaboratoryInfoResp labResp = new LaboratoryInfoResp();
        labResp.setId(1L);
        labResp.setName("TestLab");

        ExamInfoReq req = new ExamInfoReq();
        req.setLaboratory(labResp);

        Laboratory lab = new Laboratory();
        lab.setName(labResp.getName());
        lab.setId(labResp.getId());
        lab.setStatus(LaboratoryStatus.UPDATED);

        Exam examFromDB = new Exam();
        examFromDB.setId(1L);
        examFromDB.setLaboratory(lab);
        examFromDB.setName("OldName");

        when(laboratoryService.getLaboratoryFromDB(anyLong())).thenReturn(lab);
        when(examRepository.findById(examFromDB.getId())).thenReturn(Optional.of(examFromDB));
        when(examRepository.save(any(Exam.class))).thenReturn(examFromDB);

        ExamInfoResp examResp = examService.updateExam(examFromDB.getId(), req);
        assertEquals(examFromDB.getName(), examResp.getName());
        assertEquals(examFromDB.getLaboratory().getName(), examResp.getLaboratory().getName());
    }


    @Test(expected = CommonBackendException.class)
    public void updateExamLabLIQUIDATED() {
        LaboratoryInfoResp labResp = new LaboratoryInfoResp();
        labResp.setId(1L);

        ExamInfoReq req = new ExamInfoReq();
        req.setName("testLab");
        req.setLaboratory(labResp);

        Laboratory lab = new Laboratory();
        lab.setId(labResp.getId());
        lab.setStatus(LaboratoryStatus.LIQUIDATED);

        when(laboratoryService.getLaboratoryFromDB(anyLong())).thenReturn(lab);
        examService.updateExam(anyLong(), req);
    }

    @Test
    public void deleteExam() {
        Exam exam = new Exam();
        exam.setId(1L);

        when(examRepository.findById(exam.getId())).thenReturn(Optional.of(exam));
        examService.deleteExam(exam.getId());
        verify(examRepository, times(1)).save(any(Exam.class));
        assertEquals(ExamStatus.DELETED, exam.getStatus());
    }

    @Test
    public void getAllExam() {
        Exam exam1 = new Exam();
        Exam exam2 = new Exam();
        exam1.setId(1L);
        exam2.setId(2L);
        List<Exam> examList = List.of(exam1, exam2);

        when(examRepository.findAll()).thenReturn(examList);
        List<ExamInfoResp> examRespList = examService.getAllExam();
        assertEquals(examList.size(), examRespList.size());
    }

}