package fr.martres.judo.tournoi.core.rapport.bo;


public class RapportClub implements Comparable<RapportClub> {
	private String nom;
	private String nbJudoka;
	private String nbPoints;
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
	 * @return the nbJudoka
	 */
	public String getNbJudoka() {
		return nbJudoka;
	}
	/**
	 * @param nbJudoka the nbJudoka to set
	 */
	public void setNbJudoka(String nbJudoka) {
		this.nbJudoka = nbJudoka;
	}
	/**
	 * @return the nbPoints
	 */
	public String getNbPoints() {
		return nbPoints;
	}
	/**
	 * @param nbPoints the nbPoints to set
	 */
	public void setNbPoints(String nbPoints) {
		this.nbPoints = nbPoints;
	}
	
	
	@Override
	public int compareTo(RapportClub club) {
		int result = -1;
		int autre = Integer.parseInt(club.getNbPoints());
		int moi = Integer.parseInt(this.getNbPoints());
		
		if ( moi > autre){
			result = 1;
		}
		else if ( moi == autre){
			result = 0;
		}
		
		return result;
	}
	
}
