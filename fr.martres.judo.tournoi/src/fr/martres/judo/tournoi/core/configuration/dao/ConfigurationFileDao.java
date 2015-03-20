package fr.martres.judo.tournoi.core.configuration.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.PropertyResourceBundle;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;

import fr.martres.judo.tournoi.core.configuration.bo.Categorie;
import fr.martres.judo.tournoi.core.configuration.bo.CategorieHelper;
import fr.martres.judo.tournoi.core.configuration.bo.Club;
import fr.martres.judo.tournoi.core.configuration.bo.Competition;
import fr.martres.judo.tournoi.core.configuration.bo.Configuration;
import fr.martres.judo.tournoi.core.configuration.bo.Judoka;
import fr.martres.judo.tournoi.core.configuration.bo.Participation;
import fr.martres.judo.tournoi.core.configuration.bo.Poids;
import fr.martres.judo.tournoi.core.configuration.bo.PoidsHelper;
import fr.martres.judo.tournoi.core.configuration.bo.Poule;
import fr.martres.judo.tournoi.core.configuration.bo.RapportConfiguration;
import fr.martres.judo.tournoi.core.configuration.bo.Resultat;
import fr.martres.judo.tournoi.core.configuration.bo.SessionCombat;
import fr.martres.judo.tournoi.core.configuration.bo.SessionCombatHelper;
import fr.martres.judo.tournoi.core.configuration.bo.TypeCombat;
import fr.martres.judo.tournoi.core.exception.BoException;
import fr.martres.judo.tournoi.core.exception.DaoException;

public class ConfigurationFileDao implements IConfigurationDao {

	private static final int POS_START_SESSION = 7;
	private static final int POS_DATE = 2;
	private static final int POS_PRENOM = 1;
	private static final int POS_NOM = 0;
	private static final int POS_SEXE = 3;
	private static final int POS_CLUB = 4;
	private static final int POS_POIDS = 5;
	private static final int POS_CEINTURE = 6;
	// le nom du fichier de configuration de la competition
	private static final String CONFIGURATION_FILE = "configuration.properties";
	// le nomm du fichier qui contient les judokas
	private static final String JUDOKAS_FILE = "judokas.csv";
	// le nom du fichier qui contient la liste des categories
	private static final String CATEGORIES_FILE = "categories.csv";

	// properties key pour la ville
	private static final String PROP_VILLE = "ville";
	// properties key pour la nom
	private static final String PROP_NOM = "nom";
	// instance
	private static ConfigurationFileDao instance = null;
	// properties key pour la date
	private static final String PROP_DATE = "date";
	// properties key pour les types de combats
	private static final String PROP_TYPE_COMBAT = "type.combat.";
	// properties key pour les noms des types de combats
	private static final String PROP_TYPE_COMBAT_NOM = ".nom";
	// properties key pour les points des types de combats
	private static final String PROP_TYPE_COMBAT_POINT = ".classement.";
	// 
	private static final String PROP_TYPE_COMBAT_NB_JUDOKA = ".pour.";
	// properties key pour les grades
		private static final String PROP_GRADE= "grade.";
	// properties key pour les rapports de session
	private static final String PROP_RAPPORT_SESSION = "rapport.session";
	// properties key pour les rapports de club
	private static final String PROP_RAPPORT_CLUB = "rapport.club";
	// properties key pour les rapports de Competition
	private static final String PROP_RAPPORT_COMPETITION = "rapport.competition";

	// association des project et des compétitions //
	private Map<Competition, IProject> competitionProjectAssociation = new HashMap<Competition, IProject>();

	/**
	 * Constructeur par defaut.
	 * 
	 * @throws DaoException
	 *             si une erreur survient
	 */
	private ConfigurationFileDao() {
	}

	/**
	 * Retourne l'instance de la DAO.
	 * 
	 * @return
	 */
	public static ConfigurationFileDao getInstance() {
		if (instance == null) {
			instance = new ConfigurationFileDao();
		}
		return instance;
	}

