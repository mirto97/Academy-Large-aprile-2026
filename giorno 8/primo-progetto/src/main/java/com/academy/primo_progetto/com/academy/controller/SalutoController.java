package com.academy.primo_progetto.com.academy.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class SalutoController {

    @Value("${app.name}")
    private String nomeApp;

    @Value("${app.version}")
    private String versioneApp;

    @Value("${app.welcome-msg}")    
    private String messaggioBenvenuto;

    @Value("${server.port}")
    private String serverPort;

    @GetMapping("/configurazione-server")
    public String serverConfig() {
        return "La porta è: " + serverPort;
    }

    @GetMapping("/app-info")    
    public String infoAppString() {
        return "App: " + nomeApp + ", Versione: " + versioneApp + ", Messaggio: " + messaggioBenvenuto;
    }

    @GetMapping("/saluto")
    public String saluto() {
        return "Ciao dal mio corso di Spring Boot!!!";
    }
}
