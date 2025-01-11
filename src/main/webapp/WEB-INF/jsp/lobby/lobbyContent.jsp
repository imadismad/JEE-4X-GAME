<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="model.Utilisateur" %>
<%@ page import="model.LobbyPartie" %>

<%
    LobbyPartie lobby = (LobbyPartie) request.getAttribute("lobby");
%>

<style>
    /* Lobby content styles */
    p {
        font-size: 16px;
        color: #34495e;
        margin-bottom: 15px;
    }

    ul {
        list-style: none;
        padding: 0;
        margin-bottom: 20px;
    }

    ul li {
        font-size: 14px;
        color: #2c3e50;
        margin-bottom: 5px;
    }

    ul li span {
        color: #27ae60;
        font-weight: bold;
    }

    button {
        background-color: #007bff;
        color: white;
        padding: 10px 15px;
        border: none;
        border-radius: 4px;
        font-size: 16px;
        cursor: pointer;
        transition: background-color 0.3s;
        width: 100%;
    }

    button:hover {
        background-color: #0056b3;
    }
</style>

<p>Players: <%= lobby.getUtilisateurs().size() %>/<%= lobby.getMaxJoueurs() %></p>
<ul>
    <% 
        List<Utilisateur> lobbyUsers = lobby.getUtilisateurs();
        for (Utilisateur user : lobbyUsers) {
    %>
        <li>
            <%= user.getNomUtilisateur() %>
            <% if (lobby.isReady(user)) { %>
                <span>(Ready)</span>
            <% } %>
        </li>
    <% } %>
</ul>
<form action="<%= request.getContextPath() %>/lobby/ready" method="post">
    <input type="hidden" name="gameCode" value="<%= request.getAttribute("gameCode") %>">
    <button type="submit">I'm Ready</button>
</form>
