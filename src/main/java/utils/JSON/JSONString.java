package utils.JSON;

/**
 * Permet de représenter une chaîne de caractère dans un objet JSON
 * @author Théo KALIFA
 */
class JSONString implements JSONElementInterface {
	
	private String valeur;
	
	JSONString(String valeur) {
		this.valeur = valeur;
	}
	
	JSONString(char valeur) {
		this.valeur = Character.toString(valeur);
	}
		
	@Override
	public String toString() {
		return this.getValeur().toString();
	}
	
	@Override
	public Object getValeur() {
		return this.valeur;
	}
}
