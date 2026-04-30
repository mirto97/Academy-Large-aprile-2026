package com.academy.primo_progetto.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Qualifier("salutoInglese")
public class SalutoIngleseService implements SalutoService {
    
    @Override
    public String getSaluto() {
        return "Good morning!";
    }
}
