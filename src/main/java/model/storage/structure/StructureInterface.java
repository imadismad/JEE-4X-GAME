package model.storage.structure;

public interface StructureInterface {
	/**
	 * Renvoie la valeur de la clef utilisé dans un système de stockage (BDD, Système de fichier, ...)
	 * @return une chaîne de caractère représentant le nom du champs dans le système de stockage
	 */
	String getValeur();
	
	/**
	 * @return la table correspondante au champ
	 */
	TableEnum getTable();
	
	/**
	 * @return Le type attendu par le champ
	 */
	Class<?> getTypeClasse(); 
}
