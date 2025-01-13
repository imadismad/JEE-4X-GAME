package model.storage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import model.storage.exception.StockageAccesException;
import model.storage.exception.StockageStructureException;
import model.storage.exception.StockageValeurException;
import model.storage.structure.StructureInterface;
import model.storage.structure.TableEnum;
import model.storage.structure.UtilisateurStructure;

/**
 * Cette classe ne peut pas être instancié, passer par StockageInterface.getService() pour récupèrer l'instance de StockageInterface à utiliser.
 * Elle définit comment le stockage des données sous forme CSV peut être utilisé.
 * @author Théo KALIFA
 *
 */
public class CSVStockage implements StockageInterface {
	
	private static CSVStockage instance;
	
	private static final String CHEMIN_STOCKAGE = "stockage";
	private static final File FICHIER_UTILISATEUR = new File(CHEMIN_STOCKAGE, "utilisateur.csv");
	private static final File FICHIER_SCORES = new File(CHEMIN_STOCKAGE, "scores.csv");
	
	private static String SEPARATEUR = ";";
	
	private Map<TableEnum, StructureInterface[]> entetes;
	
	/**
	 * Constructeur unique de la classe.
	 * Elle initialise les différents fichiers csv nécessaires pour pouvoir utiliser le stockage.
	 * @throws IOException 
	 * @throws StockageStructureException 
	 */
	CSVStockage() throws IOException, StockageStructureException {
		this.entetes = new HashMap<>();
		
		final File dossierStockage = new File(CHEMIN_STOCKAGE);
		if (!dossierStockage.exists())
			dossierStockage.mkdirs();
		System.out.println("[CSVStockage] Storing CSV in: " + FICHIER_UTILISATEUR.getAbsolutePath());
		initCSV(FICHIER_UTILISATEUR, TableEnum.UTILISATEUR);
		initCSV(FICHIER_SCORES, TableEnum.SCORES);
	}
	
	@Override
	public boolean estPresent(TableEnum table, StructureInterface champ, Object valeur) throws StockageAccesException, StockageValeurException {
		LineNumberReader reader = getReader(table);
		
		Map<StructureInterface, Object> donnee;
		while ((donnee = this.parseLigneSuivante(table, reader)) != null) {
			if (donnee.get(champ).equals(valeur))
				return true;
		}
		
		return false;
	}
	
	@Override
	public void ajouterValeur(TableEnum table, Map<StructureInterface, Object> valeurs) throws StockageAccesException, IllegalArgumentException {
		for (StructureInterface champ : table.getChamps()) {
			if (!valeurs.containsKey(champ))
				throw new IllegalArgumentException("Le champ " + champ.toString() + " n'existe pas parmis les valeurs données");
			
			if (!champ.getTypeClasse().equals(valeurs.get(champ).getClass()))
				throw new IllegalArgumentException("Le champ " +
						champ.toString() +
						" n'a pas le type attendu (" +
						champ.getTypeClasse() +
						" était attendu mais " +
						valeurs.get(champ).getClass() +
						" a été passé)"
					);
		}
		
		// On est sur qu'il ne manque aucun champs dans notre map de valeur, maintenant on vérifie s'il n'y en a pas en trop (champ d'autre table).
		if (valeurs.size() > table.getChamps().length)
			throw new IllegalArgumentException("Certains champs passé en paramètre ne sont pas présent dans la table");
		
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(getCSVFile(table), true));
			String ligne = String.join(SEPARATEUR, trieDonnee(valeurs, getEntete(table))) + "\n";
			
