package com.esercizio.servlet;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/*
 * ESERCIZIO 57 — MaintenanceFilter
 *
 * Se IN_MANUTENZIONE == true, redirige TUTTE le richieste verso /manutenzione,
 * TRANNE la richiesta a /manutenzione stessa (eviterebbe un loop infinito di redirect).
 *
 * Come testare:
 *   - Imposta IN_MANUTENZIONE = true  → qualunque URL viene reindirizzata a /manutenzione
 *   - Imposta IN_MANUTENZIONE = false → il sito funziona normalmente
 */
public class MaintenanceFilter implements Filter {

    // *** Cambia questo flag a true/false per attivare/disattivare la manutenzione ***
    private static final boolean IN_MANUTENZIONE = true;

    // Path della pagina di manutenzione — NON va reindirizzato su se stesso
    private static final String PATH_MANUTENZIONE = "/manutenzione";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println(">>> MaintenanceFilter: init() — IN_MANUTENZIONE = " + IN_MANUTENZIONE);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException 
    {
        HttpServletRequest  httpReq  = (HttpServletRequest)  request;
        HttpServletResponse httpResp = (HttpServletResponse) response;

        if (IN_MANUTENZIONE) {
            // Ricava il path della richiesta corrente (senza context path)
            String requestURI  = httpReq.getRequestURI();
            String contextPath = httpReq.getContextPath();
            String servletPath = requestURI.substring(contextPath.length());

            // Lascia passare solo la richiesta alla pagina di manutenzione stessa
            if (servletPath.equals(PATH_MANUTENZIONE)) {
                chain.doFilter(request, response);
            } else {
                // Redirect verso /manutenzione
                String redirectURL = contextPath + PATH_MANUTENZIONE;
                System.out.println("[MaintenanceFilter] Redirect -> " + redirectURL
                        + "  (richiesta originale: " + servletPath + ")");
                httpResp.sendRedirect(redirectURL);
            }

        } else {
            // Sito operativo: lascia passare tutto normalmente
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {
        System.out.println(">>> MaintenanceFilter: destroy() chiamato!");
    }
}
