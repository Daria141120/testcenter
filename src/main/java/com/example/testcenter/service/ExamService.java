package com.example.testcenter.service;

import com.example.testcenter.model.db.entity.Exam;
import com.example.testcenter.model.dto.request.ExamInfoReq;
import com.example.testcenter.model.dto.response.ExamInfoResp;

import javax.validation.Valid;
import java.util.List;

public interface ExamService {


    Exam getExamFromDB(Long id);

    ExamInfoResp getExam(Long id);

    ExamInfoResp addExam(@Valid ExamInfoReq examInfoReq);

    ExamInfoResp updateExam(Long id, ExamInfoReq examInfoReq);

    void deleteExam(Long id);

    List<ExamInfoResp> getAllExam();
}
