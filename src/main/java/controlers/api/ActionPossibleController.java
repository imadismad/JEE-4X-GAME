package controlers.api;

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

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/api/actionPossible")
public class ActionPossibleController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Vérification de l'authentification
        Joueur joueur = (Joueur) request.getSession().getAttribute("joueur");
        if (joueur == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 Unauthorized
            return;
        }

        // Vérification que le joueur est dans une partie
        Partie partie = joueur.getPartie();
        if (partie == null) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN); // 403 Forbidden
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
                return;
            }

            // Configuration de la réponse
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_OK); // 200 OK
            PrintWriter writer = response.getWriter();

            writer.write("{");

         // Récupération du soldat sur la tuile
            Soldat soldat = tuile.getSoldat(partie);

            // Vérification qu'un soldat est bien présent sur la tuile
            if (soldat != null) {
                // Vérifie si le soldat appartient au joueur actuel
                if (!soldat.getJoueur().equals(joueur)) {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN); // 403 Forbidden
                    writer.close();
                    return;
                }

                // Vérifie si le soldat est mort
                if (soldat.estMort()) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400 Bad Request
                    writer.close();
                    return;
                }

                // Ajout des actions possibles pour le soldat
                writer.write("\"haut\": " + soldat.peutSeDeplacer("haut") + ",");
                writer.write("\"bas\": " + soldat.peutSeDeplacer("bas") + ",");
                writer.write("\"gauche\": " + soldat.peutSeDeplacer("gauche") + ",");
                writer.write("\"droite\": " + soldat.peutSeDeplacer("droite") + ",");
                writer.write("\"defricher\": " + soldat.peutDefricher() + ",");
                writer.write("\"soigner\": " + soldat.peutSeSoigner());
            } else if (tuile instanceof Ville) {
                Ville ville = (Ville) tuile;

                // Ajout des actions possibles pour la ville
                writer.write("\"recruter\": " + (ville.getAppartenance().getPP()>=15));
            }

            writer.write("}");
            writer.close();

        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400 Bad Request
        }
    }
}
