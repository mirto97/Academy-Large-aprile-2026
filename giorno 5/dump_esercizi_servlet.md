# Dump Esercizi Servlet — JEE
**Package:** `com.esercizio.servlet`
**Server:** Apache Tomcat 9.0
**Java:** 21

---

# Esercizi 1 e 2 — Servlet e Scope (pre-esistenti)

## BenvenutoServlet.java
Mappata su `/benvenuto`. Gestisce il ciclo di vita (init/destroy), conta le visite e legge il parametro `?nome=` dall'URL.

```java
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
        System.out.println(">>> BenvenutoServlet: init() chiamato!");
    }

    @Override
    public void destroy() {
        System.out.println(">>> BenvenutoServlet: destroy() chiamato!");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        contatore++;
        String nome = request.getParameter("nome");
        if (nome == null || nome.isEmpty()) {
            nome = "ospite";
        }
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE html><html><head><title>BenvenutoServlet</title></head><body>");
        out.println("<h1>Ciao, " + nome + "!</h1>");
        out.println("<p>Questa e' la mia prima Servlet.</p>");
        out.println("<p>Visitata <strong>" + contatore + "</strong> volte.</p>");
        out.println("</body></html>");
    }
}
```

**Test:**
```
http://localhost:8080/EsercizioServlet/benvenuto
http://localhost:8080/EsercizioServlet/benvenuto?nome=Mario
```

---

## InfoServlet.java
Mappata su `/info`. Mostra metodo HTTP, URL completa e tutti i parametri ricevuti.

```java
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
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();
        String metodo = request.getMethod();
        String urlCompleta = request.getRequestURL().toString();
        out.println("<!DOCTYPE html><html><body>");
        out.println("<h1>Informazioni sulla Richiesta</h1>");
        out.println("<p><strong>Metodo HTTP:</strong> " + metodo + "</p>");
        out.println("<p><strong>URL completa:</strong> " + urlCompleta + "</p>");
        out.println("<h2>Parametri ricevuti:</h2><ul>");
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
        out.println("</ul></body></html>");
    }
}
```

**Test:**
```
http://localhost:8080/EsercizioServlet/info
http://localhost:8080/EsercizioServlet/info?nome=Mario&eta=20
```

---

## Riepilogo dei 3 scope

| Scope | Oggetto | Durata | Esempio d'uso |
|---|---|---|---|
| REQUEST | `HttpServletRequest` | Una sola richiesta | Risultato di una ricerca |
| SESSION | `HttpSession` | Navigazione dell'utente | Utente loggato, carrello |
| APPLICATION | `ServletContext` | Finché il server è acceso | Contatore visite totali |

## ContatorePaginaServlet.java — Scope APPLICATION
Mappata su `/pagina`. Contatore condiviso tra tutti gli utenti.

```java
package com.esercizio.servlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class ContatorePaginaServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ServletContext context = getServletContext();
        Integer contatore = (Integer) context.getAttribute("contatoreGlobale");
        if (contatore == null) contatore = 0;
        contatore++;
        context.setAttribute("contatoreGlobale", contatore);
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE html><html><body>");
        out.println("<h1>Scope: APPLICATION</h1>");
        out.println("<p>Visitata <strong>" + contatore + "</strong> volte in totale.</p>");
        out.println("</body></html>");
    }
}
```

## ContatoreUtenteServlet.java — Scope SESSION
Mappata su `/utente`. Ogni utente ha il proprio contatore.

```java
package com.esercizio.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

public class ContatoreUtenteServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(true);
        Integer contatoreUtente = (Integer) session.getAttribute("contatoreUtente");
        if (contatoreUtente == null) contatoreUtente = 0;
        contatoreUtente++;
        session.setAttribute("contatoreUtente", contatoreUtente);
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE html><html><body>");
        out.println("<h1>Scope: SESSION</h1>");
        out.println("<p>Hai visitato questa pagina <strong>" + contatoreUtente + "</strong> volte.</p>");
        out.println("<p>ID sessione: " + session.getId() + "</p>");
        out.println("</body></html>");
    }
}
```

## EchoServlet.java — Scope REQUEST
Mappata su `/echo`. Legge `?testo=`, lo salva nello scope REQUEST e lo mostra.

