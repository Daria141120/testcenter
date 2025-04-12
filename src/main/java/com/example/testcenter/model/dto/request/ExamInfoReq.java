package com.example.testcenter.model.dto.request;

import com.example.testcenter.model.dto.response.LaboratoryInfoResp;
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
public class ExamInfoReq {

    @NotEmpty
    @Schema(description = "Название")
    private String name;

    @NotNull(message = "Лаборатория должна быть заполнена")
    @Schema(description = "Испытательная лаборатория")
    private LaboratoryInfoResp laboratory;



}
