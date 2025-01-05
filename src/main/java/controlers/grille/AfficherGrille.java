package controlers.grille;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Partie;
import model.Utilisateur;
import model.Joueur;

import java.io.IOException;

public class AfficherGrille extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Récupérer l'utilisateur connecté depuis la session
        Utilisateur utilisateur = (Utilisateur) request.getSession().getAttribute(Utilisateur.CLEF_UTILISATEUR_SESSION);
        if (utilisateur == null) {
            response.sendRedirect(request.getContextPath() + "/compte/connexion"); // Rediriger si pas connecté
            return;
        }

        // Récupérer l'objet Joueur de l'utilisateur
        Joueur joueur = utilisateur.getJoueur(); // Utilisation du joueur pour récupérer la partie
        if (joueur == null) {
            response.sendRedirect(request.getContextPath() + "/lobby"); // Rediriger si pas encore dans une partie
            return;
        }

        // Récupérer la partie associée au joueur
        Partie partie = joueur.getPartie();
        if (partie == null) {
            response.sendRedirect(request.getContextPath() + "/lobby"); // Rediriger si aucune partie
            return;
        }

        // Ajouter les données de la partie à la requête
        request.setAttribute("partie", partie);

        // Transmettre les données à la JSP
        request.getRequestDispatcher("/WEB-INF/jsp/partie/affichageGrille.jsp").forward(request, response);
    }
}