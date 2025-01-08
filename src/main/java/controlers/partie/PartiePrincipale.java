package controlers.partie;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Utilisateur;

import java.io.IOException;
import java.util.List;

public class PartiePrincipale extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Récupérer l'utilisateur connecté
        Utilisateur utilisateur = (Utilisateur) request.getSession().getAttribute(Utilisateur.CLEF_UTILISATEUR_SESSION);

        if (utilisateur == null) {
            response.sendRedirect(request.getContextPath() + "/compte/connexion");
            return;
        }

        // Messages du chat (exemple)
        List<String> chatMessages = List.of("Joueur1 a capturé une ville", "Joueur2 a déplacé un soldat");

        // Définir les données contextuelles pour la vue
        request.setAttribute("joueurTour", utilisateur.getJoueur().getPartie().getJoueurs()[0].getUtilisateur().getNomUtilisateur()); // Exemple
        request.setAttribute("chatMessages", chatMessages);

        // Rediriger vers la JSP principale
        request.getRequestDispatcher("/WEB-INF/jsp/partie/partiePrincipale.jsp").forward(request, response);
    }
}
