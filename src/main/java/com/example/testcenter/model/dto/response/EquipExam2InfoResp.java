package com.example.testcenter.model.dto.response;


import com.example.testcenter.model.db.entity.EquipExam2Key;
import com.example.testcenter.model.dto.request.EquipExam2InfoReq;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
//@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class EquipExam2InfoResp extends EquipExam2InfoReq {

    @Schema(description = "id")
    private EquipExam2Key id;

}
