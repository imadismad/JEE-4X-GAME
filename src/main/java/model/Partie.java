package model;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;

public class Partie {
    public static final int MAX_X = 10;
    public static final int MAX_Y = 10;

    private Joueur[] joueurs;
    private Tuile[][] tuiles;
    private int nombreJoueurs;

    public Partie() {
        this.joueurs = new Joueur[4]; // Limite à 4 joueurs
        this.tuiles = new Tuile[MAX_X][MAX_Y];
        this.nombreJoueurs = 0;
    }

    public Tuile getTuile(int x, int y) {
        return tuiles[x][y];
    }

    public List<Soldat> getSoldats() {
        List<Soldat> soldats = new ArrayList<>();
        for (Joueur joueur : joueurs) {
            if (joueur != null) {
                soldats.addAll(joueur.getSoldats());
            }
        }
        return soldats;
    }

    public void afficherCarte() {
        System.out.println("==== AFFICHAGE DE LA CARTE ====");

        // Affichage de l'entête avec les indices de colonnes
        System.out.print("    "); // Indentation pour les indices de colonnes
        for (int j = 0; j < MAX_Y; j++) {
            System.out.print(j + "  "); // Afficher les indices des colonnes
        }
        System.out.println(); // Retour à la ligne après l'entête des colonnes

        // Affichage de chaque ligne avec les indices des lignes
        for (int i = 0; i < MAX_X; i++) {
            System.out.print(i + "   "); // Afficher l'indice de la ligne

            List<Soldat> soldats = getSoldats();
            boolean soldatTrouve;

            for (int j = 0; j < MAX_Y; j++) {
                soldatTrouve = false;

                // Vérifier s'il y a un soldat sur cette tuile
                for (Soldat soldat : soldats) {
                    if (soldat.getX() == i && soldat.getY() == j) {
                        System.out.print("S" + soldat.getJoueur().getUtilisateur().getNomUtilisateur().charAt(0) + " ");
                        soldatTrouve = true;
                        break;
                    }
                }

                if (!soldatTrouve) {
                    // Afficher le type de tuile
                    if (tuiles[i][j] instanceof Ville) {
                        Ville ville = (Ville) tuiles[i][j];
                        if (ville.getAppartenance() != null) {
                            System.out.print("V" + ville.getAppartenance().getUtilisateur().getNomUtilisateur().charAt(0) + " ");
                        } else {
                            System.out.print("V_ ");
                        }
                    } else if (tuiles[i][j] instanceof Montagne) {
                        System.out.print("M  ");
                    } else if (tuiles[i][j] instanceof Foret) {
                        System.out.print("F  ");
                    } else {
                        System.out.print(".  "); // Tuile vide
                    }
                }
            }
            System.out.println(); // Retour à la ligne après chaque ligne de la carte
        }
        System.out.println("================================");
    }


    public void ajouterJoueur(Joueur joueur) {
        if (nombreJoueurs < joueurs.length) {
            joueurs[nombreJoueurs++] = joueur;
            System.out.println("Joueur " + joueur.getUtilisateur().getNomUtilisateur() + " ajouté à la partie.");
        } else {
            System.out.println("Partie pleine, impossible d'ajouter un autre joueur.");
        }
    }

    public void initialiserCarte() {
        Random rand = new Random();

        // Initialisation des villes et des soldats pour chaque joueur
        for (int i = 0; i < nombreJoueurs; i++) {
            int x, y;
            do {
                x = rand.nextInt(MAX_X);
                y = rand.nextInt(MAX_Y);
            } while (tuiles[x][y] != null); // Assurer qu'il n'y a pas déjà une ville

            Ville ville = new Ville();
            ville.capturer(joueurs[i]); // Attribue la ville au joueur
            tuiles[x][y] = ville;

            // Créer un soldat pour le joueur et le placer sur la ville
            Soldat soldat = new Soldat(x, y, joueurs[i], this);
            joueurs[i].ajouterSoldat(soldat);

            System.out.println("Ville attribuée au joueur " + joueurs[i].getUtilisateur().getNomUtilisateur()
                    + " en (" + x + ", " + y + ") avec un soldat.");
        }

        // Remplir les autres cases de la carte
        for (int i = 0; i < MAX_X; i++) {
            for (int j = 0; j < MAX_Y; j++) {
                if (tuiles[i][j] == null) { // Si la tuile n'a pas encore été assignée
                    double random = Math.random();
                    if (random < 0.15) {
                        tuiles[i][j] = new Montagne();
                    } else if (random < 0.5) {
                        tuiles[i][j] = new Foret();
                    } else {
                        tuiles[i][j] = new Plaine(); // Tuile vide
                    }
                    tuiles[i][j].setPosition(i, j);
                }
            }
        }
    }
}
