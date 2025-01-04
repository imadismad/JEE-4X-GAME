package model.storage.structure;

/**
 * Énumération représentant une table dans un système de stockage (BDD, Système de fichier, ...)
 * Chaque objet est représentant par un chaine de caractère représentant sa valeur dans le système de stockage,
 * et une classe représentant l'énumération contenant les champs de cette table.
 * @author Théo KALIFA
 *
 */
public enum TableEnum {
	UTILISATEUR("utilisateur", UtilisateurStructure.class);
	
	private String valeur;
	private Class<? extends Enum<? extends StructureInterface>> enumClasse;
	
	private TableEnum(String valeur, Class<? extends Enum<? extends StructureInterface>> enumClasse) {
		this.valeur = valeur;
		this.enumClasse = enumClasse;
	}
	
	public String getValeur() {
		return valeur;
	}
	
	public Class<? extends Enum<? extends StructureInterface>> getEnumClasse() {
		return enumClasse;
	}
	
	/**
	 * Permet de vérifier si un objet passé en paramètres représente un champ de la table.</br>
	 * Par exemple, un String n'est pas un champ de la valeur TableEnum.UTILISATEUR,
	 * mais UtilisateurStructure.NOM_UTILISATEUR est un champ de TableEnum.UTILISATEUR.
	 * @param obj L'objet à vérifier
	 * @return true si obj représente un champ de la table
	 */
	public boolean estChampDe(Object obj)  {
		return obj.getClass().equals(getEnumClasse());
	}
	
	/**
	 * Récupère une liste de champ pour la table donné.
	 * @return la liste de champ
	 */
	public StructureInterface[] getChamps() {
		return (StructureInterface[]) getEnumClasse().getEnumConstants();
	}
}
