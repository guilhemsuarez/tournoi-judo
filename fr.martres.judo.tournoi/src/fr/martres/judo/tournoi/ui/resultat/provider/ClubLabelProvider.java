package fr.martres.judo.tournoi.ui.resultat.provider;

import org.eclipse.jface.viewers.LabelProvider;

import fr.martres.judo.tournoi.core.configuration.bo.Club;

public class ClubLabelProvider extends LabelProvider {

	public String getText(Object obj) {
		String label =  obj.toString();
		
		if (obj instanceof Club){
			Club club = (Club)obj;
			
			label = club.getNom();
		}
		return label;
	}
	


}