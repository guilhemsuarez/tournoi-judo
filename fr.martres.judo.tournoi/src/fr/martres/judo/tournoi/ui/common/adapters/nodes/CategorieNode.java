package fr.martres.judo.tournoi.ui.common.adapters.nodes;

import fr.martres.judo.tournoi.core.configuration.bo.Categorie;
import fr.martres.judo.tournoi.ui.common.ImageHelper;

public class CategorieNode extends TreeParent implements IOverviewContent {

	private Categorie categorie;
	private int nbElement = 0 ;
	

	public int getNbElement() {
		return nbElement;
	}

	public void setNbElement(int nbElement) {
		this.nbElement = nbElement;
	}

	public CategorieNode(Categorie categorie) {
		super();
		this.categorie = categorie;
	}

	@Override
	public String getLabel() {
		String label = categorie.getNom();
		if (nbElement > 0){
			label = categorie.getNom()+" ("+nbElement+")";
		}
		return label;
	}

	@Override
	public String getType() {
		return "Catégorie";
	}

	@Override
	public String getNom() {
		return categorie.getNom();
	}

	@Override
	public String getDetails() {
		// TODO listeer les poids
		// TODO lister le nombre de judoka
		return "";
	}

	@Override
	public String getImage() {
		return ImageHelper.CATEGORIE;
	}

	public Categorie getCategorie() {
		return categorie;
	}

}