			writer.write(ligne);
		} catch (IOException e) {
			throw new StockageAccesException("Une erreur est survenu lors de l'accès au CSV", e);
		} finally {
				try {
					if (writer != null)
						writer.close();
				} catch (IOException e) {
					throw new StockageAccesException("Impossible de fermer le fichier CSV", e);
				}
			
		}
		
	}
	
	@Override
	public List<Map<StructureInterface, Object>> getToutesEntrees(TableEnum table, Map<StructureInterface, Object> valeurs) throws StockageValeurException, StockageAccesException {
		LineNumberReader reader = getReader(table);
		
		List<Map<StructureInterface, Object>> donnees = new ArrayList<>();
		Map<StructureInterface, Object> donnee;
		while ((donnee = this.parseLigneSuivante(table, reader)) != null) {
			boolean estOk = true;
			Iterator<Entry<StructureInterface, Object>> iterateur = valeurs.entrySet().iterator();
			while (estOk && iterateur.hasNext()) {
				Entry<StructureInterface, Object> condition = iterateur.next();
				
				estOk &= condition.getValue().equals(donnee.get(condition.getKey()));
			}
			
			if (estOk)
				donnees.add(donnee);
				
		}
		
		
		return donnees;
	}
	
	@Override
	public Map<StructureInterface, Object> getPremiereEntrees(TableEnum table, Map<StructureInterface, Object> valeurs) throws StockageAccesException, StockageValeurException, IllegalArgumentException {
		LineNumberReader reader = getReader(table);
		
		Map<StructureInterface, Object> donnee;
		while ((donnee = this.parseLigneSuivante(table, reader)) != null) {
			boolean estOk = true;
			Iterator<Entry<StructureInterface, Object>> iterateur = valeurs.entrySet().iterator();
			while (estOk && iterateur.hasNext()) {
				Entry<StructureInterface, Object> condition = iterateur.next();
				
				estOk &= condition.getValue().equals(donnee.get(condition.getKey()));
			}
			
			if (estOk)
				return donnee;
				
		}

		return null;
	}
	
	/**
	 * Lit la prochaine ligne du fichier CSV et la parse, voir la méthode {@link CSVStockage#parse(TableEnum, String)}</br>
	 * Renvoie null si jamais la fin du fichier a été atteint.
	 * @param table La table d'origine du reader
	 * @param reader Le reader à utiliser pour lire la prochiane ligne
	 * @return Une map contenant pour clef le nom des champs de la table et pour valeur la valeur convertie dans sont type théorique, ou null si la fin du fichier a été atteint
	 * @throws StockageValeurException Levé si une des valeurs attendu n'est pas conforme
	 * @throws StockageAccesException Levé si le fichier CSV ne peut pas être lu
	 */
	private Map<StructureInterface, Object> parseLigneSuivante(TableEnum table, LineNumberReader reader) 
	        throws StockageValeurException, StockageAccesException {
	    try {
	        String ligne;
	        while ((ligne = reader.readLine()) != null) {
	            // Si c'est la première ligne, on la saute
	            if (reader.getLineNumber() == 1) {
	                continue;
	            }
	            
	            if (!ligne.isEmpty()) {
	                return parse(table, ligne);
	            }
	        }
	        return null; // Fin du fichier
	    } catch (IOException e) {
	        throw new StockageAccesException("Impossible de lire le fichier CSV", e);
	    }
	}
	
	/**
	 * Parse une ligne d'un fichier CSV sous forme d'une Map.
	 * @param table La table d'origine de la ligne
	 * @param ligne La ligne qui vient de la table
	 * @return Une map contenant pour clef le nom des champs de la table et pour valeur la valeur convertie dans sont type théorique
	 * @throws StockageValeurException Levé si une des valeurs attendu n'est pas conforme
	 */
	private Map<StructureInterface, Object> parse(TableEnum table, String ligne) throws StockageValeurException {
	    Map<StructureInterface, Object> donnee = new HashMap<>();

	    String[] ligneSplit = ligne.split(SEPARATEUR);
	    StructureInterface[] entete = this.getEntete(table);
	    
	    if (ligneSplit.length != entete.length)
	        throw new StockageValeurException("La ligne n'a pas le bon nombre de valeur"); 
	    
	    for (int i = 0; i < entete.length; i++) {        
	        Class<?> typeClasse = entete[i].getTypeClasse();
	        Object valeur;
	        if (typeClasse.equals(String.class)) {
	            valeur = ligneSplit[i];
	        
	        } else if (typeClasse.equals(Integer.class)) {
	            try {
	                valeur = Integer.parseInt(ligneSplit[i]);
	            } catch (NumberFormatException e) {
	                throw new StockageValeurException("La valeur n'est pas de type Integer (" + ligneSplit[i] + ")", e);
	            }
	        } else if (typeClasse.equals(Double.class)) {
	            try {
	                valeur = Double.parseDouble(ligneSplit[i]);
	            } catch (NumberFormatException e) {
	                throw new StockageValeurException("La valeur n'est pas de type Double (" + ligneSplit[i] + ")", e);
	            }
	        } else if (typeClasse.equals(Boolean.class)) {
	            valeur = Boolean.parseBoolean(ligneSplit[i]);
	        } else {
	            throw new IllegalArgumentException("Le type " + typeClasse + " n'est pas supporté");
	        }
	        
	        donnee.put(entete[i], valeur);
	    }

	    return donnee;
	}
	
	/**
	 * Créer un lecteur du fichier CSV correspondant à table. Le lecteur permet une lecteur ligne par ligne
	 * @param table La table à lire
	 * @return un lecteur du fichier CSV associé à la table
	 * @throws StockageAccesException Si jamais il était impossible d'accéder au fichier CSV
	 */
	private LineNumberReader getReader(TableEnum table) throws StockageAccesException {
		try {
			return new LineNumberReader(new FileReader(getCSVFile(table)));
		} catch (FileNotFoundException e) {
			System.err.println("Attention, fichier CSV non trouvé, création d'un nouveau");
			try {
				initCSV(getCSVFile(table), table);
				return new LineNumberReader(new FileReader(getCSVFile(table)));
			} catch (StockageStructureException | IOException e2) {
				throw new StockageAccesException("Impossible d'accéder au fichier CSV", e2);
			}
		}
	}
	 
	
	/**
	 * Renvoie le fichier CSV associé a la table donné 
	 * @param table La table dont on cherche le CSV
	 * @return l'objet File du fichier CSV associé à la table
	 */
	private File getCSVFile(TableEnum table) {
		return switch (table) {
			case UTILISATEUR -> FICHIER_UTILISATEUR;
			case SCORES -> FICHIER_SCORES;
		};
	}
	
	/**
	 * Trie les données passé en paramètre dans l'odre croissant de nom. Ça permet d'ajoir une consistance lors de l'écriture des données
	 * @param valeur Les valeurs à trier
	 * @return les chaînes de caractères des donnnées triées
	 */
	private String[] trieDonnee(Map<StructureInterface, Object> valeurs, StructureInterface[] entete) {
		String[] str = new String[entete.length];
		for (int i = 0; i < entete.length; i++) {
			str[i] = valeurs.get(entete[i]).toString();
		}
		
		return str;
	}
	
	/**
	 * Permet de récupérer l'entête d'une table pour ordonner les valeurs
	 * @param table La table logique associé à la table physique
	 * @param reader Le lecteur du fichier CSV
	 * @return L'entête de la table ordonnée
	 * @throws StockageAccesException
	 */
	private StructureInterface[] getEntete(TableEnum table) {
		return this.entetes.get(table);
	}
	
	/**
	 * Initialise un fichier CSV.
	 * Si le fichier CSV n'existe pas, il est créé avec pour en-tête les champs de table.
	 * Si le fichier CSV existe déjà, l'en-tête est vérifié. 
	 * @param csv Le fichier CSV à initialiser
	 * @param table Les champs de à mettre dans le fichier CSV
	 * @throws IOException Une erreur d'écriture ou de lecture
	 * @throws StockageStructureException L'entête n'est pas valide
	 */
	private void initCSV(File csv, TableEnum table) throws IOException, StockageStructureException{
		// Récupération des champs de la table
		StructureInterface[] entete = table.getChamps();
		String[] nomChamp = Arrays.stream(entete).map((valeur) -> valeur.getValeur()).toArray(String[]::new);
		
		if (!csv.exists()) {
			System.out.println("Création du fichier CSV : " + csv.getAbsolutePath());
			
			// Initialisaiton du CSV
			BufferedWriter writer = null;
			try {
				writer = new BufferedWriter(new FileWriter(csv));
				writer.write(String.join(SEPARATEUR, nomChamp) + "\n");
				this.entetes.put(table, entete);

			} catch (IOException e) {
				System.err.println("Impossible de créer le fichier CSV " + csv.getAbsolutePath());
				throw e;

			} finally {
					try {
						if (writer != null)
							writer.close();
					} catch (IOException e) {
						System.err.println("Impossible de créer le fichier CSV " + csv.getAbsolutePath());
						throw e;
					}
			}
			
			
		} else {
			// Vérification de la première ligne du CSV
			LineNumberReader reader = null;
			try {
				reader = new LineNumberReader(new FileReader(csv));
				String[] enteteStr = reader.readLine().split(SEPARATEUR);
				
				// Vérifie l'ordre des champs, si c'est le même ordre c'est ok
				if (!Arrays.equals(enteteStr, nomChamp))
					throw new StockageStructureException("L'entête du fichier CSV est incorrect (" + csv.getAbsolutePath() + ")");
				
				this.entetes.put(table, entete);				
			} catch (IOException e) {
				System.err.println("Impossible de lire le fichier CSV " + csv.getAbsolutePath());
				throw e;
			} finally {
					try {
						if (reader != null)
							reader.close();
					} catch (IOException e) {
						System.err.println("Impossible de lire le fichier CSV " + csv.getAbsolutePath());
						throw e;
					}
			}
		}
	}

	/**
	 * Renvoie l'instance de CSVStockage à utiliser
	 * @return l'instance de CSVStockage à utiliser
	 * @throws StockageStructureException Si un problème dans la structure du CSV survient
	 * @throws StockageAccesException Si un problème d'accès au CSV survient
	 */
	static StockageInterface getInstance() throws StockageStructureException, StockageAccesException {
		try {
			if (instance == null)
				instance = new CSVStockage();
			return instance;
		} catch (IOException e) {
			throw new StockageAccesException("Erreur lors de l'accès au fichier CSV", e);
		}
		
	}

	
	public static void main(String[] args) throws IOException, StockageStructureException, StockageAccesException, StockageValeurException {
		StockageInterface stockage = new CSVStockage();
				
		HashMap<StructureInterface, Object> map = new HashMap<>();
		map.put(UtilisateurStructure.NOM_UTILISATEUR, "BG_DU_95");
		map.put(UtilisateurStructure.MOT_DE_PASSE, "MotDePasseCompliqué");
		stockage.ajouterValeur(TableEnum.UTILISATEUR, map);
		
		map.put(UtilisateurStructure.NOM_UTILISATEUR, "BG_DU_75");
		stockage.ajouterValeur(TableEnum.UTILISATEUR, map);

		map.put(UtilisateurStructure.NOM_UTILISATEUR, "Imad is mad");
		stockage.ajouterValeur(TableEnum.UTILISATEUR, map);

		System.out.println(stockage.estPresent(TableEnum.UTILISATEUR, UtilisateurStructure.NOM_UTILISATEUR, "Imad is mad"));

		
		
		System.out.println("Fin");
	}
	
}