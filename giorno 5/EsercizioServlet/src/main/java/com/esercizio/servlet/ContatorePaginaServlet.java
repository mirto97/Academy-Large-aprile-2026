package com.esercizio.servlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/*
 * SCOPE APPLICATION (ServletContext):
 * - I dati sono condivisi tra TUTTI gli utenti e TUTTE le richieste
 * - Vivono finché il server è acceso
 * - Esempio reale: contatore visite totali del sito, 
 *   configurazioni globali dell'app, cache condivisa
 */
public class ContatorePaginaServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
    {
        // ServletContext = lo "zaino condiviso" dell'intera applicazione
        // Tutti gli utenti leggono e scrivono nello stesso posto
        ServletContext context = getServletContext();

        // Leggi il contatore attuale (la prima volta è null, quindi partiamo da 0)
        Integer contatore = (Integer) context.getAttribute("contatoreGlobale");
        if (contatore == null) {
            contatore = 0;
        }

        // Incrementa e salva di nuovo nel contesto
        contatore++;
        context.setAttribute("contatoreGlobale", contatore);

        // Rispondi
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html><html><body>");
        out.println("<h1>Scope: APPLICATION</h1>");
        out.println("<p>Questa pagina è stata visitata <strong>" + contatore + "</strong> volte in totale (da tutti gli utenti).</p>");
        out.println("<p><em>Prova ad aprire questa pagina in un'altra finestra: il numero continua da dove era!</em></p>");
        out.println("</body></html>");
    }
}