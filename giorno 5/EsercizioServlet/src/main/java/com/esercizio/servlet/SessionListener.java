package com.esercizio.servlet;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/*
 * ESERCIZIO 64 — SessionListener
 *
 * Implementa HttpSessionListener per intercettare la creazione e la
 * distruzione di ogni sessione HTTP nell'applicazione.
 *
 * Viene invocato automaticamente dal container (Tomcat) senza bisogno
 * di chiamarlo esplicitamente: basta registrarlo nel web.xml.
 *
 * Utile in produzione per: monitorare utenti connessi, liberare risorse
 * legate alla sessione (es. connessioni DB), audit di sicurezza.
 */
public class SessionListener implements HttpSessionListener {

    @Override
    public void sessionCreated(HttpSessionEvent event) 
    {
        String sessionId = event.getSession().getId();
        System.out.println(">>> [SessionListener] Sessione CREATA   — ID: " + sessionId);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent event) 
    {
        String sessionId = event.getSession().getId();
        System.out.println(">>> [SessionListener] Sessione DISTRUTTA — ID: " + sessionId);
    }
}