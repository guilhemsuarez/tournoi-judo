package fr.martres.judo.tournoi.core.configuration.bo;
/**
 * Représentation de l'intervalle de poids .
 * @author guilhem
 *
 */
public class Poids implements IData {
	/** borne minimal de l'intervale. */
	private int minimum;
	/** borne maximal de l'intervale. */
	private int maximum;
	
	
	public int getMinimum() {
		return minimum;
	}
	public void setMinimum(int minimum) {
		this.minimum = minimum;
	}
	public int getMaximum() {
		return maximum;
	}
	public void setMaximum(int maximum) {
		this.maximum = maximum;
	}
	

}
