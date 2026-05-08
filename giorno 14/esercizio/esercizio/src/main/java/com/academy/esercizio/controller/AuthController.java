package com.academy.esercizio.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.academy.esercizio.entity.User;
import com.academy.esercizio.service.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Registrazione di nuovi utenti")
public class AuthController {

    @Autowired // field injection per semplicità
    private AuthService authService;

    @Operation(summary = "Registra un nuovo utente")
    @ApiResponse(responseCode = "201", description = "Utente registrato con successo")
    @ApiResponse(responseCode = "409", description = "Username già esistente")
    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(user));
    }
}
