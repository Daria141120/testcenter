package com.example.testcenter.service;

import com.example.testcenter.model.dto.auth.JwtRequest;
import com.example.testcenter.model.dto.auth.JwtResponse;
import com.example.testcenter.model.dto.request.UserInfoReq;
import com.example.testcenter.model.dto.response.UserInfoResp;

import javax.validation.Valid;

public interface AuthService {
    JwtResponse login(JwtRequest loginRequest);

    UserInfoResp createNewUser(UserInfoReq req);
}
