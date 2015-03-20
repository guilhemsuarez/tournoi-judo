package fr.martres.judo.tournoi.core.rapport.bo;

import java.util.List;

public class RapportSession {

	private String session;
	private List<RapportPoule> poules;
	private String categorie;
	private String poids;
	private String type;
	
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * @return the session
	 */
	public String getSession() {
		return session;
	}
	/**
	 * @param session the session to set
	 */
	public void setSession(String session) {
		this.session = session;
	}
	/**
	 * @return the poules
	 */
	public List<RapportPoule> getPoules() {
		return poules;
	}
	/**
	 * @param poules the poules to set
	 */
	public void setPoules(List<RapportPoule> poules) {
		this.poules = poules;
	}
	/**
	 * @return the categorie
	 */
	public String getCategorie() {
		return categorie;
	}
	/**
	 * @param categorie the categorie to set
	 */
	public void setCategorie(String categorie) {
		this.categorie = categorie;
	}
	/**
	 * @return the poids
	 */
	public String getPoids() {
		return poids;
	}
	/**
	 * @param poids the poids to set
	 */
	public void setPoids(String poids) {
		this.poids = poids;
	}
	
	

}
