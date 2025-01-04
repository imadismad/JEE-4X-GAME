package model;

public class Montagne extends Tuile {

    public Montagne() {
        this.type = "Montagne"; // Définit le type de la tuile comme "Montagne"
    }

    @Override
    public void interagir(Joueur joueur) {
        // Interaction générique avec une montagne, qui n'est pas autorisée
        System.out.println("Impossible d'interagir avec une montagne.");
    }
}
