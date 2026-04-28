<%-- Crea un'istanza del bean Prodotto tramite jsp:useBean, imposta i valori e li visualizza con jsp:getProperty --%>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="it">
<head><meta charset="UTF-8"><title>Prodotto</title>
<style>body{font-family:sans-serif;padding:32px;} table{border-collapse:collapse;} td,th{padding:8px 16px;border:1px solid #ccc;}</style>
</head>
<body>
<h2>Dettaglio Prodotto (jsp:useBean)</h2>

<jsp:useBean id="prodotto" class="beans.Prodotto" scope="page" />
<jsp:setProperty name="prodotto" property="nome"  value="Laptop" />
<jsp:setProperty name="prodotto" property="prezzo" value="999.99" />
<jsp:setProperty name="prodotto" property="disponibile" value="true" />

<table>
    <tr><th>Nome</th><td><jsp:getProperty name="prodotto" property="nome" /></td></tr>
    <tr><th>Prezzo</th><td><jsp:getProperty name="prodotto" property="prezzo" /> €</td></tr>
    <tr><th>Disponibile</th><td><jsp:getProperty name="prodotto" property="disponibile" /></td></tr>
</table>
</body>
</html>
