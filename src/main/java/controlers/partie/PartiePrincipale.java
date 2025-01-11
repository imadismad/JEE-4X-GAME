package controlers.partie;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

import model.Partie;
import model.Utilisateur;
import utils.PartiesManager;

/**
 * Displays the actual game board once a Partie is launched.
 */
@WebServlet("/partiePrincipale")
public class PartiePrincipale extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1) Ensure user is logged in
        Utilisateur utilisateur = (Utilisateur) request.getSession()
                .getAttribute(Utilisateur.CLEF_UTILISATEUR_SESSION);
        if (utilisateur == null) {
            // Not logged in? redirect to login
            response.sendRedirect(request.getContextPath() + "/compte/connexion");
            return;
        }

        // 2) Retrieve the "gameCode" from the query string
        String gameCode = request.getParameter("gameCode");
        if (gameCode == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                               "Missing 'gameCode' parameter");
            return;
        }

        // 3) Look up the Partie from your manager
        Partie partie = PartiesManager.getPartieForGameCode(gameCode);
        if (partie == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND,
                               "No active game found for code: " + gameCode);
            return;
        }

        // 4) Put it into request scope
        request.setAttribute("partie", partie);

        // 5) Forward to partiePrincipale.jsp to display the game
        request.getRequestDispatcher("/WEB-INF/jsp/partie/partiePrincipale.jsp")
               .forward(request, response);
    }
}
