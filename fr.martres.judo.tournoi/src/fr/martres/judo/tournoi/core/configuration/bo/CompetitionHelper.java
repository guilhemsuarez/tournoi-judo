package fr.martres.judo.tournoi.core.configuration.bo;

import java.text.SimpleDateFormat;


public class CompetitionHelper {


	public static final String getLabel(Competition competition) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String date = format.format(competition.getDate());
		String label = date + " - " + competition.getVille() + " - "+competition.getName();
		
		return label;
	}
	
	public static final SessionCombat getSessionCombat(Competition competition, Poule poule) {
		SessionCombat result = null;
		
		for (SessionCombat session : competition.getSessionsCombats()){
			for (Poule temp : session.getPoules()){
				if ( temp.equals(poule)){
					result = session;
					break;
				}
			}
		}
		
		return result;
	}
	
	}