```java
package com.esercizio.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class EchoServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String testo = request.getParameter("testo");
        if (testo == null || testo.isEmpty()) testo = "(nessun testo ricevuto)";
        request.setAttribute("testoEcho", testo);
        request.setAttribute("messaggio", "Testo salvato nello scope di REQUEST!");
        String testoMostrato = (String) request.getAttribute("testoEcho");
        String messaggioMostrato = (String) request.getAttribute("messaggio");
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE html><html><body>");
        out.println("<h1>Scope: REQUEST</h1>");
        out.println("<p>" + messaggioMostrato + "</p>");
        out.println("<p>Testo: <strong>" + testoMostrato + "</strong></p>");
        out.println("</body></html>");
    }
}
```

---

# Esercizio 1 (nuovi) — Filtri e Forward/Redirect

## Esercizi 54-55 — LoggingFilter.java
Intercetta `/*`. Stampa timestamp, metodo, URL e tempo di esecuzione in ms.

```java
package com.esercizio.servlet;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LoggingFilter implements Filter {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println(">>> LoggingFilter: init() chiamato!");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String timestamp  = LocalDateTime.now().format(FORMATTER);
        String metodo     = httpRequest.getMethod();
        String url        = httpRequest.getRequestURL().toString();
        String queryString = httpRequest.getQueryString();
        if (queryString != null) url += "?" + queryString;

        System.out.println("--------------------------------------------");
        System.out.println("[LOG] Timestamp : " + timestamp);
        System.out.println("[LOG] Metodo    : " + metodo);
        System.out.println("[LOG] URL       : " + url);

        long inizio = System.currentTimeMillis();
        chain.doFilter(request, response);
        long durata = System.currentTimeMillis() - inizio;

        System.out.println("[LOG] Tempo     : " + durata + " ms");
        System.out.println("--------------------------------------------");
    }

    @Override
    public void destroy() {
        System.out.println(">>> LoggingFilter: destroy() chiamato!");
    }
}
```

---

## Esercizio 56 — MaintenanceServlet.java
Mappata su `/manutenzione`. Risponde con HTTP 503 e pagina di manutenzione.

```java
package com.esercizio.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class MaintenanceServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html; charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
        PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE html><html><head><title>Manutenzione</title></head><body>");
        out.println("<h1>&#128736; Sito in manutenzione</h1>");
        out.println("<p>Sito in manutenzione, torna presto!</p>");
        out.println("</body></html>");
    }
}
```

---

## Esercizio 57 — MaintenanceFilter.java
Se `IN_MANUTENZIONE == true` redirige tutto verso `/manutenzione`, tranne la richiesta alla pagina stessa (evita loop infinito).

```java
package com.esercizio.servlet;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MaintenanceFilter implements Filter {

    // Cambia a false per disattivare la manutenzione
    private static final boolean IN_MANUTENZIONE = true;
    private static final String PATH_MANUTENZIONE = "/manutenzione";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println(">>> MaintenanceFilter: init() — IN_MANUTENZIONE = " + IN_MANUTENZIONE);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest  httpReq  = (HttpServletRequest)  request;
        HttpServletResponse httpResp = (HttpServletResponse) response;

        if (IN_MANUTENZIONE) {
            String servletPath = httpReq.getRequestURI().substring(httpReq.getContextPath().length());
            if (servletPath.equals(PATH_MANUTENZIONE)) {
                chain.doFilter(request, response);
            } else {
                httpResp.sendRedirect(httpReq.getContextPath() + PATH_MANUTENZIONE);
            }
        } else {
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {
        System.out.println(">>> MaintenanceFilter: destroy() chiamato!");
    }
}
```

---

## Esercizio 58 — ForwardRedirectServlet.java
Gestisce tre path: `/azione-forward`, `/azione-redirect`, `/destinazione`.

### Forward vs Redirect — differenze chiave

| | Forward | Redirect |
|---|---|---|
| Richieste HTTP | 1 | 2 (302 + GET) |
| URL barra browser | NON cambia | Cambia |
| Attributi REQUEST | Visibili nella destinazione | Persi (nuova richiesta) |
| URL esterne | Non possibile | Possibile |
| Quando usarlo | Pattern MVC, passare dati a JSP | Dopo POST form, login/logout, cambio URL |

