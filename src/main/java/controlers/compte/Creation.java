package controlers.compte;

import java.io.IOException;
import java.util.Arrays;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet permettant à un utilisateur de se créer un compte
 * A la fin le compte doit être crée et l'utilisateur redirigé vers une autre page.
 * Si jamais une erreur survient, un message d'erreur doit être affiché sur la JSP avec le formulaire
 * Dans le cas d'une requête GET, on redirige directement vers la page JSP
 * 
 * @author Théo KALIFA
 */
public class Creation extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String nomUtilisateur = request.getParameter("nomUtilisateur").trim();
        String motDePasse = request.getParameter("motDePasse").trim();

        // Vérification de l'existance des champs
        if (nomUtilisateur == null || motDePasse == null || nomUtilisateur.isEmpty() || motDePasse.isEmpty()) {
        	afficherJSP("Le nom d'utilisateur et le mot de passe sont requis.", request, response);
            return;
        }
        
        // Vérification de l'unicité du nom d'utilisateur
        // TODO Ajouter la vérification de l'unicité du nom de l'utilisateur 
        if (Arrays.asList("genius", "dummy").contains(nomUtilisateur)) {
        	afficherJSP("Le nom d'utilisateur est déjà utilisé. Veuillez en choisir un autre.", request, response);
        	return;
        }
        
        
        // Création du compte
        // TODO Ajouter la logique de création de compte et de login auto
        
        // Redirection
        response.sendRedirect(getServletContext().getContextPath() + "/dummyPage.html");
	}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		afficherJSP("", request, response);
	}
	
	private void afficherJSP(String erreur, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setAttribute("messageErreur", erreur);
        request.getRequestDispatcher("/WEB-INF/jsp/compte/creation.jsp").forward(request, response);
	}

}
