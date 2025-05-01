package com.example.testcenter.service.impl;


import com.example.testcenter.exception.CommonBackendException;
import com.example.testcenter.model.db.entity.EquipExam2;
import com.example.testcenter.model.db.entity.Equipment;
import com.example.testcenter.model.db.repository.EquipmentRepository;
import com.example.testcenter.model.dto.request.EquipExam2InfoReq;
import com.example.testcenter.model.dto.request.EquipmentInfoReq;
import com.example.testcenter.model.dto.response.EquipExam2InfoResp;
import com.example.testcenter.model.dto.response.EquipmentInfoResp;
import com.example.testcenter.model.dto.response.ExamInfoResp;
import com.example.testcenter.model.enums.EquipStatus;
import com.example.testcenter.service.EquipExam2Service;
import com.example.testcenter.service.EquipmentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EquipmentServiceImpl implements EquipmentService {

    private final EquipmentRepository equipmentRepository;
    private final ObjectMapper objectMapper;
    private final EquipExam2Service equipExam2Service;

    @Override
    public Equipment getEquipmentFromDB(Long id){
        Optional<Equipment> equipmentFromDB = equipmentRepository.findById(id);
        final String errMsg = String.format("equipmentId with id : %s not found", id);
        return equipmentFromDB.orElseThrow(() -> new CommonBackendException(errMsg, HttpStatus.NOT_FOUND));
    }


    @Override
    public EquipmentInfoResp getEquipment(Long id) {
        Equipment equipmentFromDB = getEquipmentFromDB(id);
        return objectMapper.convertValue(equipmentFromDB, EquipmentInfoResp.class);
    }


    @Override
    public EquipmentInfoResp addEquipment(EquipmentInfoReq req) {
        equipmentRepository.findFirstByName(req.getName()).ifPresent(
                equip -> { throw new CommonBackendException("Equipment already exist", HttpStatus.CONFLICT);
                });

        Equipment equipment = objectMapper.convertValue(req, Equipment.class);
        equipment.setStatus(EquipStatus.CREATED);

        equipment = equipmentRepository.save(equipment);
        return objectMapper.convertValue(equipment, EquipmentInfoResp.class);
    }


    @Override
    public EquipmentInfoResp updateEquipment(Long id, EquipmentInfoReq req) {
        Equipment equipmentFromDB = getEquipmentFromDB(id);
        Equipment equipmentForUpdate = objectMapper.convertValue(req, Equipment.class);

        equipmentFromDB.setName(equipmentForUpdate.getName() == null ? equipmentFromDB.getName() : equipmentForUpdate.getName());
        equipmentFromDB.setTypeEquipment(equipmentForUpdate.getTypeEquipment() == null ? equipmentFromDB.getTypeEquipment() : equipmentForUpdate.getTypeEquipment());
        equipmentFromDB.setStatus(EquipStatus.UPDATED);

        equipmentFromDB = equipmentRepository.save(equipmentFromDB);
        return objectMapper.convertValue(equipmentFromDB, EquipmentInfoResp.class);
    }

    @Override
    public void deleteEquipment(Long id) {
        Equipment equipment = getEquipmentFromDB(id);
        equipment.setStatus(EquipStatus.DELETED);
        equipmentRepository.save(equipment);
    }

    @Override
    public List<EquipmentInfoResp> getAllEquipment() {
        return equipmentRepository.findAll().stream().map(equip -> objectMapper.convertValue(equip, EquipmentInfoResp.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<ExamInfoResp> getEquipmentExams(Long id) {
        Equipment equipment = getEquipmentFromDB(id);

        List <EquipExam2> equipExamList = equipment.getEquipExam2List();
        List <ExamInfoResp> examInfoRespList = equipExamList.stream()
                .map(equipExam2 -> objectMapper.convertValue(equipExam2.getExam(), ExamInfoResp.class))
                .collect(Collectors.toList());

        return examInfoRespList;
    }

    @Override
    public List<EquipExam2InfoResp> addEquipmentExams(Long id, List<ExamInfoResp> examsRespList) {
        Equipment equipFromDB = getEquipmentFromDB(id);
        EquipmentInfoResp equipResp = objectMapper.convertValue(equipFromDB, EquipmentInfoResp.class);

        List<EquipExam2InfoResp> respList = new ArrayList<>();

        EquipExam2InfoReq equipExamReq = new EquipExam2InfoReq();
        equipExamReq.setEquipment(equipResp);

        examsRespList.forEach(
                examInfoResp -> {
                  equipExamReq.setExam(examInfoResp);
                  EquipExam2InfoResp resp =  equipExam2Service.addEquipExam(equipExamReq);
                  respList.add(resp);
                }
        );
        return respList;
    }


}
