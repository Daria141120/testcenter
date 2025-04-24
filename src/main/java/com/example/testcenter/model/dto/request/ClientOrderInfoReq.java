package com.example.testcenter.model.dto.request;


import com.example.testcenter.model.dto.response.ClientInfoResp;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString
//@JsonIgnoreProperties(ignoreUnknown = true)
public class ClientOrderInfoReq {

    @NotNull(message = "Клиент должен быть заполнен")
    @Schema(description = "Клиент")
    private ClientInfoResp client;




}
