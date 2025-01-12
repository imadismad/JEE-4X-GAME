package model;

import java.util.ArrayList; // Utilisé pour gérer une liste dynamique de soldats
import java.util.List; // Interface pour gérer des collections de soldats
// Importation des bibliothèques nécessaires
import java.util.Random; // Utilisé pour générer des nombres aléatoires

import controlers.webSocket.ConsoleJeu.ConsoleType;

// Définition de la classe Partie
public class Partie {
    // Déclaration des constantes pour les dimensions de la carte
    public static final int MAX_X = 10; // Largeur maximale de la carte
    public static final int MAX_Y = 10; // Hauteur maximale de la carte

    // Attributs de la classe Partie
    private Joueur[] joueurs; // Tableau pour stocker les joueurs participant à la partie
    private Tuile[][] tuiles; // Grille représentant les tuiles de la carte
    private int nombreJoueurs; // Nombre actuel de joueurs dans la partie
    
    private boolean estFin = false;
    
    //Attribut sur le tour
    public int tour;

    
    
    // Constructeur par défaut de la classe Partie
    public Partie() {
        this.joueurs = new Joueur[4]; // Initialise le tableau avec un maximum de 4 joueurs
        this.tuiles = new Tuile[MAX_X][MAX_Y]; // Initialise la grille de tuiles
        this.nombreJoueurs = 0; // Initialise le nombre de joueurs à 0
        this.tour = 0;
    }
    
    /**
     * Permet de savoir si la partie est terminé.
     * La partie est considéré terminé si la fonction de notification de fin de la partie a été appelé
     * @return
     */
    public boolean estFin() {
    	return estFin;
    }
    
    //Récupérer le tour
    public int getTour() {
        return tour;
    }
    
    public boolean estTourDe(Joueur joueur) {
    	return this.getJoueurs()[this.getTour()] == joueur;
    }
    
    public void incrementerTour() {
    	int tourDepart = this.getTour();
    	
    	do {
    		this.tour=(this.tour + 1) % this.nombreJoueurs;
    	} while(tourDepart != this.getTour() && !	this.getJoueurs()[this.getTour()].hasSoldatsEtVilles());
        
        this.getJoueurs()[this.tour].incrementerPPParVilles(); // Incrémente les points de production pour les villes du joueur

    }

    // Getter pour obtenir le tableau des joueurs
    public Joueur[] getJoueurs() {
        return joueurs;
    }

    // Getter pour obtenir la grille de tuiles
    public Tuile[][] getTuiles() {
        return tuiles;
    }

    // Récupère une tuile spécifique à partir de ses coordonnées
    public Tuile getTuile(int x, int y) {
        return tuiles[x][y];
    }
    
    /**
     * Vérifie si une tuile est inaccessible.
     *
     * @param x La coordonnée X de la tuile
     * @param y La coordonnée Y de la tuile
     * @return true si la tuile est inaccessible, false sinon
     */
    public boolean estTuileInaccessible(int x, int y, Soldat s) {
        // Vérifie si les coordonnées sont hors des limites de la carte
        if (x < 0 || x >= MAX_X || y < 0 || y >= MAX_Y) {
            return true; // Hors limites
        }

        // Récupère la tuile aux coordonnées spécifiées
        Tuile tuile = this.getTuile(x, y);

        // Vérifie si la tuile est une montagne (ou tout autre type inaccessible)
        if (tuile instanceof Montagne) {
            return true; // Inaccessible car montagne
        }
        
        // Vérifie si un soldat allié est déjà sur cette case
        Joueur joueur = s.getJoueur();
        for (Soldat soldatAllie : joueur.getSoldats()) {
            if (soldatAllie.getX() == x && soldatAllie.getY() == y) {
                return true; // Inaccessible car occupée par un allié
            }
        }

        // Sinon, la tuile est accessible
        return false;
    }

    // Récupère une liste de tous les soldats présents sur la carte
    public List<Soldat> getSoldats() {
        List<Soldat> soldats = new ArrayList<>(); // Liste pour stocker les soldats
        for (Joueur joueur : joueurs) { // Parcours des joueurs
            if (joueur != null) { // Si le joueur n'est pas nul
                soldats.addAll(joueur.getSoldats()); // Ajouter les soldats du joueur
            }
        }
        return soldats; // Retourne la liste des soldats
    }
    
 // Méthode pour récupérer un soldat par ses coordonnées (x, y)
    public Soldat getSoldatParCoordonnees(int x, int y) {
        // Parcours des joueurs
        for (Joueur joueur : joueurs) {
            if (joueur != null) { // Si le joueur n'est pas nul
                // Parcours des soldats du joueur
                for (Soldat soldat : joueur.getSoldats()) {
                    // Vérifie si le soldat se trouve aux coordonnées (x, y)
                    if (soldat.getX() == x && soldat.getY() == y) {
                        return soldat; // Retourne le soldat trouvé
                    }
                }
            }
        }
        // Si aucun soldat n'est trouvé aux coordonnées données
        return null;
    }

