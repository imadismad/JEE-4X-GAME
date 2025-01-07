package model;

import jakarta.servlet.http.HttpSession;
import model.storage.StockageInterface;
import model.storage.exception.StockageAccesException;
import model.storage.exception.StockageStructureException;
import model.storage.exception.StockageValeurException;
import model.storage.structure.TableEnum;
import model.storage.structure.UtilisateurStructure;

public class Utilisateur {

	/**
	 * La clef permettant de récupérer l'objet utilisateur de la session courante
	 */
	public static final String CLEF_UTILISATEUR_SESSION = "utilisateur";
	
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
     * @return Le joueur associé à cet utilisateur
     */
    public Joueur getJoueur() {
        return this.joueur;
    }
    
    public boolean estEnPartie() {
		return this.getJoueur() != null;
	}
    
    public String getNomUtilisateur() {
        return nomUtilisateur;
    }

    /**
     * Vérifie si l'utilisateur est connecté via la session.
     * 
     * @param servletSession La session actuelle de l'utilisateur
     * @return true si l'utilisateur est connecté, sinon false
     */
    public static boolean estConnecte(HttpSession servletSession) {
        return servletSession.getAttribute(CLEF_UTILISATEUR_SESSION) != null;
    }
    
    /**
     * Récupère l'utilisateur à partir de la session si l'utilisateur est connecté.
     * 
     * @param servletSession La session actuelle de l'utilisateur
     * @return L'utilisateur si connecté, sinon null
     */
    public static Utilisateur getUtilisateur(HttpSession servletSession) {
        if (estConnecte(servletSession))
            return (Utilisateur) servletSession.getAttribute(CLEF_UTILISATEUR_SESSION);

        return null;
    }
    
    /**
     * Vérifie si un nom d'utilisateur existe dans le stockage du serveur
     * @throws StockageStructureException voir {@link StockageInterface#getInstance()}
     * @throws StockageValeurException voir {@link StockageInterface#estPresent(TableEnum, model.storage.structure.StructureInterface, Object)}
     * @throws StockageAccesException voir {@link StockageInterface#estPresent(TableEnum, model.storage.structure.StructureInterface, Object)}
     */
    public static boolean nomUtilisateurExiste(String nomUtilisateur) throws StockageAccesException, StockageValeurException, StockageStructureException {
    	return StockageInterface.getInstance().estPresent(TableEnum.UTILISATEUR, UtilisateurStructure.NOM_UTILISATEUR, nomUtilisateur);
    }
}
