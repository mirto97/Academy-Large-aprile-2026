package com.esercizio.servlet;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/*
 * ESERCIZIO 58 — Forward vs Redirect
 *
 * /azione-forward   → usa RequestDispatcher.forward()
 *                     - Il browser fa UNA sola richiesta HTTP
 *                     - L'URL nella barra NON cambia
 *                     - Il server trasferisce il controllo internamente
 *                     - Gli attributi di REQUEST sono visibili nella destinazione
 *
 * /azione-redirect  → usa HttpServletResponse.sendRedirect()
 *                     - Il server risponde con 302 Found
 *                     - Il browser fa UNA SECONDA richiesta HTTP verso il nuovo URL
 *                     - L'URL nella barra CAMBIA
 *                     - Gli attributi di REQUEST vengono persi (nuova richiesta)
 *
 * /destinazione     → pagina di destinazione comune
 *
 * Come osservare la differenza:
 *   Apri DevTools (F12) → tab "Rete/Network" e visita i due URL.
 *   - /azione-forward  : vedi 1 richiesta (GET /azione-forward)
 *   - /azione-redirect : vedi 2 richieste (GET /azione-redirect → 302, poi GET /destinazione)
 *
 * ┌───────────────────────────────────────────────────────────────────────┐
 * │                        FORWARD  (server-side)                         │
 * ├───────────────────────────────────────────────────────────────────────┤
 * │ Come funziona:                                                        │
 * │   Il server trasferisce il controllo internamente a un'altra risorsa. │
 * │   Il browser NON sa che è avvenuto un trasferimento: ha fatto UNA     │
 * │   sola richiesta HTTP e riceve una sola risposta.                     │
 * │                                                                       │
 * │ Flusso HTTP (visibile nei DevTools → 1 sola riga):                    │
 * │   Browser ──GET /azione-forward──► Server                             │
 * │                                    Server internamente → /destinazione│
 * │   Browser ◄──────── 200 OK ─────── Server                             │
 * │                                                                       │
 * │ Conseguenze pratiche:                                                 │
 * │   • L'URL nella barra del browser NON cambia (rimane /azione-forward) │
 * │   • Gli attributi di REQUEST sono visibili nella destinazione         │
 * │     (è la stessa istanza di HttpServletRequest)                       │
 * │   • Non si può fare forward verso URL esterne (solo risorse interne)  │
 * │                                                                       │
 * │ Quando usarlo:                                                        │
 * │   ✔ Passare dati elaborati a una JSP che li renderizza               │
 * │   ✔ Pattern MVC: la Servlet fa da Controller e fa forward alla View  │
 * │   ✔ Quando vuoi che l'URL resti quella originale (es. /prodotti)     │
 * │   ✗ NON usarlo dopo aver scritto qualcosa nel body della response     │
 * └───────────────────────────────────────────────────────────────────────┘
 *
 * ┌───────────────────────────────────────────────────────────────────────┐
 * │                       REDIRECT  (client-side)                         │
 * ├───────────────────────────────────────────────────────────────────────┤
 * │ Come funziona:                                                        │
 * │   Il server risponde con 302 Found + header Location. Il browser      │
 * │   legge l'header e fa automaticamente una SECONDA richiesta GET       │
 * │   verso il nuovo URL.                                                 │
 * │                                                                       │
 * │ Flusso HTTP (visibile nei DevTools → 2 righe):                        │
 * │   Browser ──GET /azione-redirect──► Server                            │
 * │   Browser ◄── 302 + Location: /destinazione ── Server                 │
 * │   Browser ──GET /destinazione──► Server                               │
 * │   Browser ◄──────── 200 OK ─────── Server                             │
 * │                                                                       │
 * │ Conseguenze pratiche:                                                 │
 * │   • L'URL nella barra del browser CAMBIA (diventa /destinazione)      │
 * │   • Gli attributi di REQUEST vengono PERSI (è una nuova richiesta)    │
 * │   • Si può redirigere verso URL esterne (es. https://google.com)      │
 * │                                                                       │
 * │ Quando usarlo:                                                        │
 * │   ✔ Dopo il submit di un form (pattern POST → Redirect → GET)        │
 * │     evita il "Vuoi reinviare il modulo?" al refresh                   │
 * │   ✔ Dopo login/logout, per mandare l'utente su una pagina pulita     │
 * │   ✔ Quando l'URL deve aggiornarsi (es. /carrello dopo aggiunta item) │
 * │   ✔ Per redirigere verso siti esterni                                │
 * │   ✗ NON usarlo se hai bisogno di passare dati via REQUEST             │
 * └───────────────────────────────────────────────────────────────────────┘
 *
 * Regola pratica per scegliere:
 *   "Devo passare dati alla destinazione e l'URL non importa?" → FORWARD
 *   "Devo cambiare URL o prevenire un doppio submit?"          → REDIRECT
 */
