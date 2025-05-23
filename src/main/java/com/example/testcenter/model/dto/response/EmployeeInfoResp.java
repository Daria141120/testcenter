package com.example.testcenter.model.dto.response;

import com.example.testcenter.model.dto.request.EmployeeInfoReq;
import com.example.testcenter.model.enums.EmployeeStatus;
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
public class EmployeeInfoResp extends EmployeeInfoReq {

    @Schema(description = "id")
    private Long id;

    @Schema(description = "Статус сотрудника")
    private EmployeeStatus status;

}
