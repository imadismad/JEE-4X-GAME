package model;

import java.util.Scanner;

public class TestJeu {
    public static void main(String[] args) {
        System.out.println("==== DÉMARRAGE DU TEST DU JEU ====");

        Partie partie = new Partie();

        // Création des utilisateurs et des joueurs
        Utilisateur utilisateur1 = new Utilisateur("1J", new int[]{100, 200});
        Utilisateur utilisateur2 = new Utilisateur("2J", new int[]{150, 250});
        Utilisateur utilisateur3 = new Utilisateur("3J", new int[]{120, 300});
        Utilisateur utilisateur4 = new Utilisateur("4J", new int[]{90, 400});

        Joueur joueur1 = new Joueur(utilisateur1, partie);
        Joueur joueur2 = new Joueur(utilisateur2, partie);
        Joueur joueur3 = new Joueur(utilisateur3, partie);
        Joueur joueur4 = new Joueur(utilisateur4, partie);

        // Ajout des joueurs à la partie
        partie.ajouterJoueur(joueur1);
        partie.ajouterJoueur(joueur2);
        partie.ajouterJoueur(joueur3);
        partie.ajouterJoueur(joueur4);

        // Initialisation de la carte
        partie.initialiserCarte();

        // Scanner pour lire les entrées de l'utilisateur
        Scanner scanner = new Scanner(System.in);

        // Simulation de la partie où chaque joueur joue à son tour
        boolean partieEnCours = true;
        int tour = 1;
        while (partieEnCours) {
            // Déterminer le joueur actuel (tour de joueur)
            Joueur joueurActuel = partie.getSoldats().get((tour - 1) % partie.getSoldats().size()).getJoueur();
            System.out.println("\n--- Tour " + tour + " ---");
            System.out.println("C'est le tour de " + joueurActuel.getUtilisateur().getNomUtilisateur());
            partie.afficherCarte(); // Afficher la carte après chaque tour

            // Demander à l'utilisateur quelle action il veut effectuer
            System.out.println("Choisissez une action :");
            System.out.println("1 - Déplacer un soldat");
            System.out.println("2 - Passer son tour");
            System.out.print("Votre choix (1/2) : ");
            int choix = scanner.nextInt();

            switch (choix) {
                case 1:
                    // Demander de déplacer un soldat
                    System.out.print("Entrez les coordonnées du soldat à déplacer (x y) : ");
                    int x = scanner.nextInt();
                    int y = scanner.nextInt();
                    System.out.print("Entrez la direction de déplacement (1-Haut, 2-Bas, 3-Gauche, 4-Droite) : ");
                    int direction = scanner.nextInt();
                    Direction dir = Direction.STATIQUE;
                    if (direction == 1) dir = Direction.HAUT;
                    else if (direction == 2) dir = Direction.BAS;
                    else if (direction == 3) dir = Direction.GAUCHE;
                    else if (direction == 4) dir = Direction.DROITE;
                    
                    // Trouver le soldat correspondant et le déplacer
                    Soldat soldat = null;
                    for (Soldat s : joueurActuel.getSoldats()) {
                        if (s.getX() == x && s.getY() == y) {
                            soldat = s;
                            break;
                        }
                    }
                    if (soldat != null) {
                        soldat.deplacer(dir); // Déplacer le soldat
                    } else {
                        System.out.println("Soldat non trouvé aux coordonnées (" + x + ", " + y + ").");
                    }
                    break;

                case 2:
                    // Passer son tour
                    System.out.println("Le joueur " + joueurActuel.getUtilisateur().getNomUtilisateur() + " passe son tour.");
                    break;

                default:
                    System.out.println("Choix invalide, vous passez votre tour.");
                    break;
            }

            // Vérifier si la partie doit continuer ou non (par exemple, après un certain nombre de tours)
            System.out.print("Voulez-vous continuer à jouer ? (o/n) : ");
            String continuer = scanner.next();
            if (continuer.equalsIgnoreCase("n")) {
                partieEnCours = false; // Fin de la partie
            }
            tour++; // Passer au tour suivant
        }

        // Fin de la partie
        System.out.println("==== FIN DU TEST ====");
        scanner.close();
    }
}
