package fr.martres.judo.tournoi.core.rapport.dao;

import java.util.List;

import fr.martres.judo.tournoi.core.configuration.bo.Club;
import fr.martres.judo.tournoi.core.configuration.bo.Competition;
import fr.martres.judo.tournoi.core.configuration.bo.Configuration;
import fr.martres.judo.tournoi.core.configuration.bo.Judoka;
import fr.martres.judo.tournoi.core.configuration.bo.Poule;
import fr.martres.judo.tournoi.core.configuration.bo.SessionCombat;
import fr.martres.judo.tournoi.core.exception.DaoException;
import fr.martres.judo.tournoi.services.ServiceException;

public interface IRapportDao {

	/**
	 * Genere le rapport d'une session.
	 * @param la session
	 * @throws DaoException 
	 */
	public void genereSessionRapport(Configuration configuration,Competition competition, SessionCombat session)
			throws DaoException;
	
	/**
	 * Genere le rapport d'une poule appartenant à la session.
	 * @param la session
	 * @param la poule
	 * @throws DaoException 
	 */
	public void generePouleRapport(Configuration configuration,Competition competition, SessionCombat session,Poule poule)throws DaoException;
	
	/**
	 * Genere le rapport d'un club.
	 * @param le club
	 * @throws DaoException 
	 */
	public void genereClubRapport(Configuration configuration,Competition competition, Club club)throws DaoException;
	
	/**
	 * Genere le rapport d'une competition.
	 * @param la competition
	 * @throws DaoException 
	 */
	public void genereCompetitionRapport(Configuration configuration, Competition competition) throws DaoException;

	
	
}
