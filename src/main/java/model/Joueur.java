package model;
import java.util.List;
import java.util.ArrayList;

public class Joueur {
    private int score;
    private Object connexionWS;
    private Utilisateur utilisateur;
    private Partie partie;
    private List<Soldat> soldats;
    private int PP; // Points de production

    public Joueur(Utilisateur utilisateur, Partie partie) {
        this.utilisateur = utilisateur;
        utilisateur.setJoueur(this);
        this.score = 0;
        this.soldats = new ArrayList<>();
        this.partie = partie;
        this.PP = 0;
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

    // Getter et Setter pour les Points de Production (PP)
    public int getPP() {
        return PP;
    }

    public void setPP(int PP) {
        this.PP = PP;
    }

    // Méthode pour ajouter des PP
    public void ajouterPP(int points) {
        this.PP += points;
        System.out.println("Ajout de " + points + " PP. Total des PP : " + this.PP);
    }

    // Méthode pour retirer des PP
    public void retirerPP(int points) {
        if (this.PP >= points) {
            this.PP -= points;
            System.out.println("Retrait de " + points + " PP. Total des PP : " + this.PP);
        } else {
            System.out.println("Pas assez de PP pour effectuer cette action.");
        }
    }

    // Méthode pour créer un nouveau soldat sur une ville choisie, coût de 15 PP
    public boolean creerSoldatSurVille(Ville ville) {
        // Vérifier si la ville possède déjà un soldat
        if (ville.contientSoldat(this.partie)) {
            System.out.println("Un soldat est déjà présent sur la ville. Impossible de créer un nouveau soldat.");
            return false;
        }

        // Vérifier si le joueur a suffisamment de PP pour créer un soldat
        if (this.PP >= 15) {
            // Retirer 15 PP pour la création du soldat
            this.retirerPP(15);

            // Placer un soldat sur la ville choisie
            Soldat soldat = new Soldat(ville.getX(), ville.getY(), this, this.partie);
            this.ajouterSoldat(soldat);

            System.out.println("Un soldat a été créé sur la ville " + ville + " pour un coût de 15 PP.");
            return true;
        } else {
            System.out.println("Pas assez de PP pour créer un soldat sur cette ville.");
            return false;
        }
    }

    // Méthode pour ajouter des PP en fonction du nombre de villes possédées
    public void incrementerPPParVilles() {
        int nombreVilles = 0;

        // Compter le nombre de villes possédées par le joueur
        for (Tuile[] ligne : partie.getTuiles()) {
            for (Tuile tuile : ligne) {
                if (tuile instanceof Ville) {
                    Ville ville = (Ville) tuile;
                    if (ville.getAppartenance() == this) {
                        nombreVilles++;
                    }
                }
            }
        }

        // Ajouter 3 PP multipliés par le nombre de villes
        int ppGagnes = 3 * nombreVilles;
        this.ajouterPP(ppGagnes);
        System.out.println("Le joueur a " + nombreVilles + " villes. " + ppGagnes + " PP ajoutés.");
    }

    // Méthode pour notifier un changement d'état du joueur
    public void notifierChangement() {
        // Affichage du changement d'état du joueur
        System.out.println("Changement d'état pour le joueur : " + utilisateur.getNomUtilisateur());
        System.out.println("Score actuel : " + score);
        System.out.println("Nombre de soldats : " + soldats.size());
        System.out.println("Points de production actuels : " + PP);
    }
    
    public boolean hasSoldatsEtVilles() {
        // Vérifie si le joueur a des soldats
        boolean hasSoldats = !soldats.isEmpty();

        // Vérifie si le joueur possède des villes
        boolean hasVilles = false;
        for (Tuile[] ligne : partie.getTuiles()) {
            for (Tuile tuile : ligne) {
                if (tuile instanceof Ville) {
                    Ville ville = (Ville) tuile;
                    if (ville.getAppartenance() == this) {  // Vérifie si la ville appartient au joueur
                        hasVilles = true;
                        break; // On s'arrête dès qu'une ville appartenant au joueur est trouvée
                    }
                }
            }
            if (hasVilles) {
                break;  // On sort de la boucle si on a trouvé une ville du joueur
            }
        }

        // Le joueur doit avoir à la fois des soldats et des villes
        return hasSoldats && hasVilles;
    }


    public void rejoindrePartie(Partie partie) {
        this.partie = partie;
        partie.ajouterJoueur(this);
        System.out.println(utilisateur.getNomUtilisateur() + " a rejoint la partie.");
    }
}
