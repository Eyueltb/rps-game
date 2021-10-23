package com.rps.controllers;

import com.rps.services.TokenService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor

@CrossOrigin(origins = "*", allowedHeaders = "*")

public class TokenController {
    TokenService tokenService;
}
