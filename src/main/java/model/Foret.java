package model;

public class Foret extends Tuile {

    public Foret() {
        this.type = "Forêt";
    }

    /**
     * Simule le défrichage de la forêt par un soldat. 
     * Cette action transforme la forêt en plaine et rapporte des points de production.
     *
     * @param soldat Le soldat effectuant l'action
     * @return Les points de production gagnés
     */
    public void defricher(Soldat soldat) {
    	
    	if (this.getType()=="Forêt") {
    		// Défrichage de la forêt : transformation en plaine
    		System.out.println("Défrichage effectué par le soldat " + soldat.getJoueur().getUtilisateur().getNomUtilisateur());

    		// Remplacer la forêt par une plaine dans la carte
    		Partie partie = soldat.getPartie();
    		int x = soldat.getX();
    		int y = soldat.getY();
    		partie.getTuile(x, y).setType("Plaine");  // Changer le type de la tuile en plaine
        
    		// Ajouter les points de production pour le défrichage
    		soldat.getJoueur().ajouterPP(10);
    		
    	} else {
    		System.out.println("Forêt déjà défrichée. Pas de points de production.");
    	}
    }

    @Override
    public void interagir(Joueur joueur) {
        System.out.println("Interaction avec une forêt. Le soldat peut défricher.");
    }
}
