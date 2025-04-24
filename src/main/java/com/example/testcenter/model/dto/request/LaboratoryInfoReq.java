package com.example.testcenter.model.dto.request;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotEmpty;
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
//@JsonIgnoreProperties(ignoreUnknown = true)
public class LaboratoryInfoReq {

    @NotEmpty
    @Schema(description = "Название")
    private String name;

    @Schema(description = "Описание")
    private String description;

}
