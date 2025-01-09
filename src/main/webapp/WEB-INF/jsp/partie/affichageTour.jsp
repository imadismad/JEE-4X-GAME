<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="model.Utilisateur, model.Partie, model.Joueur"%>
<%
    Utilisateur util = Utilisateur.getUtilisateur(request.getSession());
	Partie partie = util.getJoueur().getPartie();
	Joueur currentJoueur = partie.getJoueurs()[partie.getTour()];
	String text = "";
	
	if (partie != null && currentJoueur != null) 
		text = currentJoueur.getUtilisateur().getNomUtilisateur();
	else
		text = "En attente ...";

%>
<div id="tour-actuel">
    <h3>Tour actuel</h3>
        <p>C'est le tour de : <b id="emplacement-joueur" data-nom-joueur="<%= util.getNomUtilisateur() %>" ><%= text %></b></p>
</div>
