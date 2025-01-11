<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Créer un compte</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/general/style.css">
    <script src="${pageContext.request.contextPath}/assets/js/validation.js"></script>
</head>
<body>
    <%@ include file="/WEB-INF/jsp/templates/header.jsp" %>

    <div class="main-container">
        <h2>Créer un compte</h2>
        
        <!-- Handling the error message using scriptlets -->
        <%
            String messageErreur = (String) request.getAttribute("messageErreur");
            if (messageErreur != null && !messageErreur.isBlank()) {
        %>
            <div class="error-message">
                <%= messageErreur %>
            </div>
        <%
            }
        %>

        <!-- Account creation form -->
        <form action="<%= getServletContext().getContextPath() %>/compte/creation" method="post" onsubmit="return validatePasswords();">
            <div>
                <label for="nomUtilisateur">Nom d'utilisateur :</label>
                <input type="text" id="nomUtilisateur" name="nomUtilisateur" placeholder="Entrez votre nom d'utilisateur" minlength="3" required>
            </div>
            <div>
                <label for="motDePasse">Mot de passe :</label>
                <input type="password" id="motDePasse" name="motDePasse" placeholder="Entrez votre mot de passe" minlength="4" required>
            </div>
            <div>
                <label for="confirmMotDePasse">Confirmez le mot de passe :</label>
                <input type="password" id="confirmMotDePasse" name="confirmMotDePasse" placeholder="Confirmez votre mot de passe" minlength="4" required>
            </div>
            <div>
                <button type="submit">Créer un compte</button>
            </div>
        </form>

        <!-- Navigation link -->
        <p>Vous avez déjà un compte ? <a href="${pageContext.request.contextPath}/compte/connexion">Connectez-vous ici</a>.</p>
    </div>

    <%@ include file="/WEB-INF/jsp/templates/footer.jsp" %>
</body>
</html>
