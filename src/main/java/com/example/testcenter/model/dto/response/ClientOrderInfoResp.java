package com.example.testcenter.model.dto.response;

import com.example.testcenter.model.dto.request.ClientOrderInfoReq;
import com.example.testcenter.model.enums.OrderStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;


@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ClientOrderInfoResp extends ClientOrderInfoReq {

    @Schema(description = "id")
    private Long id;

    @Schema(description = "Состояние заявки")
    private OrderStatus status;

    @Schema(description = "Номер заявки")
    private String OrderNumber;



}
