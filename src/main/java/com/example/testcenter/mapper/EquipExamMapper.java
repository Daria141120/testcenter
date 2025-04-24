package com.example.testcenter.mapper;

import com.example.testcenter.model.db.entity.EquipExam2;
import com.example.testcenter.model.dto.response.EquipExam2InfoResp;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface EquipExamMapper {

    EquipExam2InfoResp toEquipExam2InfoResp (EquipExam2 equipExam);

    List<EquipExam2InfoResp> toEquipExam2InfoRespList (List<EquipExam2> equipExam2List);
}

