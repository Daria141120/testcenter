package com.example.testcenter.model.dto.request;


import com.example.testcenter.model.dto.response.LaboratoryInfoResp;
import com.example.testcenter.model.enums.Post;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
//@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class EmployeeInfoReq {

    @NotEmpty(message = "Email должен быть заполнен") // лишнее?
    @Email(message = "должен иметь формат адреса электронной почты")
    @Schema(description = "email")
    private String email;

    @Schema(description = "Имя")
    private String firstName;

    @Schema(description = "Фамилия")
    private String lastName;

    @Schema(description = "Отчество")
    private String middleName;

    @Schema(description = "Должность")
    private Post post;

    @NotNull(message = "Лаборатория должна быть заполнена")
    @Schema(description = "Испытательная лаборатория")
    private LaboratoryInfoResp laboratory;

}
