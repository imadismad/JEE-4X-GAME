package controlers.api;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.*;
import utils.JSON.*;

import java.io.IOException;

@SuppressWarnings("serial")
@WebServlet("/api/grille")
public class GrilleController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Utilisateur utilisateur = (Utilisateur) request.getSession().getAttribute(Utilisateur.CLEF_UTILISATEUR_SESSION);

        if (utilisateur == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\":\"Utilisateur non authentifié\"}");
            return;
        }

        Partie partie = utilisateur.getJoueur().getPartie();

        if (partie == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().write("{\"error\":\"Aucune partie trouvée pour l'utilisateur\"}");
            return;
        }

        JSONObject grilleJSON = new JSONObject();
        JSONArray grilleArray = new JSONArray();

        try {
            Tuile[][] tuiles = partie.getTuiles();
            for (int i = 0; i < tuiles.length; i++) {
                JSONArray ligneArray = new JSONArray();
                for (int j = 0; j < tuiles[i].length; j++) {
                    JSONObject tuileJSON = new JSONObject();
                    tuileJSON.ajouter("type", tuiles[i][j].getType());

                    // Ajouter les informations du propriétaire si c'est une ville
                    if (tuiles[i][j] instanceof Ville) {
                        Ville ville = (Ville) tuiles[i][j];
                        tuileJSON.ajouter("hp", ville.getDP());
                        if (ville.getAppartenance() != null) {
                            tuileJSON.ajouter("proprietaire", ville.getAppartenance().getUtilisateur().getNomUtilisateur());
                        }
                    }

                    // Ajouter les informations sur le soldat
                    Soldat soldat = tuiles[i][j].getSoldat(partie);
                    if (soldat != null) {
                        JSONObject soldatJSON = new JSONObject();
                        soldatJSON.ajouter("joueur", soldat.getJoueur().getUtilisateur().getNomUtilisateur());
                        soldatJSON.ajouter("x", soldat.getX());
                        soldatJSON.ajouter("y", soldat.getY());
                        soldatJSON.ajouter("hp", soldat.getVie());
                        tuileJSON.ajouter("soldat", soldatJSON);
                    }

                    ligneArray.ajouter(tuileJSON);
                }
                grilleArray.ajouter(ligneArray);
            }

            grilleJSON.ajouter("grille", grilleArray);

            JSONArray joueursArray = new JSONArray();
            for (Joueur joueur : partie.getJoueurs()) {
                if (joueur != null) {
                    JSONObject joueurJSON = new JSONObject();
                    joueurJSON.ajouter("nom", joueur.getUtilisateur().getNomUtilisateur());
                    joueurJSON.ajouter("pp", joueur.getPP());
                    joueurJSON.ajouter("score", joueur.getScore());
                    joueursArray.ajouter(joueurJSON);
                }
            }
            grilleJSON.ajouter("joueurs", joueursArray);

            // Log du JSON pour débogage
            System.out.println("JSON généré : " + grilleJSON.toJSONString());

            // Envoi de la réponse
            response.setContentType("application/json");
            response.getWriter().write(grilleJSON.toJSONString());
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\":\"Erreur lors de la génération de la grille\"}");
        }
    }
}

