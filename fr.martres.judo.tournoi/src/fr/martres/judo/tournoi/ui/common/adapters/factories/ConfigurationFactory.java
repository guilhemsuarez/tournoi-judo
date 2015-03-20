package fr.martres.judo.tournoi.ui.common.adapters.factories;

import java.util.List;

import fr.martres.judo.tournoi.core.configuration.bo.Competition;
import fr.martres.judo.tournoi.core.configuration.bo.Configuration;
import fr.martres.judo.tournoi.ui.common.UiException;
import fr.martres.judo.tournoi.ui.common.adapters.nodes.CompetitionNode;
import fr.martres.judo.tournoi.ui.common.adapters.nodes.ConfigurationNode;

public class ConfigurationFactory extends AbstractDataFactory<Configuration,ConfigurationNode> {
	/** creation des factories*/
	private CompetitionFactory competitionFactory = null ;
	
	public  ConfigurationFactory(FactoriesManager.FACTORY_TYPE type) {
		super(type);
		};
	
	
	/**
	 * @return the competitionFactory
	 */
	private CompetitionFactory getCompetitionFactory() {
		if ( competitionFactory == null ){
			throw new FactoryRuntimeException("Factory non initialisée.");
		}
		return competitionFactory;
	}


	/**
	 * @param competitionFactory the competitionFactory to set
	 */
	public void setCompetitionFactory(CompetitionFactory competitionFactory) {
		this.competitionFactory = competitionFactory;
	}


	public ConfigurationNode getConfigurationNode ( Configuration configuration) throws UiException{
		ConfigurationNode configurationNode =  new ConfigurationNode(configuration);
		
		// recupperation des competitions
		List<Competition> competitions = configuration.getCompetitions();
		CompetitionNode node;
		CompetitionFactory factory = getCompetitionFactory();
		for(Competition competition : competitions){
			node = factory.getCompetitionNode(competition);
			configurationNode.addChild(node);
		}
		this.putData(configuration, configurationNode);
		return configurationNode;
	}
	
	
}
