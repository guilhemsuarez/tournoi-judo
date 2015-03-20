package fr.martres.judo.tournoi.core.configuration.bo;

/**
 * Cette classe définit une instance de compétition.
 * 
 * @author guilhem
 *
 */
public class Poule implements IData {
	/** le nom du combat.*/
	private int identifiant;
	
	/**
	 * Retourne le nom du combat.
	 * @return le nom du combat.
	 */
	public int getId() {
		return identifiant;
	}
	
	/**
	 * Défini le nom des combats.
	 * @param id le nom des combats.
	 */
	public void setId(int id) {
		this.identifiant = id;
	}
	

	
}
