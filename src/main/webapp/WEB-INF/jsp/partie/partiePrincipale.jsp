<%@ page language="java"
         contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"
         import="model.Partie, model.Joueur, model.Utilisateur" %>

<!DOCTYPE html>
<html>
<head>
    <title>Partie Principale</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/general/style.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/partie/affichageGrille.css">
</head>
<body>
	<%@ include file="/WEB-INF/jsp/templates/header.jsp" %>
	
	<%
	    // 1) Retrieve the Partie (set by the servlet in request scope)
	    Partie partie = (Partie) request.getAttribute("partie");
	    if (partie == null) {
	        out.println("<p>Erreur: aucune partie n'est disponible!</p>");
	        return;
	    }
	
	    // 2) We can also get the user from session if we need it
	    Utilisateur utilisateur = (Utilisateur) session.getAttribute(Utilisateur.CLEF_UTILISATEUR_SESSION);
	
	    // 3) Retrieve the array of Joueurs from the partie
	    Joueur[] joueurs = partie.getJoueurs();
	%>
	
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
	
	<!-- Some JavaScript to load your client logic -->
	<script>
		const joueurConnecte = "<%= ((Utilisateur) request.getSession().getAttribute(Utilisateur.CLEF_UTILISATEUR_SESSION)).getNomUtilisateur() %>";
	</script>
	<script src="<%= request.getContextPath() %>/assets/js/grilleManager.js"></script>
	<script src="<%= request.getContextPath() %>/assets/js/affichageSoldats.js"></script>
	<script src="<%= request.getContextPath() %>/assets/js/consoleJeu.js"></script>
	
	<%@ include file="/WEB-INF/jsp/templates/footer.jsp" %>
</body>
</html>