package com.example.testcenter.service.impl;

import com.example.testcenter.model.db.entity.User;
import com.example.testcenter.model.dto.auth.JwtRequest;
import com.example.testcenter.model.dto.auth.JwtResponse;
import com.example.testcenter.model.enums.Role;
import com.example.testcenter.security.JwtTokenProvider;
import com.example.testcenter.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.authentication.AuthenticationManager;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AuthServiceImplTest {

    @InjectMocks
    private AuthServiceImpl authService;

    @Mock
    private UserService userService;

    @Spy
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenProvider tokenProvider;

    @Test
    public void login() {
        JwtRequest jwtRequest = new JwtRequest();
        jwtRequest.setUsername("name");
        jwtRequest.setPassword("123");

        Set<Role> roles = new HashSet<>();
        roles.add(Role.ROLE_USER);

        User user = new User();
        user.setUsername(jwtRequest.getUsername());
        user.setRoles(roles);

        when(userService.getUserByUsername(user.getUsername())).thenReturn(user);
        JwtResponse jwtResponse = authService.login(jwtRequest);
        assertEquals(jwtRequest.getUsername(), jwtResponse.getUsername());
    }



}