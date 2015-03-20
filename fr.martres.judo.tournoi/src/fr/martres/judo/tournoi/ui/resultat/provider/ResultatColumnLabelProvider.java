package fr.martres.judo.tournoi.ui.resultat.provider;

import java.util.List;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.PlatformUI;

import fr.martres.judo.tournoi.core.configuration.bo.Club;
import fr.martres.judo.tournoi.core.configuration.bo.Competition;
import fr.martres.judo.tournoi.core.configuration.bo.Configuration;
import fr.martres.judo.tournoi.core.configuration.bo.Participation;
import fr.martres.judo.tournoi.core.configuration.bo.Poule;
import fr.martres.judo.tournoi.core.configuration.bo.Resultat;
import fr.martres.judo.tournoi.core.configuration.bo.SessionCombat;
import fr.martres.judo.tournoi.core.configuration.bo.TypeCombat;
import fr.martres.judo.tournoi.services.ConfigurationService;
import fr.martres.judo.tournoi.services.ServiceException;
import fr.martres.judo.tournoi.ui.judoka.NavigationView;

public class ResultatColumnLabelProvider extends ColumnLabelProvider {

	private ResultatTableColumn column;

	public ResultatColumnLabelProvider(ResultatTableColumn column) {
		this.column = column;
	}

	@Override
	public String getText(Object element) {
		
		String textToReturn = "";
		ClubNode club = (ClubNode) element;
		switch (column) {
		case NOM: {
			textToReturn = club.getClub().getNom();
			break;
		}
		case NB_JUDOKA : {
			textToReturn = Integer.toString(club.getNbJudoka());
			
			break;
		}
		case POINTS : {
			
		     textToReturn = Integer.toString(club.getPoints());
			break;
		}
		}

		return textToReturn;
	}

}
