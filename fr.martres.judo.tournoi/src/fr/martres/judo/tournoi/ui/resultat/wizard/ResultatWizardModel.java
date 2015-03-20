package fr.martres.judo.tournoi.ui.resultat.wizard;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fr.martres.judo.tournoi.core.configuration.bo.Competition;
import fr.martres.judo.tournoi.core.configuration.bo.Configuration;
import fr.martres.judo.tournoi.core.configuration.bo.Judoka;
import fr.martres.judo.tournoi.core.configuration.bo.Participation;
import fr.martres.judo.tournoi.core.configuration.bo.Poule;
import fr.martres.judo.tournoi.core.configuration.bo.SessionCombat;
import fr.martres.judo.tournoi.core.exception.BoException;
import fr.martres.judo.tournoi.services.ConfigurationService;
import fr.martres.judo.tournoi.services.ServiceException;
import fr.martres.judo.tournoi.ui.common.UiException;

public class ResultatWizardModel {
	private Competition competition;
	private int session;
	private int poule;
	private Map<Judoka, Integer> results;
	


	public ResultatWizardModel(){
	}

	/**
	 * @return the competition
	 */
	public Competition getCompetition() {
		return competition;
	}

	/**
	 * @param competition the competition to set
	 */
	public void setCompetition(Competition competition) {
		this.competition = competition;
	}

	/**
	 * @return the session
	 */
	public int getSession() {
		return session;
	}

	/**
	 * @param session the session to set
	 */
	public void setSession(int session) {
		this.session = session;
	}

	/**
	 * @return the poule
	 */
	public int getPoule() {
		return poule;
	}

	/**
	 * @param poule the poule to set
	 */
	public void setPoule(int poule) {
		this.poule = poule;
	}
	
	public SessionCombat getCurrentSessionCombat() throws UiException{
		//on reccupère la session
		SessionCombat sessionCombat = competition.getSessionsCombatMap().get(session);
		if (sessionCombat == null){
			throw new UiException("La session "+session+" n'a pas été reconnu.");
		}
			
		return sessionCombat;
	}
	
	public Poule getCurrentPoule() throws UiException{
		//on reccupère la session
		SessionCombat sessionCombat = competition.getSessionsCombatMap().get(session);
		if (sessionCombat == null){
			throw new UiException("La session "+session+" n'a pas été reconnu.");
		}
		
		// on récupère la poule
		Poule currentPoule = sessionCombat.getPoule(poule);
		if (currentPoule == null){
			throw new UiException("La poule "+poule+" n'a pas été reconnu.");
		}
			
		return currentPoule;
	}
	
	public List<Participation> getCurrentParticipations() throws UiException{
		//on reccupère la session
		SessionCombat sessionCombat = competition.getSessionsCombatMap().get(session);
		if (sessionCombat == null){
			throw new UiException("La session "+session+" n'a pas été reconnu.");
		}
		
		// on récupère la poule
		Poule currentPoule = sessionCombat.getPoule(poule);
		if (currentPoule == null){
			throw new UiException("La poule "+poule+" n'a pas été reconnu.");
		}
		
		List<Participation> participations;
		try {
			Configuration configuration = ConfigurationService.getInstance().getConfiguration();
			participations = configuration.getParticipations(competition, currentPoule);
		} catch (ServiceException | BoException e) {
			throw new UiException("Erreur dans la reccherche des participations", e);
		}
		
		
		return participations;
	}

	public void setResults(Map<Judoka, Integer> results) {
		this.results = results;
		
	}
	
	/**
	 * @return the results
	 */
	public Map<Judoka, Integer> getResults() {
		return results;
	}
}
