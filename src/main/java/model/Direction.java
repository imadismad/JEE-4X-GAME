package model;

public enum Direction {
    HAUT,    // Déplacement vers le haut
    BAS,     // Déplacement vers le bas
    GAUCHE,  // Déplacement vers la gauche
    DROITE,  // Déplacement vers la droite
    STATIQUE; // Pas de mouvement

    /**
     * Retourne les décalages x et y pour chaque direction.
     * Cela peut être utile pour déplacer un soldat sur la carte.
     */
    public int[] getDelta() {
        switch (this) {
            case HAUT:
                return new int[] { 0, -1 };
            case BAS:
                return new int[] { 0, 1 };
            case GAUCHE:
                return new int[] { -1, 0 };
            case DROITE:
                return new int[] { 1, 0 };
            case STATIQUE:
            default:
                return new int[] { 0, 0 };
        }
    }
}