	@Override
	public Configuration retreiveConfiguration() throws DaoException {
		// reset association projet / competition
		competitionProjectAssociation.clear();
		Configuration configuration = new Configuration();

		// On parcourt le contenu du workspace
		IWorkspaceRoot myWork = ResourcesPlugin.getWorkspace().getRoot();
		// Chaque projet correspond à une compétition
		IProject[] projects = myWork.getProjects();
		for (IProject project : projects) {
			if (project.isOpen()) {
				retrieveCompetition(configuration, project);
			}
		}

		return configuration;
	}

	@Override
	public void saveConfiguration(Configuration configuration)
			throws DaoException {

		// on parcours les compétitions de la configuration
		for (Competition competition : configuration.getCompetitions()) {
			saveCompetition(configuration, competition);
		}
	}

	@Override
	public void addSessionCombat(Configuration configuration,
			Competition competition, SessionCombat session) throws DaoException {
		// ajoute la session à la competition
		competition.addSessionCombat(session);
		// sauvegarde la configuration
		saveConfiguration(configuration);
	}

	@Override
	public void addJudokaOnPoule(Configuration configuration,
			Competition competition, Poule poule, Judoka judoka)
			throws DaoException {
		
		// on associe le judoka à cette poule : cela se fait par le resultat
		// de la participation
		Resultat resultat;
		Participation participation = configuration.getParticipation(competition, judoka);
			// s'il n'y a pas de participation : erreur....
			if (participation == null) {
				throw new DaoException(
						"Erreur le judoka n'a pas de participation sur cette competition.");
			}
			// on cree le resultat
			resultat = new Resultat();
			resultat.setPoule(poule);
			// on l'ajoute
			participation.addResultat(resultat);
		
		// on sauvegarde la modification
		saveConfiguration(configuration);
	}

	
	@Override
	public Poule createPouleOnSessionCombat(Configuration configuration,
			Competition competition, SessionCombat session, List<Judoka> judokas)
			throws DaoException {
		// creation de la poule
		Poule poule = new Poule();
		poule.setId(session.getMaxPoule() + 1);

		// on associe les judokas à cette poule : cela se fait par le resultat
		// de la participation
		// pour chaque judoka
		Participation participation;
		Resultat resultat;
		for (Judoka judoka : judokas) {
			participation = configuration.getParticipation(competition, judoka);
			// s'il n'y a pas de participation : erreur....
			if (participation == null) {
				throw new DaoException(
						"Erreur le judoka n'a pas de participation sur cette competition.");
			}
			// on cree le resultat
			resultat = new Resultat();
			resultat.setPoule(poule);
			// on l'ajoute
			participation.addResultat(resultat);
		}

		// on rajoute la poule à la session
		session.addPoule(poule);
		// on sauvegarde la modification
		saveConfiguration(configuration);
		return poule;
	}

	@Override
	public void deleteJudokaOnPoule(Configuration configuration,
			Competition competition, Poule poule,
			Judoka judoka) throws DaoException {

		// on supprime la reference de cette poule dans les participations du
		// judoka
		List<Resultat> toDelete = new ArrayList<Resultat>();
		Participation participation = configuration.getParticipation(competition, judoka);
		// s'il n'y a pas de participation : erreur....
		if (participation == null) {
			throw new DaoException(
					"Erreur le judoka n'a pas de participation sur cette competition.");
		}
		// on parcours les resultats
		toDelete.clear();
		for (Resultat resultat : participation.getResultats()) {
			if (poule.equals(resultat.getPoule())) {
				// suppression du resultat
				toDelete.add(resultat);

			}
		}
		participation.getResultats().removeAll(toDelete);

		// on sauvegarde la modification
		saveConfiguration(configuration);

	}

