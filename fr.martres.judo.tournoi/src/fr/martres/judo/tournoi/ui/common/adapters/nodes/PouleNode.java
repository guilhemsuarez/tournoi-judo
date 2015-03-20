package fr.martres.judo.tournoi.ui.common.adapters.nodes;

import fr.martres.judo.tournoi.core.configuration.bo.Poule;
import fr.martres.judo.tournoi.ui.common.ImageHelper;

public class PouleNode extends TreeParent implements IOverviewContent{
		
			private Poule poule;
			private int nbElement = 0 ;
			

			public int getNbElement() {
				return nbElement;
			}

			public void setNbElement(int nbElement) {
				this.nbElement = nbElement;
			}

			
		
			public PouleNode( Poule poule) {
				super();
				this.poule = poule;
			}
		
			@Override
			public String getLabel() {
				String label = "Poule "+ Integer.toString(poule.getId());
				if (nbElement > 0){
					label += " ("+nbElement+")";
				}
				return label;
		
		}

			@Override
			public String getType() {
				return "Poule";
			}

			@Override
			public String getNom() {
				return "";
			}

			@Override
			public String getDetails() {
				return "";
			}

			@Override
			public String getImage() {
				return ImageHelper.POULE;
			}

			public Poule getPoule() {
				return this.poule;
			}
	

}
