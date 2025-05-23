package com.example.testcenter.service;

import com.example.testcenter.model.db.entity.Equipment;
import com.example.testcenter.model.dto.request.EquipmentInfoReq;
import com.example.testcenter.model.dto.response.EquipExam2InfoResp;
import com.example.testcenter.model.dto.response.EquipmentInfoResp;
import com.example.testcenter.model.dto.response.ExamInfoResp;

import java.util.List;

public interface EquipmentService {

    Equipment getEquipmentFromDB(Long id);

    EquipmentInfoResp getEquipment(Long id);

    EquipmentInfoResp addEquipment(EquipmentInfoReq equipmentInfoReq);

    EquipmentInfoResp updateEquipment(Long id, EquipmentInfoReq equipmentInfoReq);

    void deleteEquipment(Long id);

    List<EquipmentInfoResp> getAllEquipment();

    List<ExamInfoResp> getEquipmentExams(Long id);

    List<EquipExam2InfoResp> addEquipmentExams(Long id, List<ExamInfoResp> examsRespList);
}
