<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Partie Principale</title>
    <!-- Ajoutez les styles et scripts globaux -->
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/styles.css">
    <script src="<%= request.getContextPath() %>/assets/js/scripts.js"></script>
</head>
<body>
    <div class="partie-container">
        <!-- Inclusion de la grille -->
        <div class="grille-section">
            <jsp:include page="/WEB-INF/jsp/partie/affichageGrille.jsp" />
        </div>
        
        <!-- Inclusion des actions -->
        <div class="actions-section">
            <jsp:include page="/WEB-INF/jsp/partie/affichageActions.jsp" />
        </div>
        
        <!-- Inclusion du chat -->
        <div class="chat-section">
            <jsp:include page="/WEB-INF/jsp/partie/affichageChat.jsp" />
        </div>
        
        <!-- Inclusion de l'indication du tour -->
        <div class="tour-section">
            <jsp:include page="/WEB-INF/jsp/partie/affichageTour.jsp" />
        </div>
    </div>
</body>
</html>
