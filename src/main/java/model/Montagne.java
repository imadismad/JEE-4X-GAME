package model;

public class Montagne extends Tuile {

    public Montagne() {
        this.type = "Montagne";
    }

    @Override
    public void interagir(Joueur joueur) {
        System.out.println("Impossible d'interagir avec une montagne.");
        // Ajouter une exception ou un comportement d'erreur si nécessaire
    }
}
