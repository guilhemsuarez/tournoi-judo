package fr.martres.judo.tournoi.core.configuration.bo;

/**
 * LE resultat représente la position du pouleant à la fin du poule dans la poule.
 * @author guilhem
 *
 */
public class Resultat implements IData {
	/** Le poule associé à cette participation.*/
	private Poule poule;

	/** la position du judoka dans le poule. */
	private int position ;
	
	/**
	 * Retourne la position du judoka dans le poule.
	 * @return
	 */
	public int getPosition() {
		return position;
	}
	
	/**
	 * Défini la position du poule.
	 * @param position la position du poule.
	 */
	public void setPosition(int position) {
		this.position = position;
	}

	/**
	 * Récupère le poule associé à la participation.
	 * @return le poule
	 */
	public Poule getPoule() {
		return poule;
	}
	
	/**
	 * Définit le poule associé à la participation.
	 * @param poule le poule de la participation
	 */
	public void setPoule(Poule poule) {
		this.poule = poule;
	}

}