public class ForwardRedirectServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
    {
        String servletPath = request.getServletPath();

        switch (servletPath) {

            // ----------------------------------------------------------------
            case "/azione-forward":
            	/*
                 * FORWARD
                 * Impostiamo un attributo di REQUEST per dimostrare che arriva
                 * intatto alla destinazione: è la stessa richiesta, stesso oggetto.
                 * dentro al server, una sola richiesta, gli attributi sopravvivono
                 */                
            	request.setAttribute("provenienza", "forward da /azione-forward");
                request.setAttribute("metodo", "FORWARD");

                RequestDispatcher dispatcher = request.getRequestDispatcher("/destinazione");
                dispatcher.forward(request, response);
                // IMPORTANTE: non scrivere nulla dopo il forward.
                // Il controllo è passato alla destinazione; qualsiasi scrittura
                // sulla response qui causerebbe un IllegalStateException.
                break;

            // ----------------------------------------------------------------
            case "/azione-redirect":
            	/*
                 * REDIRECT
                 * Il server invia 302 + Location al browser.
                 * Gli attributi impostati qui NON raggiungeranno /destinazione
                 * perché il browser farà una richiesta completamente nuova.
                 * richiesta nuova, attributi vengono persi.
                 */
            	request.setAttribute("provenienza", "redirect — questo non arriverà");

                String redirectURL = request.getContextPath() + "/destinazione";
                response.sendRedirect(redirectURL);
                // IMPORTANTE: non scrivere nulla dopo il sendRedirect.
                // La response è già "committed" con il 302; qualsiasi scrittura
                // verrebbe ignorata o causerebbe un'eccezione.
                break;

            // ----------------------------------------------------------------
            case "/destinazione":
                mostraPaginaDestinazione(request, response);
                break;

            // ----------------------------------------------------------------
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    // -----------------------------------------------------------------------
    // Pagina di destinazione — comune a forward e redirect
    // -----------------------------------------------------------------------
    private void mostraPaginaDestinazione(HttpServletRequest request,
                                          HttpServletResponse response)
            throws IOException {

    	// Con il FORWARD questo attributo è valorizzato.
        // Con il REDIRECT è null: la richiesta è nuova, gli attributi sono spariti.
        String provenienza = (String) request.getAttribute("provenienza");
        String metodo      = (String) request.getAttribute("metodo");

        if (provenienza == null) provenienza = "(nessuno — sei arrivato via redirect o direttamente)";
        if (metodo      == null) metodo      = "REDIRECT (o accesso diretto)";

        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("  <title>Destinazione</title>");
        out.println("  <style>");
        out.println("    body { font-family: sans-serif; max-width: 600px; margin: 60px auto; }");
        out.println("    .badge { display:inline-block; padding:4px 10px; border-radius:4px;");
        out.println("             font-weight:bold; color:white; }");
        out.println("    .forward  { background:#27ae60; }");
        out.println("    .redirect { background:#e67e22; }");
        out.println("    table { border-collapse:collapse; width:100%; margin-top:20px; }");
        out.println("    td,th { border:1px solid #ddd; padding:8px 12px; text-align:left; }");
        out.println("    th { background:#f0f0f0; }");
        out.println("  </style>");
        out.println("</head>");
        out.println("<body>");
        out.println("  <h1>Pagina di Destinazione</h1>");

        out.println("  <p>Metodo di arrivo: <span class='badge "
                + (metodo.equals("FORWARD") ? "forward" : "redirect") + "'>"
                + metodo + "</span></p>");

        out.println("  <p><strong>Attributo REQUEST 'provenienza':</strong><br>");
        out.println("     " + provenienza + "</p>");

        out.println("  <hr>");
        out.println("  <h2>Differenza visibile nei DevTools</h2>");
        out.println("  <table>");
        out.println("    <tr><th></th><th>Forward</th><th>Redirect</th></tr>");
        out.println("    <tr><td>N° richieste HTTP</td><td>1</td><td>2 (302 + GET)</td></tr>");
        out.println("    <tr><td>URL barra browser</td><td>NON cambia</td><td>Cambia a /destinazione</td></tr>");
        out.println("    <tr><td>Attributi REQUEST</td><td>Visibili ✔</td><td>Persi ✘</td></tr>");
        out.println("    <tr><td>Lato esecuzione</td><td>Server-side</td><td>Client-side</td></tr>");
        out.println("  </table>");

        out.println("  <hr>");
        out.println("  <p>");
        out.println("    <a href='" + request.getContextPath() + "/azione-forward'>Prova il Forward</a>");
        out.println("    &nbsp;|&nbsp;");
        out.println("    <a href='" + request.getContextPath() + "/azione-redirect'>Prova il Redirect</a>");
        out.println("  </p>");
        out.println("</body>");
        out.println("</html>");
    }
}
