package fr.martres.judo.tournoi.core.configuration.bo;

import java.util.Date;

/**
 * Cette classe représente les information d'un judoka qui ne sont pas senser bouger.
 * @author guilhem
 *
 */
public class Judoka implements IData {

	/** le nom.*/
	private String nom;
	/** le prénom.*/
	private String prenom;
	/** la date de naissance. */
	private Date dateNaissance;
	/** le sexe .*/
	private boolean homme;

	/**
	 * Retourne la date de naissance.
	 * @return la date de naiisance.
	 */
	public Date getDateNaissance() {
		return dateNaissance;
	}

	/**
	 * Définie la date de naissance.
	 * @param dateNaissance la date de naissance .
	 */
	public void setDateNaissance(Date dateNaissance) {
		this.dateNaissance = dateNaissance;
	}

	/**
	 * Retourne le nom du judoka.
	 * @return le nom du judoka.
	 */
	public String getNom() {
		return nom;
	}

	/**
	 * Définie le nom du judoka.
	 * @param nom le nom du judoka.
	 */
	public void setNom(String nom) {
		this.nom = nom;
	}
	
	/**
	 * Retourne le prénom du judoka.
	 * @return le prénom du judoka.
	 */
	public String getPrenom() {
		return prenom;
	}

	/**
	 * Définie le prénom du judka.
	 * @param prenom le prenom du judoka.
	 */
	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	/**
	 * Retourne si le judoka est un homme ou non.
	 * @return true si le judoka est un homme.
	 */
	public boolean isHomme() {
		return homme;
	}

	/**
	 * Défini si le judoka est un homme.
	 * @param homme true si le judoka est un homme.
	 */
	public void setHomme(boolean homme) {
		this.homme = homme;
	}

}
