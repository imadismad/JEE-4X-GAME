package utils.JSON;

/**
 * Permet de représenter un nombre dans un objet JSON
 * @author Théo KALIFA
 *
 * @param <T> Un type héritant de Number 
 */
class JSONNumber<T extends Number> implements JSONElementInterface {
	
	private T valeur;
	
	JSONNumber(T valeur) {
		this.valeur = valeur;
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
