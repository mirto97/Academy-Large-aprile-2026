package com.academy.primo_progetto.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Qualifier("inglese") // così gli do il nome corto ma potrei usare semplicemente il nome completo della classe con la prima lettera minuscola, quindi "salutoIngleseService"
public class SalutoIngleseService implements SalutoService {
    
    @Override
    public String getSaluto() {
        return "Good morning!";
    }
}
