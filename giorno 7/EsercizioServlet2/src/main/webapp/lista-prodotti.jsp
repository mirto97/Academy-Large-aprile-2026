<%-- Lista prodotti: usa JSTL core + EL. ZERO scriptlet Java. Mostra disponibilità colorata e fascia di prezzo. --%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>Lista Prodotti</title>
    <style>
        body { font-family: sans-serif; padding: 32px; }
        table { border-collapse: collapse; width: 100%; max-width: 700px; }
        th { background: #2c3e50; color: white; padding: 10px 16px; text-align: left; }
        td { padding: 10px 16px; border-bottom: 1px solid #ddd; }
        tr:hover td { background: #f5f5f5; }
        .disponibile   { color: #27ae60; font-weight: bold; }
        .non-disponibile { color: #e74c3c; font-weight: bold; }
        .economico { color: #27ae60; }
        .medio     { color: #e67e22; }
        .costoso   { color: #e74c3c; }
        tfoot td  { background: #ecf0f1; font-weight: bold; }
    </style>
</head>
<body>
<h2>&#128230; Lista Prodotti</h2>

<table>
    <thead>
        <tr>
            <th>Nome</th>
            <th>Prezzo (€)</th>
            <th>Disponibilità</th>
            <th>Fascia</th>
        </tr>
    </thead>
    <tbody>
        <%-- ES.74: iterazione con c:forEach --%>
        <c:forEach var="p" items="${prodotti}">
            <tr>
                <td>${p.nome}</td>
                <td>${p.prezzo}</td>

                <%-- ES.75: disponibilità in verde/rosso con c:choose --%>
                <td>
                    <c:choose>
                        <c:when test="${p.disponibile}">
                            <span class="disponibile">Disponibile</span>
                        </c:when>
                        <c:otherwise>
                            <span class="non-disponibile">Non disponibile</span>
                        </c:otherwise>
                    </c:choose>
                </td>

                <%-- ES.75: fascia prezzo con c:choose + c:when + c:otherwise --%>
                <td>
                    <c:choose>
                        <c:when test="${p.prezzo < 20}">
                            <span class="economico">Economico</span>
                        </c:when>
                        <c:when test="${p.prezzo <= 100}">
                            <span class="medio">Medio</span>
                        </c:when>
                        <c:otherwise>
                            <span class="costoso">Costoso</span>
                        </c:otherwise>
                    </c:choose>
                </td>
            </tr>
        </c:forEach>
    </tbody>

    <%-- ES.76: totale prodotti e prezzo medio --%>
    <tfoot>
        <tr>
            <td colspan="4">
                Totale prodotti: ${totaleProdotti} &nbsp;|&nbsp; Prezzo medio: ${prezzoMedio} &euro;
            </td>
        </tr>
    </tfoot>
</table>
</body>
</html>
