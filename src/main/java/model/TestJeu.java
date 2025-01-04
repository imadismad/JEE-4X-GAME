package model;

import java.util.Scanner;

public class TestJeu {
    public static void main(String[] args) {
        System.out.println("==== DÉMARRAGE DU JEU ====");

        Partie partie = new Partie(); // Initialisation de la partie

        // Création des utilisateurs et des joueurs
        Utilisateur utilisateur1 = new Utilisateur("1J", new int[]{100, 200}); // Utilisateur 1 avec coordonnées initiales
        Utilisateur utilisateur2 = new Utilisateur("2J", new int[]{150, 250}); // Utilisateur 2
        Utilisateur utilisateur3 = new Utilisateur("3J", new int[]{120, 300}); // Utilisateur 3
        Utilisateur utilisateur4 = new Utilisateur("4J", new int[]{90, 400});  // Utilisateur 4

        Joueur joueur1 = new Joueur(utilisateur1, partie); // Création du joueur 1
        Joueur joueur2 = new Joueur(utilisateur2, partie); // Création du joueur 2
        Joueur joueur3 = new Joueur(utilisateur3, partie); // Création du joueur 3
        Joueur joueur4 = new Joueur(utilisateur4, partie); // Création du joueur 4

        // Ajout des joueurs à la partie
        partie.ajouterJoueur(joueur1);
        partie.ajouterJoueur(joueur2);
        partie.ajouterJoueur(joueur3);
        partie.ajouterJoueur(joueur4);

        // Initialisation de la carte
        partie.initialiserCarte(); // Prépare la carte pour le jeu

        Scanner scanner = new Scanner(System.in); // Initialisation du scanner pour les entrées utilisateur
        boolean partieEnCours = true; // Indique si la partie est encore active
        int indexJoueurActuel = 0; // Index pour suivre le joueur en cours

        while (partieEnCours) { // Boucle principale du jeu
            Joueur joueurActuel = partie.getJoueurs()[indexJoueurActuel]; // Récupère le joueur actuel
            System.out.println("\n--- Tour de " + joueurActuel.getUtilisateur().getNomUtilisateur() + " ---");
            partie.afficherCarte(); // Affiche l'état actuel de la carte

            boolean tourTermine = false; // Indique si le joueur a terminé son tour
            while (!tourTermine) { // Boucle pour gérer les actions du joueur
                System.out.println("Vous avez " + joueurActuel.getPP() + " points de productions (PP).");
                System.out.println("Choisissez une action :");
                System.out.println("1 - Déplacer un soldat");
                System.out.println("2 - Défricher la case actuelle");
                System.out.println("3 - Soigner un soldat");
                System.out.println("4 - Créer un nouveau soldat (Coût 15 PP)");
                System.out.println("5 - Passer votre tour");
                System.out.print("Votre choix (1/2/3/4/5) : ");
                int choix = scanner.nextInt(); // Lecture du choix utilisateur

                switch (choix) { // Gestion des actions
                    case 1: // Déplacement d'un soldat
                        System.out.print("Entrez les coordonnées du soldat à déplacer (x y) : ");
                        int x = scanner.nextInt();
                        int y = scanner.nextInt();
                        System.out.print("Entrez la direction (1-Haut, 2-Bas, 3-Gauche, 4-Droite) : ");
                        int direction = scanner.nextInt();
                        Direction dir = Direction.values()[direction - 1]; // Conversion en énumération Direction

                        // Recherche du soldat à déplacer
                        Soldat soldatDeplacer = joueurActuel.getSoldats().stream()
                                .filter(s -> s.getX() == x && s.getY() == y)
                                .findFirst().orElse(null);

                        if (soldatDeplacer != null) { // Si un soldat est trouvé
                            soldatDeplacer.deplacer(dir); // Déplace le soldat
                            tourTermine = true; // Termine le tour
                        } else {
                            System.out.println("Aucun soldat trouvé aux coordonnées spécifiées.");
                        }
                        break;

                    case 2: // Défrichage d'une case
                        System.out.print("Entrez les coordonnées du soldat pour défricher (x y) : ");
                        x = scanner.nextInt();
                        y = scanner.nextInt();

                        // Recherche du soldat pour défricher
                        Soldat soldatDefricher = joueurActuel.getSoldats().stream()
                                .filter(s -> s.getX() == x && s.getY() == y)
                                .findFirst().orElse(null);

                        if (soldatDefricher != null) { // Si un soldat est trouvé
                            Tuile tuile = partie.getTuile(x, y);
                            if (tuile instanceof Foret) { // Vérifie si la tuile est une forêt
                                ((Foret) tuile).defricher(soldatDefricher); // Défriche la tuile
                                tourTermine = true; // Termine le tour
                            } else {
                                System.out.println("La case actuelle n'est pas une forêt.");
                            }
                        } else {
                            System.out.println("Aucun soldat trouvé aux coordonnées spécifiées.");
                        }
                        break;

                    case 3: // Soin d'un soldat
                        System.out.print("Entrez les coordonnées du soldat à soigner (x y) : ");
                        x = scanner.nextInt();
                        y = scanner.nextInt();

                        // Recherche du soldat à soigner
                        Soldat soldatSoigner = joueurActuel.getSoldats().stream()
                                .filter(s -> s.getX() == x && s.getY() == y)
                                .findFirst().orElse(null);

                        if (soldatSoigner != null) { // Si un soldat est trouvé
                            soldatSoigner.soin(); // Soigne le soldat
                            System.out.println("Le soldat a été soigné. Vie actuelle : " + soldatSoigner.getVie());
                            tourTermine = true; // Termine le tour
                        } else {
                            System.out.println("Aucun soldat trouvé aux coordonnées spécifiées.");
                        }
                        break;

                    case 4: // Création d'un nouveau soldat
                        System.out.print("Entrez les coordonnées de la ville où vous voulez créer un soldat (x y) : ");
                        x = scanner.nextInt();
                        y = scanner.nextInt();

                        // Recherche de la ville à ces coordonnées
                        Tuile tuileCible = partie.getTuile(x, y);
                        
                        if (tuileCible instanceof Ville) { // Vérifie si la tuile est une ville
                            Ville villeCible = (Ville) tuileCible;

                            // Vérifie que la ville appartient au joueur et tente de créer un soldat
                            if (villeCible.getAppartenance() != joueurActuel || !joueurActuel.creerSoldatSurVille(villeCible)) {
                                System.out.println("Échec de la création du soldat.");
                            } else {
                                System.out.println("Soldat créé sur la ville (" + x + ", " + y + ").");
                            }
                        } else {
                            System.out.println("Aucune ville trouvée à ces coordonnées.");
                        }
                        break;

                    case 5: // Passer son tour
                        System.out.println("Tour passé.");
                        tourTermine = true; // Termine le tour
                        break;

                    default: // Choix invalide
                        System.out.println("Choix invalide, réessayez.");
                        break;
                }
            }
            
            joueurActuel.incrementerPPParVilles(); // Incrémente les points de production pour les villes du joueur

            // Passer au joueur suivant tout en sautant les joueurs éliminés
            do {
                indexJoueurActuel = (indexJoueurActuel + 1) % partie.getJoueurs().length;
            } while (!partie.getJoueurs()[indexJoueurActuel].hasSoldatsEtVilles());
            
            // Affichage des scores après le tour complet
            if (indexJoueurActuel == 0) {
                System.out.println("--- Tous les joueurs ont joué. Fin de ce tour. ---");
                System.out.println("\n--- Scores actuels ---");
                for (Joueur joueur : partie.getJoueurs()) {
                    if (joueur != null) {
                        System.out.println(joueur.getUtilisateur().getNomUtilisateur() + ": " + joueur.getScore() + " points");
                    }
                }
            }

            // Vérification de la fin de la partie
            int joueurRestant = 0;
            Joueur dernierJoueur = null;

            for (Joueur joueur : partie.getJoueurs()) {
                if (joueur != null && joueur.hasSoldatsEtVilles()) {
                    joueurRestant +=1; // Compte les joueurs encore actifs
                    dernierJoueur = joueur; // Enregistre le dernier joueur actif
                }
            }

            // Si un seul joueur est encore en vie, la partie se termine
            if (joueurRestant == 1) {
                dernierJoueur.addScore(100); // Bonus pour le dernier joueur en vie
                System.out.println("La partie est terminée, il ne reste plus qu'un joueur avec des soldats et des villes.");
                partieEnCours = false; // Fin de la partie
            }
        }

        System.out.println("==== FIN DU JEU ====");
        scanner.close(); // Fermeture du scanner
    }
}
