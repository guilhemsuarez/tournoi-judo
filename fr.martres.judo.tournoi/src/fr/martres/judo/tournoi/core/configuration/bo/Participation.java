package fr.martres.judo.tournoi.core.configuration.bo;

import java.util.ArrayList;
import java.util.List;

/**
 * La participation correspond à l'inscription d'un judoka à une competition
 * @author guilhem
 *
 */
public class Participation  implements IData {
	/** L'instance du judoka. */
	private Judoka judoka;
	/** la competition associée. */
	private Competition competition;
	/** Le poids associé à cette participation.*/
	private double poids;
	/** La catégorie associé à cette participation.*/
	private Categorie categorie;
	/** Les resultats associés à cette participation.*/
	private List<Resultat> resultats = new ArrayList<Resultat>();
	/** Le club associé à cette participation.*/
	private Club club;
	/** La ceinture du judoka pour cette competition. */
	private String ceinture;
	
	/**
	 * Recupère la ceinture.
	 * @return
	 */
	public String getCeinture() {
		return ceinture;
	}

	/**
	 * Définie la cceinture.
	 * @param ceinture
	 */
	public void setCeinture(String ceinture) {
		this.ceinture = ceinture;
	}

	/**
	 * Retourne le club associé à cette participation.
	 * @return le club
	 */
	public Club getClub() {
		return club;
	}

	/**
	 * Définie le club associé à la participaion.
	 * @param club le club à associer.
	 */
	public void setClub(Club club) {
		this.club = club;
	}
	
	/**
	 * Retourne le judoka. 
	 * @return le judoka qui participe.
	 */
	public Judoka getJudoka() {
		return judoka;
	}
	
	/**
	 * Défini le judoka de la participation.
	 * @param judoka le judoka de la participation.
	 */
	public void setJudoka(Judoka judoka) {
		this.judoka = judoka;
	}
	
	/**
	 * Retourne la competition associé à la participation.
	 * @return la competition.
	 */
	public Competition getCompetition() {
		return competition;
	}
	
	/**
	 * Définit la competition de la participation.
	 * @param competition la competition associé à la participation.
	 */
	public void setCompetition(Competition competition) {
		this.competition = competition;
	}
	
	/**
	 * Retourne le poids associé à la participation.
	 * @return le poids
	 */
	public double getPoids() {
		return poids;
	}
	
	/**
	 * Définit le poids associé à la participation.
	 * @param poids le poids associé à la participation.
	 */
	public void setPoids(double poids) {
		this.poids = poids;
	}
	
	/**
	 * Retourne la catégorie de la participation.
	 * @return la catégorie associé à la participation.
	 */
	public Categorie getCategorie() {
		return categorie;
	}
	
	/**
	 * Définit la catégorie associé à la participation.
	 * @param categorie la catégorie à associer.
	 */
	public void setCategorie(Categorie categorie) {
		this.categorie = categorie;
	}

	public List<Resultat> getResultats() {
		return resultats;
	}

	public void addResultat(Resultat resultat) {
		this.resultats.add(resultat);
	}
	

	
}
