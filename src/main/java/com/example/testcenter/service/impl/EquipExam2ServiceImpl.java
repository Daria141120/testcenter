package com.example.testcenter.service.impl;

import com.example.testcenter.exception.CommonBackendException;
import com.example.testcenter.mapper.EquipExamMapper;
import com.example.testcenter.model.db.entity.EquipExam2;
import com.example.testcenter.model.db.entity.EquipExam2Key;
import com.example.testcenter.model.db.repository.EquipExam2Repository;
import com.example.testcenter.model.dto.request.EquipExam2InfoReq;
import com.example.testcenter.model.dto.response.EquipExam2InfoResp;
import com.example.testcenter.model.enums.Availability;
import com.example.testcenter.service.EquipExam2Service;
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
    private final EquipExamMapper equipExamMapper;

    public EquipExam2 getEquipExamFromDB(Long id_eq, Long id_exam) {
        EquipExam2Key key = new EquipExam2Key(id_eq, id_exam);
        Optional<EquipExam2> equipExam2FromDB = equipExam2Repository.findById(key);
        final String errMsg = String.format("equip-examId with id : %s not found", key);
        return equipExam2FromDB.orElseThrow(() -> new CommonBackendException(errMsg, HttpStatus.NOT_FOUND));
    }

    @Override
    public EquipExam2InfoResp getEquipExam(Long id_eq, Long id_exam) {
        EquipExam2 equipExamFromDB = getEquipExamFromDB(id_eq, id_exam);
        return equipExamMapper.toEquipExam2InfoResp(equipExamFromDB);
    }

    @Override
    public EquipExam2InfoResp addEquipExam(EquipExam2InfoReq req) {
        EquipExam2Key key = new EquipExam2Key(req.getEquipment().getId(), req.getExam().getId());
        equipExam2Repository.findById(key).ifPresent(equipExam2 -> {
            throw new CommonBackendException("EquipExam connection already exist", HttpStatus.CONFLICT);
        });

        EquipExam2 equipExam2 = objectMapper.convertValue(req, EquipExam2.class);
        equipExam2.setAvailability(Availability.AVAILABLE);
        EquipExam2 equipExam2Saved = equipExam2Repository.save(equipExam2);

        return equipExamMapper.toEquipExam2InfoResp(equipExam2Saved);
    }

    @Override
    public EquipExam2InfoResp updateEquipExamStatus(Long id_eq, Long id_exam, String status) {
        List<String> list = getAllEquipExamStatus().stream().map(Enum::name).collect(Collectors.toList());
        if (!list.contains(status)) {
            throw new CommonBackendException("Error in the status, there is no such status.", HttpStatus.BAD_REQUEST);
        }

        Availability availability = Availability.valueOf(status);

        EquipExam2 equipExam2FromDB = getEquipExamFromDB(id_eq, id_exam);
        equipExam2FromDB.setAvailability(availability);
        EquipExam2 equipExam2Saved = equipExam2Repository.save(equipExam2FromDB);

        return equipExamMapper.toEquipExam2InfoResp(equipExam2Saved);
    }

    @Override
    public List<EquipExam2InfoResp> getEquipExamAll() {
        List<EquipExam2> equipExam2List = equipExam2Repository.findAll();
        return equipExamMapper.toEquipExam2InfoRespList(equipExam2List);
    }

    @Override
    public List<Availability> getAllEquipExamStatus() {
        return Arrays.stream(Availability.values()).collect(Collectors.toList());
    }

}
