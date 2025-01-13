package controlers.webSocket;

import java.io.IOException;

import jakarta.websocket.EndpointConfig;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;
import model.Joueur;
import model.Partie;
import model.Utilisateur;
import utils.JSON.JSONArray;
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
		JEUX, CHAT, FIN_PARTIE;
	}
	
	private static final String CLEF_JSON_TYPE = "type";
	private static final String CLEF_JSON_MSG = "message";
	private static final String CLEF_JSON_RECHARG_GRILL = "rechargerGrille";
	private static final String CLEF_JSON_TOUR_DE = "tourDe";
	private static final String CLEF_GAGNANT = "gagnant";
	private static final String CLEF_SCORES = "scores";
	
	private static final String CLEF_SCORES_NOM = "nom";
	private static final String CLEF_SCORES_SCORE = "score";
	private static final String CLEF_SCORES_PP = "pp";

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
		utili.getJoueur().setWebSocket(this);
		
		this.envoyerMessage("Bienvenue " + this.getUtilisateur().getNomUtilisateur(), false, ConsoleType.JEUX);
	}
	
	@OnClose
	public void onClose(Session session) {
		Joueur j = this.getUtilisateur().getJoueur();
		Partie p = j.getPartie();
		
		j.setWebSocket(null);
		
		// Si c'est la fin de la partie, le joueur n'a plus de raison de se reconnecter au WebSocket
		if (p.estFin()) {
			j.quitterPartie();
			this.getUtilisateur().setJoueur(null);
		}
	}
	
	@OnMessage
	public void OnMessage(String message, Session session) {
		chatAll(message);		
	}
	
	private void chatAll(String message) {
		final String chatMsg = getUtilisateur().getNomUtilisateur() + " : " + message;
		for (Joueur joueur : this.getUtilisateur().getJoueur().getPartie().getJoueurs()) {
			if (joueur != null && joueur.getWebSocket() != null)
				joueur.getWebSocket().envoyerMessage(chatMsg, false, ConsoleType.CHAT);
		}
	}
	
	public void envoyerMessageFin(Joueur gagnant) {
		Partie partie = this.getUtilisateur().getJoueur().getPartie();
		JSONObject json = new JSONObject();
		JSONArray scores = new JSONArray();
		String message = "Fin de la partie, victoire de "+gagnant.getUtilisateur().getNomUtilisateur();
		
		JSONArray messages = new JSONArray();
		messages.ajouter(message);
		
		for (Joueur joueur : partie.getJoueurs()) {
			if (joueur == null) continue;
			
			JSONObject sc = new JSONObject();
			sc.ajouter(CLEF_SCORES_NOM, joueur.getUtilisateur().getNomUtilisateur());
			sc.ajouter(CLEF_SCORES_SCORE, joueur.getScore());
			sc.ajouter(CLEF_SCORES_PP, joueur.getPP());
			
			scores.ajouter(sc);
		}
		
		json.ajouter(CLEF_JSON_TYPE, ConsoleType.FIN_PARTIE.toString());
		json.ajouter(CLEF_JSON_MSG, messages);
		json.ajouter(CLEF_JSON_RECHARG_GRILL, false);
		json.ajouter(CLEF_JSON_TOUR_DE, false);
		json.ajouter(CLEF_SCORES, scores);
		json.ajouter(CLEF_GAGNANT, gagnant.getUtilisateur().getNomUtilisateur());
		
		// On doit récupérer la session de manière synchrone
		synchronized (this.webSocket) {
			//this.webSocket.getAsyncRemote().sendText(json.toJSONString());
			try {
				this.webSocket.getBasicRemote().sendText(json.toJSONString());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Envoie au client le message généré par le serveur suite à une action d'un joueur.
	 * @param message Le message associé a l'action d'un joueur
	 * @param majPlateau true si le plateau doit être mise à jour
	 */
	public void envoyerMessage(String[] message, boolean majPlateau, ConsoleType type) {
		Partie partie = this.getUtilisateur().getJoueur().getPartie();
		JSONObject json = new JSONObject();
		
		JSONArray jmsg = new JSONArray();
		for (String msg : message) {
			jmsg.ajouter(msg);
		}
		
		json.ajouter(CLEF_JSON_TYPE, type.toString());
		json.ajouter(CLEF_JSON_MSG, jmsg);
		json.ajouter(CLEF_JSON_RECHARG_GRILL, majPlateau);
		if (partie.estTourDe(this.getUtilisateur().getJoueur()))
			json.ajouter(CLEF_JSON_TOUR_DE, false);
		else
			json.ajouter(CLEF_JSON_TOUR_DE, partie.getJoueurs()[partie.getTour()].getUtilisateur().getNomUtilisateur());
		
		getWebSocket().getAsyncRemote().sendText(json.toJSONString());
	}
	
	public void envoyerMessage(String message, boolean majPlateau, ConsoleType type) {
		envoyerMessage(new String[] {message}, majPlateau, type);
	}
	
	private Utilisateur getUtilisateur() {
		return this.utili;
	}
	
	private Session getWebSocket() {
		return this.webSocket;
	}

}
