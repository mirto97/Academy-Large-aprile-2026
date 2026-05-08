package com.academy.esercizio.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordHasher {

    @Autowired // field injection per semplicità
    private PasswordEncoder passwordEncoder;

    public String hash(String pw) {
        return passwordEncoder.encode(pw);
    }
    
}
