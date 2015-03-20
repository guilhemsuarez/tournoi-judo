package fr.martres.judo.tournoi.ui.resultat.provider;

import java.util.ArrayList;
import java.util.List;

import fr.martres.judo.tournoi.core.configuration.bo.Club;
import fr.martres.judo.tournoi.core.configuration.bo.Competition;
import fr.martres.judo.tournoi.core.configuration.bo.Configuration;
import fr.martres.judo.tournoi.core.configuration.bo.Participation;
import fr.martres.judo.tournoi.core.configuration.bo.Poule;
import fr.martres.judo.tournoi.core.configuration.bo.Resultat;
import fr.martres.judo.tournoi.core.configuration.bo.SessionCombat;
import fr.martres.judo.tournoi.core.configuration.bo.TypeCombat;
import fr.martres.judo.tournoi.core.exception.BoException;
import fr.martres.judo.tournoi.services.ConfigurationService;
import fr.martres.judo.tournoi.services.ServiceException;

public class ClubNodeFactory {
	
	
	public static List<ClubNode> getClubNode(Competition competition, List<Club> clubs){
		List<ClubNode> clubNodes = new ArrayList<ClubNode>();
		
		try {
			Configuration configuration = ConfigurationService.getInstance().getConfiguration();
			ClubNode clubNode;
			for(Club club : clubs){
				  clubNode =  new ClubNode();
				  clubNode.setClub(club);
				  List<Participation> participations = configuration.getParticipations(club,competition);
				  clubNode.setNbJudoka(participations.size());
				  
				  int position;
				  int points = 0;
				  int nbJudoka = 0;
				  Poule poule ;
				  for ( Participation participation : participations){
					  for ( Resultat resultat : participation.getResultats()){
						  position = resultat.getPosition();
						  poule = resultat.getPoule();
						  nbJudoka = configuration.getJudoka(competition, poule).size();
						  for ( SessionCombat session : competition.getSessionsCombats()){
							  if ( session.getPoules().contains(poule)){
								  TypeCombat combat = session.getType();
								  points += combat.getPointsPourClassement(nbJudoka,position);
							  }
						  }
						  
					  }
				  }
				  
				  clubNode.setPoints(points);
			
				  clubNodes.add(clubNode);

			}
		} catch (ServiceException | BoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return clubNodes;
	}

}
