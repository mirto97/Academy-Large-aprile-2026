package com.academy.primo_progetto.component;

import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

@Component
public class SalutoComplesso {
    
    @PostConstruct
    public void init() {
        System.out.println("Bean inizializzato!");
    }

    @PreDestroy
    public void cleanUp() {
        System.out.println("Bean distrutto!");
    }

}
