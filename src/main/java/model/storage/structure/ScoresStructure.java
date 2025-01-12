package model.storage.structure;

public enum ScoresStructure implements StructureInterface {
	/**
	 * Même utilisateur que celui dans la table des utilisateurs
	 */
	UTILISATEUR("utilisateur", String.class),
	DATE("date", String.class),
	SCORE("score", Integer.class),
	VICTOIRE("victoire", Boolean.class);
	
	private String valeur;
	private Class<?> type;

	private ScoresStructure(String valeur, Class<?> type) {
		this.valeur = valeur;
		this.type = type;
	}
	
	@Override
	public String getValeur() {
		return this.valeur;
	}

	@Override
	public TableEnum getTable() {
		return TableEnum.SCORES;
	}

	@Override
	public Class<?> getTypeClasse() {
		return this.type;
	}

}
