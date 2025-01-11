package utils;

import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpSession;
import model.LobbyPartie;
import model.Partie;
import model.Utilisateur;
import model.Joueur;

/**
 * Manages lobbies and in-progress games.
 */
public class PartiesManager {

    /**
     * Map of gameCode -> LobbyPartie (waiting rooms).
     */
    private static final Map<String, LobbyPartie> lobbies = new HashMap<>();

    /**
     * Map of gameCode -> Partie (actual games in progress).
     */
    private static final Map<String, Partie> partiesEnCours = new HashMap<>();

    // -------------------------------------------------
    // LOBBY MANAGEMENT
    // -------------------------------------------------

    public static boolean isValidGameCode(String code) {
        return lobbies.containsKey(code);
    }

    public static LobbyPartie getLobbyByGameCode(String code) {
        return lobbies.get(code);
    }

    public static boolean addUserToLobby(String gameCode, HttpSession session, Utilisateur utilisateur) {
        LobbyPartie lobby = lobbies.get(gameCode);
        if (lobby == null) {
            return false; 
        }
        return lobby.rejoindreLobby(session, utilisateur);
    }

    public static void removeUserFromLobby(String gameCode, Utilisateur utilisateur) {
        LobbyPartie lobby = lobbies.get(gameCode);
        if (lobby != null) {
            lobby.quitLobby(utilisateur);
        }
    }

    // Example static block: add some test lobbies
    static {
        // e.g. "1234" => up to 4 players
        lobbies.put("1234", new LobbyPartie(4));
        lobbies.put("2234", new LobbyPartie(4));
        lobbies.put("3234", new LobbyPartie(4));
        // e.g. "1232" => up to 2 players
        lobbies.put("1232", new LobbyPartie(2));        // e.g. "1232" => up to 2 players
        lobbies.put("2232", new LobbyPartie(2));        // e.g. "1232" => up to 2 players
        lobbies.put("3232", new LobbyPartie(2));
    }

    // -------------------------------------------------
    // PARTIE (GAME) MANAGEMENT
    // -------------------------------------------------

    public static Partie getPartieForGameCode(String code) {
        return partiesEnCours.get(code);
    }

    /**
     * Launch a new Partie for the given code IF not already launched.
     * This should only be called if the lobby is full and all are ready.
     */
    public static Partie launchGame(String gameCode) {
        // If a Partie already exists, just return it
        if (partiesEnCours.containsKey(gameCode)) {
            return partiesEnCours.get(gameCode);
        }

        LobbyPartie lobby = lobbies.get(gameCode);
        if (lobby == null) {
            return null; 
        }

        // Create the Partie
        Partie partie = new Partie();

        // For each user in the lobby, create or retrieve their Joueur
        for (Utilisateur user : lobby.getUtilisateurs()) {
            if (user.getJoueur() == null) {
                Joueur j = new Joueur(user, partie);
                partie.ajouterJoueur(j);
                user.setJoueur(j);

            } else {
                partie.ajouterJoueur(user.getJoueur());
            }
        }

        // Initialize the map for this game
        partie.initialiserCarte();

        // Store it in memory
        partiesEnCours.put(gameCode, partie);

        return partie;
    }
  
}
