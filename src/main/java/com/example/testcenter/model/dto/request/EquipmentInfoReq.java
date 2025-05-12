package com.example.testcenter.model.dto.request;

import com.example.testcenter.model.enums.TypeEquipment;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotEmpty;

import lombok.*;
import lombok.experimental.SuperBuilder;


@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class EquipmentInfoReq {

    @NotEmpty
    @Schema(description = "Название")
    private String name;

    @Schema(description = "Тип")
    private TypeEquipment typeEquipment;


}
