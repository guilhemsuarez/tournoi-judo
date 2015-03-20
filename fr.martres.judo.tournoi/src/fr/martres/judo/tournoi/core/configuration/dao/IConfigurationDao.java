package fr.martres.judo.tournoi.core.configuration.dao;

import java.util.Date;
import java.util.List;

import fr.martres.judo.tournoi.core.configuration.bo.Club;
import fr.martres.judo.tournoi.core.configuration.bo.Competition;
import fr.martres.judo.tournoi.core.configuration.bo.Configuration;
import fr.martres.judo.tournoi.core.configuration.bo.Judoka;
import fr.martres.judo.tournoi.core.configuration.bo.Participation;
import fr.martres.judo.tournoi.core.configuration.bo.Poids;
import fr.martres.judo.tournoi.core.configuration.bo.Poule;
import fr.martres.judo.tournoi.core.configuration.bo.SessionCombat;
import fr.martres.judo.tournoi.core.exception.DaoException;

public interface IConfigurationDao {

	/**
	 * Recuppère la configuration
	 * @return la configuration
	 * @throws DaoException 
	 */
	public Configuration retreiveConfiguration() throws DaoException;
	
	/**
	 * Sauvegarde la configuration.
	 * @param configuration la configuration à sauvegarder.
	 * @throws DaoException si une erreur arrive lors de la sauvegarde.
	 */
	public void saveConfiguration(Configuration configuration) throws DaoException;

	/**
	 * Ajoute une seession de combat à la competition de cette configuration.s
	 * 
	 * @param configuration la configuration contenant la competition
	 * @param competition la competition qui contindra la session
	 * @param session la session de combat a ajouter.
	 * @throws DaoException si une erreur arrive lors de la sauvegarde.
	 */
	public void addSessionCombat(Configuration configuration, Competition competition,
			SessionCombat session) throws DaoException;
	
	/** Cree une poule à une session de combat à la competition de cette configuration.
	 * 
	 * @param configuration la configuration contenant la competition
	 * @param competition
	 * @param session
	 * @param judokas
	 * @return
	 */
	public Poule createPouleOnSessionCombat(Configuration configuration,Competition competition, SessionCombat session, List<Judoka> judokas) throws DaoException;

	/**
	 * Supprime une poule d'une session et le resultat correspondant.
	 * @param configuration
	 * @param competition
	 * @param session
	 * @param poule
	 * @throws DaoException
	 */
	public void deletePouleOnSessionCombat(Configuration configuration,Competition competition, SessionCombat session, Poule poule) throws DaoException;

	/**
	 * definie la position du judoka dans une poule.
	 * @param configuration
	 * @param competition
	 * @param Session
	 * @param judoka
	 * @param position
	 * @throws DaoException
	 */
	public void setPositionInPoule(Configuration configuration, Competition competition, SessionCombat Session, Judoka judoka, Poule poule, int position)throws DaoException;

	/**
	 * supprime un judoka d'une poule
	 * @param configuration
	 * @param competition
	 * @param session
	 * @param poule
	 * @param judoka
	 * @throws DaoException
	 */
	public void deleteJudokaOnPoule(Configuration configuration,
			Competition competition, Poule poule,
			Judoka judoka) throws DaoException;

	/**
	 * Ajoute un judoka à une poule.
	 * @param configuration
	 * @param competition
	 * @param poule
	 * @param judoka
	 * @throws DaoException
	 */
	public void addJudokaOnPoule(Configuration configuration, Competition competition,
			Poule poule, Judoka judoka) throws DaoException;

	/**
	 * Ajoute un judoka à la competition.
	 * @param configuration
	 * @param competition
	 * @param nom
	 * @param prenom
	 * @param dateDeNaissance
	 * @param isHomme
	 * @param ceinture
	 * @param club
	 * @param poids
	 * @return 
	 * @throws DaoException
	 */
	public Participation addJudokaOnCompetition(Configuration configuration,
			Competition competition, String nom, String prenom, Date dateDeNaissance,
			boolean isHomme, String ceinture, Club club, double poids) throws DaoException;

	public Participation addJudokaOnCompetition(Configuration configuration,
			Competition competition, String nom, String prenom,
			Date dateDeNaissance, boolean isHomme, String ceinture,
			String clubName, double poids)  throws DaoException;
}
