package com.example.testcenter.controllers;


import com.example.testcenter.model.dto.auth.JwtRequest;
import com.example.testcenter.model.dto.auth.JwtResponse;
import com.example.testcenter.model.dto.request.UserInfoReq;
import com.example.testcenter.model.dto.response.UserInfoResp;
import com.example.testcenter.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Контроллер аутенитификации/авторизации")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "Вход пользователя")
    public JwtResponse login(@RequestBody @Valid JwtRequest loginRequest){
        return authService.login(loginRequest);
    }

    @PostMapping("/registration")
    @Operation(summary = "Регистрация пользователя")
    public UserInfoResp register(@RequestBody UserInfoReq req){
        return authService.createNewUser(req);
    }

    @PostMapping("/refresh")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Обновление пары токенов пользователя", security = @SecurityRequirement(name = AUTHORIZATION))
    public JwtResponse refresh(@RequestBody String refreshToken) {
        return authService.refresh(refreshToken);
    }


}
