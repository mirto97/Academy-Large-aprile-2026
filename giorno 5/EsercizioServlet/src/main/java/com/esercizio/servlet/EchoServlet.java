package com.esercizio.servlet;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/*
 * SCOPE REQUEST (HttpServletRequest):
 * - I dati esistono SOLO durante quella singola richiesta
 * - Appena la risposta viene inviata, i dati spariscono
 * - Esempio reale: risultati di una ricerca, messaggio di errore
 *   di validazione di un form, dati da mostrare in una pagina
 */
public class EchoServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
    {
        // Leggi il parametro dall'URL (?testo=ciao)
        String testo = request.getParameter("testo");
        if (testo == null || testo.isEmpty()) {
            testo = "(nessun testo ricevuto)";
        }

        // Salva il testo nello scope di REQUEST
        // Questo dato esiste SOLO durante questa richiesta
        request.setAttribute("testoEcho", testo);
        request.setAttribute("messaggio", "Testo ricevuto e salvato nello scope di REQUEST!");

        // Rispondi direttamente (senza JSP per semplicità)
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();

        // Recuperiamo i dati dallo scope di request per mostrarli
        String testoMostrato = (String) request.getAttribute("testoEcho");
        String messaggioMostrato = (String) request.getAttribute("messaggio");

        out.println("<!DOCTYPE html><html><body>");
        out.println("<h1>Scope: REQUEST</h1>");
        out.println("<p>" + messaggioMostrato + "</p>");
        out.println("<p>Testo ricevuto: <strong>" + testoMostrato + "</strong></p>");
        out.println("<p><em>Questo dato esiste solo per questa richiesta. Ogni volta che ricarichi, riparte da zero.</em></p>");
        out.println("<br>");
        out.println("<p>Prova: <a href='/EsercizioServlet/echo?testo=Hello'>?testo=Hello</a> oppure ");
        out.println("<a href='/EsercizioServlet/echo?testo=Mondo'>?testo=Mondo</a></p>");
        out.println("</body></html>");
    }
}