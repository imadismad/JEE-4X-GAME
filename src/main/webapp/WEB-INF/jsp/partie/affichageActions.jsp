<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    String selection = (String) request.getAttribute("selection"); // Exemple
%>
<div id="menu-actions">
    <h3>Actions disponibles</h3>
    <% 
        if ("soldat".equals(selection)) { 
    %>
        <button>Déplacer</button>
        <button>Attaquer</button>
    <% } else if ("ville".equals(selection)) { %>
        <button>Capturer</button>
    <% } else { %>
        <p>Pas d'action disponible</p>
    <% } %>
</div>
