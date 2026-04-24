package com.esercizio.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

/*
 * ESERCIZI 59, 60, 63 — LoginServlet
 *
 * GET  /login → mostra il form HTML (esercizi 59 e 63)
 * POST /login → valida le credenziali (esercizio 60)
 *
 * Credenziali hard-coded: admin / 1234
 */
public class LoginServlet extends HttpServlet {

    private static final String USERNAME_CORRETTO = "admin";
    private static final String PASSWORD_CORRETTA = "1234";

    // GET — mostra il form di login
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
    {
        // Esercizio 63: se l'URL contiene ?errore=1 mostra messaggio rosso
        String errore = request.getParameter("errore");
        boolean mostraErrore = "1".equals(errore);

        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html><html><head><title>Login</title>");
        out.println("<style>");
        out.println("  body { font-family: sans-serif; max-width: 360px; margin: 100px auto; }");
        out.println("  input { display:block; width:100%; padding:8px; margin:8px 0; box-sizing:border-box; }");
        out.println("  button { width:100%; padding:10px; background:#2980b9; color:white; border:none; cursor:pointer; }");
        out.println("  .errore { color:red; background:#fdd; padding:8px; border-radius:4px; margin-bottom:12px; }");
        out.println("</style></head><body>");
        out.println("<h2>Login</h2>");

        // Esercizio 63 — messaggio di errore condizionale
        if (mostraErrore) {
            out.println("<p class='errore'>Credenziali non valide. Riprova.</p>");
        }

        // Esercizio 59 — form con POST verso /login
        out.println("<form method='POST' action='" + request.getContextPath() + "/login'>");
        out.println("  <label>Username</label>");
        out.println("  <input type='text' name='username' required />");
        out.println("  <label>Password</label>");
        out.println("  <input type='password' name='password' required />");
        out.println("  <button type='submit'>Accedi</button>");
        out.println("</form>");
        out.println("</body></html>");
    }

    // POST — valida le credenziali
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
    {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if (USERNAME_CORRETTO.equals(username) && PASSWORD_CORRETTA.equals(password)) 
        {
            /*
             * Credenziali corrette.
             * getSession(true) crea la sessione se non esiste ancora.
             * Salviamo l'username così DashboardServlet può verificare
             * che l'utente sia autenticato.
             */
            HttpSession session = request.getSession(true);
            session.setAttribute("username", username);

            response.sendRedirect(request.getContextPath() + "/dashboard");
        } else {
            /*
             * Credenziali errate.
             * Redirect con parametro ?errore=1: la GET di /login
             * leggerà il parametro e mostrerà il messaggio rosso.
             * Usiamo redirect (non forward) così un refresh del browser
             * non ri-sottomette il form POST.
             */
            response.sendRedirect(request.getContextPath() + "/login?errore=1");
        }
    }
}
