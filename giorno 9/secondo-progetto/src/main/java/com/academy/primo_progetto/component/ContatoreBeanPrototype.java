package com.academy.primo_progetto.component;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE) // ogni volta che viene richiesto un nuovo bean, ne viene creato uno nuovo
public class ContatoreBeanPrototype {
    
    private int contatore = 0;

    public int incrementa() {
        contatore++;
        return contatore;
    }

    public int getContatore() {
        return contatore;
    }

}
