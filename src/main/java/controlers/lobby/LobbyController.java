package controlers.lobby;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

import model.LobbyPartie;
import model.Utilisateur;
import utils.PartiesManager;

@WebServlet("/lobby/*")
public class LobbyController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String pathInfo = req.getPathInfo();
        
        if (pathInfo == null || pathInfo.equals("/") || pathInfo.equals("/enterCode")) {
            // show a page to enter code
            req.getRequestDispatcher("/WEB-INF/jsp/lobby/enterCode.jsp").forward(req, resp);
            return;
        }

        // e.g. GET /lobby/join?gameCode=1234
        if ("/join".equals(pathInfo)) {
            String gameCode = req.getParameter("gameCode");
            if (gameCode == null || !PartiesManager.isValidGameCode(gameCode)) {
                req.setAttribute("error", "Invalid or missing game code.");
                req.getRequestDispatcher("/WEB-INF/jsp/lobby/enterCode.jsp").forward(req, resp);
                return;
            }

            Utilisateur utilisateur = Utilisateur.getUtilisateur(req.getSession());
            if (utilisateur == null) {
                resp.sendRedirect(req.getContextPath() + "/compte/connexion");
                return;
            }

            // Try to join the lobby
            boolean joined = PartiesManager.addUserToLobby(gameCode, req.getSession(), utilisateur);
            if (!joined) {
                req.setAttribute("error", "Cannot join the lobby (already full?).");
            }

            // Show the lobby page
            LobbyPartie lobby = PartiesManager.getLobbyByGameCode(gameCode);
            req.setAttribute("gameCode", gameCode);
            req.setAttribute("lobby", lobby);
            req.getRequestDispatcher("/WEB-INF/jsp/lobby/lobby.jsp").forward(req, resp);
        }

        // e.g. GET /lobby/refresh?gameCode=1234
        else if ("/refresh".equals(pathInfo)) {
            String gameCode = req.getParameter("gameCode");
            LobbyPartie lobby = PartiesManager.getLobbyByGameCode(gameCode);
            if (lobby == null) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                resp.getWriter().write("Lobby not found");
                return;
            }
            req.setAttribute("gameCode", gameCode);
            req.setAttribute("lobby", lobby);
            // Partial JSP that refreshes the lobby content
            req.getRequestDispatcher("/WEB-INF/jsp/lobby/lobbyContent.jsp").forward(req, resp);
        }

        else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Page not found");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String pathInfo = req.getPathInfo();

        // ========== MARK READY ==========
        if ("/ready".equals(pathInfo)) {
            String gameCode = req.getParameter("gameCode");
            Utilisateur utilisateur = Utilisateur.getUtilisateur(req.getSession());

            if (gameCode == null || utilisateur == null 
                || !PartiesManager.isValidGameCode(gameCode)) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid request or missing game code/user.");
                return;
            }

            LobbyPartie lobby = PartiesManager.getLobbyByGameCode(gameCode);
            if (lobby == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Lobby not found for this code.");
                return;
            }

            // Mark user as "ready"
            lobby.setReady(utilisateur);

            // Check if everyone is ready AND the lobby is full
            boolean everyoneIsReady = lobby.allPlayersReady();
            boolean isLobbyFull = (lobby.getNumberOfJoinedPlayers() == lobby.getMaxJoueurs());

            if (everyoneIsReady && isLobbyFull) {
                // Launch the game
                PartiesManager.launchGame(gameCode);

                // Redirect to the game
                resp.sendRedirect(req.getContextPath() + "/partiePrincipale?gameCode=" + gameCode);

            } else {
                // Not all are ready or the lobby isn't full => stay in the lobby
                resp.sendRedirect(req.getContextPath() + "/lobby/join?gameCode=" + gameCode);
            }
        }
        // ========== QUIT LOBBY ==========
        else if ("/quit".equals(pathInfo)) {
            String gameCode = req.getParameter("gameCode");
            Utilisateur utilisateur = Utilisateur.getUtilisateur(req.getSession());

            if (gameCode != null && utilisateur != null) {
                PartiesManager.removeUserFromLobby(gameCode, utilisateur);
            }

            resp.sendRedirect(req.getContextPath() + "/lobby/enterCode");
        }
        else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Page not found.");
        }
    }
}
