package fr.martres.judo.tournoi.core.rapport.bo;

import java.util.ArrayList;
import java.util.List;

public class RapportCategorie implements Comparable<RapportCategorie>{
	private String nom;
	private List<RapportResultat> resultats = new ArrayList<RapportResultat>();
	private int anneeMinimum;
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
	public List<RapportResultat> getResultats() {
		return resultats;
	}
	
	/**
	 * @return the judokas
	 */
	public int getNombreResultat() {
		return resultats.size();
	}
	
	/**
	 * @param judokas the judokas to set
	 */
	public void addResultat(RapportResultat rapportResultat) {
		this.resultats.add(rapportResultat);
	}
	@Override
	public int compareTo(RapportCategorie categorie) {
		int result = -1 ;
		int autre = categorie.getAnneeMinimum();
		int moi = categorie.getAnneeMinimum();
		
		if( moi > autre){
			result = 1;
		}
		else if ( moi == autre){
			result = 0;
		}
		return result;
	}
	
	
	public void setAnneeMinimum(int anneeMinimum) {
		this.anneeMinimum = anneeMinimum;
		
	}
	/**
	 * @return the anneeMinimum
	 */
	public int getAnneeMinimum() {
		return anneeMinimum;
	}


	
}
