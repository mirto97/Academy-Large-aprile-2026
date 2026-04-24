package com.esercizio.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/*
 * ESERCIZIO 62 — LogoutServlet
 *
 * session.invalidate() distrugge la sessione lato server:
 * tutti gli attributi vengono cancellati e la sessione non è più valida.
 * Dopo il redirect a /login, se l'utente torna su /dashboard
 * non troverà più "username" nella sessione e verrà rimandato al login.
 */
public class LogoutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
    {

        // getSession(false): non creare una nuova sessione se per qualche
        // motivo non esiste già (es. utente che visita /logout direttamente)
        HttpSession session = request.getSession(false);

        if (session != null) 
        {
            session.invalidate(); // distrugge la sessione → il SessionListener stamperà "distrutta"
        }

        response.sendRedirect(request.getContextPath() + "/login");
    }
}