	@Override
	public void deletePouleOnSessionCombat(Configuration configuration,
			Competition competition, SessionCombat session, Poule poule)
			throws DaoException {
		// Déja on la supprime de la session
		session.getPoules().remove(poule);

		// on supprime la reference de cette poule dans les participations
		// on reccupère les judokas
		Categorie categorie = session.getCategorie();
		Poids poids = session.getPoids();
		List<Judoka> judokas = null;
		try {
			if (poids != null) {
				judokas = configuration
						.getJudoka(competition, categorie, poids);
			} else if (categorie != null) {
				judokas = configuration.getJudoka(competition, categorie);
			} else {
				judokas = configuration.getJudokas(competition);
			}
		} catch (BoException e) {
			throw new DaoException(
					"Erreur lors de la récupération des judokas", e);
		}

		Participation participation;
		List<Resultat> toDelete = new ArrayList<Resultat>();
		for (Judoka judoka : judokas) {
			participation = configuration.getParticipation(competition, judoka);
			// s'il n'y a pas de participation : erreur....
			if (participation == null) {
				throw new DaoException(
						"Erreur le judoka n'a pas de participation sur cette competition.");
			}
			// on parcours les resultats
			toDelete.clear();
			for (Resultat resultat : participation.getResultats()) {
				if (poule.equals(resultat.getPoule())) {
					// suppression du resultat
					toDelete.add(resultat);

				}
			}
			participation.getResultats().removeAll(toDelete);
		}

		// on sauvegarde la modification
		saveConfiguration(configuration);

	}

	@Override
	public void setPositionInPoule(Configuration configuration,
			Competition competition, SessionCombat Session, Judoka judoka,
			Poule poule, int position) throws DaoException {
		// on associe les judokas à cette poule : cela se fait par le resultat
		// de la participation

		Participation participation = configuration.getParticipation(
				competition, judoka);
		// s'il n'y a pas de participation : erreur....
		if (participation == null) {
			throw new DaoException(
					"Erreur le judoka n'a pas de participation sur cette competition.");
		}

		// on récupère un resultat
		for (Resultat resultat : participation.getResultats()) {
			if (poule.equals(resultat.getPoule())) {
				resultat.setPosition(position);
			}
		}
		// on sauvegarde la modification
		saveConfiguration(configuration);

	}

	/**
	 * Sauvegarde d'une competition dans les fichiers.
	 * 
	 * @param configuration
	 *            la configuration totale.
	 * @param competition
	 *            la competition a sauvegarder.
	 * @throws
	 */
	private void saveCompetition(Configuration configuration,
			Competition competition) throws DaoException {
		// on reccupère le projet associer
		IProject project = competitionProjectAssociation.get(competition);

		// TODO sauvegarde des informations générales : configuration
		// TODO sauvegarde des catégories

		// Sauvegarde des judokas
		IFile judokasIFile = project.getFile(JUDOKAS_FILE);
		File judokasFile = new File(judokasIFile.getLocationURI());
		// temp save file
		try {
			// vérifie l'existance du répertoire
			IFolder sauvegardeFolder = project.getFolder("sauvegardes");
			if ( ! sauvegardeFolder.exists() ){
				File saveFolder = new File(sauvegardeFolder.getLocationURI());
				saveFolder.mkdir();	
			}
			
			IFile judokasIFileSave = project.getFile("sauvegardes/"+JUDOKAS_FILE + ".sav"
					+ System.currentTimeMillis());
			File judokasFileSave = new File(judokasIFileSave.getLocationURI());
			judokasFileSave.createNewFile();
			DaoFileHelper.copy(judokasFile, judokasFileSave);
		} catch (IOException  e1) {
			throw new DaoException("Erreur lors de la sauvegarde d'un judoka.",
					e1);
		}

		CsvFile judokasCsvFile = new CsvFile(judokasFile);

		try {
			// on recupère les judokas par categories
			List<Categorie> categories = competition.getCategories();
			List<Judoka> judokas;
			List<String[]> lines = new ArrayList<String[]>();
			String[] line;
			Participation participation;
			for (Categorie categorie : categories) {
				judokas = configuration.getJudoka(competition, categorie);
				for (Judoka judoka : judokas) {
					participation = configuration.getParticipation(competition,
							judoka);
					line = judokaToLine(judoka, participation,
							competition.getSessionsCombats());
					lines.add(line);
				}
			}
			// on sauvegarde
			judokasCsvFile.saveData(lines);
		} catch (BoException e) {
			throw new DaoException("Erreur lors de la sauvegarde d'un judoka.",
					e);

		}

	}

