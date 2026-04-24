package com.esercizio.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/*
 * ESERCIZIO 56 — MaintenanceServlet
 *
 * Mappata su /manutenzione.
 * Mostra una pagina HTML di "sito in manutenzione".
 * Viene usata come destinazione del redirect nel MaintenanceFilter.
 */
public class MaintenanceServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
    {
        response.setContentType("text/html; charset=UTF-8");
        // Inviamo 503 Service Unavailable, semanticamente corretto
        response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);

        PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("  <title>Manutenzione</title>");
        out.println("  <style>");
        out.println("    body { font-family: sans-serif; text-align: center; margin-top: 100px; background: #f5f5f5; }");
        out.println("    .box { background: white; border-radius: 8px; padding: 40px;");
        out.println("           display: inline-block; box-shadow: 0 2px 8px rgba(0,0,0,.15); }");
        out.println("    h1 { color: #e74c3c; }");
        out.println("    p  { color: #555; }");
        out.println("  </style>");
        out.println("</head>");
        out.println("<body>");
        out.println("  <div class='box'>");
        out.println("    <h1>&#128736; Sito in manutenzione</h1>");
        out.println("    <p>Sito in manutenzione, torna presto!</p>");
        out.println("  </div>");
        out.println("</body>");
        out.println("</html>");
    }
}
