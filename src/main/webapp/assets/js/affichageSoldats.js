function afficherActionsPourSoldat(x, y) {
    fetch(`/JEE-4X-GAME/api/actionPossible?x=${x}&y=${y}`)
        .then(response => {
            if (!response.ok) {
                throw new Error(`Erreur API: ${response.status}`);
            }
            return response.json();
        })
        .then(actions => {
            const actionsContainer = document.getElementById("actions-container");
            actionsContainer.innerHTML = ""; // Vider le panneau d'actions

            // Ajouter des boutons pour chaque action disponible
            Object.entries(actions).forEach(([action, disponible]) => {
                if (disponible) {
                    const bouton = document.createElement("button");
                    bouton.textContent = action;

                    // Gestion des clics pour l'action
                    bouton.addEventListener("click", () => {
                        if (["haut", "bas", "gauche", "droite"].includes(action)) {
                            // Action de déplacement
                            effectuerAction(x, y, "deplacer", action);
                        } else {
                            // Autre action
                            effectuerAction(x, y, action);
                        }
                    });

                    actionsContainer.appendChild(bouton);
                }
            });
        })
        .catch(error => {
            console.error("Erreur lors de la récupération des actions possibles :", error);
        });
}

function effectuerAction(x, y, action, direction = null) {
    const params = new URLSearchParams({ x, y, action });
    if (direction) {
        params.append("direction", direction); // Ajout de la direction pour les déplacements
    }

    fetch(`/JEE-4X-GAME/api/action?${params.toString()}`)
        .then(response => {
            if (!response.ok) {
                throw new Error(`Erreur API: ${response.status}`);
            }
            // Tenter de parser le JSON, sinon considérer une réponse vide comme un succès
            return response.text().then(text => {
                try {
                    return text ? JSON.parse(text) : { status: "success" };
                } catch (e) {
                    console.warn("Réponse non JSON reçue :", text);
                    return { status: "success" }; // Retourner un succès par défaut
                }
            });
        })
        .then(data => {
            if (data.status === "success") {
                console.log(`Action '${action}' effectuée avec succès.`);
                chargerGrille(); // Recharge toute la grille
            } else {
                console.warn("Détails de la réponse :", data);
            }
        })
        .catch(error => {
            console.error(`Erreur lors de l'exécution de l'action : ${error.message}`);
        });
}


function appliquerColorisationSoldats(couleurs) {
    if (!couleurs || typeof couleurs !== "object") {
        console.error("Couleurs non valides ou non définies :", couleurs);
        return;
    }

    const soldats = document.querySelectorAll(".soldat");
    if (!soldats) {
        console.warn("Aucun soldat trouvé sur la grille.");
        return;
    }

    soldats.forEach(soldat => {
        const joueur = soldat.dataset.joueur ? soldat.dataset.joueur.trim() : null;
        if (joueur && joueur in couleurs) {
            soldat.style.filter = `drop-shadow(0 0 5px ${couleurs[joueur]})`;
        } else {
            console.warn(`Aucune couleur définie pour le joueur : ${joueur}`);
            // Optionnel : Réinitialiser le style si aucune couleur n'est trouvée
            soldat.style.filter = "none";
        }
    });
}


document.addEventListener("DOMContentLoaded", () => {
    // Charger la grille et écouter les événements pour la colorisation
    chargerGrille();

    document.addEventListener("grilleChargee", (event) => {
        const { couleurs } = event.detail;
        appliquerColorisationSoldats(couleurs);
    });
});