package controlers.webSocket;

import jakarta.servlet.http.HttpSession;
import jakarta.websocket.HandshakeResponse;
import jakarta.websocket.server.HandshakeRequest;
import jakarta.websocket.server.ServerEndpointConfig;
import model.Utilisateur;

/**
 * Classe permettant de verifier et ajouter des informations sur une connexion a un WebSocket
 * Afin d'être utilisé il doit être ajouté dans la classe à l'aide d'un annotation Java :
 * {@code @ServerEndpoint(value="URL", configurator = WebSocketMiddleware.class)}
 * @author Théo KALIFA
 *
 */
public class WebSocketHandshake extends ServerEndpointConfig.Configurator {

	/**
	 * Contient la clef à utiliser pour récupérer l'utilisateur actuellement connecté dans la webSocket
	 */
	public static final String ENDPOINT_CONFIG_UITL_CLEF = "utilisateur";
	
	public WebSocketHandshake() {}
	
	
	
	@Override
	public void modifyHandshake(ServerEndpointConfig conf, HandshakeRequest request, HandshakeResponse response) {
		super.modifyHandshake(conf, request, response);
		
		// Vérification utilisateur connecté
		HttpSession session = (HttpSession) request.getHttpSession();
		
		Utilisateur utili = Utilisateur.getUtilisateur(session);
		conf.getUserProperties().put(ENDPOINT_CONFIG_UITL_CLEF, utili);
	}
}
