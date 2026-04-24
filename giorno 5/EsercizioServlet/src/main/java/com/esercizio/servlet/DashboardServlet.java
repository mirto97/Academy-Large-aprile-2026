package com.esercizio.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

/*
 * ESERCIZIO 61 e 62 — DashboardServlet
 *
 * Pagina protetta: accessibile SOLO se nella sessione esiste l'attributo "username".
 * Se non esiste → redirect a /login (utente non autenticato o sessione scaduta).
 */
public class DashboardServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
    {

        /*
         * Esercizio 61 — controllo autenticazione.
         * getSession(false): NON crea una nuova sessione se non esiste.
         * Se passassimo true, creeremmo sempre una sessione vuota,
         * e il controllo su username sarebbe comunque null ma avremmo
         * creato una sessione inutile.
         */
        HttpSession session = request.getSession(false);
        String username = (session != null) ? (String) session.getAttribute("username") : null;

        if (username == null) 
        {
            // Utente non autenticato: redirect al login
            response.sendRedirect(request.getContextPath() + "/login");
            return; // fondamentale: interrompe il metodo dopo il redirect
        }

        // Utente autenticato: mostra la dashboard
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html><html><head><title>Dashboard</title>");
        out.println("<style>");
        out.println("  body { font-family: sans-serif; max-width: 500px; margin: 100px auto; }");
        out.println("  .logout { display:inline-block; margin-top:20px; padding:8px 16px;");
        out.println("            background:#e74c3c; color:white; text-decoration:none; border-radius:4px; }");
        out.println("</style></head><body>");
        out.println("<h2>Dashboard</h2>");
        out.println("<p>Benvenuto, <strong>" + username + "</strong>! Sei nella dashboard.</p>");
        out.println("<p>ID sessione: <code>" + session.getId() + "</code></p>");

        // Esercizio 62 — link di logout
        out.println("<a class='logout' href='" + request.getContextPath() + "/logout'>Logout</a>");
        out.println("</body></html>");
    }
}