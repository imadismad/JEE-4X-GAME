package controlers.webSocket;

import jakarta.websocket.EndpointConfig;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;
import model.Utilisateur;
import utils.JSON.JSONObject;

/**
 * Classe permetant d'informer un joueur d'un changement dans le jeu via WebSocket
 * @author Théo KALIFA
 *
 */
@ServerEndpoint(
	value="/console",
	configurator = WebSocketHandshake.class
)
public class ConsoleJeu {
	
	public enum ConsoleType {
		JEUX, CHAT;
	}
	
	private static final String CLEF_JSON_TYPE = "type";
	private static final String CLEF_JSON_MSG = "message";
	private static final String CLEF_JSON_RECHARG_GRILL = "rechargerGrille";

	private Utilisateur utili;
	private Session webSocket;
	
	public ConsoleJeu() {
		this.utili = null;
	}
	
	@OnOpen
	public void onOpen(Session session, EndpointConfig conf) {
		// On ajoute l'utilisateur à la session pour la garder
		// Pour des raison de praticité, on va garder la même clef
		this.utili = (Utilisateur) conf.getUserProperties().get(WebSocketHandshake.ENDPOINT_CONFIG_UITL_CLEF);
		this.webSocket = session;
		//utili.getJoueur().setWebSocket(this);
	}
	
	@OnClose
	public void onClose(Session session) {
		// TODO Auto-generated method stub

	}
	
	@OnMessage
	public void OnMessage(String message, Session session) {
		final String chatMsg = getUtilisateur().getNomUtilisateur() + " : " + message;
		envoyerMessage(chatMsg, false, ConsoleType.CHAT);
	}
	
	/**
	 * Envoie au client le message généré par le serveur suite à une action d'un joueur.
	 * @param message Le message associé a l'action d'un joueur
	 * @param majPlateau true si le plateau doit être mise à jour
	 */
	public void envoyerMessage(String message, boolean majPlateau, ConsoleType type) {
		JSONObject json = new JSONObject();
		json.ajouter(CLEF_JSON_TYPE, type.toString());
		json.ajouter(CLEF_JSON_MSG, message);
		json.ajouter(CLEF_JSON_RECHARG_GRILL, majPlateau);
		
		getWebSocket().getAsyncRemote().sendText(json.toString());
	}
	
	private Utilisateur getUtilisateur() {
		return this.utili;
	}
	
	private Session getWebSocket() {
		return this.webSocket;
	}

}
