package model;

public class Foret extends Tuile {

    public Foret() {
        this.type = "Forêt"; // Définition du type de la tuile comme "Forêt".
    }

    /**
     * Simule le défrichage de la forêt par un soldat. 
     * Cette action transforme la forêt en plaine et rapporte des points de production.
     *
     * @param soldat Le soldat effectuant l'action
     * @return Les points de production gagnés
     */
    public void defricher(Soldat soldat) {
    	
    	// Vérifie si la tuile est toujours une forêt
    	if (this.getType() == "Forêt") {
    		// Défrichage de la forêt : transformation en plaine
    		System.out.println("Défrichage effectué par le soldat " + soldat.getJoueur().getUtilisateur().getNomUtilisateur());

    		// Remplacer la forêt par une plaine dans la carte
    		Partie partie = soldat.getPartie(); // Récupère la partie en cours
    		int x = soldat.getX(); // Coordonnée X du soldat
    		int y = soldat.getY(); // Coordonnée Y du soldat
    		partie.getTuile(x, y).setType("Plaine");  // Change le type de la tuile en "Plaine".
        
    		// Ajouter les points de production pour le défrichage
    		soldat.getJoueur().ajouterPP(10); // Récompense de 10 PP pour le défrichage
    		soldat.getPartie().incrementerTour();
    		
    	} else {
    		// Si la tuile n'est pas une forêt, informe que le défrichage n'est pas possible
    		System.out.println("Forêt déjà défrichée. Pas de points de production.");
    	}
    }

    @Override
    public void interagir(Joueur joueur) {
        // Interaction générique avec une forêt
        System.out.println("Interaction avec une forêt. Le soldat peut défricher.");
    }
}
