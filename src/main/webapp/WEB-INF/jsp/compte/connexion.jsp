<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Connexion</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/general/style.css">
    <script src="${pageContext.request.contextPath}/assets/js/login.js"></script>
</head>
<body>
    <%@ include file="/WEB-INF/jsp/templates/header.jsp" %>

    <div class="main-container">
        <h2>Connexion</h2>
        
        <!-- Handling the error message using scriptlets -->
        <%
            String messageErreur = (String) request.getAttribute("messageErreur");
            if (messageErreur != null && !messageErreur.isEmpty()) {
        %>
            <div class="error-message">
                <%= messageErreur %>
            </div>
        <%
            }
        %>

        <!-- Login form -->
        <form action="<%= request.getContextPath() %>/compte/connexion" method="post">
            <div>
                <label for="nomUtilisateur">Nom d'utilisateur :</label>
                <input type="text" id="nomUtilisateur" name="nomUtilisateur" placeholder="Entrez votre nom d'utilisateur" minlength="3" required>
            </div>
            <div>
                <label for="motDePasse">Mot de passe :</label>
                <input type="password" id="motDePasse" name="motDePasse" placeholder="Entrez votre mot de passe" minlength="4" required>
                <input type="checkbox" id="showPassword" onclick="togglePassword()"> Afficher le mot de passe
            </div>
            <div>
                <button type="submit">Se connecter</button>
            </div>
        </form>

        <!-- Navigation link -->
        <p>Vous n'avez pas de compte ? <a href="${pageContext.request.contextPath}/compte/creation">Créez-en un ici</a>.</p>
    </div>

    <%@ include file="/WEB-INF/jsp/templates/footer.jsp" %>
</body>
</html>
