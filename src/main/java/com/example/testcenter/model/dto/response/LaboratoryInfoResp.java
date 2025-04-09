package com.example.testcenter.model.dto.response;


import com.example.testcenter.model.dto.request.LaboratoryInfoReq;
import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LaboratoryInfoResp extends LaboratoryInfoReq {

    @Schema(description = "id")
    private Long id;

}
