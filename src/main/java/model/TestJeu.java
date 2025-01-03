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

        partie.ajouterJoueur(joueur1);
        partie.ajouterJoueur(joueur2);
        partie.ajouterJoueur(joueur3);
        partie.ajouterJoueur(joueur4);

        // Initialisation de la carte
        partie.initialiserCarte();

        Scanner scanner = new Scanner(System.in);
        boolean partieEnCours = true;
        int indexJoueurActuel = 0;

        while (partieEnCours) {
            Joueur joueurActuel = partie.getJoueurs()[indexJoueurActuel];
            System.out.println("\n--- Tour de " + joueurActuel.getUtilisateur().getNomUtilisateur() + " ---");
            partie.afficherCarte();

            boolean tourTermine = false;
            while (!tourTermine) {
            	System.out.println("Vous avez " + joueurActuel.getPP() + " points de productions (PP).");
                System.out.println("Choisissez une action :");
                System.out.println("1 - Déplacer un soldat");
                System.out.println("2 - Défricher la case actuelle");
                System.out.println("3 - Soigner un soldat");
                System.out.println("4 - Créer un nouveau soldat (Coût 15 PP)");
                System.out.println("5 - Passer votre tour");
                System.out.print("Votre choix (1/2/3/4/5) : ");
                int choix = scanner.nextInt();

                switch (choix) {
                    case 1:
                        System.out.print("Entrez les coordonnées du soldat à déplacer (x y) : ");
                        int x = scanner.nextInt();
                        int y = scanner.nextInt();
                        System.out.print("Entrez la direction (1-Haut, 2-Bas, 3-Gauche, 4-Droite) : ");
                        int direction = scanner.nextInt();
                        Direction dir = Direction.values()[direction - 1];

                        Soldat soldatDeplacer = joueurActuel.getSoldats().stream()
                                .filter(s -> s.getX() == x && s.getY() == y)
                                .findFirst().orElse(null);

                        if (soldatDeplacer != null) {
                            soldatDeplacer.deplacer(dir);
                            tourTermine = true;
                        } else {
                            System.out.println("Aucun soldat trouvé aux coordonnées spécifiées.");
                        }
                        break;

                    case 2:
                        System.out.print("Entrez les coordonnées du soldat pour défricher (x y) : ");
                        x = scanner.nextInt();
                        y = scanner.nextInt();

                        Soldat soldatDefricher = joueurActuel.getSoldats().stream()
                                .filter(s -> s.getX() == x && s.getY() == y)
                                .findFirst().orElse(null);

                        if (soldatDefricher != null) {
                            Tuile tuile = partie.getTuile(x, y);
                            if (tuile instanceof Foret) {
                                ((Foret) tuile).defricher(soldatDefricher);
                                tourTermine = true;
                            } else {
                                System.out.println("La case actuelle n'est pas une forêt.");
                            }
                        } else {
                            System.out.println("Aucun soldat trouvé aux coordonnées spécifiées.");
                        }
                        break;

                    case 3:
                        System.out.print("Entrez les coordonnées du soldat à soigner (x y) : ");
                        x = scanner.nextInt();
                        y = scanner.nextInt();

                        Soldat soldatSoigner = joueurActuel.getSoldats().stream()
                                .filter(s -> s.getX() == x && s.getY() == y)
                                .findFirst().orElse(null);

                        if (soldatSoigner != null) {
                            soldatSoigner.soin();
                            System.out.println("Le soldat a été soigné. Vie actuelle : " + soldatSoigner.getVie());
                            tourTermine = true;
                        } else {
                            System.out.println("Aucun soldat trouvé aux coordonnées spécifiées.");
                        }
                        break;

                    case 4:
                        System.out.print("Entrez les coordonnées de la ville où vous voulez créer un soldat (x y) : ");
                        x = scanner.nextInt();
                        y = scanner.nextInt();

                        // Trouver la tuile à ces coordonnées
                        Tuile tuileCible = partie.getTuile(x, y);
                        
                        // Vérifier si la tuile est bien une instance de Ville
                        if (tuileCible instanceof Ville) {
                            Ville villeCible = (Ville) tuileCible;

                            // Créer un soldat sur la ville choisie si le joueur a assez de PP
                            if (villeCible.getAppartenance()!=joueurActuel || !joueurActuel.creerSoldatSurVille(villeCible)) {
                                System.out.println("Échec de la création du soldat.");
                            } else {
                                System.out.println("Soldat créé sur la ville (" + x + ", " + y + ").");
                            }
                        } else {
                            System.out.println("Aucune ville trouvée à ces coordonnées.");
                        }
                        break;
//ne pas pouvoir faire pop sur une ville ennemie

                    case 5:
                        System.out.println("Tour passé.");
                        tourTermine = true;
                        break;

                    default:
                        System.out.println("Choix invalide, réessayez.");
                        break;
                }
            }
            
            joueurActuel.incrementerPPParVilles();

            // Passer au joueur suivant
            indexJoueurActuel = (indexJoueurActuel + 1) % partie.getJoueurs().length;
            if (indexJoueurActuel == 0) {
                System.out.println("--- Tous les joueurs ont joué. Fin de ce tour. ---");
            }

         // Vérification de la fin de la partie
            boolean joueurRestant = false;

            for (Joueur joueur : partie.getJoueurs()) {
                if (joueur != null && joueur.hasSoldatsEtVilles()) {
                    joueurRestant = true;  // Un joueur valide est encore en vie
                    break;
                }
            }

            // Si un seul joueur valide est encore en vie, la partie continue
            if (!joueurRestant) {
                System.out.println("La partie est terminée, il ne reste plus qu'un joueur avec des soldats et des villes.");
                partieEnCours = false;
            } else {
                System.out.print("Voulez-vous continuer à jouer ? (o/n) : ");
                String continuer = scanner.next();
                if (continuer.equalsIgnoreCase("n")) {
                    partieEnCours = false;
                }
            }
        }

        System.out.println("==== FIN DU TEST ====");
        scanner.close();
    }
}
