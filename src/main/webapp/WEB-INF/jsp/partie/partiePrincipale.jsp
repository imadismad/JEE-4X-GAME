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
    <%@ include file="/WEB-INF/jsp/templates/header.jsp" %>

    <div class="partie-container">
		<div id="points-production-container">
		    <div class="info-section">
		        <span class="info-label">Points de Production :</span>
		        <span id="points-production">0</span>
		    </div>
		    <hr>
		    <div class="info-section">
		        <span class="info-label">Score :</span>
		        <span id="score">0</span>
		    </div>
		</div>
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
    
    <div id="ecran-fin" style="display: none;">
	    <div id="fin-container">
	        <h2>Fin de la Partie</h2>
	        <p>Voici les scores finaux :</p>
	        <table id="tableau-scores">
	            <thead>
	                <tr>
	                    <th>Joueur</th>
	                    <th>Score</th>
	                    <th>Points de Production</th>
	                </tr>
	            </thead>
	            <tbody>
	                <!-- Les scores seront injectés ici -->
	            </tbody>
	        </table>
	        <button id="revenir-accueil">Retour à l'Accueil</button>
	    </div>
	</div>

    <!-- Chargement des scripts JavaScript -->
	<script>
		const joueurConnecte = "<%= ((Utilisateur) request.getSession().getAttribute(Utilisateur.CLEF_UTILISATEUR_SESSION)).getNomUtilisateur() %>";
	</script>
	<script src="<%= request.getContextPath() %>/assets/js/grilleManager.js"></script>
	<script src="<%= request.getContextPath() %>/assets/js/affichageSoldats.js"></script>
	<script src="<%= request.getContextPath() %>/assets/js/consoleJeu.js"></script>
	
	<%@ include file="/WEB-INF/jsp/templates/footer.jsp" %>
</body>
</html>