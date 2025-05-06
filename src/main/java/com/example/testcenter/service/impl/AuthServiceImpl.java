package com.example.testcenter.service.impl;

import com.example.testcenter.model.dto.auth.JwtRequest;
import com.example.testcenter.model.dto.auth.JwtResponse;
import com.example.testcenter.model.dto.request.UserInfoReq;
import com.example.testcenter.model.dto.response.UserInfoResp;
import com.example.testcenter.service.AuthService;
import com.example.testcenter.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserService userService;






    @Override
    public JwtResponse login(JwtRequest loginRequest) {
        return null;
    }

    @Override
    public UserInfoResp createNewUser(UserInfoReq req) {
        return userService.createNewUser(req);
    }


}
