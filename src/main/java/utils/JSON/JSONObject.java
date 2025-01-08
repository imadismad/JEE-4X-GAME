package utils.JSON;

import java.util.HashMap;
import java.util.Map;

public class JSONObject implements JSONElementInterface {
	
	private Map<String, JSONElementInterface> map;
	
	public JSONObject() {
		this.map = new HashMap<String, JSONElementInterface>();
	}
	
	/**
	 * Ajoute une valeur à la list
	 * @param valeur La valeur à ajouter
	 */
	public void ajouter(String clef, byte valeur) {
		this.map.put(clef, new JSONNumber<Byte>(valeur));
	}

	/**
	 * Ajoute une valeur à la list
	 * @param valeur La valeur à ajouter
	 */
	public void ajouter(String clef, short valeur) {
		this.map.put(clef, new JSONNumber<Short>(valeur));
	}
	
	/**
	 * Ajoute une valeur à la list
	 * @param valeur La valeur à ajouter
	 */
	public void ajouter(String clef, int valeur) {
		this.map.put(clef, new JSONNumber<Integer>(valeur));
	}
	
	/**
	 * Ajoute une valeur à la list
	 * @param valeur La valeur à ajouter
	 */
	public void ajouter(String clef, long valeur) {
		this.map.put(clef, new JSONNumber<Long>(valeur));
	}
	
	/**
	 * Ajoute une valeur à la list
	 * @param valeur La valeur à ajouter
	 */
	public void ajouter(String clef, float valeur) {
		this.map.put(clef, new JSONNumber<Float>(valeur));
	}
	
	/**
	 * Ajoute une valeur à la list
	 * @param valeur La valeur à ajouter
	 */
	public void ajouter(String clef, double valeur) {
		this.map.put(clef, new JSONNumber<Double>(valeur));
	}
	
	/**
	 * Ajoute une valeur à la list
	 * @param valeur La valeur à ajouter
	 */
	public void ajouter(String clef, boolean valeur) {
		this.map.put(clef, new JSONBoolean(valeur));
	}
	
	/**
	 * Ajoute une valeur à la list
	 * @param valeur La valeur à ajouter
	 */
	public void ajouter(String clef, char valeur) {
		this.map.put(clef, new JSONString(valeur));
	}
	
	/**
	 * Ajoute une valeur à la list
	 * @param valeur La valeur à ajouter
	 */
	public void ajouter(String clef, String valeur) {
		this.map.put(clef, new JSONString(valeur));
	}
	
	/**
	 * Ajoute une valeur à la list
	 * @param valeur La valeur à ajouter
	 */
	public void ajouter(String clef, JSONArray array) {
		this.map.put(clef, array);
	}
	
	/**
	 * Ajoute une valeur à la list
	 * @param valeur La valeur à ajouter
	 */
	public void ajouter(String clef, JSONObject object) {
		this.map.put(clef, object);
	}
	
	@Override
	public String toString() {
		boolean premierElement = true;
		StringBuilder builder = new StringBuilder();
		builder.append("{");
		for (Map.Entry<String, JSONElementInterface> entree : map.entrySet()) {
			if (premierElement)
				premierElement = false;
			else
				builder.append(",");
			
			builder.append(entree.getKey());
			builder.append(":");
			builder.append(entree.getValue());
		}
		
		builder.append("}");
		return builder.toString();
	}
	
    @Override
    public String toJSONString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{");
        boolean premierElement = true;
        for (Map.Entry<String, JSONElementInterface> entree : map.entrySet()) {
            if (premierElement) {
                premierElement = false;
            } else {
                builder.append(",");
            }
            builder.append("\"").append(entree.getKey()).append("\":")
                   .append(entree.getValue().toJSONString());
        }
        builder.append("}");
        return builder.toString();
    }

	@Override
	public Object getValeur() {
		return map;
	}

}
