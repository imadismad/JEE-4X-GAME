package model;

public abstract class Tuile {
	private int x; // Coordonnée X de la tuile
    private int y; // Coordonnée Y de la tuile

    protected String type; // Le type de la tuile (par exemple, Forêt, Montagne, Ville)

    /**
     * Permet d'interagir avec la tuile.
     * Chaque sous-classe (Forêt, Montagne, Ville) peut définir cette méthode de manière spécifique.
     * 
     * @param joueur Le joueur qui interagit avec la tuile
     */
    public abstract void interagir(Joueur joueur);

    public String getType() {
        return type;
    }
    
    public boolean contientSoldat(Partie partie) {
        // Parcourt les soldats de la partie pour vérifier leur position
        for (Soldat soldat : partie.getSoldats()) {
            if (soldat.getX() == this.x && soldat.getY() == this.y) {
                return true;
            }
        }
        return false;
    }
    
    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public Soldat getSoldat(Partie partie) {
        for (Soldat soldat : partie.getSoldats()) {
            if (soldat.getX() == this.x && soldat.getY() == this.y) {
                return soldat;
            }
        }
        return null;
    }
}
