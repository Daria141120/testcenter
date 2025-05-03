package com.example.testcenter.model.dto.request;

import com.example.testcenter.model.dto.response.EmployeeInfoResp;
import com.example.testcenter.model.dto.response.OrderItemInfoResp;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TaskInfoReq {

    @NotNull(message = "Элемент закакза должен быть заполнен")
    @Schema(description = "Элемент заказа")
    private OrderItemInfoResp orderItem;

    @Schema(description = "Сотрудник")
    private EmployeeInfoResp employee;


}
