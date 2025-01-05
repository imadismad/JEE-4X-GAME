package model;

public class Utilisateur {

    private String nomUtilisateur;
    private int[] scores;
    private Joueur joueur;

    public Utilisateur(String nomUtilisateur, int[] scores) {
        this.nomUtilisateur = nomUtilisateur;
        this.scores = scores;
        this.joueur = null;
    }

    /**
     * Vérifie si l'utilisateur est connecté via la session.
     * 
     * @param servletSession La session actuelle de l'utilisateur
     * @return true si l'utilisateur est connecté, sinon false
     */
    public boolean estConnecte(Object servletSession) {
        return servletSession != null;
    }
    
    public void setJoueur(Joueur j) {
    	this.joueur = j;
    }
    
    public Joueur getJoueur() {
    	return joueur;
    }

    /**
     * Retourne l'objet Joueur associé à cet utilisateur.
     * 
     * @return Le joueur associé à cet utilisateur
     */
    //public Joueur getJoueur() {
    //    return new Joueur(this);
    //}

    /**
     * Récupère l'utilisateur à partir de la session si l'utilisateur est connecté.
     * 
     * @param servletSession La session actuelle de l'utilisateur
     * @return L'utilisateur si connecté, sinon null
     */
    public Utilisateur getUtilisateur(Object servletSession) {
        if (estConnecte(servletSession)) {
            return this;
        }
        return null;
    }

    public String getNomUtilisateur() {
        return nomUtilisateur;
    }
}
