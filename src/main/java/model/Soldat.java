package model;

// Importation de la bibliothèque Map
import java.util.Map;

// Définition de la classe Soldat
public class Soldat {
    // Attributs de la classe Soldat
    private int vie; // Représente les points de vie du soldat
    private int x; // Position X du soldat sur la carte
    private int y; // Position Y du soldat sur la carte
    private Joueur joueur; // Référence au joueur propriétaire du soldat
    private Partie partie; // Référence à la partie dans laquelle évolue le soldat

    // Constructeur de la classe Soldat
    public Soldat(int x, int y, Joueur joueur, Partie partie) {
        this.vie = 15; // Initialisation des points de vie à 15
        this.x = x; // Initialisation de la position X
        this.y = y; // Initialisation de la position Y
        this.joueur = joueur; // Association au joueur propriétaire
        this.partie = partie; // Association à la partie
    }

    // Getter pour récupérer la partie associée au soldat
    public Partie getPartie() {
        return partie;
    }

    // Méthode pour réduire les points de vie du soldat
    public void perdreVie(int pv) {
        vie = vie - pv; // Réduction des points de vie
    }

    // Méthode pour soigner le soldat
    public void soin() {
        int soin = (int) (Math.random() * 3) + 2; // Génère un soin entre 2 et 4 points
        int vieDepart = this.getVie();
        vie = Math.min(vie + soin, 15); // Limite les points de vie à un maximum de 15

        this.getPartie().incrementerTour();
        this.getPartie().notifierJoueurs(
    		String.format(
				"%s a soigné sont joueur en (%d, %d) qui récupère %d point de vie",
				this.getJoueur().getUtilisateur().getNomUtilisateur(),
				this.getX(),
				this.getY(),
				this.getVie() - vieDepart
			),
    		true
		);
    }

    // Getter pour récupérer les points de vie actuels du soldat
    public int getVie() {
        return vie;
    }

    // Vérifie si le soldat est mort (points de vie <= 0)
    public boolean estMort() {
        return this.vie <= 0;
    }

