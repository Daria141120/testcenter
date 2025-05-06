package com.example.testcenter.model.dto.auth;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@Schema(description = "Request for login")
public class JwtRequest {

    @Schema(description = "email")
    @NotEmpty(message = "Email должен быть заполнен")
    private String username;

    @Schema(description = "password")
    @NotEmpty(message = "Пароль должен быть заполнен")
    private String password;

}
