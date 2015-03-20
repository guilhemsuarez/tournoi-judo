package fr.martres.judo.tournoi.ui.common.adapters.nodes;

import fr.martres.judo.tournoi.core.configuration.bo.Poids;
import fr.martres.judo.tournoi.core.configuration.bo.PoidsHelper;
import fr.martres.judo.tournoi.ui.common.ImageHelper;

public class PoidsNode extends TreeParent implements IOverviewContent{
		
			private Poids poids;
			private int nbElement = 0 ;
			

			public int getNbElement() {
				return nbElement;
			}

			public void setNbElement(int nbElement) {
				this.nbElement = nbElement;
			}

			
		
			public PoidsNode( Poids poids) {
				super();
				this.poids = poids;
			}
		
			@Override
			public String getLabel() {
				String label = PoidsHelper.getLabel(poids);
				if (nbElement > 0){
					label += " ("+nbElement+")";
				}
				return label;
		
		}

			@Override
			public String getType() {
				return "Poids";
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
				return ImageHelper.POIDS;
			}

			public Poids getPoids() {
				return this.poids;
			}
	

}
