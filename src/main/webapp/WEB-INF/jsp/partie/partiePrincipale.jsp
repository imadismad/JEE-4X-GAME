<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.Utilisateur" %>
<!DOCTYPE html>
<html>
<head>
    <title>Partie Principale</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/general/style.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/partie/affichageGrille.css">
</head>
<body>
    <%@ include file="/WEB-INF/jsp/templates/header.jsp" %>

    <div class="partie-container">
        <!-- Section Grille -->
        <div class="grille-section">
            <jsp:include page="/WEB-INF/jsp/partie/affichageGrille.jsp" />
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

    <!-- Chargement des scripts JavaScript -->
	<script>
	    const joueurConnecte = "<%= ((Utilisateur) request.getSession().getAttribute(Utilisateur.CLEF_UTILISATEUR_SESSION)).getNomUtilisateur() %>";
	</script>
	<script src="<%= request.getContextPath() %>/assets/js/grilleManager.js"></script>
	<script src="<%= request.getContextPath() %>/assets/js/affichageSoldats.js"></script>

    <%@ include file="/WEB-INF/jsp/templates/footer.jsp" %>
</body>
</html>
