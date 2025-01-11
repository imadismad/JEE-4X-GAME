<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="model.Utilisateur" %>
<%@ page import="model.LobbyPartie" %>

<%
    LobbyPartie lobby = (LobbyPartie) request.getAttribute("lobby");
%>

<p>Players: <%= lobby.getUtilisateurs().size() %>/<%= lobby.getMaxJoueurs() %></p>
<ul>
    <% 
        List<Utilisateur> lobbyUsers = lobby.getUtilisateurs();
        for (Utilisateur user : lobbyUsers) {
    %>
        <li><%= user.getNomUtilisateur() %> <% if (lobby.isReady(user)) { %>(Ready)<% } %></li>
    <% } %>
</ul>
<form action="<%= request.getContextPath() %>/lobby/ready" method="post">
    <input type="hidden" name="gameCode" value="<%= request.getAttribute("gameCode") %>">
    <button type="submit">I'm Ready</button>
</form>
