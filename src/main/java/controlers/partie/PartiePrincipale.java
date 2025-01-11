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

        if (utilisateur.getJoueur().getPartie().estFin()) {
        	response.setStatus(480);
        	response.getWriter().write("La partie est déjà terminée");
        	return;
        }

        // Rediriger vers la JSP principale
        request.getRequestDispatcher("/WEB-INF/jsp/partie/partiePrincipale.jsp").forward(request, response);
    }
}
