package com.esercizio.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.esercizio.beans.Prodotto;

/**
 * Servlet che crea una lista di prodotti e fa forward a lista-prodotti.jsp
 */
@WebServlet("/prodotti")
public class ProdottiServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Creazione lista prodotti
        List<Prodotto> lista = new ArrayList<>();
        lista.add(new Prodotto("Laptop",      999.99, true));
        lista.add(new Prodotto("Mouse",        15.00, true));
        lista.add(new Prodotto("Monitor",     349.00, false));
        lista.add(new Prodotto("Tastiera",     45.50, true));
        lista.add(new Prodotto("Cavo USB",      5.99, false));

        // Calcolo totale e media nella Servlet (no logica in JSP)
        double totale = lista.stream().mapToDouble(Prodotto::getPrezzo).sum();
        double media  = totale / lista.size();

        req.setAttribute("prodotti",       lista);
        req.setAttribute("totaleProdotti", lista.size());
        req.setAttribute("prezzoMedio",    String.format("%.2f", media));

        req.getRequestDispatcher("/lista-prodotti.jsp").forward(req, resp);
    }
}
