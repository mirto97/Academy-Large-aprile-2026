package com.academy.primo_progetto.component;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AppConfig {

    @Value("${app.name}")
    private String nomeApp;

    @Value("${app.version}")
    private String versioneApp;

    @Value("${app.welcome-msg}")
    private String messaggioBenvenuto;

    @Value("${server.port}")
    private String serverPort;

    @Value("${spring.profiles.active}")
    private String profiloAttivo;

    // PostConstruct viene eseguito dopo che tutte le proprietà sono state iniettate con Value
    @PostConstruct
    public void stampaProprietà() {
        System.out.println("==============================================");
        System.out.println("       CONFIGURAZIONE APPLICAZIONE            ");
        System.out.println("==============================================");
        System.out.println("Nome app:    " + nomeApp);
        System.out.println("Versione:    " + versioneApp);
        System.out.println("Profilo attivo: " + profiloAttivo);
        System.out.println("Benvenuto:   " + messaggioBenvenuto);
        System.out.println("Porta:       " + serverPort);
        System.out.println("==============================================");
    }
}