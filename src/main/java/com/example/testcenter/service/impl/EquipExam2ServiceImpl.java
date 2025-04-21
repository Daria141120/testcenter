package com.example.testcenter.service.impl;

import com.example.testcenter.exception.CommonBackendException;
import com.example.testcenter.model.db.entity.EquipExam2;
import com.example.testcenter.model.db.entity.EquipExam2Key;
import com.example.testcenter.model.db.entity.Equipment;
import com.example.testcenter.model.db.entity.Exam;
import com.example.testcenter.model.db.repository.EquipExam2Repository;
import com.example.testcenter.model.dto.request.EquipExam2InfoReq;
import com.example.testcenter.model.dto.response.EquipExam2InfoResp;
import com.example.testcenter.model.dto.response.EquipmentInfoResp;
import com.example.testcenter.model.enums.Availability;
import com.example.testcenter.model.enums.EquipStatus;
import com.example.testcenter.model.enums.ExamStatus;
import com.example.testcenter.service.EquipExam2Service;
import com.example.testcenter.service.EquipmentService;
import com.example.testcenter.service.ExamService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EquipExam2ServiceImpl implements EquipExam2Service {

    private final EquipExam2Repository equipExam2Repository;
    private final ObjectMapper objectMapper;
//    private final EquipmentService equipmentService;
//    private final ExamService examService;


    private EquipExam2 getEquipExamFromDB (Long id_eq, Long id_exam){
        EquipExam2Key key = new EquipExam2Key(id_eq, id_exam);
        Optional<EquipExam2> equipExam2FromDB = equipExam2Repository.findById(key);
        final String errMsg = String.format("equip-examId with id : %s not found", key);
        return equipExam2FromDB.orElseThrow(()-> new CommonBackendException(errMsg, HttpStatus.NOT_FOUND));
    }


    @Override
    public EquipExam2InfoResp getEquipExam(Long id_eq, Long id_exam) {
        EquipExam2 equipExamFromDB = getEquipExamFromDB(id_eq, id_exam);
        EquipExam2InfoResp resp = objectMapper.convertValue(equipExamFromDB, EquipExam2InfoResp.class);
        resp.setEquipment(objectMapper.convertValue(equipExamFromDB.getEquipment(), EquipmentInfoResp.class));
        return resp;
    }



    @Override
    public EquipExam2InfoResp addEquipExam(EquipExam2InfoReq req) {
//        Equipment equipment = equipmentService.getEquipmentFromDB(req.getEquipment().getId());  //проверка что оборуд и испыт существует
//        Exam exam = examService.getExamFromDB(req.getExam().getId());

//        if (exam.getStatus() == ExamStatus.DELETED || equipment.getStatus() == EquipStatus.DELETED){
//            throw new CommonBackendException("Equipment or examId have status - DELETED", HttpStatus.BAD_REQUEST);
//        }

        EquipExam2Key key = new EquipExam2Key(req.getEquipment().getId(), req.getExam().getId());
        equipExam2Repository.findById(key).ifPresent(equipExam2 -> {
            throw new CommonBackendException("EquipExam connection already exist", HttpStatus.CONFLICT);
            });

        EquipExam2 equipExam2 = objectMapper.convertValue(req, EquipExam2.class);
        equipExam2.setAvailability(Availability.AVAILABLE);
        EquipExam2 equipExam2Saved = equipExam2Repository.save(equipExam2);

        EquipExam2InfoResp resp = objectMapper.convertValue(equipExam2Saved, EquipExam2InfoResp.class);
        resp.setEquipment(objectMapper.convertValue(equipExam2Saved.getEquipment(), EquipmentInfoResp.class));
        return resp;
    }



    @Override
    public EquipExam2InfoResp updateEquipExamStatus(Long id_eq, Long id_exam, String status) {
        List <String> list = getAllEquipExamStatus().stream().map(Enum::name).collect(Collectors.toList());
        if (!list.contains(status)){
            throw new CommonBackendException("Error in the status, there is no such status.", HttpStatus.BAD_REQUEST);
        }

        Availability availability = Availability.valueOf(status);

        EquipExam2 equipExam2FromDB = getEquipExamFromDB(id_eq, id_exam);
        equipExam2FromDB.setAvailability(availability);
        EquipExam2 equipExam2Saved = equipExam2Repository.save(equipExam2FromDB);

        EquipExam2InfoResp resp = objectMapper.convertValue(equipExam2Saved, EquipExam2InfoResp.class);
        resp.setEquipment(objectMapper.convertValue(equipExam2Saved.getEquipment(), EquipmentInfoResp.class));
        return resp;
    }


    @Override
    public List<EquipExam2InfoResp> getEquipExamAll() {

        return equipExam2Repository.findAll().stream().map(equipExam ->
                {
                    EquipmentInfoResp equipResp = objectMapper.convertValue(equipExam.getEquipment(), EquipmentInfoResp.class);
                    EquipExam2InfoResp equipExamResp = objectMapper.convertValue(equipExam, EquipExam2InfoResp.class);
                    equipExamResp.setEquipment(equipResp);
                    return equipExamResp;
                })
               .collect(Collectors.toList());
    }


    @Override
    public List<Availability> getAllEquipExamStatus() {
        return Arrays.stream(Availability.values()).collect(Collectors.toList());
    }




}
