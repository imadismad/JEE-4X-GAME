package controlers;
import utils.PartiesManager;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import model.storage.StockageInterface;
import model.storage.exception.StockageAccesException;
import model.storage.exception.StockageStructureException;

/**
 * Cette classe est appelé par Tomcat au démarage du serveur, elle permet d'initialiser les services utiles tel que le service de stockage
 * @author Théo KALIFA
 *
 */
public class TomcatInitServlet extends HttpServlet {
	
	@Override
	public void init() throws ServletException {

	    System.out.println("Parties CSV initialized.");
		
		super.init();
		
		// Initialisation du service de stockage en faisant un accès vers l'instance de stockage à utiliser
		try {
			StockageInterface.getInstance();
		} catch (StockageStructureException | StockageAccesException e) {
			throw new ServletException("Impossible d'initialiser un accès vers le système de stockage", e);
		} 
	}

}
