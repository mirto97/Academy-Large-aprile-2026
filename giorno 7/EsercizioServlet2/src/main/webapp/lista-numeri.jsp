<%-- Genera una lista HTML con i numeri da 1 a 10 tramite ciclo for e include header.jsp --%>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>Lista Numeri</title>
    <style>body{font-family:sans-serif;margin:0;} main{padding:32px;} ul{line-height:2;}</style>
</head>
<body>
<%@ include file="header.jsp" %>
<main>
    <h2>Numeri da 1 a 10</h2>
    <ul>
        <% for (int i = 1; i <= 10; i++) { %>
            <li><%= i %></li>
        <% } %>
    </ul>
</main>
<%@ include file="footer.jsp" %>
</body>
</html>
