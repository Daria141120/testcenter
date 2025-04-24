package com.example.testcenter.model.dto.response;

import com.example.testcenter.model.dto.request.EquipmentInfoReq;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;


@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)

public class EquipmentInfoResp extends EquipmentInfoReq {

    @Schema(description = "id")
    private Long id;


}
