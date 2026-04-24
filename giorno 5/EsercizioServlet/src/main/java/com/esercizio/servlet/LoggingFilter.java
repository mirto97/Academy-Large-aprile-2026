package com.esercizio.servlet;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/*
 * ESERCIZI 54 e 55 — LoggingFilter
 *
 * Intercetta TUTTE le richieste (pattern /*).
 * Per ogni richiesta stampa su console:
 *   - Timestamp
 *   - Metodo HTTP
 *   - URL richiesta
 *   - Tempo di esecuzione (esercizio 55)
 */
public class LoggingFilter implements Filter {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println(">>> LoggingFilter: init() chiamato!");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException 
    {
        // Cast a HttpServletRequest per leggere metodo e URL
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        String timestamp   = LocalDateTime.now().format(FORMATTER);
        String metodo      = httpRequest.getMethod();
        String url         = httpRequest.getRequestURL().toString();
        String queryString = httpRequest.getQueryString();
        if (queryString != null) {
            url += "?" + queryString;
        }

        System.out.println("--------------------------------------------");
        System.out.println("[LOG] Timestamp : " + timestamp);
        System.out.println("[LOG] Metodo    : " + metodo);
        System.out.println("[LOG] URL       : " + url);

        // Esercizio 55 — misura il tempo di esecuzione
        long inizio = System.currentTimeMillis();

        // Passa la richiesta al prossimo filtro (o alla Servlet)
        chain.doFilter(request, response);

        long fine    = System.currentTimeMillis();
        long durata  = fine - inizio;

        System.out.println("[LOG] Tempo     : " + durata + " ms");
        System.out.println("--------------------------------------------");
    }

    @Override
    public void destroy() {
        System.out.println(">>> LoggingFilter: destroy() chiamato!");
    }
}
