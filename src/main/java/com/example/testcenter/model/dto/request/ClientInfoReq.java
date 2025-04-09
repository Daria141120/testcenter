package com.example.testcenter.model.dto.request;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
//@ToString
//@JsonIgnoreProperties(ignoreUnknown = true)
public class ClientInfoReq {

    @NotEmpty
    @Schema(description = "email")
    private String email;

    @Schema(description = "Телефон")
    private String phone;

    @Schema(description = "Имя")
    private String firstName;

    @Schema(description = "Фамилия")
    private String lastName;

    @Schema(description = "Отчество")
    private String middleName;


}
