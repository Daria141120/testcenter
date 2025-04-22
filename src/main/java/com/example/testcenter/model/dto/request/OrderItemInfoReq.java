package com.example.testcenter.model.dto.request;


import com.example.testcenter.model.dto.response.ClientOrderInfoResp;
import com.example.testcenter.model.dto.response.EquipExam2InfoResp;
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
//@ToString
//@JsonIgnoreProperties(ignoreUnknown = true)

public class OrderItemInfoReq {

    @NotNull(message = "Заявка должна быть заполнена")
    @Schema(description = "Заявка")
    private ClientOrderInfoResp clientOrder;

    @Schema(description = "Связь оборудование-испытание")
    private EquipExam2InfoResp equipExam;

    @Schema(description = "Количество")
    private Integer quantity;

    @Schema(description = "Результат испытания")
    private String examResult;

    @Schema(description = "Дополнительная информация")
    private String info;


}
