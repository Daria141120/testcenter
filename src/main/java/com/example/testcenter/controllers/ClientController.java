package com.example.testcenter.controllers;


import com.example.testcenter.service.ClientService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("clients")
@RequiredArgsConstructor
@Tag(name = "Клиенты")
public class ClientController {
    private final ClientService clientService;








}
