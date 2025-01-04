package model;

// Définition de la classe abstraite Tuile
public abstract class Tuile {
    // Attributs pour stocker les coordonnées de la tuile sur la carte
    private int x; // Coordonnée X de la tuile
    private int y; // Coordonnée Y de la tuile

    // Le type de la tuile (par exemple, "Forêt", "Montagne", "Ville")
    protected String type;

    /**
     * Méthode abstraite pour interagir avec la tuile.
     * Chaque sous-classe doit implémenter cette méthode pour définir
     * un comportement spécifique lors de l'interaction avec un joueur.
     *
     * @param joueur Le joueur qui interagit avec la tuile
     */
    public abstract void interagir(Joueur joueur);

    /**
     * Getter pour le type de la tuile.
     * 
     * @return Le type de la tuile sous forme de chaîne de caractères
     */
    public String getType() {
        return type;
    }

    /**
     * Setter pour définir le type de la tuile.
     * 
     * @param type Le type de la tuile
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Getter pour la coordonnée X de la tuile.
     * 
     * @return La coordonnée X
     */
    public int getX() {
        return x;
    }

    /**
     * Getter pour la coordonnée Y de la tuile.
     * 
     * @return La coordonnée Y
     */
    public int getY() {
        return y;
    }

    /**
     * Vérifie si un soldat se trouve sur cette tuile.
     * 
     * @param partie La partie en cours
     * @return true si un soldat est présent sur cette tuile, false sinon
     */
    public boolean contientSoldat(Partie partie) {
        // Parcourt les soldats de la partie pour vérifier leur position
        for (Soldat soldat : partie.getSoldats()) {
            if (soldat.getX() == this.x && soldat.getY() == this.y) {
                return true;
            }
        }
        return false; // Aucun soldat trouvé sur cette tuile
    }

    /**
     * Définit la position de la tuile sur la carte.
     * 
     * @param x La coordonnée X
     * @param y La coordonnée Y
     */
    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Récupère un soldat présent sur cette tuile.
     * 
     * @param partie La partie en cours
     * @return Le soldat trouvé sur cette tuile ou null si aucun soldat n'est présent
     */
    public Soldat getSoldat(Partie partie) {
        // Parcourt les soldats de la partie pour trouver celui qui est sur cette tuile
        for (Soldat soldat : partie.getSoldats()) {
            if (soldat.getX() == this.x && soldat.getY() == this.y) {
                return soldat; // Renvoie le soldat trouvé
            }
        }
        return null; // Aucun soldat trouvé
    }
}