	/**
	 * Retourne les information du judoka dans un tableau de chaine de caractère
	 * 
	 * @param judoka
	 * @param participation
	 * @param sessionCombats
	 * @return
	 */
	private String[] judokaToLine(Judoka judoka, Participation participation,
			List<SessionCombat> sessionCombats) {
		List<String> line = new ArrayList<String>();
		line.add(judoka.getNom());
		line.add(judoka.getPrenom());
		line.add(DaoFileHelper.convertDate(judoka.getDateNaissance()));
		if (judoka.isHomme()) {
			line.add("M");
		} else {
			line.add("F");
		}
		line.add(participation.getClub().getNom());
		line.add(Double.toString(participation.getPoids()));
		line.add(participation.getCeinture());
		// ajout des informations de sessions
		List<Resultat> resultats = participation.getResultats();
		boolean found = false;
		for (SessionCombat session : sessionCombats) {
			found = false;
			for (Resultat res : resultats) {
				if (session.getPoules().contains(res.getPoule())) {
					line.add(Integer.toString(session.getId()));
					line.add(session.getType().getNom());
					line.add(Integer.toString(res.getPoule().getId()));
					line.add(Integer.toString(res.getPosition()));
					found = true;
				}
			}
			if (!found) {
				line.add(null);
				line.add(null);
				line.add(null);
				line.add(null);
			}
		}
		return line.toArray(new String[line.size()]);

	}

	/**
	 * Retourn les properties .
	 * 
	 * @param path
	 *            le chemin des properties
	 * @return properties
	 * @throws IOException
	 */
	private PropertyResourceBundle getPropertiesBundle(String path)
			throws IOException {
		FileInputStream fis = new FileInputStream(path);
		try {
			return new PropertyResourceBundle(fis);
		} finally {
			fis.close();
		}
	}

	/**
	 * Retourne une competition.
	 * 
	 * @param configuration
	 *            la configuration a mettre à jour
	 * @param project
	 *            le projet qui contient la nouvelle competition
	 * @throws DaoException
	 *             si une erreur de lecture
	 */
	private void retrieveCompetition(Configuration configuration,
			IProject project) throws DaoException {
		try {
			Competition competition = new Competition();
			// on mémorise le project : c'est lié à l'implémentation sous forme
			// de fichier,
			// donc il ne faut pas le stocker dans les bo mais dans la dao
			competitionProjectAssociation.put(competition, project);

			// On recupère les informations générales
			IFile configurationFile = project.getFile(CONFIGURATION_FILE);
			PropertyResourceBundle bundle = getPropertiesBundle(configurationFile
					.getLocation().toPortableString());

			// Reccuperation des categories
			IFile categoriesIFile = project.getFile(CATEGORIES_FILE);
			File categoriesFile = new File(categoriesIFile.getLocationURI());
			CsvFile categoriesCsvFile = new CsvFile(categoriesFile);

			// Reccuperation des judokas
			IFile judokasIFile = project.getFile(JUDOKAS_FILE);
			File judokasFile = new File(judokasIFile.getLocationURI());
			CsvFile judokasCsvFile = new CsvFile(judokasFile);

			// retreive first properties part
			competition.setName(bundle.getString(PROP_NOM));
			competition.setVille(bundle.getString(PROP_VILLE));
			Date date = DaoFileHelper.convertDate(bundle.getString(PROP_DATE));
			competition.setDate(date);

			// recupérer les types de combats et leur points
			retrieveTypeCombat(competition, bundle);
			// recupèrer les grades
			retrieveGrades(competition,bundle);

			// recupération des categories de la compétition
			List<String[]> data = categoriesCsvFile.getData();
			Categorie categorie;
			for (String[] oneData : data) {
				categorie = retrieveCategorie(oneData);
				competition.addCategorie(categorie);
			}
			// on recupère les judokas
			data = judokasCsvFile.getData();
			for (String[] oneData : data) {
				retrieveJudoka(oneData, configuration, competition);
			}
			// on reccupere les templates
			RapportConfiguration rapport = competition
					.getRapportConfiguration();
			String path = bundle.getString(PROP_RAPPORT_SESSION);
			File file = new File(project.getFile(path).getLocationURI());
			rapport.addTemplates(RapportConfiguration.RAPPORT_SESSION, file);
			path = bundle.getString(PROP_RAPPORT_CLUB);
			file = new File(project.getFile(path).getLocationURI());
			rapport.addTemplates(RapportConfiguration.RAPPORT_CLUB, file);
			path = bundle.getString(PROP_RAPPORT_COMPETITION);
			file = new File(project.getFile(path).getLocationURI());
			rapport.addTemplates(RapportConfiguration.RAPPORT_COMPETITION, file);
			
			rapport.setOutputFolder(new File(file.getParentFile(),"rapports"));
			rapport.setProjet(project);
			
			// on rajoute la competition à la configuration
			configuration.addCompetition(competition);
		} catch (IOException e) {
			throw new DaoException(
					"Erreur pendant la lecture de fichiers de la competition.",
					e);
		}

	}

