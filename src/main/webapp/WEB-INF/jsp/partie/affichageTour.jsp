<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    String joueurTour = (String) request.getAttribute("joueurTour");
%>
<div id="tour-actuel">
    <h3>Tour actuel</h3>
    <% 
        if (joueurTour != null) { 
    %>
        <p>C'est le tour de : <b><%= joueurTour %></b></p>
    <% } else { %>
        <p>En attente...</p>
    <% } %>
</div>
