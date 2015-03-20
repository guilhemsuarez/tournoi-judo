package fr.martres.judo.tournoi.ui.common.adapters.factories;

import java.util.List;

import fr.martres.judo.tournoi.core.configuration.bo.Categorie;
import fr.martres.judo.tournoi.core.configuration.bo.Competition;
import fr.martres.judo.tournoi.core.configuration.bo.SessionCombat;
import fr.martres.judo.tournoi.ui.common.UiException;
import fr.martres.judo.tournoi.ui.common.adapters.nodes.CategorieNode;
import fr.martres.judo.tournoi.ui.common.adapters.nodes.CompetitionNode;
import fr.martres.judo.tournoi.ui.common.adapters.nodes.SessionCombatNode;

public class CompetitionFactory extends
		AbstractDataFactory<Competition, CompetitionNode> {

	/** construit les factorires. */
	private SessionCombatFactory sessionCombatFactory = null;
	private CategorieFactory categorieFactory = null;

	public CompetitionFactory(FactoriesManager.FACTORY_TYPE type) {
		super(type);
	};

	/**
	 * @return the sessionCombatFactory
	 */
	private SessionCombatFactory getSessionCombatFactory() {
		if (sessionCombatFactory == null) {
			throw new FactoryRuntimeException("Factory non initialisée.");
		}
		return sessionCombatFactory;
	}

	/**
	 * @param sessionCombatFactory
	 *            the sessionCombatFactory to set
	 */
	public void setSessionCombatFactory(
			SessionCombatFactory sessionCombatFactory) {
		this.sessionCombatFactory = sessionCombatFactory;
	}

	/**
	 * @return the sessionCombatFactory
	 */
	private CategorieFactory getCategorieFactory() {
		if (categorieFactory == null) {
			throw new FactoryRuntimeException("Factory non initialisée.");
		}
		return categorieFactory;
	}

	/**
	 * @param categorieFactory
	 *            the sessionCombatFactory to set
	 */
	public void setCategorieFactory(CategorieFactory categorieFactory) {
		this.categorieFactory = categorieFactory;
	}

	public CompetitionNode getCompetitionNode(Competition competition)
			throws UiException {
		CompetitionNode competitionNode = new CompetitionNode(competition);

		// Affichage des session que dns le cas des FULL
		if (FactoriesManager.FACTORY_TYPE.FULL.equals(getType())) {

			// recupperation des sessions de combat
			SessionCombatNode sessionNode;
			SessionCombatFactory factorySession = getSessionCombatFactory();
			for (SessionCombat session : competition.getSessionsCombats()) {
				// Seule les sessions sans catégorie sont affichées
				if (session.getCategorie() == null) {
					sessionNode = factorySession.getSessionCombatNode(session, competition);
					competitionNode.addChild(sessionNode);
				}
			}

		}
		// recupperation des categories

		List<Categorie> categories = competition.getCategories();
		CategorieNode node;

		CategorieFactory categorieFactory = getCategorieFactory();
		for (Categorie cat : categories) {
			node = categorieFactory.getCategorieNode(cat, competition);
			competitionNode.addChild(node);
		}

		this.putData(competition, competitionNode);
		return competitionNode;
	}

}
