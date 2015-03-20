package fr.martres.judo.tournoi.core.configuration.bo;


public class CompetitionHelper {


	public static final String getLabel(Competition competition) {
		String label = competition.getVille() + " - "+competition.getName();
		
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
