package controlers.webSocket;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Utilisateur;

/**
 * Classe permetant d'annuler la connexion WebSocket si l'utilisateur n'est pas connecté ou pas en partie 
 * @author Théo KALIFA
 *
 */
@WebFilter(urlPatterns = "/console")
public class UtilisateurEnPartieFilter implements Filter {

	public UtilisateurEnPartieFilter() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		HttpSession session = ((HttpServletRequest) req).getSession();
		if (session == null || !Utilisateur.estConnecte(session)) {
			((HttpServletResponse) res).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return;
		}
		
		if (!Utilisateur.getUtilisateur(session).estEnPartie()) {
			((HttpServletResponse) res).setStatus(HttpServletResponse.SC_FORBIDDEN);
			return;
		}
		
		chain.doFilter(req, res);
	}
}
