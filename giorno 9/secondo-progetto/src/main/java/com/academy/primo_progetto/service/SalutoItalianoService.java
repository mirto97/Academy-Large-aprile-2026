package com.academy.primo_progetto.service;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary // ricorda che qualifier ha la priorità su primary, quindi se c'è un qualifier specifico per un bean, Spring lo userà invece del bean contrassegnato come primary.
public class SalutoItalianoService implements SalutoService {
    
    @Override
    public String getSaluto() {
        return "Buongiorno!";
    }
}
