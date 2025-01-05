<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.Partie, model.Tuile, model.Soldat, model.Joueur, model.Utilisateur" %>
<!DOCTYPE html>
<html>
<head>
    <title>Partie Principale</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/general/style.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/partie/affichageGrille.css">
</head>
<body>
    <div class="partie-container">
        <!-- Section Grille -->
        <div class="grille-section">
            <jsp:include page="/WEB-INF/jsp/partie/affichageGrille.jsp" />
        </div>

        <!-- Section Actions -->
        <div class="actions-section">
            <jsp:include page="/WEB-INF/jsp/partie/affichageActions.jsp" />
        </div>

        <!-- Section Chat -->
        <div class="chat-section">
            <jsp:include page="/WEB-INF/jsp/partie/affichageChat.jsp" />
        </div>

        <!-- Section Tour -->
        <div class="tour-section">
            <jsp:include page="/WEB-INF/jsp/partie/affichageTour.jsp" />
        </div>
    </div>

    <!-- Variables dynamiques générées par le serveur -->
    <script>
        const joueurConnecte = "<%= ((Utilisateur) request.getSession().getAttribute(Utilisateur.CLEF_UTILISATEUR_SESSION)).getNomUtilisateur() %>";
        const soldats = [
            <% for (Soldat soldat : ((Partie)((Utilisateur)request.getSession().getAttribute(Utilisateur.CLEF_UTILISATEUR_SESSION)).getJoueur().getPartie()).getSoldats()) { %>
                {
                    joueur: "<%= soldat.getJoueur().getUtilisateur().getNomUtilisateur() %>",
                    x: <%= soldat.getX() %>,
                    y: <%= soldat.getY() %>
                },
            <% } %>
        ];
        const joueurs = [
            <% for (Joueur joueur : ((Partie)((Utilisateur)request.getSession().getAttribute(Utilisateur.CLEF_UTILISATEUR_SESSION)).getJoueur().getPartie()).getJoueurs()) { %>
                <% if (joueur != null) { %>
                    "<%= joueur.getUtilisateur().getNomUtilisateur() %>",
                <% } %>
            <% } %>
        ];
    </script>

    <!-- Inclusion du fichier JS externe -->
    <script src="<%= request.getContextPath() %>/assets/js/affichageSoldats.js"></script>
</body>
</html>

