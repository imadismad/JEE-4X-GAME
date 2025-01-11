package controlers.api;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Direction;
import model.Foret;
import model.Joueur;
import model.Partie;
import model.Soldat;
import model.Tuile;
import model.Utilisateur;
import model.Ville;

@SuppressWarnings("serial")
@WebServlet("/api/action")
public class ActionController extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Récupérer la session et l'utilisateur à partir de la session
        HttpSession session = request.getSession();
        Utilisateur utilisateur = (Utilisateur) session.getAttribute("utilisateur");

        // Vérification si l'utilisateur est authentifié
        if (utilisateur == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        
        // Vérification de si le joueur a le droit de faire une action
        if (!utilisateur.getJoueur().getPartie().estTourDe(utilisateur.getJoueur())) {
        	response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // Récupérer les autres paramètres
        int x = Integer.parseInt(request.getParameter("x"));
        int y = Integer.parseInt(request.getParameter("y"));
        String actionParam = request.getParameter("action");
        String directionParam = request.getParameter("direction");

        // Récupérer la partie en cours et le joueur associé à l'utilisateur
        Partie partie = utilisateur.getJoueur().getPartie();
        if (partie == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // Récupérer le joueur en fonction de l'utilisateur
        Joueur joueur = utilisateur.getJoueur();
        if (joueur == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // Trouver le soldat ou la tuile à l'emplacement (x, y)
        Soldat soldat = joueur.getPartie().getSoldatParCoordonnees(x, y);
        Tuile tuile = partie.getTuile(x, y);

        // Traitement de l'action
        switch (actionParam.toUpperCase()) {
            case "DEPLACER":
                // Si l'action est un déplacement, on vérifie la direction
                Direction direction = Direction.valueOf(directionParam.toUpperCase());

                switch (direction) {
                    case HAUT:
                    case BAS:
                    case GAUCHE:
                    case DROITE:
                        // Déplacer le soldat
                        if (soldat != null) {
                            soldat.deplacer(direction);
                            response.setStatus(HttpServletResponse.SC_OK); // Action réussie
                        } else {
                            response.setStatus(HttpServletResponse.SC_NOT_FOUND); // Soldat non trouvé
                        }
                        break;

                    default:
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // Direction invalide
                        break;
                }
                break;

            case "DEFRICHER":
                // Si l'action est un défrichage
                if (soldat != null && tuile instanceof Foret) {
                    ((Foret) tuile).defricher(soldat);
                    response.setStatus(HttpServletResponse.SC_OK); // Action réussie
                } else {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // Pas une forêt ou soldat non trouvé
                }
                break;

            case "SOIGNER":
                // Si l'action est un soin
                if (soldat != null) {
                    soldat.soin();
                    response.setStatus(HttpServletResponse.SC_OK); // Action réussie
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND); // Soldat non trouvé
                }
                break;

            case "CREER_SOLDAT":
                // Si l'action est la création de soldat
                if (tuile instanceof Ville) {
                    Ville ville = (Ville) tuile;
                    if (ville.getAppartenance() == joueur) {
                        if (joueur.creerSoldatSurVille(ville)) {
                            response.setStatus(HttpServletResponse.SC_OK); // Création réussie
                        } else {
                            response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // Échec création soldat
                        }
                    } else {
                        response.setStatus(HttpServletResponse.SC_FORBIDDEN); // Ville n'appartient pas au joueur
                    }
                } else {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // Pas une ville
                }
                break;

            default:
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // Action non reconnue
                break;
        }
        
        // On a fait une action, mais il est possible que cette action est amené à la mort d'un joueur
        int joueurRestant = 0;

        for (Joueur j : partie.getJoueurs()) {
            if (j != null && j.hasSoldatsEtVilles()) {
                joueurRestant +=1; // Compte les joueurs encore actifs
            }
        }
        
        // Si un seul joueur est encore en vie, la partie se termine
        if (joueurRestant == 1) {
            partie.notifierFinPartie();
        }
    }
}
