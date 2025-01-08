package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GrilleManager {
    private final Partie partie;

    public GrilleManager(Partie partie) {
        this.partie = partie;
    }

    public Map<String, Object> genererGrille() {
        Map<String, Object> grille = new HashMap<>();

        // Générer les tuiles
        List<Map<String, Object>> tuiles = new ArrayList<>();
        for (int x = 0; x < partie.getTuiles().length; x++) {
            for (int y = 0; y < partie.getTuiles()[x].length; y++) {
                Tuile tuile = partie.getTuile(x, y);
                Map<String, Object> tuileData = new HashMap<>();
                tuileData.put("x", x);
                tuileData.put("y", y);
                tuileData.put("type", tuile.getType()); // Nom du type de tuile
                tuiles.add(tuileData);
            }
        }

        // Générer les soldats
        List<Map<String, Object>> soldats = new ArrayList<>();
        for (Soldat soldat : partie.getSoldats()) {
            Map<String, Object> soldatData = new HashMap<>();
            soldatData.put("x", soldat.getX());
            soldatData.put("y", soldat.getY());
            soldatData.put("joueur", soldat.getJoueur().getUtilisateur().getNomUtilisateur());
            soldats.add(soldatData);
        }

        // Ajouter les données à la grille
        grille.put("tuiles", tuiles);
        grille.put("soldats", soldats);
        grille.put("joueurs", partie.getJoueurs()); // Liste des joueurs

        return grille;
    }
}