    // Méthode pour afficher l'état actuel de la carte
    public void afficherCarte() {
        System.out.println("==== AFFICHAGE DE LA CARTE ====");

        // Affiche les indices de colonnes
        System.out.print("    "); // Indentation pour aligner les indices
        for (int j = 0; j < MAX_Y; j++) {
            System.out.print(j + "  "); // Affiche les indices des colonnes
        }
        System.out.println(); // Retour à la ligne après les indices

        // Parcours des lignes et affichage de chaque tuile
        for (int i = 0; i < MAX_X; i++) {
            System.out.print(i + "   "); // Affiche l'indice de la ligne

            List<Soldat> soldats = getSoldats(); // Récupère les soldats
            boolean soldatTrouve;

            for (int j = 0; j < MAX_Y; j++) {
                soldatTrouve = false;

                // Vérifie s'il y a un soldat sur la tuile
                for (Soldat soldat : soldats) {
                    if (soldat.getX() == i && soldat.getY() == j) {
                        System.out.print("S" + soldat.getJoueur().getUtilisateur().getNomUtilisateur().charAt(0) + " ");
                        soldatTrouve = true;
                        break; // Sort de la boucle dès qu'un soldat est trouvé
                    }
                }

                if (!soldatTrouve) {
                    // Affiche le type de tuile si aucun soldat n'est présent
                    if (tuiles[i][j] instanceof Ville) {
                        Ville ville = (Ville) tuiles[i][j];
                        if (ville.getAppartenance() != null) {
                            System.out.print("V" + ville.getAppartenance().getUtilisateur().getNomUtilisateur().charAt(0) + " ");
                        } else {
                            System.out.print("V_ "); // Ville sans propriétaire
                        }
                    } else if (tuiles[i][j] instanceof Montagne) {
                        System.out.print("M  "); // Montagne
                    } else if (tuiles[i][j].getType() == "Forêt") {
                        System.out.print("F  "); // Forêt
                    } else {
                        System.out.print(".  "); // Tuile vide
                    }
                }
            }
            System.out.println(); // Retour à la ligne après chaque ligne
        }
        System.out.println("================================");
    }

    // Ajoute un joueur à la partie
    public void ajouterJoueur(Joueur joueur) {
        if (nombreJoueurs < joueurs.length) { // Vérifie s'il reste de la place
            joueurs[nombreJoueurs++] = joueur; // Ajoute le joueur et incrémente le compteur
            System.out.println("Joueur " + joueur.getUtilisateur().getNomUtilisateur() + " ajouté à la partie.");
            this.notifierJoueurs(joueur.getUtilisateur().getNomUtilisateur() + " à rejoind la partie.", false);
        } else {
            System.out.println("Partie pleine, impossible d'ajouter un autre joueur.");
        }
    }
    
    // Initialise la carte avec des tuiles et attribue des villes et soldats aux joueurs
    public void initialiserCarte() {
        Random rand = new Random(); // Générateur de nombres aléatoires

        // Initialisation des villes et des soldats pour chaque joueur
        for (int i = 0; i < nombreJoueurs; i++) {
            int x, y;
            do {
                x = rand.nextInt(MAX_X); // Génère une position X aléatoire
                y = rand.nextInt(MAX_Y); // Génère une position Y aléatoire
            } while (tuiles[x][y] != null); // Vérifie que la tuile est libre

            Ville ville = new Ville(); // Crée une nouvelle ville
            ville.capturer(joueurs[i]); // Attribue la ville au joueur
            tuiles[x][y] = ville; // Place la ville sur la carte
            tuiles[x][y].setPosition(x, y); // Définit la position de la ville

            // Crée un soldat pour le joueur et le place sur la ville
            Soldat soldat = new Soldat(x, y, joueurs[i], this);
            joueurs[i].ajouterSoldat(soldat);

            System.out.println("Ville attribuée au joueur " + joueurs[i].getUtilisateur().getNomUtilisateur()
                    + " en (" + x + ", " + y + ") avec un soldat.");
        }

        // Remplit les cases restantes avec des tuiles aléatoires
        for (int i = 0; i < MAX_X; i++) {
            for (int j = 0; j < MAX_Y; j++) {
                if (tuiles[i][j] == null) { // Vérifie si la case est libre
                    double random = Math.random(); // Génère un nombre entre 0 et 1
                    if (random < 0.15) {
                        tuiles[i][j] = new Montagne(); // Place une montagne
                    } else if (random < 0.5) {
                        tuiles[i][j] = new Foret(); // Place une forêt
                    } else {
                        tuiles[i][j] = new Plaine(); // Place une plaine (tuile vide)
                    }
                    tuiles[i][j].setPosition(i, j); // Définit la position de la tuile
                }
            }
        }
    }
    
    /**
     * Permet de notifier tous les joueurs de la partie d'un changement dans le jeu
     * @param message Le message associé a ce changement
     * @param plateauChange Indique si le plateau a changer. C'est a dire, indique si une entité a été créée ou déplacé, si une ville a été capturé, etc. 
     */
    public void notifierJoueurs(String message, boolean plateauChange) {
    	for (Joueur joueur : this.getJoueurs()) {
    		if (joueur != null && joueur.getWebSocket() != null)
    			joueur.getWebSocket().envoyerMessage(message, plateauChange, ConsoleType.JEUX);
		}
    }
    
    /**
     * Permet de notifier la fin de la partie à tous les joueurs$
     * Cette fonction coupe aussi le lien entre les joueurs et la partie.
     */
    public void notifierFinPartie() {
    	// Car on est sur que c'est le tour d'un joueur qui peut joueur, si il n'en reste qu'un alors c'est lui le dernier
    	Joueur dernierJoueur = this.getJoueurs()[this.getTour()];
    	dernierJoueur.addScore(100);
    	
    	for (Joueur joueur : this.getJoueurs()) {
    		if (joueur != null && joueur.getWebSocket() != null) {
				joueur.getWebSocket().envoyerMessageFin(dernierJoueur);
    			
    		}
    	}
	}
}