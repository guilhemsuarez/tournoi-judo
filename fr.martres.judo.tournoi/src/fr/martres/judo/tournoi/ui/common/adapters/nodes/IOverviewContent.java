package fr.martres.judo.tournoi.ui.common.adapters.nodes;

public interface IOverviewContent {

	/**
	 * Retoune le type du content a afficher.
	 * @return le type
	 */
	public String getType();
	/**
	 * Retourne le nom du content a afficher.
	 * @return
	 */
	public String getNom();
	/**
	 * Retourne le détails du content à afficher.
	 * @return
	 */
	public String getDetails();
}
