<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.Utilisateur" %>
<header class="header-container">
    <h1>JEE 4X GAME</h1>
    <nav>
        <%
            Utilisateur utilisateurHeader = Utilisateur.getUtilisateur(session);
            if (utilisateurHeader != null) {
        %>
            <a href="${pageContext.request.contextPath}/compte/deconnexion">Déconnexion</a>
            |
            <a href="${pageContext.request.contextPath}/statistiques">Statistiques</a>
        <%
            } else {
        %>
            <a href="${pageContext.request.contextPath}/compte/connexion">Connexion</a>
            |
            <a href="${pageContext.request.contextPath}/compte/creation">Créer un compte</a>
        <%
            }
        %>
    </nav>
</header>
