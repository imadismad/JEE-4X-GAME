package model;

public enum Action {
    DEPLACER,   // Déplacer une unité sur la carte
    SOIGNER,    // Soigner une unité
    ATTAQUER,   // Attaquer une autre unité
    DEFRICHER,  // Défricher une tuile Forêt
    RIEN;       // Aucune action effectuée

    /**
     * Retourne une description de l'action.
     * Cela peut être utile pour les messages ou l'affichage dans l'interface utilisateur.
     */
    public String getDescription() {
        switch (this) {
            case DEPLACER:
                return "Déplacer l'unité";
            case SOIGNER:
                return "Soigner l'unité";
            case ATTAQUER:
                return "Attaquer une unité ennemie";
            case DEFRICHER:
                return "Défricher une forêt";
            case RIEN:
                return "Aucune action";
            default:
                return "Action inconnue";
        }
    }
}
