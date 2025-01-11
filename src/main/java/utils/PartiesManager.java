package utils;

import java.io.*;
import java.util.*;

import jakarta.servlet.http.HttpSession;
import model.LobbyPartie;
import model.Utilisateur;

public class PartiesManager {

    private static final String PARTIES_FILE_PATH = System.getProperty("user.dir") + "/WEB-INF/storage/parties.csv";
    private static final Map<String, LobbyPartie> lobbies = new HashMap<>();

    static {
        try {
            loadPartiesFromCSV();
        } catch (IOException e) {
            System.err.println("Error loading parties from CSV: " + e.getMessage());
        }
    }

    // Load parties from the CSV file
    private static void loadPartiesFromCSV() throws IOException {
        File file = new File(PARTIES_FILE_PATH);
        if (!file.exists()) {
            System.out.println("Creating default parties.csv file.");
            createDefaultCSV();
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty() || line.startsWith("code")) continue;
                String[] parts = line.split(";");
                String code = parts[0];
                int numberOfPlayers = Integer.parseInt(parts[1]);
                lobbies.put(code, new LobbyPartie(numberOfPlayers));
            }
        }
    }

    private static void createDefaultCSV() throws IOException {
        File file = new File(PARTIES_FILE_PATH);
        System.out.println("Creating default parties.csv file at: " + file.getAbsolutePath());

        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("code;numberOfPlayers\n");
            writer.write("1232;2\n");
            writer.write("1233;4\n");
            writer.write("1234;4\n");
        }
    }

    public static boolean isValidGameCode(String code) {
        return lobbies.containsKey(code);
    }

    public static LobbyPartie getLobbyByGameCode(String code) {
        return lobbies.get(code);
    }

    public static boolean addUserToLobby(String gameCode, HttpSession session, Utilisateur utilisateur) {
        LobbyPartie lobby = lobbies.get(gameCode);
        if (lobby == null) {
            return false; // Lobby does not exist
        }

        // Prevent duplicate entries
        if (lobby.getUtilisateurs().contains(utilisateur)) {
            session.setAttribute("lobbyCode", gameCode); // Redirect to the existing lobby
            return false;
        }

        return lobby.rejoindreLobby(session, utilisateur);
    }

    public static void removeUserFromLobby(String gameCode, Utilisateur utilisateur) {
        LobbyPartie lobby = lobbies.get(gameCode);
        if (lobby != null) {
            lobby.getUtilisateurs().remove(utilisateur);
        }
    }

    public static List<Utilisateur> getLobbyUsers(String gameCode) {
        LobbyPartie lobby = lobbies.get(gameCode);
        return (lobby != null) ? lobby.getUtilisateurs() : Collections.emptyList();
    }
}
