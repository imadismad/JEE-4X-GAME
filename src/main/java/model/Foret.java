package model;

public class Foret extends Tuile {

    public Foret() {
        this.type = "Forêt";
    }

    /**
     * Simule le défrichage de la forêt par un soldat. 
     * Cette action rapporte des points de production.
     *
     * @param soldat Le soldat effectuant l'action
     * @return Les points de production gagnés
     */
    public int defricher(Soldat soldat) {
        // Défrichage de la forêt
        System.out.println("Défrichage effectué par le soldat " + soldat.getJoueur().getUtilisateur().getNomUtilisateur());
        return 10; // Points de production
    }

    @Override
    public void interagir(Joueur joueur) {
        System.out.println("Interaction avec une forêt. Le soldat peut défricher.");
    }
}
