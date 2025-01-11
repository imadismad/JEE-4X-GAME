package model.storage;

import java.util.List;
import java.util.Map;

import model.storage.exception.StockageAccesException;
import model.storage.exception.StockageStructureException;
import model.storage.exception.StockageValeurException;
import model.storage.structure.StructureInterface;
import model.storage.structure.TableEnum;

/**
 * Permet de définir les méthodes pour intéragir avec une méthode de stockage (BDD, Système de fichier, ...)
 * @author Théo KALIFA
 *
 */
public interface StockageInterface {
		
	/**
	 * Méthode vérifiant si une valeur existe déjà dans une table donnée
	 * @param table La table dans la quelle on doit chercher
	 * @param champ Le champ de la valeur
	 * @param valeur La valeur dont l'existance doit être vérifié
	 * @throws StockageAccesException Levé si une erreur d'accés au système de stockage suvient
	 * @throws StockageValeurException Levé si le système de stockage est corrompu.
	 */
	boolean estPresent(TableEnum table, StructureInterface champ, Object valeur) throws StockageAccesException, StockageValeurException;
	
	/**
	 * Méthode ajoutant une entrée dans une table de donnée
	 * @param table La table de donnée ou les valeurs doivent-être ajouté
	 * @param valeurs Les valeurs à ajouter. Les clefs de la map sont les champs de la table, et les valeurs les objets dans le bon type.
	 * @throws StockageAccesException Levé si une erreur d'accés au système de stockage suvient
	 * @throws IllegalArgumentException Levé si valeurs n'a pas tous les champs de la table, ou si le type est incorrect.
	 */
	void ajouterValeur(TableEnum table, Map<StructureInterface, Object> valeurs) throws StockageAccesException, IllegalArgumentException;
	
	/**
	 * Méthode qui récupère toutes les valeurs correspondante aux données d'entrée passé en paramètre.
	 * valeurs contient pour chaque champs d'une table, les valeurs attendues
	 * @param table La table d'origine des données
	 * @param valeurs Les valeurs attendu pour chaque entrée de la table
	 * @return Un tableau d'entrée pour chaque valeurs de la table qui corresponde aux valeurs attendu
	 * @throws StockageAccesException Levé si une erreur d'accés au système de stockage suvient
	 * @throws StockageValeurException Levé si le système de stockage est corrompu.
	 * @throws IllegalArgumentException Levé si un champ n'a pas le bon type, ou s'il n'est pas dans la table
	 */
	List<Map<StructureInterface, Object>> getToutesEntrees(TableEnum table, Map<StructureInterface, Object> valeurs) throws StockageAccesException, StockageValeurException, IllegalArgumentException;

	/**
	 * Méthode qui récupère la premières valeur correspondante aux données d'entrée passé en paramètre.
	 * valeurs contient pour chaque champs d'une table, les valeurs attendues
	 * @param table La table d'origine des données
	 * @param valeurs Les valeurs attendu pour chaque entrée de la table
	 * @return Les valeurs de la table qui corresponde aux valeurs attendu, ou null si aucune entrée n'a été trouvé
	 * @throws StockageAccesException Levé si une erreur d'accés au système de stockage suvient
	 * @throws StockageValeurException Levé si le système de stockage est corrompu.
	 * @throws IllegalArgumentException Levé si un champ n'a pas le bon type, ou s'il n'est pas dans la table
	 */
	Map<StructureInterface, Object> getPremiereEntrees(TableEnum table, Map<StructureInterface, Object> valeurs) throws StockageAccesException, StockageValeurException, IllegalArgumentException;
	
	static boolean typeCorrespond(StructureInterface champ, Object valeur) {
		return champ.getTypeClasse().equals(valeur.getClass());
	}
	
	/**
	 * Renvoie l'instance de StockageInterface à utiliser
	 * @return l'instance de StockageInterface à utiliser
	 * @throws StockageAccesException Levé si une erreur d'accès au système de stockage survient
	 * @throws StockageStructureException Levé en cas de problème dans la structure des données
	 */
	public static StockageInterface getInstance() throws StockageStructureException, StockageAccesException {
		return CSVStockage.getInstance();
	}
}
