package com.example.testcenter.model.dto.response;


import com.example.testcenter.model.dto.request.EquipExam2InfoReq;
import io.swagger.v3.oas.annotations.media.Schema;

public class EquipExam2InfoResp extends EquipExam2InfoReq {
    @Schema(description = "id")
    private Long id;


}
