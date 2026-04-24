package com.esercizio.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class BenvenutoServlet extends HttpServlet {

    private int contatore = 0;

    @Override
    public void init() throws ServletException {
        System.out.println(">>> BenvenutoServlet: init() chiamato! La Servlet è stata caricata.");
    }

    @Override
    public void destroy() {
        System.out.println(">>> BenvenutoServlet: destroy() chiamato! La Servlet è stata rimossa.");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
    {
        contatore++;

        String nome = request.getParameter("nome");
        if (nome == null || nome.isEmpty()) {
            nome = "ospite";
        }

        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head><title>BenvenutoServlet</title></head>");
        out.println("<body>");
        out.println("<h1>Ciao, " + nome + "!</h1>");
        out.println("<p>Ciao! Questa è la mia prima Servlet.</p>");
        out.println("<p>Questa pagina è stata visitata <strong>" + contatore + "</strong> volte.</p>");
        out.println("</body>");
        out.println("</html>");
    }
}