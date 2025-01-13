package model;

import java.util.ArrayList; // Utilisé pour créer une liste dynamique de soldats
// Importation des bibliothèques nécessaires
import java.util.List; // Interface pour gérer des collections de soldats

import controlers.webSocket.ConsoleJeu;
import jakarta.websocket.Session;

// Définition de la classe Joueur
public class Joueur {
    // Attributs de la classe
    private int score; // Score du joueur
    private ConsoleJeu connexionWS; // Objet représentant la connexion WebSocket (non utilisé dans le code)
    private Utilisateur utilisateur; // L'utilisateur associé au joueur
    private Partie partie; // La partie à laquelle le joueur participe
    private List<Soldat> soldats; // Liste des soldats contrôlés par le joueur
    private int PP; // Points de production (ressources pour créer des unités)

    // Constructeur de la classe Joueur
    public Joueur(Utilisateur utilisateur, Partie partie) {
        this.utilisateur = utilisateur; // Associe un utilisateur au joueur
        utilisateur.setJoueur(this); // Relie l'utilisateur à cet objet joueur
        this.score = 0; // Initialise le score à 0
        this.soldats = new ArrayList<>(); // Initialise une liste vide de soldats
        this.partie = partie; // Définit la partie à laquelle le joueur participe
        this.PP = 0; // Initialise les points de production à 0
        this.connexionWS = null;
    }

    public ConsoleJeu getWebSocket() {
		return this.connexionWS;
	}
    
    public void setWebSocket(ConsoleJeu webSocket) {
    	this.connexionWS = webSocket;
    }
    
    // Getter pour obtenir l'utilisateur associé au joueur
    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    // Getter pour le score
    public int getScore() {
        return score;
    }

    // Méthode pour ajouter des points au score
    public void addScore(int sc) {
        this.score += sc; // Augmente le score du joueur
    }

    // Getter pour obtenir la partie associée au joueur
    public Partie getPartie() {
        return partie;
    }

    // Méthode pour ajouter un soldat à la liste du joueur
    public void ajouterSoldat(Soldat soldat) {
        soldats.add(soldat);
    }

    // Méthode pour retirer un soldat de la liste du joueur
    public void retirerSoldat(Soldat soldat) {
        soldats.remove(soldat);
    }

    // Getter pour obtenir la liste des soldats du joueur
    public List<Soldat> getSoldats() {
        return soldats;
    }


    // Getter pour les Points de Production (PP)
    public int getPP() {
        return PP;
    }

    // Setter pour définir les Points de Production
    public void setPP(int PP) {
        this.PP = PP;
    }

    // Méthode pour ajouter des Points de Production
    public void ajouterPP(int points) {
        this.PP += points;
        System.out.println("Ajout de " + points + " PP. Total des PP : " + this.PP);
    }

    // Méthode pour retirer des Points de Production
    public void retirerPP(int points) {
        if (this.PP >= points) { // Vérifie si le joueur a suffisamment de PP
            this.PP -= points; // Diminue les PP du joueur
            System.out.println("Retrait de " + points + " PP. Total des PP : " + this.PP);
        } else {
            System.out.println("Pas assez de PP pour effectuer cette action.");
        }
    }

    // Méthode pour créer un nouveau soldat sur une ville
    public boolean creerSoldatSurVille(Ville ville) {
        // Vérifie si la ville possède déjà un soldat
        if (ville.contientSoldat(this.partie)) {
            System.out.println("Un soldat est déjà présent sur la ville. Impossible de créer un nouveau soldat.");
            return false;
        }

        // Vérifie si le joueur a suffisamment de PP
        if (this.PP >= 15) {
            this.retirerPP(15); // Retire 15 PP pour la création du soldat

            // Crée un nouveau soldat sur la position de la ville
            Soldat soldat = new Soldat(ville.getX(), ville.getY(), this, this.partie);
            this.ajouterSoldat(soldat); // Ajoute le soldat à la liste du joueur

            System.out.println("Un soldat a été créé sur la ville " + ville + " pour un coût de 15 PP.");

            this.getPartie().incrementerTour();
            this.getPartie().notifierJoueurs(
        		String.format(
    				"%s a créé un soldat sur une ville contre 15 PP.",
    				this.getUtilisateur().getNomUtilisateur()
    			),
        		true
        	);
            return true;
        } else {
            System.out.println("Pas assez de PP pour créer un soldat sur cette ville.");
            return false;
        }
    }

    // Méthode pour augmenter les PP en fonction du nombre de villes possédées
    public void incrementerPPParVilles() {
        int nombreVilles = 0; // Initialise le compteur de villes

        // Parcourt les tuiles de la carte pour compter les villes appartenant au joueur
        for (Tuile[] ligne : partie.getTuiles()) {
            for (Tuile tuile : ligne) {
                if (tuile instanceof Ville) { // Vérifie si la tuile est une ville
                    Ville ville = (Ville) tuile;
                    if (ville.getAppartenance() == this) { // Vérifie si la ville appartient au joueur
                        nombreVilles++;
                    }
                }
            }
        }

        // Ajoute 3 PP pour chaque ville possédée
        int ppGagnes = 3 * nombreVilles;
        this.ajouterPP(ppGagnes);
        System.out.println("Le joueur a " + nombreVilles + " villes. " + ppGagnes + " PP ajoutés.");
    }

    // Méthode pour notifier un changement d'état du joueur
    public void notifierChangement() {
        // Affiche les informations sur l'état actuel du joueur
        System.out.println("Changement d'état pour le joueur : " + utilisateur.getNomUtilisateur());
        System.out.println("Score actuel : " + score);
        System.out.println("Nombre de soldats : " + soldats.size());
        System.out.println("Points de production actuels : " + PP);
    }

    // Méthode pour vérifier si le joueur possède à la fois des soldats et des villes
    public boolean hasSoldatsEtVilles() {
        // Vérifie si le joueur a des soldats
        boolean hasSoldats = !soldats.isEmpty();

        // Vérifie si le joueur possède des villes
        boolean hasVilles = false;
        for (Tuile[] ligne : partie.getTuiles()) {
            for (Tuile tuile : ligne) {
                if (tuile instanceof Ville) {
                    Ville ville = (Ville) tuile;
                    if (ville.getAppartenance() == this) { // Vérifie si la ville appartient au joueur
                        hasVilles = true;
                        break; // Sort de la boucle si une ville est trouvée
                    }
                }
            }
            if (hasVilles) {
                break; // Sort de la boucle principale si une ville est trouvée
            }
        }

        // Retourne vrai si le joueur a des soldats et des villes
        return hasSoldats && hasVilles;
    }

    // Méthode pour rejoindre une partie
    public void rejoindrePartie(Partie partie) {
        this.partie = partie; // Associe la partie au joueur
        partie.ajouterJoueur(this); // Ajoute le joueur à la partie
        System.out.println(utilisateur.getNomUtilisateur() + " a rejoint la partie.");
    }
    
    public void quitterPartie() {
    	this.partie = null;
    }
}
