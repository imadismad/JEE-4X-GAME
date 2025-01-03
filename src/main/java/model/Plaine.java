package model;

public class Plaine extends Tuile {

    public Plaine() {
        this.type = "Plaine";
    }

    @Override
    public void interagir(Joueur joueur) {
        System.out.println("Interaction avec une plaine. Rien ne se passe.");
    }
}