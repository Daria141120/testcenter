package com.example.testcenter.service.impl;

import com.example.testcenter.exception.CommonBackendException;
import com.example.testcenter.model.db.entity.User;
import com.example.testcenter.model.dto.auth.JwtRequest;
import com.example.testcenter.model.dto.auth.JwtResponse;
import com.example.testcenter.model.dto.request.UserInfoReq;
import com.example.testcenter.model.dto.response.UserInfoResp;
import com.example.testcenter.security.JwtTokenProvider;
import com.example.testcenter.service.AuthService;
import com.example.testcenter.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;


    @Override
    public JwtResponse login(JwtRequest loginRequest) {
        JwtResponse jwtResponse = new JwtResponse();
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        } catch (BadCredentialsException e) {
            throw new CommonBackendException("некорректный логин и/или пароль", HttpStatus.UNAUTHORIZED);
        }
        User user = userService.getUserByUsername(loginRequest.getUsername());
        String accessToken = tokenProvider.createAccessToken(user.getUsername(), user.getRoles());
        String refreshToken = tokenProvider.createRefreshToken(user.getUsername());

        jwtResponse.setUsername(user.getUsername());
        jwtResponse.setAccessToken(accessToken);
        jwtResponse.setRefreshToken(refreshToken);
        return jwtResponse;
    }

    @Override
    public UserInfoResp createNewUser(UserInfoReq req) {
        return userService.createNewUser(req);
    }


    @Override
    public JwtResponse refresh(String refreshToken) {
        return tokenProvider.refreshUserTokens(refreshToken);
    }
}
