package fr.martres.judo.tournoi.ui.common.adapters.factories;

public class FactoriesManager {
	
	public enum FACTORY_TYPE { FULL,SESSION_COMBAT,POULE};
	

	/** Categorie Factory. */
	private CategorieFactory categorieFactory = null;
	/** Competition Factory */
	private CompetitionFactory competitionFactory = null ;
	/** Configuration Factory.*/
	private ConfigurationFactory configurationFactory = null;
	/** Judoka Factory.*/
	private JudokaFactory judokaFactory = null;
	/** poids Factory.*/
	private PoidsFactory poidsFactory = null;
	/** Session de combats factory.*/
	private SessionCombatFactory sessionCombatFactory = null;
	/** Poule Factory.*/
	private PouleFactory pouleFactory = null;
	
	/**
	 * Constructeur.
	 */
	public FactoriesManager(FACTORY_TYPE type){
		// Création des factories
		sessionCombatFactory = new SessionCombatFactory(type);
		judokaFactory = new JudokaFactory(type);
		poidsFactory = new PoidsFactory(type);
		categorieFactory = new CategorieFactory(type);
		competitionFactory = new CompetitionFactory(type);
		configurationFactory = new ConfigurationFactory(type);
		pouleFactory = new PouleFactory(type);
		
		// associe les factgories
		poidsFactory.setJudokaFactory(judokaFactory);
		poidsFactory.setSessionCombatFactory(sessionCombatFactory);
		categorieFactory.setPoidsFactory(poidsFactory);
		categorieFactory.setSessionCombatFactory(sessionCombatFactory);
		competitionFactory.setCategorieFactory(categorieFactory);
		competitionFactory.setSessionCombatFactory(sessionCombatFactory);
		configurationFactory.setCompetitionFactory(competitionFactory);
		sessionCombatFactory.setPouleFactory(pouleFactory);
		pouleFactory.setJudokaFactory(judokaFactory);
		
	}

	/**
	 * @return the categorieFactory
	 */
	public CategorieFactory getCategorieFactory() {
		return categorieFactory;
	}

	/**
	 * @return the competitionFactory
	 */
	public CompetitionFactory getCompetitionFactory() {
		return competitionFactory;
	}

	/**
	 * @return the configurationFactory
	 */
	public ConfigurationFactory getConfigurationFactory() {
		return configurationFactory;
	}

	/**
	 * @return the judokaFactory
	 */
	public JudokaFactory getJudokaFactory() {
		return judokaFactory;
	}

	/**
	 * @return the poidsFactory
	 */
	public PoidsFactory getPoidsFactory() {
		return poidsFactory;
	}

	/**
	 * @return the sessionCombatFactory
	 */
	public SessionCombatFactory getSessionCombatFactory() {
		return sessionCombatFactory;
	}

	/**
	 * @return the pouleFactory
	 */
	public PouleFactory getPouleFactory() {
		return pouleFactory;
	}
	
}
