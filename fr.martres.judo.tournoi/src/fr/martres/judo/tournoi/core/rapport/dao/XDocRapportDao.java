package fr.martres.judo.tournoi.core.rapport.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;

import fr.martres.judo.tournoi.core.configuration.bo.Categorie;
import fr.martres.judo.tournoi.core.configuration.bo.Club;
import fr.martres.judo.tournoi.core.configuration.bo.Competition;
import fr.martres.judo.tournoi.core.configuration.bo.CompetitionHelper;
import fr.martres.judo.tournoi.core.configuration.bo.Configuration;
import fr.martres.judo.tournoi.core.configuration.bo.Judoka;
import fr.martres.judo.tournoi.core.configuration.bo.Participation;
import fr.martres.judo.tournoi.core.configuration.bo.PoidsHelper;
import fr.martres.judo.tournoi.core.configuration.bo.Poule;
import fr.martres.judo.tournoi.core.configuration.bo.RapportConfiguration;
import fr.martres.judo.tournoi.core.configuration.bo.Resultat;
import fr.martres.judo.tournoi.core.configuration.bo.SessionCombat;
import fr.martres.judo.tournoi.core.configuration.bo.TypeCombat;
import fr.martres.judo.tournoi.core.exception.BoException;
import fr.martres.judo.tournoi.core.exception.DaoException;
import fr.martres.judo.tournoi.core.rapport.bo.RapportCategorie;
import fr.martres.judo.tournoi.core.rapport.bo.RapportClub;
import fr.martres.judo.tournoi.core.rapport.bo.RapportCompetition;
import fr.martres.judo.tournoi.core.rapport.bo.RapportJudoka;
import fr.martres.judo.tournoi.core.rapport.bo.RapportPoule;
import fr.martres.judo.tournoi.core.rapport.bo.RapportResultat;
import fr.martres.judo.tournoi.core.rapport.bo.RapportSession;
import fr.martres.judo.tournoi.core.rapport.bo.RapportTypeCombat;
import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.TemplateEngineKind;

public class XDocRapportDao implements IRapportDao {

	// instance
	private static XDocRapportDao instance = null;

	/**
	 * Constructeur par defaut.
	 * 
	 * @throws DaoException
	 *             si une erreur survient
	 */
	private XDocRapportDao() {
	}

	/**
	 * Retourne l'instance de la DAO.
	 * 
	 * @return
	 */
	public static XDocRapportDao getInstance() {
		if (instance == null) {
			instance = new XDocRapportDao();
		}
		return instance;
	}

	@Override
	public void genereSessionRapport(Configuration configuration,
			Competition competition, SessionCombat session) throws DaoException {
		try {
			// création de la competition
			RapportCompetition rapportCompetition = new RapportCompetition();
			rapportCompetition.setNom(competition.getName());
			rapportCompetition.setVille(competition.getVille());
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			rapportCompetition.setDate(formatter.format(competition.getDate()));
			// Création du Rapport de la Session
			List<RapportPoule> poules = new ArrayList<RapportPoule>();
			// pour chaque poule
			RapportPoule rapportPoule;
			for (Poule poule : session.getPoules()) {
				rapportPoule = createRapportPoule(configuration, competition,
						poule);

				poules.add(rapportPoule);
			}
			RapportSession rapport = new RapportSession();
			rapport.setSession(Integer.toString(session.getId()));
			rapport.setPoules(poules);
			if (session.getCategorie() != null) {
				rapport.setCategorie(session.getCategorie().getNom());
			} else {
				rapport.setCategorie("-");
			}
			if (session.getPoids() != null) {
				rapport.setPoids(PoidsHelper.getLabel(session.getPoids()));
			} else {
				rapport.setPoids("-");
			}
			rapport.setType(session.getType().getNom());
			// Generation du rapport
			// 1) Load Odt file by filling Velocity template engine and cache
			// it to the registry
			File template = competition.getRapportConfiguration().getTemplate(
					RapportConfiguration.RAPPORT_SESSION);
			InputStream in = new FileInputStream(template);
			IXDocReport report = XDocReportRegistry.getRegistry().loadReport(
					in, TemplateEngineKind.Velocity);

			// FieldsMetadata metadata = report.createFieldsMetadata();
			// metadata.load( "competition", RapportCompetition.class, false );
			// metadata.load( "session", RapportSession.class, false );
			// metadata.load( "poules", RapportPoule.class, true );

			// 3) Create context Java model
			IContext context = report.createContext();
			context.put("competition", rapportCompetition);
			context.put("session", rapport);
			context.put("poules", poules);

			// 4) Generate report by merging Java model with the Docx
			String fichierName = rapport.getType();
			if (!rapport.getCategorie().equals("-")) {
				fichierName += "_" + rapport.getCategorie();
			}
			if (!rapport.getPoids().equals("-")) {
				fichierName += "_" + rapport.getPoids();
			}
			fichierName += "_" + rapport.getSession() + ".odt";

			File parent = getoutputFolder(competition,template);
			OutputStream out = new FileOutputStream(new File(
					parent, fichierName));
			report.process(context, out);

			// on rafraichit le projet
			refreshProject(competition);
		} catch (BoException | IOException | XDocReportException e) {
			throw new DaoException(
					"Erreur lors de la création du rapport de session", e);
		}

	}
	
