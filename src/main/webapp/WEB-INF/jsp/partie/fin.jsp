<%@page import="model.Utilisateur, model.Joueur, model.Partie"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<div>
	<%
		Utilisateur u = Utilisateur.getUtilisateur(request.getSession());
		Partie p = u.getJoueur().getPartie();
		for (Joueur j : p.getJoueurs()) {
	%>
	<div>Joueur <%= j.getUtilisateur().getNomUtilisateur() %> avec <%= j.getScore() %> point de score.</div>
	<%
		}
	%>
</div>