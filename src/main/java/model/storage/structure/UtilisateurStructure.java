package model.storage.structure;

/**
 * Énumération représentant la strucutre d'un utilisateur dans un système de stockage
 * Cette énumération contient tous les champs d'un utilisateur
 * Elle peut être utilisé pour intéragir avec le système utilisé pour stocker les données des utilisateurs
 * @author Théo KALIFA
 *
 */
public enum UtilisateurStructure implements StructureInterface {
	NOM_UTILISATEUR("nomUtilisateur", String.class),
	MOT_DE_PASSE("motDePasse", String.class);

	/**
	 * Représente le nom d'un champ permetant de représenter un utilisateur
	 */
	private String valeur;
	private Class<?> typeClasse;
	
	private UtilisateurStructure(String valeur, Class<?> typeClasse) {
		this.valeur = valeur;
		this.typeClasse = typeClasse;
	}
	
	@Override
	public String getValeur() {
		return this.valeur;
	}
	
	@Override
	public TableEnum getTable() {
		return TableEnum.UTILISATEUR;
	}

	@Override
	public Class<?> getTypeClasse() {
		return this.typeClasse;
	}
}
