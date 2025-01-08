package utils.JSON;

import java.util.ArrayList;
import java.util.List;

public class JSONArray implements JSONElementInterface {
	
	private List<JSONElementInterface> valeur;
	
	public JSONArray() {
		this.valeur = new ArrayList<>();
	}
	
	/**
	 * Ajoute une valeur à la list
	 * @param valeur La valeur à ajouter
	 */
	public void ajouter(byte valeur) {
		this.valeur.add(new JSONNumber<Byte>(valeur));
	}

	/**
	 * Ajoute une valeur à la list
	 * @param valeur La valeur à ajouter
	 */
	public void ajouter(short valeur) {
		this.valeur.add(new JSONNumber<Short>(valeur));
	}
	
	/**
	 * Ajoute une valeur à la list
	 * @param valeur La valeur à ajouter
	 */
	public void ajouter(int valeur) {
		this.valeur.add(new JSONNumber<Integer>(valeur));
	}
	
	/**
	 * Ajoute une valeur à la list
	 * @param valeur La valeur à ajouter
	 */
	public void ajouter(long valeur) {
		this.valeur.add(new JSONNumber<Long>(valeur));
	}
	
	/**
	 * Ajoute une valeur à la list
	 * @param valeur La valeur à ajouter
	 */
	public void ajouter(float valeur) {
		this.valeur.add(new JSONNumber<Float>(valeur));
	}
	
	/**
	 * Ajoute une valeur à la list
	 * @param valeur La valeur à ajouter
	 */
	public void ajouter(double valeur) {
		this.valeur.add(new JSONNumber<Double>(valeur));
	}
	
	/**
	 * Ajoute une valeur à la list
	 * @param valeur La valeur à ajouter
	 */
	public void ajouter(boolean valeur) {
		this.valeur.add(new JSONBoolean(valeur));
	}
	
	/**
	 * Ajoute une valeur à la list
	 * @param valeur La valeur à ajouter
	 */
	public void ajouter(char valeur) {
		this.valeur.add(new JSONString(valeur));
	}
	
	/**
	 * Ajoute une valeur à la list
	 * @param valeur La valeur à ajouter
	 */
	public void ajouter(String valeur) {
		this.valeur.add(new JSONString(valeur));
	}
	
    /**
     * Ajoute un objet JSON (JSONObject) à la liste.
     * @param jsonObject L'objet JSON à ajouter
     */
    public void ajouter(JSONObject jsonObject) {
        this.valeur.add(jsonObject);
    }

    /**
     * Ajoute un autre JSONArray à la liste.
     * @param jsonArray Le tableau JSON à ajouter
     */
    public void ajouter(JSONArray jsonArray) {
        this.valeur.add(jsonArray);
    }
	
	@Override
	public String toString() {
		boolean premierElement = true;
		StringBuilder builder = new StringBuilder();
		builder.append("[");
		for (JSONElementInterface element : this.valeur) {
			if (premierElement)
				premierElement = false;
			else
				builder.append(",");
			
			builder.append(element.toString());
		}
		
		builder.append("]");
		return builder.toString();
	}

	@Override
	public Object getValeur() {
		return this.valeur;
	}
	
    @Override
    public String toJSONString() {
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        boolean premierElement = true;
        for (JSONElementInterface element : valeur) {
            if (premierElement) {
                premierElement = false;
            } else {
                builder.append(",");
            }
            builder.append(element.toJSONString());
        }
        builder.append("]");
        return builder.toString();
    }
}