```java
package com.esercizio.servlet;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class ForwardRedirectServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String servletPath = request.getServletPath();

        switch (servletPath) {

            case "/azione-forward":
                // Stessa richiesta: l'attributo arriverà intatto a /destinazione
                request.setAttribute("provenienza", "forward da /azione-forward");
                request.setAttribute("metodo", "FORWARD");
                RequestDispatcher dispatcher = request.getRequestDispatcher("/destinazione");
                dispatcher.forward(request, response);
                // Non scrivere nulla dopo il forward: causerebbe IllegalStateException
                break;

            case "/azione-redirect":
                // Nuova richiesta: gli attributi impostati qui andranno persi
                request.setAttribute("provenienza", "questo andrà perso");
                response.sendRedirect(request.getContextPath() + "/destinazione");
                // Non scrivere nulla dopo il sendRedirect
                break;

            case "/destinazione":
                mostraPaginaDestinazione(request, response);
                break;

            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void mostraPaginaDestinazione(HttpServletRequest request,
                                          HttpServletResponse response)
            throws IOException {
        String provenienza = (String) request.getAttribute("provenienza");
        String metodo      = (String) request.getAttribute("metodo");
        if (provenienza == null) provenienza = "(nessuno — sei arrivato via redirect o direttamente)";
        if (metodo      == null) metodo      = "REDIRECT (o accesso diretto)";

        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE html><html><head><title>Destinazione</title></head><body>");
        out.println("<h2>Pagina di Destinazione</h2>");
        out.println("<p>Metodo di arrivo: <strong>" + metodo + "</strong></p>");
        out.println("<p>Attributo REQUEST 'provenienza': " + provenienza + "</p>");
        out.println("<p><a href='" + request.getContextPath() + "/azione-forward'>Prova Forward</a>");
        out.println(" | <a href='" + request.getContextPath() + "/azione-redirect'>Prova Redirect</a></p>");
        out.println("</body></html>");
    }
}
```

---

# Esercizio 2 (nuovi) — Sistema di login con sessione

## Esercizi 59, 60, 63 — LoginServlet.java
- **GET** `/login` → mostra il form (con messaggio errore se `?errore=1`)
- **POST** `/login` → valida le credenziali hard-coded (`admin` / `1234`)

```java
package com.esercizio.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

public class LoginServlet extends HttpServlet {

    private static final String USERNAME_CORRETTO = "admin";
    private static final String PASSWORD_CORRETTA = "1234";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
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
        if (mostraErrore) {
            out.println("<p class='errore'>Credenziali non valide. Riprova.</p>");
        }
        out.println("<form method='POST' action='" + request.getContextPath() + "/login'>");
        out.println("  <label>Username</label>");
        out.println("  <input type='text' name='username' required />");
        out.println("  <label>Password</label>");
        out.println("  <input type='password' name='password' required />");
        out.println("  <button type='submit'>Accedi</button>");
        out.println("</form></body></html>");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if (USERNAME_CORRETTO.equals(username) && PASSWORD_CORRETTA.equals(password)) {
            // Credenziali corrette: salva username in sessione e vai alla dashboard
            HttpSession session = request.getSession(true);
            session.setAttribute("username", username);
            response.sendRedirect(request.getContextPath() + "/dashboard");
        } else {
            // Credenziali errate: redirect con ?errore=1
            // Usiamo redirect (non forward) per evitare il re-submit del form al refresh
            response.sendRedirect(request.getContextPath() + "/login?errore=1");
        }
    }
}
```

---

## Esercizi 61, 62 — DashboardServlet.java
Pagina protetta. Se `username` non è in sessione → redirect a `/login`.

```java
package com.esercizio.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

public class DashboardServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // getSession(false): non crea una nuova sessione se non esiste già
        HttpSession session = request.getSession(false);
        String username = (session != null) ? (String) session.getAttribute("username") : null;

        if (username == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return; // fondamentale: interrompe il metodo dopo il redirect
        }

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
        out.println("<a class='logout' href='" + request.getContextPath() + "/logout'>Logout</a>");
        out.println("</body></html>");
    }
}
```

