package fr.martres.judo.tournoi.services;

import java.util.Date;
import java.util.List;

import fr.martres.judo.tournoi.core.configuration.bo.Club;
import fr.martres.judo.tournoi.core.configuration.bo.Competition;
import fr.martres.judo.tournoi.core.configuration.bo.Configuration;
import fr.martres.judo.tournoi.core.configuration.bo.Judoka;
import fr.martres.judo.tournoi.core.configuration.bo.Poule;
import fr.martres.judo.tournoi.core.configuration.bo.SessionCombat;
import fr.martres.judo.tournoi.core.configuration.bo.Participation;
import fr.martres.judo.tournoi.core.configuration.dao.ConfigurationFileDao;
import fr.martres.judo.tournoi.core.configuration.dao.IConfigurationDao;
import fr.martres.judo.tournoi.core.exception.DaoException;

public class ConfigurationService {

	private Configuration configuration = null;

	private static ConfigurationService instance = new ConfigurationService();

	private ConfigurationService() {
	}

	public static ConfigurationService getInstance() {
		return instance;
	}

	public Configuration getConfiguration() throws ServiceException{
		if (configuration == null) {
			try {
				IConfigurationDao dao = ConfigurationFileDao.getInstance();
				configuration = dao.retreiveConfiguration();
			} catch (DaoException e) {
				throw new ServiceException("Erreur pendant la récupération des données.",e);
			}

		}
		return configuration;
	}

	public void saveConfiguration() throws ServiceException{
		try {
			IConfigurationDao dao = ConfigurationFileDao.getInstance();
			dao.saveConfiguration(configuration);
		} catch (DaoException e) {
			throw new ServiceException("Erreur pendant la sauvarde des données.",e);
		}
	}
	
	public void addSessionCombat(Competition competition,SessionCombat session) throws ServiceException{
		try {
			IConfigurationDao dao = ConfigurationFileDao.getInstance();
			dao.addSessionCombat(configuration, competition, session);
		} catch (DaoException e) {
			throw new ServiceException("Erreur pendant l'ajout d'une session.",e);
		}
	}

	public Poule createPouleOnSessionCombat(Competition competition, SessionCombat session, List<Judoka> judokas) throws ServiceException{
		Poule result = null;
		try {
			IConfigurationDao dao = ConfigurationFileDao.getInstance();
			result = dao.createPouleOnSessionCombat(configuration, competition, session, judokas);
		} catch (DaoException e) {
			throw new ServiceException("Erreur pendant la creation d'un poule.",e);
		}
		return result;
	}
	
	public void addJudokaOnPoule(Competition competition, Poule poule, Judoka judoka) throws ServiceException{
		try {
			IConfigurationDao dao = ConfigurationFileDao.getInstance();
			dao.addJudokaOnPoule(configuration, competition, poule, judoka);
		} catch (DaoException e) {
			throw new ServiceException("Erreur pendant la creation d'un poule.",e);
		}
		
	}
	
	public void setPositionInPoule(
			Competition competition, SessionCombat session, Judoka judoka,
			Poule poule, int position) throws ServiceException{
		try {
			IConfigurationDao dao = ConfigurationFileDao.getInstance();
			dao.setPositionInPoule(configuration, competition, session, judoka, poule, position);
		} catch (DaoException e) {
			throw new ServiceException("Erreur l'ajout d'une position.",e);
		}
	}
	
	public void deletePouleOnSession(
			Competition competition, SessionCombat session, Poule poule) throws ServiceException{
		try {
			IConfigurationDao dao = ConfigurationFileDao.getInstance();
			dao.deletePouleOnSessionCombat(configuration, competition, session, poule);
		} catch (DaoException e) {
			throw new ServiceException("Erreur la suppression d'une poule.",e);
		}
	}
	
	
	public void deleteJudokaOnPoule(Configuration configuration,
			Competition competition, Poule poule,
			Judoka judoka) throws ServiceException{
				try {
					IConfigurationDao dao = ConfigurationFileDao.getInstance();
					dao.deleteJudokaOnPoule(configuration, competition, poule, judoka);
				} catch (DaoException e) {
					throw new ServiceException("Erreur la suppression d'un judoka.",e);
				}
			}
	/**
	 * force le rechargement de la configuration
	 */
	public void reset() {
		configuration = null;		
	}

	/**
	 * Ajoute un judoka à la competition.
	 * @param competition
	 * @param nom
	 * @param prenom
	 * @param dateDeNaissance
	 * @param isHomme
	 * @param ceinture
	 * @param clubName
	 * @param poids
	 * @throws ServiceException
	 */
	public Participation addJudoka(Competition competition, String nom,
			String prenom, Date dateDeNaissance, boolean isHomme,
			String ceinture, String clubName, double poids) throws ServiceException {
		Participation result = null;
		try {
			IConfigurationDao dao = ConfigurationFileDao.getInstance();
			result = dao.addJudokaOnCompetition(configuration, competition, nom, prenom, dateDeNaissance,isHomme,ceinture,clubName,poids);
		} catch (DaoException e) {
			throw new ServiceException("Erreur pendant la creation d'un poule.",e);
		}
		return result;
		
	}

	public Participation addJudoka(Competition competition,
			String nom, String prenom, Date dateDeNaissance, boolean isHomme,
			String ceinture, Club club, double poids) throws ServiceException {
		Participation result = null;
		try {
			IConfigurationDao dao = ConfigurationFileDao.getInstance();
			result = dao.addJudokaOnCompetition(configuration, competition, nom, prenom, dateDeNaissance,isHomme,ceinture,club,poids);
		} catch (DaoException e) {
			throw new ServiceException("Erreur pendant la creation d'un poule.",e);
		}
		return result;
	}
}
