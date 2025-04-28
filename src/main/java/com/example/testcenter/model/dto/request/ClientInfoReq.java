package com.example.testcenter.model.dto.request;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
//@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class ClientInfoReq {

    @NotEmpty (message = "Email должен быть заполнен")
    @Email (message = "должен иметь формат адреса электронной почты")
    @Schema(description = "email")
    private String email;

    @NotEmpty (message = "Телефон должен быть заполнен")
    @Schema(description = "Телефон")
    private String phone;

    @Schema(description = "Имя")
    private String firstName;

    @Schema(description = "Фамилия")
    private String lastName;

    @Schema(description = "Отчество")
    private String middleName;


}
