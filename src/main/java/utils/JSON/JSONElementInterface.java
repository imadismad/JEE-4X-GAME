package utils.JSON;

/**
 * Permet de définir le comportement de base d'un element JSON
 * @author Théo KALIFA
 *
 */
interface JSONElementInterface {	
	/**
	 * Renvoie la valeur de l'élement
	 * @return la valeur de l'élement
	 */
	Object getValeur();
	
    /**
     * Renvoie une représentation JSON de cet élément.
     * 
     * @return la chaîne JSON.
     */
    String toJSONString();
}
