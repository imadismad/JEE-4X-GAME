package controlers.api;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.*;
import utils.JSON.*;

import java.io.IOException;

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

        // Vérification si la partie est terminée
        if (partie.isPartieTerminee()) {
            Joueur gagnant = partie.getGagnant();

            // Préparer la réponse JSON pour l'écran de fin
            JSONObject resultatFin = new JSONObject();
            JSONArray scores = new JSONArray();

            for (Joueur joueurPartie : partie.getJoueurs()) {
                if (joueurPartie != null) {
                    JSONObject joueurJSON = new JSONObject();
                    joueurJSON.ajouter("nom", joueurPartie.getUtilisateur().getNomUtilisateur());
                    joueurJSON.ajouter("score", joueurPartie.getScore());
                    joueurJSON.ajouter("pp", joueurPartie.getPP());
                    scores.ajouter(joueurJSON);
                }
            }

            if (gagnant != null) {
                resultatFin.ajouter("gagnant", gagnant.getUtilisateur().getNomUtilisateur());
            } else {
                resultatFin.ajouter("gagnant", "Aucun"); // Pas de gagnant
            }
            resultatFin.ajouter("scores", scores);

            response.setContentType("application/json");
            response.getWriter().write(resultatFin.toString());
            return;
        }

        // Sinon, retour standard
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
