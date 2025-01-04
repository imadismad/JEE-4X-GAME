package model;

public class Ville extends Tuile {
    private Joueur appartenance; // Le joueur qui possède la ville
    private int DP; // Points de Défense de la ville

    public Ville() {
        this.type = "Ville"; // Définition du type de la tuile comme "Ville"
        this.appartenance = null; // Initialisation de l'appartenance à null, la ville n'a pas encore de propriétaire
        this.DP = 15; // Valeur initiale des points de défense (15 points)
    }

    @Override
    public void interagir(Joueur joueur) {
        // Interaction générique avec une ville
        System.out.println("Interaction avec une ville.");
    }

    public void capturer(Joueur joueur) {
        // Cette méthode permet de capturer la ville pour le joueur spécifié
        this.appartenance = joueur; // Attribue la ville au joueur
        System.out.println("La ville a été capturée par le joueur : "+joueur.getUtilisateur().getNomUtilisateur());
    }

    public Joueur getAppartenance() {
        return appartenance; // Retourne le joueur qui possède la ville
    }
    
    public void setAppartenance(Joueur app) {
        this.appartenance = app; // Définit le propriétaire de la ville
    }
    
    public int getDP() {
        return DP; // Retourne le nombre de points de défense de la ville
    }
    
    public void setDP(int dp) {
        this.DP = dp; // Modifie les points de défense de la ville
    }
    
    public void loseDP(int att) {
        // Réduit les points de défense de la ville en fonction des dégâts d'une attaque
    	this.DP = DP - att; // Soustrait les points d'attaque du total de points de défense
    }
}