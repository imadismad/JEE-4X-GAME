package model;

public class Ville extends Tuile {
    private Joueur appartenance;
    private int DP; //Defense Points

    public Ville() {
        this.type = "Ville";
        this.appartenance = null;
        this.DP = 3;
    }

    @Override
    public void interagir(Joueur joueur) {
        System.out.println("Interaction avec une ville.");
    }

    public void capturer(Joueur joueur) {
        this.appartenance = joueur;
        System.out.println("La ville a été capturée par le joueur : "+joueur.getUtilisateur().getNomUtilisateur());
    }

    public Joueur getAppartenance() {
        return appartenance;
    }
    
    public void setAppartenance(Joueur app) {
        this.appartenance = app;
    }
    
    public int getDP() {
        return DP;
    }
    
    public void setDP(int dp) {
        this.DP = dp;
    }
    
    public void loseDP() {
    	this.DP--;
    }

}
