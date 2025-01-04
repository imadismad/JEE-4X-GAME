package model;

import java.util.Map;

public class Soldat {
    private int vie;
    private int x;
    private int y;
    private Joueur joueur;
    private Partie partie;

    public Soldat(int x, int y, Joueur joueur, Partie partie) {
        this.vie = 15;
        this.x = x;
        this.y = y;
        this.joueur = joueur;
        this.partie = partie;
    }
    
    public Partie getPartie() {
    	return partie;
    }
    
    public void perdreVie(int pv) {
    	vie = vie-pv;
    }
    
    public void soin() {
        int soin = (int) (Math.random() * 3) + 2; // Génère un nombre entre 2 et 4
        vie = Math.min(vie + soin, 15); // Assure que la vie ne dépasse pas 15
    }

    public int getVie() {
    	return vie;
    }
    
    public boolean estMort() {
    	return this.vie <= 0;
    }

    public void deplacer(Direction direction) {
        int nx = x, ny = y;
        boolean dep = true;

        // Calcul des nouvelles coordonnées
        switch (direction) {
            case HAUT: nx--; break;
            case BAS: nx++; break;
            case GAUCHE: ny--; break;
            case DROITE: ny++; break;
            default: break;
        }

        // Vérification des limites
        if (nx < 0 || nx >= Partie.MAX_X || ny < 0 || ny >= Partie.MAX_Y) {
            System.out.println("Déplacement hors limites.");
            return;
        }

        Tuile cible = joueur.getPartie().getTuile(nx, ny);

        if (cible instanceof Montagne) {
            System.out.println("Impossible de se déplacer sur une montagne. Réessayez.");
            return;
        }

        // Gestion des interactions avec un soldat sur la tuile cible
        System.out.println("Vérification d'un soldat.");
        if (cible.contientSoldat(this.partie)) {
            System.out.println("Soldat détecté.");
            Soldat soldatCible = cible.getSoldat(this.partie);
            if (soldatCible.getJoueur() != this.joueur) {
                System.out.println("Attaque du soldat ennemi !");
                int forceAttaque = (int) (Math.random() * 6) + 1;
                System.out.println("Force d'attaque contre le soldat : " + forceAttaque);

                soldatCible.perdreVie(forceAttaque);
                if (soldatCible.estMort()) {
                    System.out.println("Soldat ennemi éliminé. Vous prenez sa place.");
                    soldatCible.getJoueur().retirerSoldat(soldatCible);
                    this.getJoueur().addScore(10); //10 points par soldat mort
                } else {
                    System.out.println("Soldat ennemi encore en vie.");
                    dep = false;
                }
            } else {
                System.out.println("Case occupée par un allié. Déplacement annulé.");
                dep = false;
            }
        }

        // Gestion des interactions avec une ville sur la tuile cible
        if (cible instanceof Ville) {
            Ville ville = (Ville) cible;
            if (ville.getAppartenance() != this.joueur) {
                System.out.println("Attaque de la ville ennemie !");
                int forceAttaqueVille = (int) (Math.random() * 6) + 1;
                System.out.println("Force d'attaque contre la ville : " + forceAttaqueVille);

                ville.loseDP(forceAttaqueVille);
                if (ville.getDP() <= 0) {
                    System.out.println("Ville capturée !");
                    ville.setAppartenance(this.getJoueur());
                    this.getJoueur().addScore(50); //50 points par ville capturée

                    // Vérifier s'il y a un soldat ennemi sur la ville
                    if (cible.contientSoldat(this.partie)) {
                        Soldat soldatEnnemi = cible.getSoldat(this.partie);
                        if (soldatEnnemi.getJoueur() != this.joueur) {
                            System.out.println("Soldat ennemi présent sur la ville capturée. Élimination du soldat.");
                            soldatEnnemi.getJoueur().retirerSoldat(soldatEnnemi);
                            this.getJoueur().addScore(10);
                            dep = true;
                        }
                    }
                } else {
                    System.out.println("Points de Défense restants : " + ville.getDP());
                    dep = false;
                }
            } else {
                System.out.println("Ville alliée détectée. Pas d'attaque possible.");
            }
        }


        // Déplacement normal si aucune contrainte n'annule l'action
        if (dep) {
            this.setX(nx);
            this.setY(ny);
            System.out.println("Soldat déplacé en direction de " + direction + " vers (" + nx + ", " + ny + ")");
        }
    }



    public boolean attaquer(Direction direction) {
        // Simule une attaque
        System.out.println("Attaque dans la direction de " + direction);
        return true;
    }

    public Map<Direction, Action> getActionPossible() {
        // Retourne une carte des actions possibles
        return Map.of(Direction.HAUT, Action.ATTAQUER, Direction.BAS, Action.DEPLACER);
    }

    public void notifierObserver() {
        // Notifie les observateurs du changement d'état
        System.out.println("Notification des observateurs pour le soldat.");
    }

    public Joueur getJoueur() {
        return joueur;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
    
    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }
}
