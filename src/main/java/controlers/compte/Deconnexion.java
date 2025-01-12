package controlers.compte;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/compte/deconnexion")
public class Deconnexion extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Récupération de la session en cours
        HttpSession session = request.getSession(false); // false pour ne pas créer une session si elle n'existe pas

        if (session != null) {
            // Invalide la session pour déconnecter l'utilisateur
            session.invalidate();
        }

        // Redirection vers la page de connexion ou d'accueil
        response.sendRedirect(request.getContextPath() + "/compte/connexion");
    }
}
