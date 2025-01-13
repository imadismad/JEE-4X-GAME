package model;

import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents the "waiting room" for a future Partie.
 */
public class LobbyPartie {

    private final int maxJoueurs;
    private final List<Utilisateur> utilisateurs;       // The users who joined
    private final Map<Utilisateur, Boolean> readinessMap; // Tracks each user's "ready" state

    /**
     * Constructs a lobby with a maximum number of players allowed.
     */
    public LobbyPartie(int maxJoueurs) {
        this.maxJoueurs = maxJoueurs;
        this.utilisateurs = new ArrayList<>();
        this.readinessMap = new HashMap<>();
    }

    /**
     * Attempt to add a user to the lobby session. 
     * - If the user is already in the lobby, do NOT add them again (unique constraint).
     * - If the lobby is full, do NOT add them.
     *
     * @return true if the user was successfully added, false otherwise
     */
    public boolean rejoindreLobby(HttpSession session, Utilisateur utilisateur) {
        // If user is already in this lobby, block re-join
        if (utilisateurs.contains(utilisateur)) {
            System.out.println(utilisateur.getNomUtilisateur() + " is already in this lobby, blocking re-join attempt.");
            return false; // <--- Key: No re-joining
        }

        // Check if lobby is full
        if (utilisateurs.size() >= maxJoueurs) {
            System.out.println("Lobby is full. " + utilisateur.getNomUtilisateur() + " cannot join.");
            return false;
        }

        // Otherwise, add them
        utilisateurs.add(utilisateur);
        readinessMap.put(utilisateur, false);
        session.setAttribute(Utilisateur.CLEF_UTILISATEUR_SESSION, utilisateur);

        System.out.println(utilisateur.getNomUtilisateur() + " joined the lobby. (" 
                           + utilisateurs.size() + "/" + maxJoueurs + ")");
        return true;
    }

    /**
     * Sets the given user to "ready".
     */
    public void setReady(Utilisateur utilisateur) {
        if (readinessMap.containsKey(utilisateur)) {
            readinessMap.put(utilisateur, true);
            System.out.println(utilisateur.getNomUtilisateur() + " is ready.");
        }
    }

    /**
     * Returns true if the user is "ready".
     */
    public boolean isReady(Utilisateur utilisateur) {
        return readinessMap.getOrDefault(utilisateur, false);
    }

    /**
     * Returns true if all joined players are "ready".
     */
    public boolean allPlayersReady() {
        // Only returns true if every joined user in the lobby has readiness = true
        return readinessMap.size() == utilisateurs.size()
            && readinessMap.values().stream().allMatch(Boolean::booleanValue);
    }

    /**
     * Number of players currently joined in the lobby.
     */
    public int getNumberOfJoinedPlayers() {
        return utilisateurs.size();
    }

    /**
     * The maximum allowed players in this lobby.
     */
    public int getMaxJoueurs() {
        return maxJoueurs;
    }

    /**
     * Returns a list of all users who joined this lobby.
     */
    public List<Utilisateur> getUtilisateurs() {
        return utilisateurs;
    }

    /**
     * Removes a user from the lobby. 
     * If found, also removes them from readiness tracking.
     */
    public void quitLobby(Utilisateur utilisateur) {
        if (utilisateurs.remove(utilisateur)) {
            readinessMap.remove(utilisateur);
            System.out.println(utilisateur.getNomUtilisateur() + " left the lobby.");
        } else {
            System.out.println(utilisateur.getNomUtilisateur() + " was not in this lobby.");
        }
    }
    
    public void reset() {
    	for (Utilisateur utilisateur : utilisateurs) {
    		this.readinessMap.remove(utilisateur);
		}
    	this.utilisateurs.removeAll(utilisateurs);
    }
}
