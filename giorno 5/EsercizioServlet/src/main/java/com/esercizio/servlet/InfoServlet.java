package com.esercizio.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

public class InfoServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
    {
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();

        String metodo = request.getMethod();
        String urlCompleta = request.getRequestURL().toString();

        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head><title>InfoServlet</title></head>");
        out.println("<body>");
        out.println("<h1>Informazioni sulla Richiesta</h1>");
        out.println("<p><strong>Metodo HTTP:</strong> " + metodo + "</p>");
        out.println("<p><strong>URL completa:</strong> " + urlCompleta + "</p>");

        out.println("<h2>Parametri ricevuti:</h2>");
        out.println("<ul>");

        Enumeration<String> parametri = request.getParameterNames();
        if (!parametri.hasMoreElements()) {
            out.println("<li>Nessun parametro ricevuto</li>");
        } else {
            while (parametri.hasMoreElements()) {
                String nomeParam = parametri.nextElement();
                String valoreParam = request.getParameter(nomeParam);
                out.println("<li>" + nomeParam + " = " + valoreParam + "</li>");
            }
        }

        out.println("</ul>");
        out.println("</body>");
        out.println("</html>");
    }
}