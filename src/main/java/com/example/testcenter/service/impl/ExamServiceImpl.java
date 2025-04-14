package com.example.testcenter.service.impl;

import com.example.testcenter.exception.CommonBackendException;
import com.example.testcenter.model.db.entity.Exam;
import com.example.testcenter.model.db.entity.Laboratory;
import com.example.testcenter.model.enums.ExamStatus;
import com.example.testcenter.model.enums.LaboratoryStatus;
import com.example.testcenter.service.LaboratoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.testcenter.model.db.repository.ExamRepository;
import com.example.testcenter.model.dto.request.ExamInfoReq;
import com.example.testcenter.model.dto.response.ExamInfoResp;
import com.example.testcenter.service.ExamService;
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
public class ExamServiceImpl implements ExamService {

    private final ExamRepository examRepository;
    private final ObjectMapper objectMapper;
    private final LaboratoryService laboratoryService;

    private Exam getExamFromDB(Long id) {
        Optional<Exam>  examFromDB = examRepository.findById(id);
        final String errMsg = String.format("exam with id : %s not found", id);
        return examFromDB.orElseThrow(() ->  new CommonBackendException(errMsg, HttpStatus.NOT_FOUND));
    }


    @Override
    public ExamInfoResp getExam(Long id) {
        Exam examFromDB = getExamFromDB(id);
        return objectMapper.convertValue(examFromDB, ExamInfoResp.class);
    }


    @Override
    public ExamInfoResp addExam(ExamInfoReq examInfoReq) {
        examRepository.findFirsByName(examInfoReq.getName()).ifPresent(
                exam -> { throw new CommonBackendException("Exam already exist", HttpStatus.CONFLICT);
                });
        if (laboratoryService.getLaboratoryFromDB(examInfoReq.getLaboratory().getId()).getStatus() == LaboratoryStatus.LIQUIDATED ){
            throw new CommonBackendException("Laboratory LIQUIDATED ", HttpStatus.CONFLICT);
        }

        Exam exam = objectMapper.convertValue(examInfoReq, Exam.class);
        exam.setStatus(ExamStatus.CREATED);

        Exam examSaved = examRepository.save(exam);
        return objectMapper.convertValue(examSaved, ExamInfoResp.class);
    }


    @Override
    public ExamInfoResp updateExam(Long id, ExamInfoReq examInfoReq) {
        if (laboratoryService.getLaboratoryFromDB(examInfoReq.getLaboratory().getId()).getStatus() == LaboratoryStatus.LIQUIDATED ){
            throw new CommonBackendException("Laboratory LIQUIDATED ", HttpStatus.CONFLICT);
        }

        Exam examFromDB = getExamFromDB(id);
        Exam examForUpdate = objectMapper.convertValue(examInfoReq, Exam.class);

        examFromDB.setName(examForUpdate.getName() == null ? examFromDB.getName() : examForUpdate.getName());
        examFromDB.setLaboratory(examForUpdate.getLaboratory() == null ? examFromDB.getLaboratory() : examForUpdate.getLaboratory());

        examFromDB.setStatus(ExamStatus.UPDATED);
        examFromDB = examRepository.save(examFromDB);

        return objectMapper.convertValue(examFromDB, ExamInfoResp.class);
    }


    @Override
    public void deleteExam(Long id) {
        Exam examFromDB = getExamFromDB(id);
        examFromDB.setStatus(ExamStatus.DELETED);
        examRepository.save(examFromDB);
    }


    @Override
    public List<ExamInfoResp> getAllExam() {
        return examRepository.findAll().stream().map(exam -> objectMapper.convertValue(exam, ExamInfoResp.class))
                .collect(Collectors.toList());
    }



}
