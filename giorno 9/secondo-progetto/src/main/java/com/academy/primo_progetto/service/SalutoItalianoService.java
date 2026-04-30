package com.academy.primo_progetto.service;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
public class SalutoItalianoService implements SalutoService {
    
    @Override
    public String getSaluto() {
        return "Buongiorno!";
    }
}