	private void retrieveGrades(Competition competition,
			PropertyResourceBundle bundle) {
		
		int indexGrade = 0;
		StringBuilder typeElement = new StringBuilder();
		typeElement.append(PROP_GRADE);
		typeElement.append(indexGrade);
		
		String grade;
		while (bundle.containsKey(typeElement.toString())) {
			// la clé actuelle existe on ajoute les points
			grade = bundle.getString(typeElement.toString());
			competition.addGrade(grade, indexGrade);
			// on passe au grade suivant
			indexGrade++;
			typeElement= new StringBuilder();
			typeElement.append(PROP_GRADE);
			typeElement.append(indexGrade);
		}
		
	}

	/**
	 * Retrieve typde de combat dans la configuration.
	 * 
	 * @param competition
	 *            a completer
	 * @param bundle
	 *            de la configuration
	 */
	private void retrieveTypeCombat(Competition competition,
			PropertyResourceBundle bundle) {
		// on recherche la présence des types dans la configuration
		int indexType = 1;
		StringBuilder typeRacine = new StringBuilder();
		typeRacine.append(PROP_TYPE_COMBAT);
		typeRacine.append(indexType);
		StringBuilder typeElement = new StringBuilder();
		typeElement.append(typeRacine.toString());
		typeElement.append(PROP_TYPE_COMBAT_NOM);

		TypeCombat type;
		int indexPoint = 0;
		int indexNbJudoka = 0;
		String point;
		StringBuilder typeClassementElement = new StringBuilder();
		while (bundle.containsKey(typeElement.toString())) {
			// la clé actuelle existe on crée le type de combat
			type = new TypeCombat();
			type.setNom(bundle.getString(typeElement.toString()));

			//pour chaque nombre de judoka dans le combat : on commenence par le defaut
			indexNbJudoka = 0;
			indexPoint = 0;
			typeElement = new StringBuilder();
			typeElement.append(typeRacine.toString());
			typeElement.append(PROP_TYPE_COMBAT_NB_JUDOKA);
			typeElement.append(indexNbJudoka);
			typeElement.append(PROP_TYPE_COMBAT_POINT);
			
			typeClassementElement= new StringBuilder();
			typeClassementElement.append(typeElement.toString());
			typeClassementElement.append(indexPoint);
			
			while (bundle.containsKey(typeClassementElement.toString())) {
			
				while (bundle.containsKey(typeClassementElement.toString())) {
					// la clé actuelle existe on ajoute les points
					point = bundle.getString(typeClassementElement.toString());
					type.addPointsPourClassement(indexNbJudoka,indexPoint,
							Integer.parseInt(point));
	
					// on passe au classement suivant
					indexPoint++;
					typeClassementElement= new StringBuilder();
					typeClassementElement.append(typeElement.toString());
					typeClassementElement.append(indexPoint);
				}
				
				// on augment le nombre de judoka, on reset les points
				indexPoint = 0;
				indexNbJudoka++;
				typeElement = new StringBuilder();
				typeElement.append(typeRacine.toString());
				typeElement.append(PROP_TYPE_COMBAT_NB_JUDOKA);
				typeElement.append(indexNbJudoka);
				typeElement.append(PROP_TYPE_COMBAT_POINT);
				
				typeClassementElement= new StringBuilder();
				typeClassementElement.append(typeElement.toString());
				typeClassementElement.append(indexPoint);
				
			}
			// on ajoute le type à la competition
			competition.addTypeCombat(type);

			// on passe au type suivant
			indexType++;
			typeRacine = new StringBuilder();
			typeRacine.append(PROP_TYPE_COMBAT);
			typeRacine.append(indexType);
			typeElement = new StringBuilder();
			typeElement.append(typeRacine.toString());
			typeElement.append(PROP_TYPE_COMBAT_NOM);

		}

	}

