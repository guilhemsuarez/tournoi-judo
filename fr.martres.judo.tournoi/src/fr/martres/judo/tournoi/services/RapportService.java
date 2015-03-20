package fr.martres.judo.tournoi.services;

import fr.martres.judo.tournoi.core.configuration.bo.Club;
import fr.martres.judo.tournoi.core.configuration.bo.Competition;
import fr.martres.judo.tournoi.core.configuration.bo.Configuration;
import fr.martres.judo.tournoi.core.configuration.bo.Poule;
import fr.martres.judo.tournoi.core.configuration.bo.SessionCombat;
import fr.martres.judo.tournoi.core.exception.DaoException;
import fr.martres.judo.tournoi.core.rapport.dao.IRapportDao;
import fr.martres.judo.tournoi.core.rapport.dao.XDocRapportDao;


public class RapportService {

	private static RapportService instance = new RapportService();

	private RapportService() {
	}

	public static RapportService getInstance() {
		return instance;
	}


	public void genereSessionRapport(Competition competition, SessionCombat session) throws ServiceException{
		try {
			Configuration configuration = ConfigurationService.getInstance().getConfiguration();
			IRapportDao rapportDao = XDocRapportDao.getInstance();
			rapportDao.genereSessionRapport(configuration, competition, session);
		} catch (DaoException e) {
			throw new ServiceException("Erreur pendant la generation d'un rapport de session.",e);
		}
	}
	
	public void generePouleRapport(SessionCombat session,Poule poule) throws ServiceException{
//		try {
//			
//		} catch (DaoException e) {
//			throw new ServiceException("Erreur pendant la generation d'un rapport de poule.",e);
//		}
	}
	
	public void genereClubRapport(Competition competition, Club club) throws ServiceException{
		try {
			Configuration configuration = ConfigurationService.getInstance().getConfiguration();
			IRapportDao rapportDao = XDocRapportDao.getInstance();
			rapportDao.genereClubRapport(configuration, competition, club);
		} catch (DaoException e) {
			throw new ServiceException("Erreur pendant la generation d'un rapport d'un club.",e);
		}
	}
	
	public void genereCompetitionRapport(Competition competition) throws ServiceException{
		try {
			Configuration configuration = ConfigurationService.getInstance().getConfiguration();
			IRapportDao rapportDao = XDocRapportDao.getInstance();
			rapportDao.genereCompetitionRapport(configuration, competition);
		} catch (DaoException e) {
			throw new ServiceException("Erreur pendant la generation d'un rapport de session.",e);
		}
		}

}
