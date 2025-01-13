<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List, java.util.Map" %>
<%@ page import="model.storage.StockageInterface" %>
<%@ page import="model.storage.structure.ScoresStructure" %>
<%@ page import="model.Utilisateur" %>
<%@ page import="model.storage.exception.StockageAccesException, model.storage.exception.StockageValeurException" %>
<%@ include file="/WEB-INF/jsp/templates/header.jsp" %>
<link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/general/style.css">
<link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/statistiques/style.css">
<%
    // Récupération de l'utilisateur connecté
    Utilisateur utilisateur = Utilisateur.getUtilisateur(session);
    if (utilisateur == null) {
        response.sendRedirect(request.getContextPath() + "/compte/connexion");
        return;
    }

    String nomUtilisateur = utilisateur.getNomUtilisateur();

    List<Map<model.storage.structure.StructureInterface, Object>> scores = null;
    try {
        StockageInterface stockage = StockageInterface.getInstance();
        scores = stockage.getToutesEntrees(
            model.storage.structure.TableEnum.SCORES,
            StockageInterface.filtreScoresUtilisateur(nomUtilisateur)
        );
    } catch (Exception e) {
        e.printStackTrace(); // Debugging
        request.setAttribute("error", "Erreur lors de la récupération des données.");
    }
%>

<div class="statistiques-container">
    <h1>Statistiques de <%= nomUtilisateur %></h1>

    <%
        if (scores == null || scores.isEmpty()) {
    %>
        <p>Aucune donnée disponible pour cet utilisateur.</p>
    <%
        } else {
    %>
        <div class="statistiques-content">
            <table>
                <thead>
                    <tr>
                        <th>Date</th>
                        <th>Score</th>
                        <th>Victoire</th>
                    </tr>
                </thead>
                <tbody>
                <%
                    for (Map<model.storage.structure.StructureInterface, Object> score : scores) {
                        String date = (String) score.get(ScoresStructure.DATE);
                        Integer scoreValue = (Integer) score.get(ScoresStructure.SCORE);
                        Boolean victoire = (Boolean) score.get(ScoresStructure.VICTOIRE);
                %>
                    <tr>
                        <td><%= date %></td>
                        <td><%= scoreValue %></td>
                        <td><%= victoire ? "Oui" : "Non" %></td>
                    </tr>
                <%
                    }
                %>
                </tbody>
            </table>
        </div>
    <%
        }
    %>
</div>

<%@ include file="/WEB-INF/jsp/templates/footer.jsp" %>
