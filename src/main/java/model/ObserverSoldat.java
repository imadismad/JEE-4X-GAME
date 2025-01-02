package model;

public interface ObserverSoldat {

    /**
     * Méthode de notification qui est appelée lorsque les points de vie du soldat changent.
     *
     * @param ancienPointVie Le nombre de points de vie avant le changement
     * @param nouveauPointVie Le nombre de points de vie après le changement
     * @param soldat Le soldat dont les points de vie ont changé
     */
    void notificationVie(int ancienPointVie, int nouveauPointVie, Soldat soldat);
}
