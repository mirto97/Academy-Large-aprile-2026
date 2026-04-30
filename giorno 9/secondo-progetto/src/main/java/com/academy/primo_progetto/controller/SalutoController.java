package com.academy.primo_progetto.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.academy.primo_progetto.component.ContatoreBeanPrototype;
import com.academy.primo_progetto.component.ContatoreBeanSingleton;
import com.academy.primo_progetto.service.SalutoService;


@RestController
public class SalutoController {

    // field injection (come javasifu), non è consigliato ma funziona lo stesso, è più semplice da scrivere ma meno chiaro e più difficile da testare.
    @Autowired
    private SalutoService salutoService;

    // CONSTRUCTOR INJECTION, è il più consigliato perché rende esplicite le dipendenze e facilita i test, ma è più verboso da scrivere.
    private ContatoreBeanSingleton contatore;
    private ContatoreBeanPrototype contProto;

    // @Autowired      (andrebbe qua, però Spring lo capisce anche senza perché c'è un solo costruttore, in caso di più costruttori serve per indicare a Spring quale usare)
    public SalutoController(ContatoreBeanPrototype contProto, ContatoreBeanSingleton contatore) {
        this.contProto = contProto;
        this.contatore = contatore;
    }   


    // setter injection: Spring chiama questo metodo dopo aver creato il bean.
    // È utile quando la dipendenza è opzionale o può essere cambiata dopo la creazione.
    // Meno consigliato del costruttore, ma più esplicito del field injection.
    private SalutoService salutoServiceSetter;

    @Autowired
    public void setSalutoServiceSetter(SalutoService salutoService) {
        this.salutoServiceSetter = salutoService;
    }

    @GetMapping("/saluto-setter")
    public String salutoConSetter() {
        return "Setter injection -> " + salutoServiceSetter.getSaluto();
    }


    // --------------------------------------------------------------

    @GetMapping("/contatore")
    public String getContatore() {
        return "Contatore: " + contatore.incrementa();
    }

    // ogni volta che chiamo questo endpoint, mi restituisce un numero diverso perché è un bean prototype, mentre il bean singleton mi restituisce sempre lo stesso numero incrementato di 1.  
    @GetMapping("/contatore-prototype")
    public String getContatorePrototype() {
        return "Contatore Prototype: " + contProto.incrementa();
    }

    /*
    * =====================================================================
    * NOTE SUL DEPENDENCY INJECTION E GLI SCOPE IN SPRING
    * =====================================================================
    *
    * @Primary vs @Qualifier
    * ----------------------
    * Quando esistono più implementazioni di una stessa interfaccia, Spring
    * non sa quale iniettare e lancia un errore di ambiguità.
    * - @Primary si mette sull'implementazione: dice a Spring "usa questa
    *   per default ogni volta che qualcuno chiede questo tipo". È una
    *   scelta globale, definita lato bean.
    * - @Qualifier si mette nel punto di iniezione (costruttore, campo...):
    *   dice a Spring "qui voglio esattamente questo bean specifico".
    *   È una scelta locale, definita lato consumer.
    *   @Qualifier ha sempre la precedenza su @Primary.
    *
    * Singleton vs Prototype
    * ----------------------
    * Lo scope definisce quante istanze di un bean Spring crea e gestisce.
    * - Singleton (default): Spring crea UNA SOLA istanza del bean per
    *   tutto il contesto applicativo. Ogni classe che lo inietta riceve
    *   sempre lo stesso oggetto. Per questo il contatore in
    *   ContatoreBeanSingleton continua ad aumentare tra una richiesta
    *   e l'altra: è sempre lo stesso oggetto in memoria.
    * - Prototype: Spring crea una NUOVA istanza ogni volta che il bean
    *   viene richiesto. Per questo il contatore in ContatoreBeanPrototype
    *   riparte sempre da zero: ogni iniezione produce un oggetto fresco.
    *   Attenzione: iniettare un prototype in un singleton "congela" di
    *   fatto il prototype, perché il singleton viene creato una volta
    *   sola e non chiederà mai una nuova istanza.
    * =====================================================================
    */



    // giorno 8
    // --------------------------------------------------------------

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
        return salutoService.getSaluto();
    }
}
