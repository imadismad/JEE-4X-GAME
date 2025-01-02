package model;
import java.util.List;
import java.util.ArrayList;

public class Joueur {
    private int score;
    private Object connexionWS;
    private Utilisateur utilisateur;
    private Partie partie;
    private List<Soldat> soldats;

    public Joueur(Utilisateur utilisateur, Partie partie) {
        this.utilisateur = utilisateur;
        this.score = 0;
        this.soldats = new ArrayList<>();
        this.partie = partie;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public Partie getPartie() {
        return partie;
    }

    public void ajouterSoldat(Soldat soldat) {
        soldats.add(soldat);
    }
    
    public void retirerSoldat(Soldat soldat) {
        soldats.remove(soldat);
    }

    public List<Soldat> getSoldats() {
        return soldats;
    }

    public void notifierChangement() {
        // Affichage du changement d'état du joueur
        System.out.println("Changement d'état pour le joueur : " + utilisateur.getNomUtilisateur());
        System.out.println("Score actuel : " + score);
        System.out.println("Nombre de soldats : " + soldats.size());
    }

    public void rejoindrePartie(Partie partie) {
        this.partie = partie;
        partie.ajouterJoueur(this);
        System.out.println(utilisateur.getNomUtilisateur() + " a rejoint la partie.");
    }
}
