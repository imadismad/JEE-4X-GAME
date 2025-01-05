<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="model.Partie, model.Tuile, model.Soldat, model.Joueur, model.Utilisateur" %>
<%@ page import="utils.StringUtils" %>
<%
    Partie partie = (Partie) request.getAttribute("partie");
    Tuile[][] tuiles = partie.getTuiles();
    List<Soldat> soldats = partie.getSoldats();
    String joueurConnecte = ((Utilisateur) request.getSession().getAttribute(Utilisateur.CLEF_UTILISATEUR_SESSION)).getNomUtilisateur();
%>
<html>
<head>
    <title>Affichage de la Grille</title>
    <style>
        table {
            border-collapse: collapse;
            margin: auto;
        }
        td {
            width: 50px;
            height: 50px;
            position: relative;
            text-align: center;
            vertical-align: middle;
        }
        .tuile {
            position: absolute;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
        }
        .soldat {
            position: absolute;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
        }
	    #legende {
	        margin-top: 20px;
	        text-align: center;
	    }
	    .legende-joueur {
	        background-color: var(--couleur-joueur, gray); /* Couleur par défaut */
	        color: white;
	        padding: 5px 10px;
	        margin: 5px auto;
	        width: 150px;
	        text-align: center;
	        border-radius: 5px;
	        font-weight: bold;
	    }
    </style>
    <script>
        // Variables dynamiques générées par le serveur
        const joueurConnecte = "<%= joueurConnecte %>";
        const soldats = [
            <% for (Soldat soldat : soldats) { %>
                {
                    joueur: "<%= soldat.getJoueur().getUtilisateur().getNomUtilisateur() %>",
                    x: <%= soldat.getX() %>,
                    y: <%= soldat.getY() %>
                },
            <% } %>
        ];
        const joueurs = [
            <% for (Joueur joueur : partie.getJoueurs()) { %>
                <% if (joueur != null) { %>
                    "<%= joueur.getUtilisateur().getNomUtilisateur() %>",
                <% } %>
            <% } %>
        ];
    </script>
    <script src="<%= request.getContextPath() %>/assets/js/affichageSoldats.js"></script>
</head>
<body>
    <table>
        <% for (int i = 0; i < tuiles.length; i++) { %>
            <tr>
                <% for (int j = 0; j < tuiles[i].length; j++) { %>
                    <td>
                        <img class="tuile" src="<%= request.getContextPath() %>/assets/images/tiles/<%= StringUtils.enleverAccents(tuiles[i][j].getType()) %>.png" alt="Tuile" />
                        <% Soldat soldat = tuiles[i][j].getSoldat(partie); %>
                        <% if (soldat != null) { %>
                            <img class="soldat" src="<%= request.getContextPath() %>/assets/images/soldats/soldat.png" 
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
    <div id="legende"></div>
</body>
</html>
