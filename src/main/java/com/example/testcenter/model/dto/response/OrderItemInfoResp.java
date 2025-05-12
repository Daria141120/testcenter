package com.example.testcenter.model.dto.response;


import com.example.testcenter.model.dto.request.OrderItemInfoReq;
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
public class OrderItemInfoResp extends OrderItemInfoReq {

    @Schema(description = "id")
    private Long id;

}
