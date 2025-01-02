package model;

import java.util.Map;

public class Soldat {
    private int vie;
    private int x;
    private int y;
    private Joueur joueur;
    private Partie partie;

    public Soldat(int x, int y, Joueur joueur, Partie partie) {
        this.vie = 3;
        this.x = x;
        this.y = y;
        this.joueur = joueur;
        this.partie = partie;
    }
    
    public void perdreVie() {
    	vie--;
    }
    
    public void soin() {
    	vie++;
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

        Tuile cible = joueur.getPartie().getTuile(nx, ny); // Suppose qu'un soldat a un lien vers son joueur et sa partie.
        if (cible instanceof Montagne) {
            System.out.println("Impossible de se déplacer sur une montagne. Réessayez.");
            return;
        }

        //TODO VERIFIER LE CAS OU LE SOLDAT EST SUR UNE CASE VIDE
        
        // Vérification des interactions avec le contenu de la case
        if (cible.contientSoldat(this.partie)) {
            Soldat soldatCible = cible.getSoldat(this.partie);
            if (soldatCible.getJoueur() != this.joueur) {
                System.out.println("Attaque du soldat ennemi !");
                soldatCible.perdreVie();
                if (soldatCible.estMort()) {
                    System.out.println("Soldat ennemi éliminé. Vous prenez sa place.");
                    joueur.retirerSoldat(soldatCible);
                } else {
                    System.out.println("Soldat ennemi encore en vie. Déplacement annulé.");
                    dep = false;
                }
            } else {
                System.out.println("Case occupée par un allié. Déplacement annulé.");
                dep = false;
            }
        }

        if (cible instanceof Ville) {
            Ville ville = (Ville)cible;
            if (ville.getAppartenance() != this.joueur) {
                System.out.println("Attaque de la ville ennemie !");
                ville.loseDP();
                if (ville.getDP()<=0) {
                	System.out.println("Capture de la ville.");
                	ville.setAppartenance(this.getJoueur());
                } else {
                	System.out.println("Points de Defense restants : " + ville.getDP());
                	dep = false;
                }
            } else {
                System.out.println("Vous ne pouvez pas attaquer une ville alliée.");
            }
        }

        // Déplacement normal
        if (dep != false) {
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
