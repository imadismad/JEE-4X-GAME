let actionOk = false;
function peutFaireAction(action) {
	actionOk = action
}

function afficherActionsPourSoldat(x, y) {
	if (actionOk !== true)
		return;
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

function afficherActionsPourTuile(x, y) {
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

            let actionsDisponibles = false;

            // Ajouter des boutons pour chaque action disponible
            Object.entries(actions).forEach(([action, disponible]) => {
                if (disponible) {
                    actionsDisponibles = true;
                    const bouton = document.createElement("button");
                    bouton.textContent = action;

                    // Gestion des clics pour l'action
                    bouton.addEventListener("click", () => {
                        if (action === "recruter") {
                            effectuerActionVille(x, y, "CREER_SOLDAT");
                        } else {
                            effectuerAction(x, y, action);
                        }
                    });

                    actionsContainer.appendChild(bouton);
                }
            });

            if (!actionsDisponibles) {
                actionsContainer.innerHTML = "<p>Aucune action disponible.</p>";
            }
        })
        .catch(error => {
            console.error("Erreur lors de la récupération des actions possibles :", error);
        });
}

function effectuerAction(x, y, action, direction = null) {
    if (actionOk !== true) return; // Vérification de l'état de l'action
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
            if (data.gagnant) {
                // La partie est terminée, afficher l'écran de fin
                afficherEcranDeFin(data);
            } else if (data.status === "success") {
                // L'action s'est déroulée avec succès, recharger la grille
                chargerGrille();

                // Vider le volet des actions
                const actionsContainer = document.getElementById("actions-container");
                if (actionsContainer) {
                    actionsContainer.innerHTML = ""; // Vider le panneau
                }
            } else {
                console.warn("Détails de la réponse :", data);
            }
        })
        .catch(error => {
            console.error(`Erreur lors de l'exécution de l'action : ${error.message}`);
        });
}

function effectuerActionVille(x, y, action) {
	if (actionOk !== true) return; // Vérification de l'état de l'action
    const params = new URLSearchParams({ x, y, action });

    fetch(`/JEE-4X-GAME/api/action?${params.toString()}`)
        .then(response => {
            if (!response.ok) {
                throw new Error(`Erreur API: ${response.status}`);
            }
            return response.json();
        })
        .then(data => {
            if (data.status === "success") {
                chargerGrille(); // Recharger la grille pour afficher les modifications
                const actionsContainer = document.getElementById("actions-container");
                if (actionsContainer) {
                    actionsContainer.innerHTML = ""; // Vider le panneau d'actions
                }
            } else {
                console.warn("Action pour la ville échouée :", data);
            }
        })
        .catch(error => {
            console.error(`Erreur lors de l'exécution de l'action pour la ville : ${error.message}`);
        });
}

/**
 * Affiche l'écran de fin du jeu.
 * @param {Object} data - Données contenant le gagnant et les scores.
 */
function afficherEcranDeFin(data) {
    const { gagnant, scores } = data;

    // Créez un conteneur pour l'écran de fin
    const finContainer = document.createElement("div");
    finContainer.id = "fin-container";
    finContainer.style.position = "fixed";
    finContainer.style.top = "0";
    finContainer.style.left = "0";
    finContainer.style.width = "100%";
    finContainer.style.height = "100%";
    finContainer.style.backgroundColor = "rgba(0, 0, 0, 0.8)";
    finContainer.style.color = "white";
    finContainer.style.display = "flex";
    finContainer.style.flexDirection = "column";
    finContainer.style.justifyContent = "center";
    finContainer.style.alignItems = "center";
    finContainer.style.zIndex = "1000";

    // Titre de l'écran de fin
    const title = document.createElement("h1");
    title.textContent = `Le gagnant est : ${gagnant}`;
    title.style.marginBottom = "20px";
    finContainer.appendChild(title);

    // Tableau des scores
    const scoreTable = document.createElement("table");
    scoreTable.style.border = "1px solid white";
    scoreTable.style.borderCollapse = "collapse";
    scoreTable.style.margin = "20px";
    scoreTable.style.width = "50%";

    // En-têtes
    const headerRow = document.createElement("tr");
    ["Joueur", "Score", "Points de Production"].forEach(headerText => {
        const th = document.createElement("th");
        th.textContent = headerText;
        th.style.border = "1px solid white";
        th.style.padding = "10px";
        scoreTable.appendChild(th);
    });
    scoreTable.appendChild(headerRow);

    // Ajout des scores
    scores.forEach(joueur => {
        const row = document.createElement("tr");
        ["nom", "score", "pp"].forEach(key => {
            const td = document.createElement("td");
            td.textContent = joueur[key];
            td.style.border = "1px solid white";
            td.style.padding = "10px";
            row.appendChild(td);
        });
        scoreTable.appendChild(row);
    });

    finContainer.appendChild(scoreTable);

    // Bouton pour quitter ou rejouer
    const replayButton = document.createElement("button");
    replayButton.textContent = "Rejouer";
    replayButton.style.padding = "10px 20px";
    replayButton.style.marginTop = "20px";
    replayButton.style.backgroundColor = "#4CAF50";
    replayButton.style.color = "white";
    replayButton.style.border = "none";
    replayButton.style.borderRadius = "5px";
    replayButton.style.cursor = "pointer";

    // Redirection vers la page de saisie de code pour rejouer
    replayButton.addEventListener("click", () => {
        window.location.href = "/JEE-4X-GAME/lobby/enterCode"; // Redirection vers l'URL spécifiée
    });

    finContainer.appendChild(replayButton);

    // Ajouter l'écran de fin au DOM
    document.body.appendChild(finContainer);
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
            soldat.style.border = `3px solid ${couleurs[joueur]}`; // Ajout du contour coloré
        } else {
            console.warn(`Aucune couleur définie pour le joueur : ${joueur}`);
            soldat.style.filter = "none";
            soldat.style.border = "3px solid transparent"; // Réinitialise le contour si non défini
        }
    });
}

function appliquerColorisationVilles(couleurs) {
    document.querySelectorAll(".ville").forEach(ville => {
        const joueur = ville.dataset.joueur ? ville.dataset.joueur.trim() : null;
        if (joueur && joueur in couleurs) {
            ville.style.borderColor = couleurs[joueur]; // Appliquer la couleur du propriétaire
            ville.style.boxShadow = `0 0 10px ${couleurs[joueur]}`; // Ombre colorée pour visibilité
        } else {
            console.warn(`Aucune couleur définie pour le joueur : ${joueur}`);
            ville.style.borderColor = "gray"; // Couleur par défaut
        }
    });
}



document.addEventListener("DOMContentLoaded", () => {
    // Charger la grille et écouter les événements pour la colorisation
    chargerGrille();

    document.addEventListener("grilleChargee", (event) => {
        const { couleurs } = event.detail;
        appliquerColorisationSoldats(couleurs);
        appliquerColorisationVilles(couleurs);
    });
});