---

## Esercizio 62 — LogoutServlet.java
Invalida la sessione e redirige al login.

```java
package com.esercizio.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LogoutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // getSession(false): non creare una sessione nuova solo per invalidarla
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate(); // il SessionListener stamperà "DISTRUTTA"
        }
        response.sendRedirect(request.getContextPath() + "/login");
    }
}
```

---

## Esercizio 64 — SessionListener.java
Stampa su console ogni volta che una sessione viene creata o distrutta.

```java
package com.esercizio.servlet;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class SessionListener implements HttpSessionListener {

    @Override
    public void sessionCreated(HttpSessionEvent event) {
        System.out.println(">>> [SessionListener] Sessione CREATA    — ID: " + event.getSession().getId());
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
        System.out.println(">>> [SessionListener] Sessione DISTRUTTA — ID: " + event.getSession().getId());
    }
}
```

> **Nota:** il listener NON si mappa come una servlet. Si registra nel `web.xml` con `<listener>`, non con `<servlet-mapping>`.

---

# web.xml — Configurazione completa

```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xmlns="http://java.sun.com/xml/ns/javaee"
         xsi:schemaLocation="http://java.sun.com/xml/ns/javaee
         http://java.sun.com/xml/ns/javaee/web-app_2_5.xsd"
         id="WebApp_ID" version="2.5">

  <display-name>EsercizioServlet</display-name>

  <!-- LISTENER -->
  <listener>
    <listener-class>com.esercizio.servlet.SessionListener</listener-class>
  </listener>

  <!-- FILTRI (eseguiti nell'ordine dichiarato) -->
  <filter>
    <filter-name>LoggingFilter</filter-name>
    <filter-class>com.esercizio.servlet.LoggingFilter</filter-class>
  </filter>
  <filter-mapping>
    <filter-name>LoggingFilter</filter-name>
    <url-pattern>/*</url-pattern>
  </filter-mapping>

  <filter>
    <filter-name>MaintenanceFilter</filter-name>
    <filter-class>com.esercizio.servlet.MaintenanceFilter</filter-class>
  </filter>
  <filter-mapping>
    <filter-name>MaintenanceFilter</filter-name>
    <url-pattern>/*</url-pattern>
  </filter-mapping>

  <!-- SERVLET — Esercizio 1 -->
  <servlet>
    <servlet-name>BenvenutoServlet</servlet-name>
    <servlet-class>com.esercizio.servlet.BenvenutoServlet</servlet-class>
  </servlet>
  <servlet-mapping>
    <servlet-name>BenvenutoServlet</servlet-name>
    <url-pattern>/benvenuto</url-pattern>
  </servlet-mapping>

  <servlet>
    <servlet-name>InfoServlet</servlet-name>
    <servlet-class>com.esercizio.servlet.InfoServlet</servlet-class>
  </servlet>
  <servlet-mapping>
    <servlet-name>InfoServlet</servlet-name>
    <url-pattern>/info</url-pattern>
  </servlet-mapping>

  <!-- SERVLET — Esercizio 2 (scope) -->
  <servlet>
    <servlet-name>ContatorePaginaServlet</servlet-name>
    <servlet-class>com.esercizio.servlet.ContatorePaginaServlet</servlet-class>
  </servlet>
  <servlet-mapping>
    <servlet-name>ContatorePaginaServlet</servlet-name>
    <url-pattern>/pagina</url-pattern>
  </servlet-mapping>

  <servlet>
    <servlet-name>ContatoreUtenteServlet</servlet-name>
    <servlet-class>com.esercizio.servlet.ContatoreUtenteServlet</servlet-class>
  </servlet>
  <servlet-mapping>
    <servlet-name>ContatoreUtenteServlet</servlet-name>
    <url-pattern>/utente</url-pattern>
  </servlet-mapping>

  <servlet>
    <servlet-name>EchoServlet</servlet-name>
    <servlet-class>com.esercizio.servlet.EchoServlet</servlet-class>
  </servlet>
  <servlet-mapping>
    <servlet-name>EchoServlet</servlet-name>
    <url-pattern>/echo</url-pattern>
  </servlet-mapping>

  <!-- SERVLET — Filtri e Forward/Redirect -->
  <servlet>
    <servlet-name>MaintenanceServlet</servlet-name>
    <servlet-class>com.esercizio.servlet.MaintenanceServlet</servlet-class>
  </servlet>
  <servlet-mapping>
    <servlet-name>MaintenanceServlet</servlet-name>
    <url-pattern>/manutenzione</url-pattern>
  </servlet-mapping>

  <servlet>
    <servlet-name>ForwardRedirectServlet</servlet-name>
    <servlet-class>com.esercizio.servlet.ForwardRedirectServlet</servlet-class>
  </servlet>
  <servlet-mapping>
    <servlet-name>ForwardRedirectServlet</servlet-name>
    <url-pattern>/azione-forward</url-pattern>
  </servlet-mapping>
  <servlet-mapping>
    <servlet-name>ForwardRedirectServlet</servlet-name>
    <url-pattern>/azione-redirect</url-pattern>
  </servlet-mapping>
  <servlet-mapping>
    <servlet-name>ForwardRedirectServlet</servlet-name>
    <url-pattern>/destinazione</url-pattern>
  </servlet-mapping>

  <!-- SERVLET — Sistema di login -->
  <servlet>
    <servlet-name>LoginServlet</servlet-name>
    <servlet-class>com.esercizio.servlet.LoginServlet</servlet-class>
  </servlet>
  <servlet-mapping>
    <servlet-name>LoginServlet</servlet-name>
    <url-pattern>/login</url-pattern>
  </servlet-mapping>

  <servlet>
    <servlet-name>DashboardServlet</servlet-name>
    <servlet-class>com.esercizio.servlet.DashboardServlet</servlet-class>
  </servlet>
  <servlet-mapping>
    <servlet-name>DashboardServlet</servlet-name>
    <url-pattern>/dashboard</url-pattern>
  </servlet-mapping>

  <servlet>
    <servlet-name>LogoutServlet</servlet-name>
    <servlet-class>com.esercizio.servlet.LogoutServlet</servlet-class>
  </servlet>
  <servlet-mapping>
    <servlet-name>LogoutServlet</servlet-name>
    <url-pattern>/logout</url-pattern>
  </servlet-mapping>

</web-app>
```

