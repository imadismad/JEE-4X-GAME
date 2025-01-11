<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="model.Partie, model.Utilisateur" %>
<%
    Utilisateur utilisateur = (Utilisateur) request.getSession().getAttribute(Utilisateur.CLEF_UTILISATEUR_SESSION);
    Partie partie = utilisateur.getJoueur().getPartie();
%>

<!DOCTYPE html>
<html>
<head>
    <title>Game</title>
</head>
<body>
    <h1>Game Board</h1>
    <p>Welcome, <%= utilisateur.getNomUtilisateur() %>!</p>

    <!-- Display the game board -->
    <div id="game-board">
        <!-- Include your game grid and other interactive elements -->
    </div>
</body>
</html>
