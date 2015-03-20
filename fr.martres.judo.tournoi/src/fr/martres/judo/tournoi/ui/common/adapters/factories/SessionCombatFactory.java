package fr.martres.judo.tournoi.ui.common.adapters.factories;

import java.rmi.UnexpectedException;

import fr.martres.judo.tournoi.core.configuration.bo.Competition;
import fr.martres.judo.tournoi.core.configuration.bo.Poule;
import fr.martres.judo.tournoi.core.configuration.bo.SessionCombat;
import fr.martres.judo.tournoi.ui.common.UiException;
import fr.martres.judo.tournoi.ui.common.adapters.nodes.PouleNode;
import fr.martres.judo.tournoi.ui.common.adapters.nodes.SessionCombatNode;

public class SessionCombatFactory extends AbstractDataFactory <SessionCombat,SessionCombatNode>{
public SessionCombatFactory(FactoriesManager.FACTORY_TYPE type) {
	super(type);
	};

private PouleFactory pouleFactory = null;
	
	/**
	 * @return the judokaFactory
	 * @throws UnexpectedException 
	 */
	private PouleFactory getPouleFactory(){
		if ( pouleFactory == null ){
			throw new FactoryRuntimeException("Factory non initialisée.");
		}
		return pouleFactory;
	}


	/**
	 * @param pouleFactory the judokaFactory to set
	 */
	public void setPouleFactory(PouleFactory pouleFactory) {
		this.pouleFactory = pouleFactory;
	}


	public SessionCombatNode getSessionCombatNode(SessionCombat session, Competition competition) throws UiException {
		SessionCombatNode sessionNode = new SessionCombatNode(
				session);
		
		// Dans le cadre de l'affichage des poules il faut que je les affiche
		if ( FactoriesManager.FACTORY_TYPE.POULE.equals(getType())){
				
					PouleNode pouleNode = null;
					for ( Poule poule : session.getPoules()){
						pouleNode = getPouleFactory().getPouleNode(poule, competition);
						sessionNode.addChild(pouleNode);
					}
							
		
		}
		
		this.putData(session, sessionNode);
		return sessionNode;
	}

}