---

# URL di test — riepilogo completo

```
# Esercizio 1
http://localhost:8080/EsercizioServlet/benvenuto
http://localhost:8080/EsercizioServlet/benvenuto?nome=Mario
http://localhost:8080/EsercizioServlet/info
http://localhost:8080/EsercizioServlet/info?a=1&b=2

# Esercizio 2 — scope
http://localhost:8080/EsercizioServlet/pagina       (APPLICATION: contatore globale)
http://localhost:8080/EsercizioServlet/utente        (SESSION: contatore per utente)
http://localhost:8080/EsercizioServlet/echo?testo=ciao  (REQUEST: dato non persiste)

# Filtri e forward/redirect
http://localhost:8080/EsercizioServlet/manutenzione
http://localhost:8080/EsercizioServlet/azione-forward
http://localhost:8080/EsercizioServlet/azione-redirect
http://localhost:8080/EsercizioServlet/destinazione

# Sistema di login
http://localhost:8080/EsercizioServlet/login
http://localhost:8080/EsercizioServlet/dashboard
http://localhost:8080/EsercizioServlet/logout
```

---

# Note e gotcha

- `getSession(false)` in DashboardServlet e LogoutServlet: non creare una sessione vuota solo per controllare se l'utente è loggato.
- `return` dopo `sendRedirect` in DashboardServlet: senza di esso il codice continua ad eseguire e causa `IllegalStateException`.
- Pattern **POST → Redirect → GET** in LoginServlet: evita il popup "Vuoi reinviare il modulo?" al refresh.
- Il `SessionListener` si registra con `<listener>` nel web.xml, **non** con `<servlet-mapping>`.
- L'ordine dei filtri nel web.xml conta: LoggingFilter prima (logga sempre), MaintenanceFilter dopo.
- Nel `MaintenanceFilter` la guardia su `PATH_MANUTENZIONE` è obbligatoria per evitare il loop infinito di redirect.
