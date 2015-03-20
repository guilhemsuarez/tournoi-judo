package fr.martres.judo.tournoi.ui.common.adapters.factories;

import java.util.List;

import fr.martres.judo.tournoi.core.configuration.bo.Categorie;
import fr.martres.judo.tournoi.core.configuration.bo.Competition;
import fr.martres.judo.tournoi.core.configuration.bo.Configuration;
import fr.martres.judo.tournoi.core.configuration.bo.Judoka;
import fr.martres.judo.tournoi.core.configuration.bo.Poids;
import fr.martres.judo.tournoi.core.configuration.bo.SessionCombat;
import fr.martres.judo.tournoi.core.exception.BoException;
import fr.martres.judo.tournoi.services.ConfigurationService;
import fr.martres.judo.tournoi.ui.common.UiException;
import fr.martres.judo.tournoi.ui.common.adapters.nodes.CategorieNode;
import fr.martres.judo.tournoi.ui.common.adapters.nodes.PoidsNode;
import fr.martres.judo.tournoi.ui.common.adapters.nodes.SessionCombatNode;

public class CategorieFactory extends
		AbstractDataFactory<Categorie, CategorieNode> {

	public CategorieFactory(FactoriesManager.FACTORY_TYPE type) {
		super(type);
	}

	/** Definie les fectory. */
	private PoidsFactory poidsFactory = null;
	private SessionCombatFactory sessionCombatFactory = null;

	/**
	 * @return the poidsFactory
	 */
	private PoidsFactory getPoidsFactory() {
		if (poidsFactory == null) {
			throw new FactoryRuntimeException("Factory non initialisée.");
		}
		return poidsFactory;
	}

	/**
	 * @param poidsFactory
	 *            the poidsFactory to set
	 */
	public void setPoidsFactory(PoidsFactory poidsFactory) {
		this.poidsFactory = poidsFactory;
	}

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

	public CategorieNode getCategorieNode(Categorie categorie,
			Competition competition) throws UiException {
		CategorieNode categorieNode = new CategorieNode(categorie);

		// recupperation des Judokas
		List<Judoka> judokas;
		Configuration configuration;
		try {
			configuration = ConfigurationService.getInstance()
					.getConfiguration();

			judokas = configuration.getJudoka(competition, categorie);
		} catch (Exception e) {
			throw new UiException("Error when displaying configuation node", e);
		}
		// affichage des sessions que dans le cas fulls
		if (FactoriesManager.FACTORY_TYPE.FULL.equals(getType())) {
			// ajout des sessions de la competition
			SessionCombatNode sessionNode;
			SessionCombatFactory factorySession = getSessionCombatFactory();
			for (SessionCombat session : competition.getSessionsCombats()) {
				// Seule les sessions avec la bonne catégorie et sans poids sont affichées
				if (categorie.equals(session.getCategorie()) && session.getPoids() == null) {
					sessionNode = factorySession.getSessionCombatNode(session,competition);
					categorieNode.addChild(sessionNode);
				}
			}
		}
		try {
			// Réccupère la liste des poids de la categorie
			List<Poids> poidsList = categorie.getPoids();
			// on affiche les poids qui sont nécessaire
			PoidsNode node;
			List<Judoka> judokasAAfficher;
			PoidsFactory factoryPoids = getPoidsFactory();

			for (Poids poidsAAfficher : poidsList) {

				judokasAAfficher = configuration.getJudoka(competition,
						categorie, poidsAAfficher);
				if (judokasAAfficher.size() > 0) {
					node = factoryPoids.getPoidsNode(poidsAAfficher,
							judokasAAfficher, competition);
					categorieNode.addChild(node);
				}

			}
		} catch (BoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		categorieNode.setNbElement(judokas.size());
		this.putData(categorie, categorieNode);
		return categorieNode;
	}

}
