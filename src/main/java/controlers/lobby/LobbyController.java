package controlers.lobby;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.LobbyPartie;
import model.Utilisateur;
import utils.PartiesManager;

import java.io.IOException;

@WebServlet("/lobby/*")
public class LobbyController extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	    String pathInfo = req.getPathInfo();

	    if (pathInfo == null || pathInfo.equals("/") || pathInfo.equals("/enterCode")) {
	        req.getRequestDispatcher("/WEB-INF/jsp/lobby/enterCode.jsp").forward(req, resp);
	        return;
	    }

	    if (pathInfo.equals("/join")) {
	        String gameCode = req.getParameter("gameCode");
	        if (gameCode == null || !PartiesManager.isValidGameCode(gameCode)) {
	            req.setAttribute("error", "Invalid game code. Please try again.");
	            req.getRequestDispatcher("/WEB-INF/jsp/lobby/enterCode.jsp").forward(req, resp);
	            return;
	        }

	        Utilisateur utilisateur = Utilisateur.getUtilisateur(req.getSession());
	        if (utilisateur == null) {
	            resp.sendRedirect(req.getContextPath() + "/compte/connexion");
	            return;
	        }

	        boolean joined = PartiesManager.addUserToLobby(gameCode, req.getSession(), utilisateur);
	        if (!joined) {
	            req.setAttribute("error", "You are already in this lobby.");
	        }

	        req.setAttribute("gameCode", gameCode);
	        req.setAttribute("lobby", PartiesManager.getLobbyByGameCode(gameCode));
	        req.getRequestDispatcher("/WEB-INF/jsp/lobby/lobby.jsp").forward(req, resp);
	        return;
	    }

	    if (pathInfo.equals("/refresh")) {
	        String gameCode = req.getParameter("gameCode");
	        LobbyPartie lobby = PartiesManager.getLobbyByGameCode(gameCode);
	        if (lobby == null) {
	            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
	            resp.getWriter().write("Lobby not found");
	            return;
	        }

	        req.setAttribute("gameCode", gameCode);
	        req.setAttribute("lobby", lobby);
	        req.getRequestDispatcher("/WEB-INF/jsp/lobby/lobbyContent.jsp").forward(req, resp); // Subsection JSP
	        return;
	    }

	    resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Page not found.");
	}


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();

        if ("/ready".equals(pathInfo)) {
            String gameCode = req.getParameter("gameCode");
            Utilisateur utilisateur = Utilisateur.getUtilisateur(req.getSession());

            if (gameCode == null || utilisateur == null || !PartiesManager.isValidGameCode(gameCode)) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid request.");
                return;
            }

            PartiesManager.getLobbyByGameCode(gameCode).setReady(utilisateur);
            resp.sendRedirect(req.getContextPath() + "/lobby/join?gameCode=" + gameCode);
        } else if ("/quit".equals(pathInfo)) {
            String gameCode = req.getParameter("gameCode");
            Utilisateur utilisateur = Utilisateur.getUtilisateur(req.getSession());

            if (gameCode != null && utilisateur != null) {
                PartiesManager.removeUserFromLobby(gameCode, utilisateur);
            }

            resp.sendRedirect(req.getContextPath() + "/lobby/enterCode");
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Page not found.");
        }
    }
}
