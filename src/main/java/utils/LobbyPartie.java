package utils;

import model.Utilisateur;

import java.util.ArrayList;
import java.util.List;

public class LobbyPartie {

    private final String code;
    private final int numberOfPlayers;
    private final List<PlayerStatus> players;

    public LobbyPartie(String code, int numberOfPlayers) {
        this.code = code;
        this.numberOfPlayers = numberOfPlayers;
        this.players = new ArrayList<>();
    }

    public boolean addPlayer(Utilisateur utilisateur) {
        if (players.size() < numberOfPlayers) {
            players.add(new PlayerStatus(utilisateur.getNomUtilisateur()));
            return true;
        }
        return false;
    }

    public void setPlayerReady(String username) {
        players.stream()
                .filter(player -> player.getUsername().equals(username))
                .findFirst()
                .ifPresent(player -> player.setReady(true));
    }

    public boolean areAllPlayersReady() {
        return players.size() == numberOfPlayers &&
               players.stream().allMatch(PlayerStatus::isReady);
    }

    public List<PlayerStatus> getPlayers() {
        return players;
    }

    public String getCode() {
        return code;
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public static class PlayerStatus {
        private final String username;
        private boolean isReady;

        public PlayerStatus(String username) {
            this.username = username;
            this.isReady = false;
        }

        public String getUsername() {
            return username;
        }

        public boolean isReady() {
            return isReady;
        }

        public void setReady(boolean ready) {
            isReady = ready;
        }
    }
}
