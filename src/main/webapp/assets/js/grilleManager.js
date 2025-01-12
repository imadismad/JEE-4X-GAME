function chargerGrille() {
    fetch('/JEE-4X-GAME/api/grille')
        .then(response => {
            if (!response.ok) {
                throw new Error(`Erreur API: ${response.status}`);
            }
            return response.json();
        })
        .then(data => {
            afficherGrille(data.grille);
            const couleurs = mettreAJourLegende(data.joueurs);

            // Déclencher l'événement personnalisé avec les couleurs des joueurs
            document.dispatchEvent(new CustomEvent("grilleChargee", { detail: { couleurs } }));
        })
        .catch(error => {
            console.error("Erreur lors du chargement de la grille :", error);
        });
}

function normalizeType(type) {
    // Supprimer les accents et mettre en minuscule
    return type.normalize("NFD").replace(/[\u0300-\u036f]/g, "").toLowerCase();
}

function afficherGrille(grille) {
    const grilleContainer = document.getElementById("grille-container");
    grilleContainer.innerHTML = ""; // Effacer le contenu existant

    const table = document.createElement("table");
    grille.forEach((row, i) => {
        const tr = document.createElement("tr");
        row.forEach((cell, j) => {
            const td = document.createElement("td");

            // Ajouter l'image de la tuile
            const imgTuile = document.createElement("img");
            imgTuile.className = "tuile";
            imgTuile.src = `/JEE-4X-GAME/assets/images/tiles/${normalizeType(cell.type)}.png`;
            imgTuile.alt = cell.type;

            // Ajouter une classe supplémentaire si c'est une ville
            if (cell.type === "Ville") {
                imgTuile.classList.add("ville");
                if (cell.proprietaire) {
                    imgTuile.dataset.joueur = cell.proprietaire; // Ajouter un dataset pour le propriétaire
                }
                if (cell.hp) {
                    const cityLifeBar = document.createElement("div");
                    cityLifeBar.className = "city-life-bar";

                    const cityLifeBarFill = document.createElement("div");
                    cityLifeBarFill.className = "city-life-bar-fill";
                    cityLifeBarFill.style.width = `${(cell.hp / 15) * 100}%`; // Échelle de 0 à 15
                    cityLifeBar.appendChild(cityLifeBarFill);

                    // Ajouter la barre de vie en bas de la ville
                    td.appendChild(cityLifeBar);
                }
            }

            td.appendChild(imgTuile);

            // Ajouter un soldat s'il est présent
			if (cell.soldat) {
                const soldierWrapper = document.createElement("div");
                soldierWrapper.className = "soldier-wrapper"; // Conteneur pour le soldat et la barre de vie

                const imgSoldat = document.createElement("img");
                imgSoldat.className = "soldat";
                imgSoldat.src = "/JEE-4X-GAME/assets/images/soldats/soldat.png";
                imgSoldat.dataset.x = cell.soldat.x;
                imgSoldat.dataset.y = cell.soldat.y;
                imgSoldat.dataset.joueur = cell.soldat.joueur;
                imgSoldat.alt = "Soldat";

                // Ajouter la barre de vie
                const lifeBar = document.createElement("div");
                lifeBar.className = "life-bar";
                const lifeBarFill = document.createElement("div");
                lifeBarFill.className = "life-bar-fill";
                lifeBarFill.style.width = `${(cell.soldat.hp / 15) * 100}%`;
                lifeBar.appendChild(lifeBarFill);

                // Ajouter les éléments au conteneur
                soldierWrapper.appendChild(lifeBar);
                soldierWrapper.appendChild(imgSoldat);
                td.appendChild(soldierWrapper);

                // Ajouter le gestionnaire de clic pour le soldat
                imgSoldat.addEventListener("click", () => {
                    afficherActionsPourSoldat(cell.soldat.x, cell.soldat.y);
                });
            }

            tr.appendChild(td);
        });
        table.appendChild(tr);
    });

    grilleContainer.appendChild(table);
}




function mettreAJourLegende(joueurs) {
    const palette = ["#0074D9", "#FF4136", "#2ECC40", "#FFDC00"]; // Bleu, Rouge, Vert, Jaune
    const couleurs = {};

    // Assigner une couleur unique à chaque joueur
    joueurs.forEach((joueur, index) => {
        if (index < palette.length) {
            couleurs[joueur.nom] = palette[index];
        } else {
            console.warn(`Pas assez de couleurs pour le joueur : ${joueur.nom}`);
        }
    });

    const legende = document.getElementById("legende");
    legende.innerHTML = ""; // Effacer la légende existante

    joueurs.forEach(joueur => {
        const joueurElement = document.createElement("div");
        joueurElement.className = "legende-joueur";
        joueurElement.style.backgroundColor = couleurs[joueur.nom]; // Appliquer la couleur du joueur
        joueurElement.style.color = "white";
        joueurElement.style.padding = "5px 10px";
        joueurElement.style.margin = "5px auto";
        joueurElement.style.width = "150px";
        joueurElement.style.textAlign = "center";
        joueurElement.style.borderRadius = "5px";
        joueurElement.style.fontWeight = "bold";
        joueurElement.textContent = joueur.nom;
        legende.appendChild(joueurElement);
        
        // Mettre à jour les PP et le score pour le joueur connecté
        if (joueur.nom === joueurConnecte) {
            const ppElement = document.getElementById("points-production");
            const scoreElement = document.getElementById("score");
            ppElement.textContent = joueur.pp;
            scoreElement.textContent = joueur.score;
        }
    });
    
    

    // Retourner les couleurs pour réutilisation (ex. pour coloriser les soldats)
    return couleurs;
}


document.addEventListener("DOMContentLoaded", () => {
    chargerGrille();
});
