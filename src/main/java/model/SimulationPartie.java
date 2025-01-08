package model;

import jakarta.servlet.http.HttpSession;

public class SimulationPartie {

    public static void simulerPartie(HttpSession session) {
        // Initialiser une nouvelle partie
        Partie partie = new Partie();

        // Créer des utilisateurs
        Utilisateur utilisateur1 = new Utilisateur("Joueur1", null);
        Utilisateur utilisateur2 = new Utilisateur("Joueur2", null);
        Utilisateur utilisateur3 = new Utilisateur("Joueur3", null);
        Utilisateur utilisateur4 = new Utilisateur("Joueur4", null);

        // Associer les utilisateurs à des joueurs
        Joueur joueur1 = new Joueur(utilisateur1, partie);
        Joueur joueur2 = new Joueur(utilisateur2, partie);
        Joueur joueur3 = new Joueur(utilisateur3, partie);
        Joueur joueur4 = new Joueur(utilisateur4, partie);

        // Ajouter les joueurs à la partie
        partie.ajouterJoueur(joueur1);
        partie.ajouterJoueur(joueur2);
        partie.ajouterJoueur(joueur3);
        partie.ajouterJoueur(joueur4);

        // Initialiser la carte
        partie.initialiserCarte();

        // Ajouter un deuxième soldat pour Joueur1
        Tuile[][] tuiles = partie.getTuiles();

        for (int i = 0; i < tuiles.length; i++) {
            for (int j = 0; j < tuiles[i].length; j++) {
                // Rechercher une case vide pour le deuxième soldat
                if (!(tuiles[i][j] instanceof Ville) && !tuiles[i][j].contientSoldat(partie)) {
                    Soldat secondSoldat = new Soldat(i, j, joueur1, partie);
                    joueur1.ajouterSoldat(secondSoldat);
                    tuiles[i][j].setPosition(i, j); // Assigner la position sur la tuile
                    System.out.println("Soldat ajouté pour Joueur1 en position (" + i + ", " + j + ")");
                    break;
                }
            }
        }

        // Associer un joueur à la session utilisateur pour le test
        // Par exemple : connecter Joueur1
        session.setAttribute(Utilisateur.CLEF_UTILISATEUR_SESSION, utilisateur1);
    }
}
