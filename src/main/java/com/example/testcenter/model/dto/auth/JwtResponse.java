package com.example.testcenter.model.dto.auth;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Request after login")
public class JwtResponse {

    private String username;
    private String accessToken;
    private String refreshToken;

}
