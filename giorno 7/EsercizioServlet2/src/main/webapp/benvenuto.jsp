<%-- Mostra un messaggio di benvenuto con la data e l'ora correnti usando LocalDateTime --%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.time.LocalDateTime, java.time.format.DateTimeFormatter" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>Benvenuto</title>
    <style>body{font-family:sans-serif;margin:0;} main{padding:32px;}</style>
</head>
<body>
<%@ include file="header.jsp" %>
<main>
    <%
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String dataOra = now.format(fmt);
    %>
    <h2>Benvenuto!</h2>
    <p>Data e ora correnti: <strong><%= dataOra %></strong></p>
</main>
<%@ include file="footer.jsp" %>
</body>
</html>
