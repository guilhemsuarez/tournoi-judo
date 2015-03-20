package fr.martres.judo.tournoi.core.configuration.bo;

import java.util.Map;

import fr.martres.judo.tournoi.core.exception.BoException;

/**
 * Cette classe fournit des méthodes d'aide à la gestion des sessions de combats.
 * 
 * @author guilhem
 *
 */
public class SessionCombatHelper {
	
	

	public static SessionCombat getSessionCombat(Competition competition,
			String nomSession, String typeCombatSession,Categorie categorie,Poids poids) throws BoException {
		SessionCombat result = null;
		Map<Integer,SessionCombat> sess = competition.getSessionsCombatMap();
		int sessionId = Integer.parseInt(nomSession);
		// si la session existe on la reccupère
		if (sess.containsKey(sessionId)){
			result = sess.get(sessionId);
			// on vérifie la pertinence de la categorie
			Categorie categorieActuelle = result.getCategorie();
			if ( categorieActuelle != null){
				// si ce n'est pas la mème, on la supprime
				if ( ! categorie.equals(categorieActuelle)) {
					result.setCategorie(null);
					// par la même occasion on supprime le poids
					result.setPoids(null);
				}
			}
			// on vérifie la pertinence du poids
			Poids poidsActuel = result.getPoids();
			if (poidsActuel != null){
				// si ce n'est le même poids, on le supprime
				if ( ! poids.equals(poidsActuel)){
					result.setPoids(null);
				}
			}
		}
		// sinon on la cree
		else {
			result = new SessionCombat();
			result.setId(sessionId);
			TypeCombat typeCombat = competition.getTypeCombat(typeCombatSession);
			if ( typeCombat == null ){
				throw new BoException("Le Type de combat n'est pas reconnu.");
			}
			result.setType(typeCombat);
			result.setCategorie(categorie);
			result.setPoids(poids);
			competition.addSessionCombat(result);
			
		}
		return result;
	}
	
	
	public static String getLabel (SessionCombat sessionCombat){
		String label = "Session ";
		if ( sessionCombat.getType() != null){
			label += sessionCombat.getType().getNom()+" - ";
		}
		if ( sessionCombat.getId() != 0){
			label += sessionCombat.getId();
		}
		if ( sessionCombat.getCategorie() != null){
			label += " - " + sessionCombat.getCategorie().getNom();
		}
		if ( sessionCombat.getPoids() != null){
			label += " - [" + PoidsHelper.getLabel(sessionCombat.getPoids()) +"]";
		}
		 return label;
	}
}