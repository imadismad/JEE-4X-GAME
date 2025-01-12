package model;

public interface PartieObserver {
	/**
	 * Fonction appelé lors de la fin de la partie
	 * @param p La partie qui est terminé
	 */
	void onFin(Partie p);
}