	/**
	 * Récupération d'un judoka.
	 * 
	 * @param oneData
	 *            une ligne d'un fichier
	 * @param configuration
	 *            la configuration a completer
	 * @param competition
	 *            la competition a completer
	 * @throws DaoException
	 *             si une erreur survient
	 */
	private void retrieveJudoka(String[] oneData, Configuration configuration,
			Competition competition) throws DaoException {

		// recuperation du judoka
		//gestion de seulement l'année
		String date = oneData[POS_DATE];
		if (date.length() == 4){
			date = "01/01/" + oneData[POS_DATE];
		}
		Date dateNaissance = DaoFileHelper.convertDate(date);
		// gestion du nom prénom dans le même champs
		String nom = oneData[POS_NOM];
		String prenom = oneData[POS_PRENOM];
		if ( prenom.length() == 0){
			int sep = nom.lastIndexOf(" ");
			if ( sep != -1 ){
			prenom = nom.substring(sep+1, nom.length());
			nom = nom.substring(0, sep);
			}
			else {
				System.out.println(nom);
			}
		}
		Judoka judoka = configuration.getJudoka(nom,
				prenom, dateNaissance,
				oneData[POS_SEXE].equals("M"));

		Club club = configuration.getClub(oneData[POS_CLUB]);

		Participation participation = new Participation();
		participation.setJudoka(judoka);
		participation.setClub(club);
		participation.setCompetition(competition);
		participation.setPoids(Double.parseDouble(oneData[POS_POIDS]));
		participation.setCeinture(oneData[POS_CEINTURE]);
		Categorie categorie = CategorieHelper.getCategorie(competition,
				dateNaissance, judoka.isHomme());
		participation.setCategorie(categorie);

		// détermine le poid associé à la participation
		boolean found = false;
		Iterator<Poids> iterator;
		Poids poids = null;
		iterator = categorie.getPoids().iterator();
		while (!found && iterator.hasNext()) {
			poids = iterator.next();
			// check le poids
			if (poids.getMinimum() < participation.getPoids()
					&& participation.getPoids() <= poids.getMaximum()) {
				found = true;
			}

		}

		if (!found) {
			throw new DaoException(
					"Erreur lors de la réccupération de la catégorie de poids");
		}
		// recupérer les sessions de combats
		retrieveSessionCombat(oneData, configuration, competition,
				participation, categorie, poids);

		configuration.addParticipation(participation);
	}

