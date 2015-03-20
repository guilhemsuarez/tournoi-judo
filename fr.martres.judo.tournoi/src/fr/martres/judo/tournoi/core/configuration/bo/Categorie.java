package fr.martres.judo.tournoi.core.configuration.bo;

import java.util.ArrayList;
import java.util.List;

/**
 * Une categorie est définie par une liste de poids et une plage d'année de naissance
 * 
 * @author guilhem
 *
 */
public class Categorie implements IData{
	/** Le nom de la catégorie. */
	private String nom;
	/** La liste de poids associé à la catégorie */
	private List<Poids> poids = new ArrayList<Poids>();
	
	/** l'année basse de la catégorie */
	private int anneeMinimum;
	
	/** l'année haute de la catégorie */
	private int anneeMaximum;
	
	/** Catégorie homme ou femme */
	private boolean homme;
	
	/**
	 * @return un identifiant unique representant la catégorie. 
	 */
	public String getUniqueID(){
		return nom+anneeMinimum+anneeMaximum+homme;
	}
	
	/**
	 * @return la liste de poids associée à la catégorie.
	 */
	public List<Poids> getPoids() {
		return poids;
	}

	/**
	 * Ajoute un poids à la liste.
	 * @param poid le poids à ajouter à la liste de poids.
	 */
	public void addPoids(Poids poid) {
		this.poids.add(poid);
	}

	/**
	 * 
	 * @return l'année minimale de la categorie.
	 */
	public int getAnneeMinimum() {
		return anneeMinimum;
	}

	/**
	 * Définie l'année minimum.
	 * @param anneeMinimum l'année minimum à définir.
	 */
	public void setAnneeMinimum(int anneeMinimum) {
		this.anneeMinimum = anneeMinimum;
	}

	/**
	 * Retourne l'année maximale de la catégorie.
	 * @return l'année maximale de la catégorie.
	 */
	public int getAnneeMaximum() {
		return anneeMaximum;
	}

	/**
	 * Définie l'anné maximale de la catégorie.
	 * @param anneeMaximum l'année maximale de la catégorie.
	 */
	public void setAnneeMaximum(int anneeMaximum) {
		this.anneeMaximum = anneeMaximum;
	}

	/**
	 * Retourne le nom de la catégorie.
	 * @return le nom de la catégorie.
	 */
	public String getNom() {
		return nom;
	}

	/**
	 * Définie le nom de la catégorie.
	 * @param nom le nom de la catégorie.
	 */
	public void setNom(String nom) {
		this.nom = nom;
	}

	/**
	 * Détemine s'il s'agit du catégorie homme ou femme.
	 * @return true s'il s'agit d'une catégorie homme.
	 */
	public boolean isHomme() {
		return homme;
	}

	/**
	 * Définie s'il s'agit d'une catégorie homme ou femme.
	 * @param homme true s'il s'agit d'une catégorie homme.
	 */
	public void setHomme(boolean homme) {
		this.homme = homme;
	}
}
