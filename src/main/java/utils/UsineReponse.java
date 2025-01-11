package utils;

import java.io.IOException;

import jakarta.servlet.http.HttpServletResponse;
import utils.JSON.JSONObject;

/**
 * Contient toutes les méthode pour envoyer des réponses au client.
 * Les méthodes permettent d'envoyer des erreur, ou des réponses au format JSON.
 * Cette classe contient aussi bien de méthode spécifique (erreur 400, 404) que des méthodes pour envoyer des réponses générique.
 * </br></br>
 * <b>Concernant l'utilisation de cette classe</b>
 * <ul>
 * <li>Pour accéder à l'usine de réponse, il faut utiliser la méthode UsineReponse.getUsine() car celle-ci n'est pas instanciable de l'extèrieur</li>
 * <li>Aucune information ne doit avoir été écrit via HTTPServletResponse.getWritter()</li>
 * </ul> 
 * @author Théo KALIFA
 *
 */
public class UsineReponse {
	private final static String JSON_MIME_TYPE = "application/json";
	
	private static final UsineReponse usine = new UsineReponse();

	private UsineReponse() {}
	
	public void reponse400(String erreur, HttpServletResponse reponse) throws IOException {
		JSONObject json = new JSONObject();
		json.ajouter("message", erreur);
		
		reponse.setContentType(UsineReponse.JSON_MIME_TYPE);
		reponse.sendError(400, json.toString());
	}
	
	public static UsineReponse getUsine() {
		return usine;
	}
}
