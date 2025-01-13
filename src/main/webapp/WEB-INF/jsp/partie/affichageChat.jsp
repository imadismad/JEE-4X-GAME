<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%
    List<String> chatMessages = (List<String>) request.getAttribute("chatMessages");
%>
<div id="chat">
    <h3>Chat du jeu</h3>
    <ul id="console">
        <!-- Les messages du chat seront ajoutés dynamiquement ici -->
    </ul>
    <!-- Champ de saisie et bouton d'envoi -->
    <div id="chat-input-container">
        <input type="text" id="chat-input" placeholder="Entrez votre message..." />
        <button id="chat-send-button">Envoyer</button>
    </div>
</div>
