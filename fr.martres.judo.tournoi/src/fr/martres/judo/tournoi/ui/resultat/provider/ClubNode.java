package fr.martres.judo.tournoi.ui.resultat.provider;

import fr.martres.judo.tournoi.core.configuration.bo.Club;

public class ClubNode {
	private Club club;
	private int nbJudoka;
	private int points;
	/**
	 * @return the club
	 */
	public Club getClub() {
		return club;
	}
	/**
	 * @param club the club to set
	 */
	public void setClub(Club club) {
		this.club = club;
	}
	/**
	 * @return the nbJudoka
	 */
	public int getNbJudoka() {
		return nbJudoka;
	}
	/**
	 * @param nbJudoka the nbJudoka to set
	 */
	public void setNbJudoka(int nbJudoka) {
		this.nbJudoka = nbJudoka;
	}
	/**
	 * @return the points
	 */
	public int getPoints() {
		return points;
	}
	/**
	 * @param points the points to set
	 */
	public void setPoints(int points) {
		this.points = points;
	}
	

}
