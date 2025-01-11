package model;

import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LobbyPartie {

    private List<Utilisateur> utilisateurs;
    private int maxJoueurs;
    private Map<Utilisateur, Boolean> readinessMap;

    public LobbyPartie(int maxJoueurs) {
        this.utilisateurs = new ArrayList<>();
        this.maxJoueurs = maxJoueurs;
        this.readinessMap = new HashMap<>();
    }

    /**
     * Adds a user to the lobby and associates it with their session.
     *
     * @param session The HTTP session of the user.
     * @param utilisateur The user to add.
     * @return True if the user was added successfully, false otherwise.
     */
    public boolean rejoindreLobby(HttpSession session, Utilisateur utilisateur) {
        // If the user is already in the lobby, update their session
        if (utilisateurs.contains(utilisateur)) {
            System.out.println(utilisateur.getNomUtilisateur() + " is already in the lobby, rejoining.");
            session.setAttribute(Utilisateur.CLEF_UTILISATEUR_SESSION, utilisateur);
            return true; // Consider it a successful join
        }

        // Check if the lobby is full
        if (utilisateurs.size() >= maxJoueurs) {
            System.out.println("Lobby is full. " + utilisateur.getNomUtilisateur() + " cannot join.");
            return false;
        }

        // Add the user to the lobby
        utilisateurs.add(utilisateur);
        readinessMap.put(utilisateur, false); // Default readiness is false
        session.setAttribute(Utilisateur.CLEF_UTILISATEUR_SESSION, utilisateur);
        System.out.println(utilisateur.getNomUtilisateur() + " joined the lobby.");
        return true;
    }

    /**
     * Marks the player as ready.
     *
     * @param utilisateur The user who is ready.
     */
    public void setReady(Utilisateur utilisateur) {
        if (readinessMap.containsKey(utilisateur)) {
            readinessMap.put(utilisateur, true);
            System.out.println(utilisateur.getNomUtilisateur() + " is ready.");
        }
    }

    /**
     * Checks if a specific user is ready.
     *
     * @param utilisateur The user to check.
     * @return True if the user is ready, false otherwise.
     */
    public boolean isReady(Utilisateur utilisateur) {
        return readinessMap.getOrDefault(utilisateur, false);
    }

    /**
     * Checks if all players in the lobby are ready.
     *
     * @return True if all players are ready, false otherwise.
     */
    public boolean allPlayersReady() {
        return readinessMap.values().stream().allMatch(ready -> ready);
    }

    /**
     * Gets the list of players in the lobby.
     *
     * @return List of users.
     */
    public List<Utilisateur> getUtilisateurs() {
        return utilisateurs;
    }

    /**
     * Gets the number of players who have joined.
     *
     * @return The number of joined players.
     */
    public int getNumberOfJoinedPlayers() {
        return utilisateurs.size();
    }

    /**
     * Gets the maximum number of players.
     *
     * @return The maximum number of players.
     */
    public int getMaxJoueurs() {
        return maxJoueurs;
    }
    public void quitLobby(Utilisateur utilisateur) {
        if (utilisateurs.remove(utilisateur)) {
            readinessMap.remove(utilisateur);
            System.out.println(utilisateur.getNomUtilisateur() + " left the lobby.");
        } else {
            System.out.println(utilisateur.getNomUtilisateur() + " is not in the lobby.");
        }
    }

}