    // Méthode pour déplacer le soldat dans une direction donnée
    public void deplacer(Direction direction) {
        int nx = x, ny = y; // Variables pour les nouvelles coordonnées
        boolean dep = true; // Indique si le déplacement est possible
        boolean incr = false;

        // Calcul des nouvelles coordonnées en fonction de la direction
        switch (direction) {
            case HAUT: nx--; break;
            case BAS: nx++; break;
            case GAUCHE: ny--; break;
            case DROITE: ny++; break;
            default: break;
        }

        // Vérifie si les nouvelles coordonnées sont dans les limites de la carte
        if (nx < 0 || nx >= Partie.MAX_X || ny < 0 || ny >= Partie.MAX_Y) {
            System.out.println("Déplacement hors limites.");
            return; // Annule le déplacement si hors limites
        }

        // Récupération de la tuile cible sur la carte
        Tuile cible = joueur.getPartie().getTuile(nx, ny);

        // Vérifie si la tuile cible est une montagne
        if (cible instanceof Montagne) {
            System.out.println("Impossible de se déplacer sur une montagne. Réessayez.");
            return; // Annule le déplacement
        }

        // Gestion des interactions avec un soldat sur la tuile cible
        System.out.println("Vérification d'un soldat.");
        if (cible.contientSoldat(this.partie)) {
            System.out.println("Soldat détecté.");
            Soldat soldatCible = cible.getSoldat(this.partie);
            if (soldatCible.getJoueur() != this.joueur) {
                System.out.println("Attaque du soldat ennemi !");
                int forceAttaque = (int) (Math.random() * 6) + 1; // Force d'attaque aléatoire entre 1 et 6
                System.out.println("Force d'attaque contre le soldat : " + forceAttaque);

                soldatCible.perdreVie(forceAttaque); // Réduction des points de vie du soldat ennemi
                if (soldatCible.estMort()) {
                    System.out.println("Soldat ennemi éliminé. Vous prenez sa place.");
                    soldatCible.getJoueur().retirerSoldat(soldatCible); // Retrait du soldat ennemi
                    this.getJoueur().addScore(10); // Ajout de points pour l'élimination
                    
                    this.getPartie().notifierJoueurs(
                		String.format(
            				"%s a tué l'unité de %s en (%d, %d), il gagne 10 points.",
            				this.getJoueur().getUtilisateur().getNomUtilisateur(),
            				soldatCible.getJoueur().getUtilisateur().getNomUtilisateur(),
            				soldatCible.getX(),
            				soldatCible.getY()
            			),
                		false
                	);
                } else {
                    System.out.println("Soldat ennemi encore en vie.");
                    incr=true;
                    dep = false; // Annule le déplacement
                    
                    this.getPartie().notifierJoueurs(
                		String.format(
            				"%s a attaqué l'unité de %s en (%d, %d) qui a perdu %d point de vie.",
            				this.getJoueur().getUtilisateur().getNomUtilisateur(),
            				soldatCible.getJoueur().getUtilisateur().getNomUtilisateur(),
            				soldatCible.getX(),
            				soldatCible.getY(),
            				forceAttaque
            			),
                		true
                	);
                }
            } else {
                System.out.println("Case occupée par un allié. Déplacement annulé.");
                dep = false; // Annule le déplacement
            }
        }

        // Gestion des interactions avec une ville sur la tuile cible
        if (cible instanceof Ville) {
            Ville ville = (Ville) cible;
            if (ville.getAppartenance() != this.joueur) {
                System.out.println("Attaque de la ville ennemie !");
                int forceAttaqueVille = (int) (Math.random() * 6) + 1; // Force d'attaque aléatoire contre la ville
                System.out.println("Force d'attaque contre la ville : " + forceAttaqueVille);

                ville.loseDP(forceAttaqueVille); // Réduction des points de défense de la ville
                if (ville.getDP() <= 0) {
                    System.out.println("Ville capturée !");
                    Joueur ancienProprio = ville.getAppartenance();
                    ville.setAppartenance(this.getJoueur()); // Changement de propriétaire
                    this.getJoueur().addScore(50); // Ajout de points pour la capture

                    // Vérifie s'il y a un soldat ennemi sur la ville
                    if (cible.contientSoldat(this.partie)) {
                        Soldat soldatEnnemi = cible.getSoldat(this.partie);
                        if (soldatEnnemi.getJoueur() != this.joueur) {
                            System.out.println("Soldat ennemi présent sur la ville capturée. Élimination du soldat.");
                            soldatEnnemi.getJoueur().retirerSoldat(soldatEnnemi); // Élimination du soldat ennemi
                            this.getJoueur().addScore(10); // Ajout de points pour l'élimination
                            dep = true; // Autorise le déplacement
                        }
                    }
                    
                    this.getPartie().notifierJoueurs(
                		String.format(
            				"%s a capturé la ville de %s en (%d, %d), il gagne 50 points.",
            				this.getJoueur().getUtilisateur().getNomUtilisateur(),
            				ancienProprio.getUtilisateur().getNomUtilisateur(),
            				ville.getX(),
            				ville.getY()
            			),
                		false
                	);
                } else {
                    System.out.println("Points de Défense restants : " + ville.getDP());
                    incr=true;
                    dep = false; // Annule le déplacement
                    
                    this.getPartie().notifierJoueurs(
                		String.format(
            				"%s a attaqué la ville de %s en (%d, %d) qui perd %d point de vie.",
            				this.getJoueur().getUtilisateur().getNomUtilisateur(),
            				ville.getAppartenance().getUtilisateur().getNomUtilisateur(),
            				ville.getX(),
            				ville.getY(),
            				forceAttaqueVille
            			),
                		true
                	);
                }
            } else {
                System.out.println("Ville alliée détectée. Pas d'attaque possible.");
            }
        }
        
    	if (incr==true && dep==false) {
    		this.getPartie().incrementerTour();
    	}

        // Si le déplacement est autorisé, met à jour les coordonnées
        if (dep) {
            this.setX(nx);
            this.setY(ny);
            this.getPartie().incrementerTour();
            System.out.println("Soldat déplacé en direction de " + direction + " vers (" + nx + ", " + ny + ")");
            this.getPartie().notifierJoueurs(
        		String.format(
    				"%s déplace son unité en (%d, %d).",
    				this.getJoueur().getUtilisateur().getNomUtilisateur(),
    				this.getX(),
    				this.getY()
    			),
        		true
        	);
        }
    }

    // Simule une attaque dans une direction donnée
    public boolean attaquer(Direction direction) {
        System.out.println("Attaque dans la direction de " + direction);
        return true; // Retourne toujours vrai (simulation)
    }

    // Retourne une carte des actions possibles en fonction des directions
    public Map<Direction, Action> getActionPossible() {
        return Map.of(Direction.HAUT, Action.ATTAQUER, Direction.BAS, Action.DEPLACER); // Exemples d'actions
    }
    
    public boolean peutSeDeplacer(String direction) {
        switch (direction) {
            case "haut": return !partie.estTuileInaccessible(x - 1, y,this);
            case "bas": return !partie.estTuileInaccessible(x + 1, y,this);
            case "gauche": return !partie.estTuileInaccessible(x, y - 1,this);
            case "droite": return !partie.estTuileInaccessible(x, y + 1,this);
            default: return false;
        }
    }

    public boolean peutDefricher() {
        return this.getPartie().getTuile(this.getX(), this.getY()).getType() == "Forêt";
    }

    public boolean peutSeSoigner() {
        return this.getVie() < 15;
    }


    // Notifie les observateurs d'un changement d'état
    public void notifierObserver() {
        System.out.println("Notification des observateurs pour le soldat.");
    }

    // Getters et setters pour les attributs X, Y et joueur
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