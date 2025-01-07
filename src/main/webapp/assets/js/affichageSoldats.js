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
    
    const actionsContainer = document.getElementById("actions-container");

    // Appliquer les couleurs et gérer les clics
    document.querySelectorAll(".soldat").forEach(soldat => {
        const joueur = soldat.dataset.joueur.trim();
        if (joueur === joueurConnecte) {
            // Ajouter un gestionnaire de clic pour les soldats appartenant au joueur connecté
            soldat.addEventListener("click", () => {
                const x = soldat.dataset.x;
                const y = soldat.dataset.y;
                afficherActionsPourSoldat(x, y);
            });
        }
    });

    // Fonction pour récupérer et afficher les actions possibles
    function afficherActionsPourSoldat(x, y) {
	    fetch(`/JEE-4X-GAME/api/actionPossible?x=${x}&y=${y}`)
	        .then(response => {
	            if (!response.ok) {
	                return response.json().then(error => {
	                    throw new Error(error.error || `Erreur API: ${response.status}`);
	                });
	            }
	            return response.json();
	        })
	        .then(actions => {
	            // Mettre à jour le contenu du panneau d'actions
	            actionsContainer.innerHTML = ""; // Vider le contenu actuel
	            Object.entries(actions).forEach(([action, disponible]) => {
	                if (disponible) {
	                    const bouton = document.createElement("button");
	                    bouton.textContent = action;
	                    bouton.addEventListener("click", () => effectuerAction(x, y, action));
	                    actionsContainer.appendChild(bouton);
	                }
	            });
	        })
	        .catch(error => {
	            console.error("Erreur lors de la récupération des actions possibles :", error.message);
	            actionsContainer.innerHTML = `<p>${error.message}</p>`;
	        });
	}

    // Fonction pour effectuer une action (place un appel à l'API `action` ici)
    function effectuerAction(x, y, action) {
        console.log(`Effectuer l'action '${action}' sur (${x}, ${y})`);
        // Implémentez l'API d'exécution des actions ici
    }
    
});
