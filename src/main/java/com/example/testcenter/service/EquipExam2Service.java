package com.example.testcenter.service;

import com.example.testcenter.model.dto.request.EquipExam2InfoReq;
import com.example.testcenter.model.dto.response.EquipExam2InfoResp;
import com.example.testcenter.model.enums.Availability;

import java.util.List;

public interface EquipExam2Service {

    EquipExam2InfoResp addEquipExam(EquipExam2InfoReq req);

    List<EquipExam2InfoResp> getEquipExamAll();

    EquipExam2InfoResp getEquipExam(Long id_exam, Long id_eq);

    EquipExam2InfoResp updateEquipExamStatus(Long id, Long id2, String status);

    List<Availability> getAllEquipExamStatus();

}
