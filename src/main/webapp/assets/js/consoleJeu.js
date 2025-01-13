const cheminJs = document.currentScript.src;
const cheminPage = window.location.href;

let i = window.location.host.length;
while (cheminJs[i] === cheminPage[i] && i < cheminJs.length && i < cheminPage.length) i++;

const cheminWebSocket = `${cheminPage.substring(0, i)}console`;
console.log(cheminWebSocket);
const webSocket = new WebSocket(cheminWebSocket);

const consoleListe = document.getElementById("console");
const emplacementTour = document.getElementById("emplacement-joueur");
const pseudo = emplacementTour.dataset.nomJoueur;

webSocket.onmessage = (event) => {
	/**
	 * @type {{
	 * 	type: "JEUX"|"CHAT"|"FIN_PARTIE";
	 * 	message : string
	 *  rechargerGrille: boolean
	 *  tourDe: string | false
	 * }}
	 */
	const data = JSON.parse(event.data);
	
	const li = document.createElement("li");
	li.innerText = data.message;
	
	if (data.type == "FIN_PARTIE") {
		// Fin de la partie, on affiche l'écran de fin
		afficherEcranDeFin(data);
		return;
	}
	
	if (data.type === "JEUX") {
		li.classList.add("jeux");
	}
	
	consoleListe.append(li);
	
	// Si le serveur informe qu'il faut recharger la grille, alors on la recharge
	if (data.rechargerGrille === true)
		chargerGrille();
	
	if (data.tourDe == false) {
		// C'est notre tour
		emplacementTour.innerHTML = pseudo;
		peutFaireAction(true);
	} else {
		// C'est au tour de quelqu'un d'autre
		emplacementTour.innerHTML = data.tourDe;
		peutFaireAction(false);
	}
}

/**
 * Fonction qui permet d'envoyer un message à tous les utilisateurs
 */
function envoyerMessage() {
	/**
	 * Chaine de caratère représentant ce qu'a tapé l'utilisateur
	 * Il faut l'envoyer tel quel au serveur, il s'occupera de la renvoyer à tous les utilisateurs (nous compris)
	 * @type string
	 */
	msg = ""; // Méthode de récupération du message à implémenter
	webSocket.send(msg);
}
	