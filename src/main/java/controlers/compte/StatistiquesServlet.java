package controlers.compte;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Utilisateur;

import java.io.IOException;

@WebServlet("/statistiques")
public class StatistiquesServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Vérifier si l'utilisateur est connecté
        Utilisateur utilisateur = Utilisateur.getUtilisateur(request.getSession());
        if (utilisateur == null) {
            // Rediriger vers la page de connexion si non connecté
            response.sendRedirect(request.getContextPath() + "/compte/connexion");
            return;
        }

        // Passer les informations nécessaires à la JSP
        request.setAttribute("nomUtilisateur", utilisateur.getNomUtilisateur());

        // Afficher la page statistiques.jsp
        request.getRequestDispatcher("/WEB-INF/jsp/partie/statistiques.jsp").forward(request, response);
    }
}