	/**
	 * Recuppère le répertoire de sortie de la competition.
	 * S'il n'est pas défnit, prends le réertoire parents du template.
	 * @param competition
	 * @param template
	 * @return
	 */
	private File getoutputFolder(Competition competition,File template){
		File outputFolder = competition.getRapportConfiguration().getOutputFolder();
		if ( outputFolder !=  null){
		// vérifie l'existance du répertoire
			if ( ! outputFolder.exists()){
					
						outputFolder.mkdir();
			}
		}
		else{
			outputFolder = template.getParentFile();
		}
		return outputFolder;
	}
	
	private void refreshProject(Competition competition){
		IProject project = competition.getRapportConfiguration().getProjet();
		if (project != null){
			try {
				project.refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor() );
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * @param configuration
	 * @param competition
	 * @param poule
	 * @return
	 * @throws BoException
	 */
	private RapportPoule createRapportPoule(Configuration configuration,
			Competition competition, Poule poule) throws BoException {
		RapportPoule rapportPoule;
		rapportPoule = new RapportPoule();
		rapportPoule.setNom(Integer.toString(poule.getId()));
		// Parcours les judokas
		List<RapportJudoka> rapportJudokas = new ArrayList<RapportJudoka>();
		List<Judoka> judokas = configuration.getJudoka(competition, poule);
		RapportJudoka rapportJudoka;
		for (Judoka judoka : judokas) {
			rapportJudoka = createRapportJudoka(configuration, competition,
					judoka);

			rapportJudokas.add(rapportJudoka);
		}
		rapportPoule.setJudokas(rapportJudokas);
		return rapportPoule;
	}

	/**
	 * @param configuration
	 * @param competition
	 * @param judoka
	 * @return
	 */
	private RapportJudoka createRapportJudoka(Configuration configuration,
			Competition competition, Judoka judoka) {
		RapportJudoka rapportJudoka;
		Participation participation;
		rapportJudoka = new RapportJudoka();
		rapportJudoka.setNom(judoka.getNom() + " " + judoka.getPrenom());
		participation = configuration.getParticipation(competition, judoka);
		rapportJudoka.setGrade(participation.getCeinture());
		rapportJudoka.setClub(participation.getClub().getNom());
		return rapportJudoka;
	}

	@Override
	public void generePouleRapport(Configuration configuration,
			Competition competition, SessionCombat session, Poule poule)
			throws DaoException {
		// TODO Auto-generated method stub

	}

	// TODO : attention il peut y avoir des suptibilités, un judoka qui s'est inscrit et qui n'a pas concourru
	// De même, la catégorie du judoka n'est pas forcément celle de la participation à une session de comba...
	// Mais cette dernière peut être vide.
	@Override
	public void genereClubRapport(Configuration configuration,
			Competition competition, Club club) throws DaoException {
		try {
			// création de la competition
			RapportCompetition rapportCompetition = new RapportCompetition();
			rapportCompetition.setNom(competition.getName());
			rapportCompetition.setVille(competition.getVille());
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			rapportCompetition.setDate(formatter.format(competition.getDate()));

			// initialise les types de combats
			Map<TypeCombat, RapportTypeCombat> types = new HashMap<TypeCombat, RapportTypeCombat>();
			RapportTypeCombat rapportType;
			for (TypeCombat type : competition.getTypeCombats()) {
				rapportType = new RapportTypeCombat();
				rapportType.setNom(type.getNom());
				types.put(type, rapportType);
			}
			
			// associe les catégories et les participations
			List<Participation> participations = configuration
					.getParticipations(club, competition);
			Categorie categorie;
			String ceinture;
			Judoka judoka;
			double poids;
			List<Resultat> resultats;
			Poule poule;
			SessionCombat session;
			List<Judoka> judokas;
			TypeCombat typeCombat;
			RapportTypeCombat rapportTypeCombat;
			Map<TypeCombat,Map<Categorie, RapportCategorie>> categoriesParType = new HashMap<TypeCombat,Map<Categorie, RapportCategorie>>();
			Map<Categorie, RapportCategorie> categories;
			RapportCategorie rapportCategorie;
			RapportResultat rapportResultat;
			int nbJudokaClub = 0;
			int nbPointsClub = 0;
			int nbPoints;
			for (Participation participation : participations) {
				// recup des infos
				ceinture = participation.getCeinture();
				judoka = participation.getJudoka();
				poids = participation.getPoids();
				categorie = participation.getCategorie();
				resultats = participation.getResultats();
				nbJudokaClub++;
				for (Resultat resultat : resultats) {
					poule = resultat.getPoule();
					session = CompetitionHelper.getSessionCombat(competition,
							poule);
					typeCombat = session.getType();
					judokas = configuration.getJudoka(competition, poule);

					// association à une catégorie
					rapportTypeCombat = types.get(typeCombat);
					//recup des catégories du type de combat
					categories = categoriesParType.get(typeCombat);
					if (categories == null){
						categories = new HashMap<Categorie,RapportCategorie>();
						categoriesParType.put(typeCombat, categories);
					}
					//recup du rapport associé à la categorie
					rapportCategorie = categories.get(categorie);
					if (rapportCategorie == null) {
						rapportCategorie = new RapportCategorie();
						rapportCategorie.setNom(categorie.getNom());
						rapportCategorie.setAnneeMinimum(categorie.getAnneeMinimum());
						categories.put(categorie, rapportCategorie);
					}

					// on vérifie que la catégorie soit associé au type de
					// combat
					if (!rapportTypeCombat.getCategories().contains(
							rapportCategorie)) {
						rapportTypeCombat.addCategories(rapportCategorie);
					}

					// on créé le rapport de resultat
					rapportResultat = new RapportResultat();
					rapportResultat.setNom(judoka.getNom());
					rapportResultat.setPrenom(judoka.getPrenom());
					rapportResultat.setGrade(ceinture);
					rapportResultat.setPosition(Integer.toString(resultat
							.getPosition()));
					rapportResultat.setNbCombattant(Integer.toString(judokas
							.size()));
					nbPoints = typeCombat.getPointsPourClassement(
							judokas.size(), resultat.getPosition());
					rapportResultat.setNbPoint(Integer.toString(nbPoints));
					nbPointsClub += nbPoints;
					rapportCategorie.addResultat(rapportResultat);
				}

	
			}
			
			// sort les catégories
			for( RapportTypeCombat type : types.values()){
				Collections.sort(type.getCategories(), Collections.reverseOrder());
			}
			
			RapportClub rapportClub = new RapportClub();
			rapportClub.setNom(club.getNom());
			rapportClub.setNbJudoka(Integer.toString(nbJudokaClub));
			rapportClub.setNbPoints(Integer.toString(nbPointsClub));

			// Generation du rapport
			// 1) Load Odt file by filling Velocity template engine and cache
			// it to the registry
			File template = competition.getRapportConfiguration().getTemplate(
					RapportConfiguration.RAPPORT_CLUB);
			InputStream in = new FileInputStream(template);
			IXDocReport report = XDocReportRegistry.getRegistry().loadReport(
					in, TemplateEngineKind.Velocity);

			// 3) Create context Java model
			IContext context = report.createContext();
			context.put("competition", rapportCompetition);
			context.put("club", rapportClub);
			context.put("types",types);

			// 4) Generate report by merging Java model with the Docx

			SimpleDateFormat formatterFile = new SimpleDateFormat("yyyy-MM-dd");
			String fichierName = "Resultats_" 
					+ competition.getName() + "_"
					+ formatterFile.format(competition.getDate()) + "_"
					+ club.getNom()+ ".odt";

			File parent = getoutputFolder(competition,template);
			OutputStream out = new FileOutputStream(new File(
					parent, fichierName));
			report.process(context, out);


			// on rafraichit le projet
			refreshProject(competition);
		} catch (BoException | XDocReportException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void genereCompetitionRapport(Configuration configuration,
			Competition competition) throws DaoException {
		// création de la competition
		RapportCompetition rapportCompetition = new RapportCompetition();
		rapportCompetition.setNom(competition.getName());
		rapportCompetition.setVille(competition.getVille());
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		rapportCompetition.setDate(formatter.format(competition.getDate()));

		try {
			// Récupère le tableau des résultats par club
			List<Club> clubs = configuration.getClubs(competition);
			List<RapportClub> rapportClubs = new ArrayList<RapportClub>();
			RapportClub rapportClub;
			int nbTotalJudoka = 0;
			int nbTotalPoints = 0;
			for (Club club : clubs) {
				rapportClub = createRapportClub(configuration, competition,
						club);

				nbTotalJudoka += Integer.parseInt(rapportClub.getNbJudoka());
				nbTotalPoints += Integer.parseInt(rapportClub.getNbPoints());
				rapportClubs.add(rapportClub);
			}

			Collections.sort(rapportClubs, Collections.reverseOrder());

			rapportCompetition.setNbJudokas(Integer.toString(nbTotalJudoka));
			rapportCompetition.setNbPoints(Integer.toString(nbTotalPoints));

			// Generation du rapport
			// 1) Load Odt file by filling Velocity template engine and cache
			// it to the registry
			File template = competition.getRapportConfiguration().getTemplate(
					RapportConfiguration.RAPPORT_COMPETITION);
			InputStream in = new FileInputStream(template);
			IXDocReport report = XDocReportRegistry.getRegistry().loadReport(
					in, TemplateEngineKind.Velocity);

			// 3) Create context Java model
			IContext context = report.createContext();
			context.put("competition", rapportCompetition);
			context.put("clubs", rapportClubs);

			// 4) Generate report by merging Java model with the Docx

			SimpleDateFormat formatterFile = new SimpleDateFormat("yyyy-MM-dd");
			String fichierName = "Classement_" + competition.getVille() + "_"
					+ competition.getName() + "_"
					+ formatterFile.format(competition.getDate()) + ".odt";

			File parent = getoutputFolder(competition,template);
			OutputStream out = new FileOutputStream(new File(
					parent, fichierName));
			report.process(context, out);


			// on rafraichit le projet
			refreshProject(competition);
		} catch (BoException | XDocReportException | IOException e) {
			throw new DaoException(
					"Erreur lors de la création du rapport général de la compétition",
					e);
		}

	}

	/**
	 * @param configuration
	 * @param competition
	 * @param club
	 * @return
	 * @throws BoException
	 */
	private RapportClub createRapportClub(Configuration configuration,
			Competition competition, Club club) throws BoException {
		RapportClub rapportClub;
		rapportClub = new RapportClub();
		rapportClub.setNom(club.getNom());
		List<Participation> participations = configuration.getParticipations(
				club, competition);
		rapportClub.setNbJudoka(Integer.toString(participations.size()));

		int position;
		int points = 0;
		int nbJudoka = 0;
		Poule poule;
		for (Participation participation : participations) {
			for (Resultat resultat : participation.getResultats()) {
				position = resultat.getPosition();
				poule = resultat.getPoule();
				nbJudoka = configuration.getJudoka(competition, poule).size();
				for (SessionCombat session : competition.getSessionsCombats()) {
					if (session.getPoules().contains(poule)) {
						TypeCombat combat = session.getType();
						points += combat.getPointsPourClassement(nbJudoka,
								position);
					}
				}

			}
		}

		rapportClub.setNbPoints(Integer.toString(points));
		return rapportClub;
	}

}
