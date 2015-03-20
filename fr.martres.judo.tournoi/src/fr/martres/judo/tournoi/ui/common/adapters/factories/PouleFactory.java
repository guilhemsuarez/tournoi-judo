package fr.martres.judo.tournoi.ui.common.adapters.factories;

import java.rmi.UnexpectedException;
import java.util.List;

import fr.martres.judo.tournoi.core.configuration.bo.Competition;
import fr.martres.judo.tournoi.core.configuration.bo.Configuration;
import fr.martres.judo.tournoi.core.configuration.bo.Judoka;
import fr.martres.judo.tournoi.core.configuration.bo.Participation;
import fr.martres.judo.tournoi.core.configuration.bo.Poule;
import fr.martres.judo.tournoi.services.ConfigurationService;
import fr.martres.judo.tournoi.ui.common.UiException;
import fr.martres.judo.tournoi.ui.common.adapters.nodes.JudokaNode;
import fr.martres.judo.tournoi.ui.common.adapters.nodes.PouleNode;

public class PouleFactory extends AbstractDataFactory<Poule,PouleNode> {

	private JudokaFactory judokaFactory = null;
	
	/**
	 * @return the judokaFactory
	 * @throws UnexpectedException 
	 */
	private JudokaFactory getJudokaFactory(){
		if ( judokaFactory == null ){
			throw new FactoryRuntimeException("Factory non initialisée.");
		}
		return judokaFactory;
	}


	/**
	 * @param judokaFactory the judokaFactory to set
	 */
	public void setJudokaFactory(JudokaFactory judokaFactory) {
		this.judokaFactory = judokaFactory;
	}


	public PouleFactory(FactoriesManager.FACTORY_TYPE type) {
		super(type);
	};

	
	public PouleNode getPouleNode( Poule poule, Competition competition) throws UiException {
		PouleNode pouleNode = new PouleNode(poule);

		
		JudokaNode node;
		JudokaFactory factory = getJudokaFactory();

		try {
			Configuration configuration = ConfigurationService.getInstance()
					.getConfiguration();
			Participation participation = null;
			List<Judoka> judokas = configuration.getJudoka(competition, poule); 
			for (Judoka judoka : judokas) {
				participation = configuration.getParticipation(competition, judoka);
				node = factory.getJudokaNode(judoka,participation);
				pouleNode.addChild(node);
			}
			pouleNode.setNbElement(judokas.size());
			
			this.putData(poule, pouleNode);

		} catch (Exception e) {
			throw new UiException("Error when displaying configuation node", e);
		}
		

		return pouleNode;
	}

}
