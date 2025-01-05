document.addEventListener("DOMContentLoaded", () => {
    // Vérification que les variables nécessaires existent
    if (typeof joueurConnecte === "undefined" || typeof soldats === "undefined" || typeof joueurs === "undefined") {
        console.error("Les variables nécessaires (joueurConnecte, soldats, joueurs) ne sont pas définies !");
        return;
    }

    // Palette de couleurs prédéfinie (max 4 joueurs)
    const palette = ["#0074D9", "#FF4136", "#2ECC40", "#FFDC00"]; // Bleu, Rouge, Vert, Jaune
    const couleurs = {};

    // Assigner une couleur unique à chaque joueur (max 4 joueurs)
    joueurs.forEach((joueur, index) => {
        if (index < palette.length) {
            couleurs[joueur] = palette[index];
        } else {
            console.warn(`Pas assez de couleurs pour le joueur : ${joueur}`);
        }
    });

    // Appliquer les couleurs aux soldats
    document.querySelectorAll(".soldat").forEach(soldat => {
        const joueur = soldat.dataset.joueur.trim(); // Supprime les espaces potentiels
        if (joueur in couleurs) {
            soldat.style.filter = `drop-shadow(0 0 5px ${couleurs[joueur]})`;
        } else {
            console.warn(`Aucune couleur définie pour : ${joueur}`);
        }
    });

    // Ajouter la légende des couleurs
    const legende = document.getElementById("legende");
    if (legende) {
        Object.entries(couleurs).forEach(([joueur, couleur]) => {
            const joueurElement = document.createElement("div");
            joueurElement.className = "legende-joueur";
            joueurElement.style.backgroundColor = couleur; // Couleur du rectangle
            joueurElement.style.color = "white";
            joueurElement.style.padding = "5px 10px";
            joueurElement.style.margin = "5px auto";
            joueurElement.style.width = "150px";
            joueurElement.style.textAlign = "center";
            joueurElement.style.borderRadius = "5px";
            joueurElement.style.fontWeight = "bold";

            // Ajouter "(vous)" pour le joueur connecté
            joueurElement.textContent = joueur === joueurConnecte ? `${joueur} (vous)` : joueur;

            legende.appendChild(joueurElement);
        });
    } else {
        console.error("L'élément #legende est introuvable dans le DOM.");
    }
});
