package com.esercizio.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

/*
 * SCOPE SESSION (HttpSession):
 * - I dati appartengono a UN solo utente
 * - Durano finché l'utente non chiude il browser o la sessione scade
 * - Esempio reale: utente loggato, carrello della spesa,
 *   preferenze dell'utente durante la navigazione
 */
public class ContatoreUtenteServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
    {
        // HttpSession = lo "zaino personale" di ogni utente
        // Ogni utente ha la sua sessione separata
        // true = se non esiste ancora, creala adesso
        HttpSession session = request.getSession(true);

        // Leggi il contatore di questo utente specifico
        Integer contatoreUtente = (Integer) session.getAttribute("contatoreUtente");
        if (contatoreUtente == null) {
            contatoreUtente = 0;
        }

        // Incrementa e salva nella sessione
        contatoreUtente++;
        session.setAttribute("contatoreUtente", contatoreUtente);

        // Rispondi
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html><html><body>");
        out.println("<h1>Scope: SESSION</h1>");
        out.println("<p>Hai visitato questa pagina <strong>" + contatoreUtente + "</strong> volte.</p>");
        out.println("<p><em>Apri in incognito: vedrai che il contatore riparte da 1 (sessione diversa!)</em></p>");
        out.println("<p>ID della tua sessione: " + session.getId() + "</p>");
        out.println("</body></html>");
    }
}