package controlers.api;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Joueur;
import model.Partie;
import model.Soldat;
import model.Tuile;
import model.Ville;

@SuppressWarnings("serial")
@WebServlet("/api/actionPossible")
public class ActionPossibleController extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    response.setContentType("application/json");

	    // Vérification de l'authentification
	    Joueur joueur = (Joueur) request.getSession().getAttribute("joueur");
	    if (joueur == null) {
	        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 Unauthorized
	        response.getWriter().write("{\"error\": \"Utilisateur non authentifié.\"}");
	        return;
	    }

	    // Vérification que le joueur est dans une partie
	    Partie partie = joueur.getPartie();
	    if (partie == null) {
	        response.setStatus(HttpServletResponse.SC_FORBIDDEN); // 403 Forbidden
	        response.getWriter().write("{\"error\": \"Vous n'êtes pas dans une partie.\"}");
	        return;
	    }

	    try {
	        // Récupération des coordonnées X et Y
	        int x = Integer.parseInt(request.getParameter("x"));
	        int y = Integer.parseInt(request.getParameter("y"));

	        // Vérification de l'unité à la position donnée
	        Tuile tuile = partie.getTuile(x, y);
	        if (tuile == null) {
	            response.setStatus(HttpServletResponse.SC_NOT_FOUND); // 404 Not Found
	            response.getWriter().write("{\"error\": \"Tuile introuvable.\"}");
	            return;
	        }

	        Soldat soldat = tuile.getSoldat(partie);

	        if (soldat != null) {
	            // Vérifie si le soldat appartient au joueur actuel
	            if (!soldat.getJoueur().equals(joueur)) {
	                response.setStatus(HttpServletResponse.SC_FORBIDDEN); // 403 Forbidden
	                response.getWriter().write("{\"error\": \"Ce soldat ne vous appartient pas.\"}");
	                return;
	            }

	            // Vérifie si le soldat est mort
	            if (soldat.estMort()) {
	                response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400 Bad Request
	                response.getWriter().write("{\"error\": \"Ce soldat est mort et ne peut pas agir.\"}");
	                return;
	            }

	            // Actions possibles pour le soldat
	            response.setStatus(HttpServletResponse.SC_OK); // 200 OK
	            response.getWriter().write("{");
	            response.getWriter().write("\"haut\": " + soldat.peutSeDeplacer("haut") + ",");
	            response.getWriter().write("\"bas\": " + soldat.peutSeDeplacer("bas") + ",");
	            response.getWriter().write("\"gauche\": " + soldat.peutSeDeplacer("gauche") + ",");
	            response.getWriter().write("\"droite\": " + soldat.peutSeDeplacer("droite") + ",");
	            response.getWriter().write("\"defricher\": " + soldat.peutDefricher() + ",");
	            response.getWriter().write("\"soigner\": " + soldat.peutSeSoigner());
	            response.getWriter().write("}");
	            return;
	        } else if (tuile instanceof Ville) {
	            Ville ville = (Ville) tuile;
	            response.setStatus(HttpServletResponse.SC_OK); // 200 OK
	            response.getWriter().write("{\"recruter\": " + (ville.getAppartenance().getPP() >= 15) + "}");
	            return;
	        } else {
	            response.setStatus(HttpServletResponse.SC_NOT_FOUND); // 404 Not Found
	            response.getWriter().write("{\"error\": \"Aucun soldat ou action possible sur cette tuile.\"}");
	        }

	    } catch (NumberFormatException e) {
	        response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400 Bad Request
	        response.getWriter().write("{\"error\": \"Coordonnées invalides.\"}");
	    }
	}
}
