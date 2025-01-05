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

        // Associer un joueur au session utilisateur pour le test
        // Par exemple : connecter Joueur1
        session.setAttribute(Utilisateur.CLEF_UTILISATEUR_SESSION, utilisateur1);
    }
}