	/**
	 * Recupère les information de sessioin de combat
	 * 
	 * @param oneData
	 * @param configuration
	 * @param competition
	 * @param participation
	 * @param poids
	 * @throws DaoException
	 */
	private void retrieveSessionCombat(String[] oneData,
			Configuration configuration, Competition competition,
			Participation participation, Categorie categorie, Poids poids)
			throws DaoException {
		int size = oneData.length;
		int sessionIndex = POS_START_SESSION;
		// boucle sur toutes les sessions
		String nomSession;
		String typeCombatSession;
		String nomPoule;
		String position;
		SessionCombat sessionCombat;
		while (sessionIndex + 3 <= size) {

			nomSession = oneData[sessionIndex];
			typeCombatSession = oneData[sessionIndex + 1];
			nomPoule = oneData[sessionIndex + 2];
			position = oneData[sessionIndex + 3];
			// si la session n'est pas défini.
			if (!nomSession.isEmpty()) {
				try {
					sessionCombat = SessionCombatHelper.getSessionCombat(
							competition, nomSession, typeCombatSession,
							categorie, poids);

					// on recuppère ou on cré la poule
					int idPoule = Integer.parseInt(nomPoule);
					Poule poule = sessionCombat.getPoule(idPoule);
					if (poule == null) {
						poule = new Poule();
						poule.setId(idPoule);
						sessionCombat.addPoule(poule);
					}
					// on y associe le résultat
					Resultat resultat = new Resultat();
					resultat.setPoule(poule);
					resultat.setPosition(Integer.parseInt(position));

					participation.addResultat(resultat);

				} catch (BoException e) {
					throw new DaoException(
							"Erreur pendant la construction d'une Session de Combat.",
							e);
				}
			}
			// on passe au suivant
			sessionIndex += 4;
		}
	}

	private Categorie retrieveCategorie(String[] oneData) {
		Categorie result = new Categorie();
		result.setNom(oneData[0]);
		result.setHomme(oneData[1].equals("M"));
		result.setAnneeMinimum(Integer.parseInt(oneData[2]));
		result.setAnneeMaximum(Integer.parseInt(oneData[3]));

		String[] poidsRepresentation;
		Poids poids;
		for (int indice = 4; indice < oneData.length; indice++) {
			poidsRepresentation = oneData[indice].split("-");

			poids = new Poids();
			if (oneData[indice].startsWith("-")) {
				poids.setMinimum(PoidsHelper.MINIMUM);
				poids.setMaximum(Integer.parseInt(poidsRepresentation[1]));
			} else if (oneData[indice].endsWith("-")) {
				poids.setMaximum(PoidsHelper.MAXIMUM);
				poids.setMinimum(Integer.parseInt(poidsRepresentation[0]));
			} else {
				poids.setMinimum(Integer.parseInt(poidsRepresentation[0]));
				poids.setMaximum(Integer.parseInt(poidsRepresentation[1]));
			}
			result.addPoids(poids);
		}

		return result;
	}

	@Override
	public Participation addJudokaOnCompetition(Configuration configuration,
			Competition competition, String nom, String prenom, Date dateDeNaissance,
			boolean isHomme, String ceinture, Club club, double poids) throws DaoException {
		// recuperation du judoka
				Judoka judoka = configuration.getJudoka(nom,
						prenom, dateDeNaissance,
						isHomme);
				// on vérifie s'il n'y a pas déjà une participation définie pour le judoka
				if ( configuration.getParticipation(competition, judoka) != null){
					throw new DaoException("Le judoka existe déjà pour la competition");
				}

				//Club club = configuration.getClub(oneData[POS_CLUB]);

				Participation participation = new Participation();
				participation.setJudoka(judoka);
				participation.setClub(club);
				participation.setCompetition(competition);
				participation.setPoids(poids);
				participation.setCeinture(ceinture);
				Categorie categorie = CategorieHelper.getCategorie(competition,
						dateDeNaissance, isHomme);
				participation.setCategorie(categorie);

				
				configuration.addParticipation(participation);

				//on sauvegarde la configuration
				saveConfiguration(configuration);
				return participation;		
	}

	@Override
	public Participation addJudokaOnCompetition(Configuration configuration,
			Competition competition, String nom, String prenom,
			Date dateDeNaissance, boolean isHomme, String ceinture,
			String clubName, double poids) throws DaoException {
		Club club = configuration.getClub(clubName);

		return addJudokaOnCompetition(configuration, competition, nom, prenom, dateDeNaissance, isHomme, ceinture, club, poids);
	}

}
