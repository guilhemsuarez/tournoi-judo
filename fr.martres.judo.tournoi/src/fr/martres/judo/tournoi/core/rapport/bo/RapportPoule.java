package fr.martres.judo.tournoi.core.rapport.bo;

import java.util.List;

public class RapportPoule {
	private String nom;
	private List<RapportJudoka> judokas;
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
	public List<RapportJudoka> getJudokas() {
		return judokas;
	}
	
	/**
	 * @return the judokas
	 */
	public int getNombreJudoka() {
		return judokas.size();
	}
	
	/**
	 * @param judokas the judokas to set
	 */
	public void setJudokas(List<RapportJudoka> judokas) {
		this.judokas = judokas;
	}
	
	
}
