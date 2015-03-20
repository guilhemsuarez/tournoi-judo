package fr.martres.judo.tournoi.core.rapport.bo;

import java.util.ArrayList;
import java.util.List;

public class RapportTypeCombat {
	private String nom;
	private List<RapportCategorie> categories = new ArrayList<RapportCategorie>();
	/**
	 * @return the nom
	 */
	public String getNom() {
		return nom;
	}
	/**
	 * @param nom the nom to set
	 */
	public void setNom(String nom) {
		this.nom = nom;
	}
	/**
	 * @return the judokas
	 */
	public List<RapportCategorie> getCategories() {
		return categories;
	}
	
	/**
	 * @return the judokas
	 */
	public int getNombreJudoka() {
		return categories.size();
	}
	
	/**
	 * @param judokas the judokas to set
	 */
	public void addCategories(RapportCategorie categorie ){
		this.categories.add(categorie);
	}
	
	
}
