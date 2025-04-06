package com.hubspot.hubspot.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hubspot.hubspot.dto.AcessTokenResponseDto;
import com.hubspot.hubspot.services.AuthenticationService;

import io.micrometer.common.lang.Nullable;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @GetMapping("/")
    public ResponseEntity<String> urlAuthorization() {
        return ResponseEntity.ok(authenticationService.getUrlAuthorization());
    }


    @GetMapping("/callback")
    public ResponseEntity<AcessTokenResponseDto> callback(@RequestParam("code") String code, @Nullable @RequestParam("state") String state) {
        try {
            AcessTokenResponseDto acessTokenResponseDto = authenticationService.getAccessToken(code);
            return ResponseEntity.ok(acessTokenResponseDto);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }
    
    
}
