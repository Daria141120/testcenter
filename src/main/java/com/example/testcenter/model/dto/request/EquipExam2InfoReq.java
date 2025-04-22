package com.example.testcenter.model.dto.request;

import com.example.testcenter.model.dto.response.EquipmentInfoResp;
import com.example.testcenter.model.dto.response.ExamInfoResp;
import com.example.testcenter.model.enums.Availability;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
//@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class EquipExam2InfoReq {

    @Schema(description = "Оборуование")
    @NotNull(message = "Оборудование должно быть заполнено")
    private EquipmentInfoResp equipment;


    @Schema(description = "Испытание")
    @NotNull(message = "Испытание должно быть заполнено")
    private ExamInfoResp exam;


}
