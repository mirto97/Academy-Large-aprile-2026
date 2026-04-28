<%-- Legge num1 e num2 dalla URL, li somma e mostra il risultato; include footer con jsp:include --%>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>Calcolatrice</title>
    <style>
        body{font-family:sans-serif;margin:0;}
        main{padding:32px;}
        .risultato{font-size:1.5rem;color:#27ae60;font-weight:bold;}
        .istruzione{color:#888;}
    </style>
</head>
<body>
<%@ include file="header.jsp" %>
<main>
    <h2>Calcolatrice</h2>
    <%
        String p1 = request.getParameter("num1");
        String p2 = request.getParameter("num2");
        if (p1 != null && p2 != null && !p1.isEmpty() && !p2.isEmpty()) {
            try {
                double n1 = Double.parseDouble(p1);
                double n2 = Double.parseDouble(p2);
                double somma = n1 + n2;
    %>
        <p class="risultato"><%= p1 %> + <%= p2 %> = <%= somma %></p>
    <%
            } catch (NumberFormatException e) {
    %>
        <p style="color:red;">Errore: i parametri devono essere numeri validi.</p>
    <%
            }
        } else {
    %>
        <p class="istruzione">Passa due numeri come parametri URL.<br>
        Esempio: <code>calcolatrice.jsp?num1=5&amp;num2=3</code></p>
    <%
        }
    %>
</main>
<jsp:include page="footer.jsp" />
</body>
</html>
