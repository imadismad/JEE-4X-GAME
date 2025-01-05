<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="model.Partie, model.Tuile, model.Soldat, model.Joueur, model.Utilisateur" %>
<%@ page import="utils.StringUtils" %>
<%
    // Récupérer l'utilisateur connecté depuis la session
    Utilisateur utilisateur = (Utilisateur) request.getSession().getAttribute(Utilisateur.CLEF_UTILISATEUR_SESSION);

    if (utilisateur == null) {
        throw new ServletException("Utilisateur non connecté.");
    }

    // Obtenir la partie depuis le joueur associé à l'utilisateur
    Partie partie = utilisateur.getJoueur().getPartie();

    if (partie == null) {
        throw new ServletException("Aucune partie associée au joueur.");
    }

    Tuile[][] tuiles = partie.getTuiles();
    List<Soldat> soldats = partie.getSoldats();
    String joueurConnecte = utilisateur.getNomUtilisateur();
%>


<div id="grille-container">
    <!-- Table de la grille -->
    <table>
        <% for (int i = 0; i < tuiles.length; i++) { %>
            <tr>
                <% for (int j = 0; j < tuiles[i].length; j++) { %>
                    <td>
                        <img class="tuile" 
                             src="<%= request.getContextPath() %>/assets/images/tiles/<%= StringUtils.enleverAccents(tuiles[i][j].getType()) %>.png" 
                             alt="Tuile" />
                        <% Soldat soldat = tuiles[i][j].getSoldat(partie); %>
                        <% if (soldat != null) { %>
                            <img class="soldat" 
                                 src="<%= request.getContextPath() %>/assets/images/soldats/soldat.png" 
                                 data-x="<%= soldat.getX() %>" 
                                 data-y="<%= soldat.getY() %>" 
                                 data-joueur="<%= soldat.getJoueur().getUtilisateur().getNomUtilisateur() %>" 
                                 alt="Soldat" />
                        <% } %>
                    </td>
                <% } %>
            </tr>
        <% } %>
    </table>

    <!-- Légende des joueurs -->
    <div id="legende"></div>
</div>
