package controlers.compte;

import java.io.IOException;
import java.util.HashMap;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Utilisateur;
import model.storage.StockageInterface;
import model.storage.exception.StockageAccesException;
import model.storage.exception.StockageStructureException;
import model.storage.exception.StockageValeurException;
import model.storage.structure.StructureInterface;
import model.storage.structure.TableEnum;
import model.storage.structure.UtilisateurStructure;

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
        
        try {

        	// Vérification de l'unicité du nom d'utilisateur
			if (Utilisateur.nomUtilisateurExiste(nomUtilisateur)) {
				afficherJSP("Le nom d'utilisateur est déjà utilisé. Veuillez en choisir un autre.", request, response);
				return;
			}
        
        
	        // Création du compte
			HashMap<StructureInterface, Object> map = new HashMap<>();
			map.put(UtilisateurStructure.NOM_UTILISATEUR, nomUtilisateur);
			map.put(UtilisateurStructure.MOT_DE_PASSE, motDePasse);
			StockageInterface.getInstance().ajouterValeur(TableEnum.UTILISATEUR, map);
			System.out.println(map.get(UtilisateurStructure.NOM_UTILISATEUR));
			
	        // Connexion auto
			request.getSession().setAttribute(Utilisateur.CLEF_UTILISATEUR_SESSION, new Utilisateur(nomUtilisateur, null));
	        
	        // Redirection
	        //response.sendRedirect(getServletContext().getContextPath() + "/dummyPage.html");
			afficherJSP("", request, response);
        
		} catch (StockageAccesException | StockageValeurException | StockageStructureException e) {
			throw new ServletException("Erreur survenu lors de l'accès au système de sotckage", e);
		}
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
