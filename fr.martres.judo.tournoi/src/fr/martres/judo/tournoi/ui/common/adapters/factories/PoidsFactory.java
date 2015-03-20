package fr.martres.judo.tournoi.ui.common.adapters.factories;

import java.rmi.UnexpectedException;
import java.util.List;

import fr.martres.judo.tournoi.core.configuration.bo.Competition;
import fr.martres.judo.tournoi.core.configuration.bo.Configuration;
import fr.martres.judo.tournoi.core.configuration.bo.Judoka;
import fr.martres.judo.tournoi.core.configuration.bo.Participation;
import fr.martres.judo.tournoi.core.configuration.bo.Poids;
import fr.martres.judo.tournoi.core.configuration.bo.SessionCombat;
import fr.martres.judo.tournoi.services.ConfigurationService;
import fr.martres.judo.tournoi.ui.common.UiException;
import fr.martres.judo.tournoi.ui.common.adapters.nodes.JudokaNode;
import fr.martres.judo.tournoi.ui.common.adapters.nodes.PoidsNode;
import fr.martres.judo.tournoi.ui.common.adapters.nodes.SessionCombatNode;

public class PoidsFactory extends AbstractDataFactory<Poids, PoidsNode> {

	private JudokaFactory judokaFactory = null;
	private SessionCombatFactory sessionCombatFactory = null;

	/**
	 * @return the judokaFactory
	 * @throws UnexpectedException
	 */
	private JudokaFactory getJudokaFactory() {
		if (judokaFactory == null) {
			throw new FactoryRuntimeException("Factory non initialisée.");
		}
		return judokaFactory;
	}

	/**
	 * @param judokaFactory
	 *            the judokaFactory to set
	 */
	public void setJudokaFactory(JudokaFactory judokaFactory) {
		this.judokaFactory = judokaFactory;
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

	public PoidsFactory(FactoriesManager.FACTORY_TYPE type) {
		super(type);
	};

	public PoidsNode getPoidsNode(Poids poids, List<Judoka> judokas,
			Competition competition) throws UiException {
		PoidsNode poidsNode = new PoidsNode(poids);

		JudokaNode node;
		JudokaFactory factory = getJudokaFactory();

		Configuration configuration;
		try {
			configuration = ConfigurationService.getInstance()
					.getConfiguration();

		} catch (Exception e) {
			throw new UiException("Error when displaying configuation node", e);
		}

		// affichage des sessions que dans le cas fulls
		if (FactoriesManager.FACTORY_TYPE.FULL.equals(getType())) {
			// ajout des sessions de la competition
			SessionCombatNode sessionNode;
			SessionCombatFactory factorySession = getSessionCombatFactory();
			for (SessionCombat session : competition.getSessionsCombats()) {
				// Seule les sessions avec le bon poids sont affichées
				if (poids.equals(session.getPoids())) {
					sessionNode = factorySession.getSessionCombatNode(session,
							competition);
					poidsNode.addChild(sessionNode);
				}
			}
		}

		Participation participation = null;
		for (Judoka judoka : judokas) {
			participation = configuration.getParticipation(competition, judoka);
			node = factory.getJudokaNode(judoka, participation);
			poidsNode.addChild(node);
		}
		poidsNode.setNbElement(judokas.size());

		this.putData(poids, poidsNode);
		return poidsNode;
	}

}
